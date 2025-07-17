document.addEventListener("DOMContentLoaded", function () {
    const tabla = document.getElementById("tablaTransacciones").querySelector("tbody");
    const formulario = document.getElementById("formularioTransaccion");

    const selectUsuario = document.getElementById("usuario");
    const selectLibro = document.getElementById("libro");
    const inputFechaPrestamo = document.getElementById("fechaPrestamo");
    const inputFechaDevolucion = document.getElementById("fechaDevolucion");
    const selectTipo = document.getElementById("tipo");

    function ajustarCamposSegunTipo() {
        const tipo = selectTipo.value;

        if (tipo === "PRESTAMO") {
            inputFechaPrestamo.disabled = false;
            inputFechaPrestamo.required = true;
            inputFechaDevolucion.disabled = true;
            inputFechaDevolucion.required = false;
            inputFechaDevolucion.value = "";
        } else if (tipo === "DEVOLUCION") {
            inputFechaPrestamo.disabled = true;
            inputFechaPrestamo.required = false;
            inputFechaPrestamo.value = "";
            inputFechaDevolucion.disabled = false;
            inputFechaDevolucion.required = true;
        } else {
            inputFechaPrestamo.disabled = false;
            inputFechaDevolucion.disabled = false;
        }
    }

    selectTipo.addEventListener("change", ajustarCamposSegunTipo);

    function cargarUsuarios() {
        fetch("/api/usuarios")
            .then(res => res.json())
            .then(usuarios => {
                selectUsuario.innerHTML = '<option value="">Seleccione un usuario</option>';
                usuarios.forEach(u => {
                    const option = document.createElement("option");
                    option.value = u.id;
                    option.textContent = u.nombre;
                    selectUsuario.appendChild(option);
                });
            });
    }

    function cargarLibros() {
        fetch("/api/libros")
            .then(res => res.json())
            .then(libros => {
                selectLibro.innerHTML = '<option value="">Seleccione un libro</option>';
                libros.forEach(l => {
                    const option = document.createElement("option");
                    option.value = l.id;
                    option.textContent = l.titulo;
                    selectLibro.appendChild(option);
                });
            });
    }

    function cargarTransacciones() {
        fetch("/api/transacciones")
            .then(res => res.json())
            .then(data => {
                tabla.innerHTML = "";
                data.forEach(tx => {
                    const fila = document.createElement("tr");
                    fila.innerHTML = `
                        <td>${tx.id}</td>
                        <td>${tx.usuario?.nombre || 'N/A'}</td>
                        <td>${tx.libro?.titulo || 'N/A'}</td>
                        <td>${tx.fechaPrestamo || ''}</td>
                        <td>${tx.fechaDevolucion || ''}</td>
                        <td>${tx.tipo}</td>
                        <td><button onclick="eliminarTransaccion(${tx.id})">Eliminar</button></td>
                    `;
                    tabla.appendChild(fila);
                });
            });
    }

    formulario.addEventListener("submit", function (e) {
        e.preventDefault();

        const tipoSeleccionado = selectTipo.value;

        // Validaciones manuales adicionales
        if (!selectUsuario.value || !selectLibro.value || !tipoSeleccionado) {
            alert("Complete todos los campos obligatorios.");
            return;
        }

        if (tipoSeleccionado === "PRESTAMO" && !inputFechaPrestamo.value) {
            alert("Debe ingresar la fecha de préstamo.");
            return;
        }

        if (tipoSeleccionado === "DEVOLUCION" && !inputFechaDevolucion.value) {
            alert("Debe ingresar la fecha de devolución.");
            return;
        }

        const transaccion = {
            usuario: { id: parseInt(selectUsuario.value) },
            libro: { id: parseInt(selectLibro.value) },
            fechaPrestamo: inputFechaPrestamo.value || null,
            fechaDevolucion: inputFechaDevolucion.value || null,
            tipo: tipoSeleccionado
        };

        fetch("/api/transacciones", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(transaccion)
        })
            .then(res => {
                if (!res.ok) throw new Error("Error al registrar transacción");
                return res.json();
            })
            .then(() => {
                formulario.reset();
                ajustarCamposSegunTipo(); // Restaurar estado inicial
                cargarTransacciones();
            })
            .catch(error => alert(error.message));
    });

    window.eliminarTransaccion = function (id) {
        if (confirm("¿Eliminar esta transacción?")) {
            fetch(`/api/transacciones/${id}`, { method: "DELETE" })
                .then(() => cargarTransacciones())
                .catch(() => alert("No se pudo eliminar la transacción"));
        }
    };

    cargarUsuarios();
    cargarLibros();
    cargarTransacciones();
    ajustarCamposSegunTipo(); // Ejecutar al inicio
});
