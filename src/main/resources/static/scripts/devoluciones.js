document.addEventListener("DOMContentLoaded", function () {
    const transaccionSelect = document.getElementById("transaccionSelect");
    const fechaDevolucionInput = document.getElementById("fechaDevolucion");
    const enBuenEstadoCheckbox = document.getElementById("enBuenEstado");
    const formulario = document.getElementById("formularioDevolucion");
    const tablaDevoluciones = document.getElementById("tablaDevoluciones").querySelector("tbody");

    function cargarTransaccionesPendientes() {
        transaccionSelect.innerHTML = "<option value=''>Seleccione una transacción</option>";
        fetch("/api/transacciones")
            .then(res => res.json())
            .then(transacciones => {
                fetch("/api/devoluciones")
                    .then(res => res.json())
                    .then(devoluciones => {
                        const transaccionesConDevolucion = devoluciones.map(d => d.transaccion.id);
                        transacciones
                            .filter(t => t.estado === "PRESTADO" && !transaccionesConDevolucion.includes(t.id))
                            .forEach(t => {
                                const opcion = document.createElement("option");
                                opcion.value = t.id;
                                opcion.textContent = `${t.libro.titulo} - ${t.usuario.nombre}`;
                                transaccionSelect.appendChild(opcion);
                            });
                    });
            });
    }

    formulario.addEventListener("submit", function (e) {
        e.preventDefault();

        const transaccionId = transaccionSelect.value;
        const fechaDevolucion = fechaDevolucionInput.value;
        const enBuenEstado = enBuenEstadoCheckbox.checked;

        if (!transaccionId || !fechaDevolucion) {
            alert("Por favor complete todos los campos");
            return;
        }

        const devolucion = {
            transaccion: { id: transaccionId },
            fechaDevolucion: fechaDevolucion,
            enBuenEstado: enBuenEstado
        };

        fetch("/api/devoluciones", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(devolucion)
        })
            .then(res => {
                if (!res.ok) throw new Error("Error al registrar la devolución");
                return res.json();
            })
            .then(() => {
                alert("Devolución registrada con éxito");
                formulario.reset();
                cargarTransaccionesPendientes();
                cargarDevoluciones();
            })
            .catch(error => {
                alert("Error: " + error.message);
            });
    });

    function cargarDevoluciones() {
        tablaDevoluciones.innerHTML = "";
        fetch("/api/devoluciones/historial")
            .then(res => res.json())
            .then(data => {
                data.forEach(dev => {
                    const fila = document.createElement("tr");
                    fila.innerHTML = `
                        <td>${dev.id}</td>
                        <td>${dev.libro}</td>
                        <td>${dev.usuario}</td>
                        <td>${dev.fechaDevolucion}</td>
                        <td>${dev.diasRetraso}</td>
                        <td>${dev.enBuenEstado ? 'No' : 'Sí'}</td>
                        <td>${dev.multaMonto ? `₡${dev.multaMonto} (${dev.multaPagada ? 'Pagada' : 'Pendiente'})` : 'Sin multa'}</td>
                    `;
                    tablaDevoluciones.appendChild(fila);
                });
            });
    }

    cargarTransaccionesPendientes();
    cargarDevoluciones();
});
