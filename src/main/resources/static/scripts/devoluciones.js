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
        fetch("/api/devoluciones")
            .then(res => res.json())
            .then(data => {
                data.forEach(dev => {
                    const diasRetraso = dev.conRetraso ? calcularDiasRetraso(dev.transaccion.fechaDevolucion, dev.fechaDevolucion) : 0;
                    const fila = document.createElement("tr");
                    fila.innerHTML = `
                        <td>${dev.id}</td>
                        <td>${dev.transaccion.libro?.titulo || 'N/A'}</td>
                        <td>${dev.transaccion.usuario?.nombre || 'N/A'}</td>
                        <td>${dev.fechaDevolucion}</td>
                        <td>${diasRetraso}</td>
                        <td>${dev.enBuenEstado ? 'No' : 'Sí'}</td>
                        <td>${dev.multa ? `₡${dev.multa.monto}` : 'Sin multa'}</td>
                    `;
                    tablaDevoluciones.appendChild(fila);
                });
            });
    }

    function calcularDiasRetraso(fechaEsperada, fechaReal) {
        const f1 = new Date(fechaEsperada);
        const f2 = new Date(fechaReal);
        const dif = f2 - f1;
        return dif > 0 ? Math.floor(dif / (1000 * 60 * 60 * 24)) : 0;
    }

    cargarTransaccionesPendientes();
    cargarDevoluciones();
});
