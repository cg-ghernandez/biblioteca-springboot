-- Usuarios
INSERT INTO usuario (id, nombre, email) VALUES (1, 'Ana López', 'ana@example.com');
INSERT INTO usuario (id, nombre, email) VALUES (2, 'Carlos Méndez', 'carlos@example.com');
INSERT INTO usuario (id, nombre, email) VALUES (3, 'Laura Ramírez', 'laura@example.com');

-- Libros
INSERT INTO libro (id, titulo, autor, isbn, disponible) VALUES (1, 'Cien años de soledad', 'Gabriel García Márquez', '978-3-16-148410-0', true);
INSERT INTO libro (id, titulo, autor, isbn, disponible) VALUES (2, '1984', 'George Orwell', '978-0-452-28423-4', true);

-- Transacción (Ana pide el libro 1)
INSERT INTO transaccion (id, usuario_id, libro_id, fecha_prestamo, fecha_devolucion, tipo)
VALUES (1, 1, 1, CURRENT_DATE, NULL, 'PRESTAMO');
