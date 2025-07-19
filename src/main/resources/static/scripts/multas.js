document.addEventListener("DOMContentLoaded", function () {
    const tablaMultas = document.getElementById("tablaMultas").querySelector("tbody");
    const filtroEstado = document.getElementById("filtroEstado");

    function cargarMultas() {
        fetch("/api/multas")
            .then(res => res.json())
            .then(multas => {
                mostrarMultasFiltradas(multas);
            });
    }

    function mostrarMultasFiltradas(multas) {
        const estadoSeleccionado = filtroEstado.value;

        let multasFiltradas = multas;
        if (estadoSeleccionado === "PAGADAS") {
            multasFiltradas = multas.filter(m => m.pagada === true);
        } else if (estadoSeleccionado === "NOPAGADAS") {
            multasFiltradas = multas.filter(m => m.pagada === false);
        }

        tablaMultas.innerHTML = "";
        multasFiltradas.forEach(multa => {
            const fila = document.createElement("tr");
            fila.innerHTML = `
                <td>${multa.id}</td>
                <td>${multa.devolucion.transaccion.usuario.nombre}</td>
                <td>${multa.devolucion.transaccion.libro.titulo}</td>
                <td>${multa.devolucion.fechaDevolucion}</td>
                <td>₡${multa.monto}</td>
                <td>${multa.pagada ? 'Pagada' : 'Pendiente'}</td>
                <td>
                    ${multa.pagada ? '-' : `<button class="marcarPagadaBtn" data-id="${multa.id}">Marcar como pagada</button>`}
                </td>
            `;
            tablaMultas.appendChild(fila);
        });

        // Reasignar eventos
        document.querySelectorAll(".marcarPagadaBtn").forEach(btn => {
            btn.addEventListener("click", () => {
                const id = btn.dataset.id;
                marcarComoPagada(id);
            });
        });
    }

    function marcarComoPagada(id) {
        fetch(`/api/multas/${id}`)
            .then(res => res.json())
            .then(multa => {
                multa.pagada = true;
                multa.fechaPago = new Date().toISOString().split("T")[0];

                fetch(`/api/multas/${id}`, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(multa)
                })
                    .then(res => {
                        if (!res.ok) throw new Error("Error al actualizar la multa");
                        return res.json();
                    })
                    .then(() => {
                        alert("Multa marcada como pagada");
                        cargarMultas();
                    })
                    .catch(error => {
                        alert("Error: " + error.message);
                    });
            });
    }

    filtroEstado.addEventListener("change", cargarMultas);

    cargarMultas();
});
