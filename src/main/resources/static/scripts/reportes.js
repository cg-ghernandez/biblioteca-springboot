document.addEventListener("DOMContentLoaded", () => {
    cargarLibrosMasPrestados();
    cargarUsuariosMasActivos();
});

function cargarLibrosMasPrestados() {
    fetch("/api/reportes/libros-mas-prestados")
        .then(res => res.json())
        .then(data => {
            const tabla = document.getElementById("tablaLibrosMasPrestados").querySelector("tbody");
            tabla.innerHTML = "";

            data.forEach((libro, index) => {
                const fila = document.createElement("tr");
                fila.innerHTML = `
                    <td>${index + 1}</td>
                    <td>${libro.titulo}</td>
                    <td>${libro.autor || 'N/A'}</td>
                    <td>${libro.cantidadPrestamos}</td>
                `;
                tabla.appendChild(fila);
            });
        })
        .catch(error => {
            console.error("Error al cargar el reporte de libros:", error);
        });
}

function cargarUsuariosMasActivos() {
    fetch("/api/reportes/usuarios-mas-activos")
        .then(res => res.json())
        .then(data => {
            const tabla = document.getElementById("tablaUsuariosMasActivos").querySelector("tbody");
            tabla.innerHTML = "";

            data.forEach((usuario, index) => {
                const fila = document.createElement("tr");
                fila.innerHTML = `
                    <td>${index + 1}</td>
                    <td>${usuario.nombre}</td>
                    <td>${usuario.email}</td>
                    <td>${usuario.cantidadPrestamos}</td>
                `;
                tabla.appendChild(fila);
            });
        })
        .catch(error => {
            console.error("Error al cargar reporte de usuarios:", error);
        });
}
