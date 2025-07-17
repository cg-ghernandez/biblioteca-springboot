document.addEventListener("DOMContentLoaded", function () {
    const tabla = document.getElementById("tablaCategorias").querySelector("tbody");
    const formulario = document.getElementById("formularioCategoria");
    const inputNombre = document.getElementById("nombreCategoria");

    let modoEdicion = false;
    let categoriaEditandoId = null;

    function cargarCategorias() {
        fetch("http://localhost:8080/api/categorias")
            .then(response => response.json())
            .then(data => {
                tabla.innerHTML = "";
                data.forEach(categoria => {
                    const fila = document.createElement("tr");
                    fila.innerHTML = `
                        <td>${categoria.id}</td>
                        <td>${categoria.nombre}</td>
                        <td>
                            <button onclick="editarCategoria(${categoria.id})">✏️ Editar</button>
                            <button onclick="eliminarCategoria(${categoria.id})">🗑️ Eliminar</button>
                        </td>
                    `;
                    tabla.appendChild(fila);
                });
            })
            .catch(error => console.error("Error al cargar categorías:", error));
    }

    window.editarCategoria = function (id) {
        fetch(`http://localhost:8080/api/categorias/${id}`)
            .then(response => {
                if (!response.ok) throw new Error("Categoría no encontrada");
                return response.json();
            })
            .then(categoria => {
                inputNombre.value = categoria.nombre;
                modoEdicion = true;
                categoriaEditandoId = categoria.id;
            })
            .catch(error => alert(error.message));
    };

    window.eliminarCategoria = function (id) {
        if (confirm("¿Estás seguro de eliminar esta categoría?")) {
            fetch(`http://localhost:8080/api/categorias/${id}`, {
                method: "DELETE"
            })
                .then(() => cargarCategorias())
                .catch(error => alert("Error al eliminar categoría"));
        }
    };

    formulario.addEventListener("submit", function (e) {
        e.preventDefault();

        const categoria = {
            nombre: inputNombre.value
        };

        if (modoEdicion) {
            fetch(`http://localhost:8080/api/categorias/${categoriaEditandoId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(categoria)
            })
                .then(response => {
                    if (!response.ok) throw new Error("No se pudo actualizar la categoría");
                    return response.json();
                })
                .then(() => {
                    formulario.reset();
                    modoEdicion = false;
                    categoriaEditandoId = null;
                    cargarCategorias();
                })
                .catch(error => alert(error.message));
        } else {
            fetch("http://localhost:8080/api/categorias", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(categoria)
            })
                .then(response => {
                    if (!response.ok) throw new Error("No se pudo registrar la categoría");
                    return response.json();
                })
                .then(() => {
                    formulario.reset();
                    cargarCategorias();
                })
                .catch(error => alert(error.message));
        }
    });

    cargarCategorias();
});
