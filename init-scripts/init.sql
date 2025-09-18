-- Conectar a la base de datos authentication
\c authentication;

-- Crear extensiones necesarias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Crear esquemas si son necesarios
-- CREATE SCHEMA IF NOT EXISTS auth;

-- Crear tabla roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200)
);

-- Crear tabla users
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birth_date DATE,
    address VARCHAR(200),
    phone VARCHAR(50),
    email VARCHAR(150),
    base_salary INTEGER,
    identification_number VARCHAR(50),
    password VARCHAR(100),
    role_id BIGINT,
    CONSTRAINT fk_users_roles FOREIGN KEY (role_id) REFERENCES roles(id)
        ON UPDATE CASCADE ON DELETE SET NULL
);

-- Índice para la FK
CREATE INDEX IF NOT EXISTS idx_users_role_id ON users (role_id);

-- Insertar datos de prueba (opcional)
-- Insert the specific roles for your system
INSERT INTO roles (id, name, description) VALUES
(1, 'ADMIN', 'Rol con permisos de administrador'),
(2, 'ASESOR', 'Rol para asesores'),
(3, 'CLIENTE', 'Rol para clientes')
ON CONFLICT (id) DO NOTHING;

-- Reset the sequence to continue from the next available ID
SELECT setval('roles_id_seq', 3, true);