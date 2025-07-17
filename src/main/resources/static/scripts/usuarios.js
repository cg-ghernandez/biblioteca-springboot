document.addEventListener("DOMContentLoaded", () => {
    const tablaUsuarios = document.querySelector("#tablaUsuarios tbody");
    const formulario = document.getElementById("formularioUsuario");

    function cargarUsuarios() {
        fetch("/api/usuarios")
            .then(response => response.json())
            .then(usuarios => {
                tablaUsuarios.innerHTML = "";
                usuarios.forEach(usuario => {
                    const fila = document.createElement("tr");
                    fila.innerHTML = `
                        <td>${usuario.id}</td>
                        <td>${usuario.nombre}</td>
                        <td>${usuario.email}</td>
                        <td>${usuario.telefono || '-'}</td>
                    `;
                    tablaUsuarios.appendChild(fila);
                });
            })
            .catch(error => {
                console.error("Error al cargar usuarios:", error);
            });
    }

    formulario.addEventListener("submit", (event) => {
        event.preventDefault();

        const nuevoUsuario = {
            nombre: document.getElementById("nombre").value,
            email: document.getElementById("email").value,
            telefono: document.getElementById("telefono").value
        };

        fetch("/api/usuarios", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(nuevoUsuario)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("No se pudo registrar el usuario");
            }
            return response.json();
        })
        .then(usuario => {
            formulario.reset();
            cargarUsuarios();
        })
        .catch(error => {
            console.error("Error al registrar usuario:", error);
        });
    });

    // Carga inicial de usuarios
    cargarUsuarios();
});
