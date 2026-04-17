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
GO

-- =============================================
-- Datos de ejemplo
-- =============================================

INSERT INTO customer (dni, cellphone, first_name, last_name, active, created_at)
VALUES 
('74564578', '987654321', 'Luis', 'Lopez', 1, GETDATE()),
('87654321', '912345678', 'María', 'García', 1, GETDATE()),
('45678912', '999888777', 'Carlos', 'Ramirez', 1, GETDATE());
GO

-- =============================================
-- Consultar datos
-- =============================================

SELECT * FROM customer;
GO

