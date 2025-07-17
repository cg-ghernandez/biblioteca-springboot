document.addEventListener("DOMContentLoaded", function () {
    const tabla = document.getElementById("tablaLibros").querySelector("tbody");
    const formulario = document.getElementById("formularioLibro");
    const selectCategoria = document.getElementById("categoria");

    let modoEdicion = false;
    let libroEditandoId = null;

    function cargarLibros() {
        fetch("http://localhost:8080/api/libros")
            .then(response => response.json())
            .then(data => {
                tabla.innerHTML = "";
                data.forEach(libro => {
                    const fila = document.createElement("tr");
                    fila.innerHTML = `
                        <td>${libro.id}</td>
                        <td>${libro.titulo}</td>
                        <td>${libro.autor}</td>
                        <td>${libro.isbn}</td>
                        <td>${libro.disponible ? "Sí" : "No"}</td>
                        <td>
                            <button onclick="editarLibro(${libro.id})">Editar</button>
                            <button onclick="eliminarLibro(${libro.id})">Eliminar</button>
                        </td>
                    `;
                    tabla.appendChild(fila);
                });
            })
            .catch(error => console.error("Error al cargar libros:", error));
    }

    function cargarCategorias() {
        fetch("http://localhost:8080/api/categorias")
            .then(response => response.json())
            .then(categorias => {
                selectCategoria.innerHTML = '<option value="">Seleccione una categoría</option>';
                categorias.forEach(cat => {
                    const option = document.createElement("option");
                    option.value = cat.id;
                    option.textContent = cat.nombre;
                    selectCategoria.appendChild(option);
                });
            })
            .catch(error => console.error("Error al cargar categorías:", error));
    }

    window.editarLibro = function (id) {
        fetch(`http://localhost:8080/api/libros/${id}`)
            .then(response => {
                if (!response.ok) throw new Error("Libro no encontrado");
                return response.json();
            })
            .then(libro => {
                document.getElementById("titulo").value = libro.titulo;
                document.getElementById("autor").value = libro.autor;
                document.getElementById("isbn").value = libro.isbn;
                selectCategoria.value = libro.categoria?.id || "";

                modoEdicion = true;
                libroEditandoId = libro.id;
            })
            .catch(error => alert(error.message));
    };

    window.eliminarLibro = function (id) {
        if (confirm("¿Estás seguro de eliminar este libro?")) {
            fetch(`http://localhost:8080/api/libros/${id}`, {
                method: "DELETE"
            })
                .then(() => cargarLibros())
                .catch(error => alert("Error al eliminar libro"));
        }
    };

    formulario.addEventListener("submit", function (e) {
        e.preventDefault();

        const libro = {
            titulo: document.getElementById("titulo").value,
            autor: document.getElementById("autor").value,
            isbn: document.getElementById("isbn").value,
            disponible: true,
            categoria: {
                id: parseInt(selectCategoria.value)
            }
        };

        if (modoEdicion) {
            fetch(`http://localhost:8080/api/libros/${libroEditandoId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(libro)
            })
                .then(response => {
                    if (!response.ok) throw new Error("No se pudo actualizar el libro");
                    return response.json();
                })
                .then(() => {
                    formulario.reset();
                    modoEdicion = false;
                    libroEditandoId = null;
                    cargarLibros();
                })
                .catch(error => alert(error.message));
        } else {
            fetch("http://localhost:8080/api/libros", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(libro)
            })
                .then(response => {
                    if (!response.ok) throw new Error("No se pudo registrar el libro");
                    return response.json();
                })
                .then(() => {
                    formulario.reset();
                    cargarLibros();
                })
                .catch(error => alert(error.message));
        }
    });

    cargarCategorias();
    cargarLibros();
});
