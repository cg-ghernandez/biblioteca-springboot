document.addEventListener("DOMContentLoaded", function () {
    const tabla = document.getElementById("tablaUsuarios").querySelector("tbody");
    const formulario = document.getElementById("formularioUsuario");
    const inputNombre = document.getElementById("nombre");
    const inputEmail = document.getElementById("email");

    let modoEdicion = false;
    let usuarioEditandoId = null;

    function cargarUsuarios() {
        fetch("/api/usuarios")
            .then(response => response.json())
            .then(data => {
                tabla.innerHTML = "";
                data.forEach(usuario => {
                    const fila = document.createElement("tr");
                    fila.innerHTML = `
                        <td>${usuario.id}</td>
                        <td>${usuario.nombre}</td>
                        <td>${usuario.email}</td>
                        <td>
                            <button onclick="editarUsuario(${usuario.id})">Editar</button>
                            <button onclick="eliminarUsuario(${usuario.id})">Eliminar</button>
                        </td>
                    `;
                    tabla.appendChild(fila);
                });
            })
            .catch(error => console.error("Error al cargar usuarios:", error));
    }

    window.editarUsuario = function (id) {
        fetch(`/api/usuarios/${id}`)
            .then(response => {
                if (!response.ok) throw new Error("Usuario no encontrado");
                return response.json();
            })
            .then(usuario => {
                inputNombre.value = usuario.nombre;
                inputEmail.value = usuario.email;
                modoEdicion = true;
                usuarioEditandoId = usuario.id;
            })
            .catch(error => alert(error.message));
    };

    window.eliminarUsuario = function (id) {
        if (confirm("¿Estás seguro de eliminar este usuario?")) {
            fetch(`/api/usuarios/${id}`, {
                method: "DELETE"
            })
                .then(() => cargarUsuarios())
                .catch(error => alert("Error al eliminar usuario"));
        }
    };

    formulario.addEventListener("submit", function (e) {
        e.preventDefault();

        const usuario = {
            nombre: inputNombre.value,
            email: inputEmail.value
        };

        if (modoEdicion) {
            fetch(`/api/usuarios/${usuarioEditandoId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(usuario)
            })
                .then(response => {
                    if (!response.ok) throw new Error("No se pudo actualizar el usuario");
                    return response.json();
                })
                .then(() => {
                    formulario.reset();
                    modoEdicion = false;
                    usuarioEditandoId = null;
                    cargarUsuarios();
                })
                .catch(error => alert(error.message));
        } else {
            fetch("/api/usuarios", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(usuario)
            })
                .then(response => {
                    if (!response.ok) throw new Error("No se pudo registrar el usuario");
                    return response.json();
                })
                .then(() => {
                    formulario.reset();
                    cargarUsuarios();
                })
                .catch(error => alert(error.message));
        }
    });

    cargarUsuarios();
});
