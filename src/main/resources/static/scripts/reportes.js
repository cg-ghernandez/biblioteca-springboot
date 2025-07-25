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

function exportarLibrosMasPrestadosPDF() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    doc.text("Reporte: Libros Más Prestados", 14, 15);
    doc.autoTable({
        head: [['#', 'Título', 'Autor', 'Total de Préstamos']],
        body: Array.from(document.querySelectorAll("#tablaLibrosMasPrestados tbody tr")).map((fila, index) => {
            return [
                fila.cells[0].innerText,
                fila.cells[1].innerText,
                fila.cells[2].innerText,
                fila.cells[3].innerText
            ];
        }),
        startY: 20
    });

    doc.save("libros_mas_prestados.pdf");
}

function exportarUsuariosMasActivosPDF() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    doc.text("Reporte: Usuarios Más Activos", 14, 15);
    doc.autoTable({
        head: [['#', 'Nombre', 'Email', 'Total de Préstamos']],
        body: Array.from(document.querySelectorAll("#tablaUsuariosMasActivos tbody tr")).map((fila, index) => {
            return [
                fila.cells[0].innerText,
                fila.cells[1].innerText,
                fila.cells[2].innerText,
                fila.cells[3].innerText
            ];
        }),
        startY: 20
    });

    doc.save("usuarios_mas_activos.pdf");
}
function exportarLibrosMasPrestadosExcel() {
    const tabla = document.getElementById("tablaLibrosMasPrestados");
    const wb = XLSX.utils.book_new();
    const ws = XLSX.utils.table_to_sheet(tabla);
    XLSX.utils.book_append_sheet(wb, ws, "Libros Más Prestados");
    XLSX.writeFile(wb, "libros_mas_prestados.xlsx");
}

function exportarUsuariosMasActivosExcel() {
    const tabla = document.getElementById("tablaUsuariosMasActivos");
    const wb = XLSX.utils.book_new();
    const ws = XLSX.utils.table_to_sheet(tabla);
    XLSX.utils.book_append_sheet(wb, ws, "Usuarios Más Activos");
    XLSX.writeFile(wb, "usuarios_mas_activos.xlsx");
}
