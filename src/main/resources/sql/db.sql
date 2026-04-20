-- =============================================
-- Base de datos: CustomerDB
-- =============================================

CREATE DATABASE CustomerDB;
GO

USE CustomerDB;
GO

-- =============================================
-- Tabla: customer
-- =============================================

CREATE TABLE customer (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    dni VARCHAR(20) NOT NULL,
    cellphone VARCHAR(20) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    active BIT DEFAULT 1,
    created_at DATETIME2,
    updated_at DATETIME2,
    deleted_at DATETIME2,
    restored_at DATETIME2
);

-- =============================================
-- Tabla: productos
-- =============================================

CREATE TABLE productos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    
    nombre NVARCHAR(255) NULL,
    nombre_tecnico NVARCHAR(255) NULL,
    
    precio DECIMAL(10,2) NULL,
    stock DECIMAL(10,2) NULL,
    
    estado BIT NULL,
    
    fecha_registro DATE NULL,
    
    created_at DATETIME2 NULL,
    updated_at DATETIME2 NULL,
    deleted_at DATETIME2 NULL,
    restored_at DATETIME2 NULL
);

-- =============================================
-- Datos de ejemplo
-- =============================================

INSERT INTO customer (dni, cellphone, first_name, last_name, active, created_at)
VALUES 
('74564578', '987654321', 'Luis', 'Lopez', 1, GETDATE()),
('87654321', '912345678', 'María', 'García', 1, GETDATE()),
('45678912', '999888777', 'Carlos', 'Ramirez', 1, GETDATE());

-- =============================================
-- Productos (frutas y verduras)
-- =============================================

INSERT INTO productos (nombre, nombre_tecnico, precio, stock, estado, fecha_registro, created_at)
VALUES ('Palta', 'Persea americana', 6.50, 100, 1, '2026-04-19', SYSDATETIME());

INSERT INTO productos (nombre, nombre_tecnico, precio, stock, estado, fecha_registro, created_at)
VALUES ('Mandarina', 'Citrus reticulata', 3.20, 200, 1, '2026-04-19', SYSDATETIME());

INSERT INTO productos (nombre, nombre_tecnico, precio, stock, estado, fecha_registro, created_at)
VALUES ('Arándanos', 'Vaccinium corymbosum', 12.00, 80, 1, '2026-04-19', SYSDATETIME());

INSERT INTO productos (nombre, nombre_tecnico, precio, stock, estado, fecha_registro, created_at)
VALUES ('Alberjas', 'Pisum sativum', 4.00, 150, 1, '2026-04-19', SYSDATETIME());

-- =============================================
-- Consultar datos
-- =============================================

SELECT * FROM customer;
SELECT * FROM productos;
GO