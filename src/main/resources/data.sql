-- Categorías
INSERT INTO categoria (id, nombre) VALUES (1, 'Ficción');
INSERT INTO categoria (id, nombre) VALUES (2, 'Tecnología');

-- Libros
INSERT INTO libro (id, titulo, autor, isbn, disponible, categoria_id)
VALUES (1, 'El Principito', 'Saint-Exupéry', '123456789', true, 1);
INSERT INTO libro (id, titulo, autor, isbn, disponible, categoria_id)
VALUES (2, 'Spring Boot Avanzado', 'Celina Dev', '987654321', true, 2);

-- Usuarios
INSERT INTO usuario (id, nombre, email)
VALUES (1, 'Gerald Hernandez Solis', 'gher@gh.com');

-- Bibliotecarios
INSERT INTO bibliotecario (id, nombre, email, telefono)
VALUES (1, 'Ana Maria López', 'ana@usm.cr', '8888-8888');
