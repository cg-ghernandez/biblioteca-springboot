document.addEventListener("DOMContentLoaded", function () {
    const tabla = document.getElementById("tablaBibliotecarios").querySelector("tbody");
    const formulario = document.getElementById("formularioBibliotecario");

    const inputNombre = document.getElementById("nombre");
    const inputEmail = document.getElementById("email");
    const inputTelefono = document.getElementById("telefono");

    let modoEdicion = false;
    let bibliotecarioEditandoId = null;

    function cargarBibliotecarios() {
        fetch("/api/bibliotecarios")
            .then(response => response.json())
            .then(data => {
                tabla.innerHTML = "";
                data.forEach(bibliotecario => {
                    const fila = document.createElement("tr");
                    fila.innerHTML = `
                        <td>${bibliotecario.id}</td>
                        <td>${bibliotecario.nombre}</td>
                        <td>${bibliotecario.email}</td>
                        <td>${bibliotecario.telefono}</td>
                        <td>
                            <button class="btn-editar" onclick="editarBibliotecario(${bibliotecario.id})">
                                ✏️
                            </button>
                            <button class="btn-eliminar" onclick="eliminarBibliotecario(${bibliotecario.id})">
                                🗑️
                            </button>
                        </td>
                    `;
                    tabla.appendChild(fila);
                });
            });
    }

    window.editarBibliotecario = function (id) {
        fetch(`/api/bibliotecarios/${id}`)
            .then(response => response.json())
            .then(b => {
                inputNombre.value = b.nombre;
                inputEmail.value = b.email;
                inputTelefono.value = b.telefono;
                modoEdicion = true;
                bibliotecarioEditandoId = b.id;
            });
    };

    window.eliminarBibliotecario = function (id) {
        if (confirm("¿Desea eliminar este bibliotecario?")) {
            fetch(`/api/bibliotecarios/${id}`, {
                method: "DELETE"
            })
                .then(() => cargarBibliotecarios());
        }
    };

    formulario.addEventListener("submit", function (e) {
        e.preventDefault();

        const bibliotecario = {
            nombre: inputNombre.value,
            email: inputEmail.value,
            telefono: inputTelefono.value
        };

        const url = modoEdicion
            ? `/api/bibliotecarios/${bibliotecarioEditandoId}`
            : "/api/bibliotecarios";

        const method = modoEdicion ? "PUT" : "POST";

        fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(bibliotecario)
        })
            .then(response => {
                if (!response.ok) throw new Error("Error al guardar");
                return response.json();
            })
            .then(() => {
                formulario.reset();
                modoEdicion = false;
                bibliotecarioEditandoId = null;
                cargarBibliotecarios();
            })
            .catch(error => alert(error.message));
    });

    cargarBibliotecarios();
});
