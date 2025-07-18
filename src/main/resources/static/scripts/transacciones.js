document.addEventListener("DOMContentLoaded", function () {
    const tabla = document.getElementById("tablaTransacciones").querySelector("tbody");
    const formulario = document.getElementById("formularioTransaccion");

    const selectUsuario = document.getElementById("usuario");
    const selectLibro = document.getElementById("libro");
    const selectBibliotecario = document.getElementById("bibliotecario");
    const inputFechaPrestamo = document.getElementById("fechaPrestamo");
    const inputFechaDevolucion = document.getElementById("fechaDevolucion");

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

    function cargarLibrosDisponibles() {
        fetch("/api/libros/disponibles")
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

    function cargarBibliotecarios() {
        fetch("/api/bibliotecarios")
            .then(res => res.json())
            .then(data => {
                selectBibliotecario.innerHTML = '<option value="">Seleccione un bibliotecario</option>';
                data.forEach(b => {
                    const option = document.createElement("option");
                    option.value = b.id;
                    option.textContent = b.nombre;
                    selectBibliotecario.appendChild(option);
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
                        <td>${tx.bibliotecario?.nombre || 'N/A'}</td>
                        <td>${tx.fechaPrestamo || ''}</td>
                        <td>${tx.fechaDevolucion || ''}</td>
                        <td>${tx.tipo}</td>
                        <td>${tx.estado}</td>
                        <td><button onclick="eliminarTransaccion(${tx.id})">🗑️ Eliminar</button></td>
                    `;
                    tabla.appendChild(fila);
                });
            });
    }

    formulario.addEventListener("submit", function (e) {
        e.preventDefault();

        const transaccion = {
            usuario: { id: parseInt(selectUsuario.value) },
            libro: { id: parseInt(selectLibro.value) },
            bibliotecario: { id: parseInt(selectBibliotecario.value) },
            fechaPrestamo: inputFechaPrestamo.value || null,
            fechaDevolucion: inputFechaDevolucion.value || null,
            tipo: "PRESTAMO" // fijo como préstamo
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
                cargarLibrosDisponibles(); // recargar libros disponibles
                cargarTransacciones();
            })
            .catch(error => alert(error.message));
    });

    window.eliminarTransaccion = function (id) {
        if (confirm("¿Eliminar esta transacción?")) {
            fetch(`/api/transacciones/${id}`, { method: "DELETE" })
                .then(() => {
                    cargarLibrosDisponibles(); // recargar libros disponibles
                    cargarTransacciones();
                })
                .catch(() => alert("No se pudo eliminar la transacción"));
        }
    };

    // Cargar datos al iniciar
    cargarUsuarios();
    cargarLibrosDisponibles();
    cargarBibliotecarios();
    cargarTransacciones();
});
