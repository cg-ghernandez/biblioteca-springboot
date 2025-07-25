document.addEventListener("DOMContentLoaded", () => {
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
                    <td>${libro.autor}</td>
                    <td>${libro.cantidadPrestamos}</td>
                `;
                tabla.appendChild(fila);
            });
        })
        .catch(error => {
            console.error("Error al cargar el reporte:", error);
        });
});
