-- ============================================================
-- AVSA Database: avsa_db (SQL Server)
-- Schema + Soft Delete + Restore Procedures
-- ============================================================

-- ============================================================
-- Database
-- ============================================================
IF DB_ID('avsa_db') IS NULL
BEGIN
    CREATE DATABASE avsa_db;
END
GO

USE avsa_db;
GO

-- ============================================================
-- Table: agrochemical
-- ============================================================
IF OBJECT_ID('dbo.agrochemical', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.agrochemical (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        commercial_name NVARCHAR(200) NOT NULL,
        active_ingredient NVARCHAR(200) NOT NULL,
        category NVARCHAR(50) NOT NULL,
        senasa_registration_number NVARCHAR(50) NULL,
        registration_expiry DATE NOT NULL,
        max_dose DECIMAL(10,4) NOT NULL,
        waiting_period_days INT NOT NULL,
        manufacturer NVARCHAR(200) NULL,
        active BIT NOT NULL,
        created_at DATETIME2 NULL,
        updated_at DATETIME2 NULL,
        deleted_at DATETIME2 NULL,
        restored_at DATETIME2 NULL
    );
    ALTER TABLE dbo.agrochemical
        ADD CONSTRAINT CK_agrochemical_category
        CHECK (category IN ('INSECTICIDE','FUNGICIDE','HERBICIDE','FERTILIZER','BIOSTIMULANT','OTHER'));
END
GO

-- ============================================================
-- Table: app_users
-- ============================================================
IF OBJECT_ID('dbo.app_users', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.app_users (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        username NVARCHAR(50) NOT NULL UNIQUE,
        password_hash NVARCHAR(255) NOT NULL,
        full_name NVARCHAR(200) NOT NULL,
        email NVARCHAR(100) NOT NULL UNIQUE,
        role NVARCHAR(30) NOT NULL,
        active BIT NOT NULL,
        last_login DATETIME2 NULL,
        created_at DATETIME2 NULL,
        updated_at DATETIME2 NULL,
        deleted_at DATETIME2 NULL,
        restored_at DATETIME2 NULL
    );
    ALTER TABLE dbo.app_users
        ADD CONSTRAINT CK_app_users_role
        CHECK (role IN ('ROLE_ADMIN','ROLE_SUPERVISOR','ROLE_TECHNICIAN','ROLE_LOGISTICS','ROLE_CERTIFICACIONES'));
END
GO

-- ============================================================
-- Table: producers
-- ============================================================
IF OBJECT_ID('dbo.producers', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.producers (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        full_name NVARCHAR(200) NOT NULL,
        dni NVARCHAR(20) NOT NULL UNIQUE,
        phone NVARCHAR(20) NULL,
        email NVARCHAR(100) NULL,
        location NVARCHAR(200) NOT NULL,
        district NVARCHAR(100) NULL,
        province NVARCHAR(100) NULL,
        region NVARCHAR(100) NULL,
        active BIT NOT NULL,
        qr_token NVARCHAR(100) NULL UNIQUE,
        created_at DATETIME2 NULL,
        updated_at DATETIME2 NULL,
        deleted_at DATETIME2 NULL,
        restored_at DATETIME2 NULL
    );
END
GO

-- ============================================================
-- Table: fields
-- ============================================================
IF OBJECT_ID('dbo.fields', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.fields (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        producer_id BIGINT NOT NULL,
        name NVARCHAR(150) NOT NULL,
        area_hectares FLOAT NOT NULL,
        active_crop NVARCHAR(100) NOT NULL,
        location NVARCHAR(200) NULL,
        active BIT NOT NULL,
        qr_token NVARCHAR(100) NULL UNIQUE,
        created_at DATETIME2 NULL,
        updated_at DATETIME2 NULL,
        deleted_at DATETIME2 NULL,
        restored_at DATETIME2 NULL
    );
    ALTER TABLE dbo.fields
        ADD CONSTRAINT FK_fields_producer
        FOREIGN KEY (producer_id) REFERENCES dbo.producers(id);
END
GO
CREATE INDEX IX_fields_producer_id ON dbo.fields(producer_id);
GO

-- ============================================================
-- Table: production_lots
-- ============================================================
IF OBJECT_ID('dbo.production_lots', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.production_lots (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        field_id BIGINT NOT NULL,
        code NVARCHAR(50) NOT NULL UNIQUE,
        start_date DATE NOT NULL,
        end_date DATE NULL,
        crop NVARCHAR(100) NOT NULL,
        status NVARCHAR(20) NOT NULL,
        observations NVARCHAR(500) NULL,
        active BIT NOT NULL,
        created_at DATETIME2 NULL,
        updated_at DATETIME2 NULL,
        deleted_at DATETIME2 NULL,
        restored_at DATETIME2 NULL
    );
    ALTER TABLE dbo.production_lots
        ADD CONSTRAINT FK_production_lots_field
        FOREIGN KEY (field_id) REFERENCES dbo.fields(id);

    ALTER TABLE dbo.production_lots
        ADD CONSTRAINT CK_production_lots_status
        CHECK (status IN ('ACTIVE','CLOSED','CANCELLED'));
END
GO
CREATE INDEX IX_production_lots_field_id ON dbo.production_lots(field_id);
GO

-- ============================================================
-- Table: field_applications
-- ============================================================
IF OBJECT_ID('dbo.field_applications', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.field_applications (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        producer_id BIGINT NOT NULL,
        field_id BIGINT NOT NULL,
        lot_id BIGINT NOT NULL,
        agrochemical_id BIGINT NOT NULL,
        dose FLOAT NOT NULL,
        dose_unit NVARCHAR(30) NOT NULL,
        application_date DATE NOT NULL,
        technician_id BIGINT NOT NULL,
        observations NVARCHAR(500) NULL,
        senasa_valid BIT NULL,
        dose_exceeded BIT NULL,
        active BIT NOT NULL,
        created_at DATETIME2 NULL,
        updated_at DATETIME2 NULL,
        deleted_at DATETIME2 NULL,
        restored_at DATETIME2 NULL
    );
    ALTER TABLE dbo.field_applications
        ADD CONSTRAINT FK_field_applications_producer
        FOREIGN KEY (producer_id) REFERENCES dbo.producers(id);

    ALTER TABLE dbo.field_applications
        ADD CONSTRAINT FK_field_applications_field
        FOREIGN KEY (field_id) REFERENCES dbo.fields(id);

    ALTER TABLE dbo.field_applications
        ADD CONSTRAINT FK_field_applications_lot
        FOREIGN KEY (lot_id) REFERENCES dbo.production_lots(id);

    ALTER TABLE dbo.field_applications
        ADD CONSTRAINT FK_field_applications_agrochemical
        FOREIGN KEY (agrochemical_id) REFERENCES dbo.agrochemical(id);

    ALTER TABLE dbo.field_applications
        ADD CONSTRAINT FK_field_applications_technician
        FOREIGN KEY (technician_id) REFERENCES dbo.app_users(id);
END
GO
CREATE INDEX IX_field_applications_producer_id ON dbo.field_applications(producer_id);
CREATE INDEX IX_field_applications_field_id ON dbo.field_applications(field_id);
CREATE INDEX IX_field_applications_lot_id ON dbo.field_applications(lot_id);
CREATE INDEX IX_field_applications_agrochemical_id ON dbo.field_applications(agrochemical_id);
CREATE INDEX IX_field_applications_technician_id ON dbo.field_applications(technician_id);
GO

-- ============================================================
-- Table: inventory_deliveries
-- ============================================================
IF OBJECT_ID('dbo.inventory_deliveries', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.inventory_deliveries (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        producer_id BIGINT NOT NULL,
        agrochemical_id BIGINT NOT NULL,
        quantity_delivered FLOAT NOT NULL,
        unit NVARCHAR(30) NOT NULL,
        delivery_date DATE NOT NULL,
        responsible_id BIGINT NOT NULL,
        observations NVARCHAR(500) NULL,
        active BIT NOT NULL,
        created_at DATETIME2 NULL,
        deleted_at DATETIME2 NULL,
        restored_at DATETIME2 NULL
    );
    ALTER TABLE dbo.inventory_deliveries
        ADD CONSTRAINT FK_inventory_deliveries_producer
        FOREIGN KEY (producer_id) REFERENCES dbo.producers(id);

    ALTER TABLE dbo.inventory_deliveries
        ADD CONSTRAINT FK_inventory_deliveries_agrochemical
        FOREIGN KEY (agrochemical_id) REFERENCES dbo.agrochemical(id);

    ALTER TABLE dbo.inventory_deliveries
        ADD CONSTRAINT FK_inventory_deliveries_responsible
        FOREIGN KEY (responsible_id) REFERENCES dbo.app_users(id);
END
GO
CREATE INDEX IX_inventory_deliveries_producer_id ON dbo.inventory_deliveries(producer_id);
CREATE INDEX IX_inventory_deliveries_agrochemical_id ON dbo.inventory_deliveries(agrochemical_id);
CREATE INDEX IX_inventory_deliveries_responsible_id ON dbo.inventory_deliveries(responsible_id);
GO

-- ============================================================
-- Table: alerts
-- ============================================================
IF OBJECT_ID('dbo.alerts', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.alerts (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        type NVARCHAR(50) NOT NULL,
        severity NVARCHAR(10) NOT NULL,
        title NVARCHAR(200) NOT NULL,
        description NVARCHAR(500) NOT NULL,
        producer_id BIGINT NULL,
        field_id BIGINT NULL,
        agrochemical_id BIGINT NULL,
        status NVARCHAR(20) NOT NULL,
        reviewed_by NVARCHAR(100) NULL,
        review_observation NVARCHAR(500) NULL,
        reviewed_at DATETIME2 NULL,
        active BIT NOT NULL,
        created_at DATETIME2 NULL,
        deleted_at DATETIME2 NULL,
        restored_at DATETIME2 NULL
    );

    ALTER TABLE dbo.alerts
        ADD CONSTRAINT FK_alerts_producer
        FOREIGN KEY (producer_id) REFERENCES dbo.producers(id);

    ALTER TABLE dbo.alerts
        ADD CONSTRAINT FK_alerts_field
        FOREIGN KEY (field_id) REFERENCES dbo.fields(id);

    ALTER TABLE dbo.alerts
        ADD CONSTRAINT FK_alerts_agrochemical
        FOREIGN KEY (agrochemical_id) REFERENCES dbo.agrochemical(id);

    ALTER TABLE dbo.alerts
        ADD CONSTRAINT CK_alerts_type
        CHECK (type IN ('NO_APPLICATION_REGISTERED','DOSE_EXCEEDED','LOW_STOCK','SENASA_EXPIRING','SENASA_EXPIRED'));

    ALTER TABLE dbo.alerts
        ADD CONSTRAINT CK_alerts_severity
        CHECK (severity IN ('HIGH','MEDIUM','LOW'));

    ALTER TABLE dbo.alerts
        ADD CONSTRAINT CK_alerts_status
        CHECK (status IN ('PENDING','REVIEWED','DISMISSED'));
END
GO
CREATE INDEX IX_alerts_producer_id ON dbo.alerts(producer_id);
CREATE INDEX IX_alerts_field_id ON dbo.alerts(field_id);
CREATE INDEX IX_alerts_agrochemical_id ON dbo.alerts(agrochemical_id);
GO

-- ============================================================
-- Table: audit_logs
-- ============================================================
IF OBJECT_ID('dbo.audit_logs', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.audit_logs (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        user_id BIGINT NOT NULL,
        user_name NVARCHAR(100) NOT NULL,
        action NVARCHAR(100) NOT NULL,
        module NVARCHAR(50) NOT NULL,
        entity_id BIGINT NULL,
        details NVARCHAR(1000) NULL,
        ip_address NVARCHAR(50) NULL,
        [timestamp] DATETIME2 NOT NULL,
        active BIT NOT NULL,
        deleted_at DATETIME2 NULL,
        restored_at DATETIME2 NULL
    );
END
GO

-- ============================================================
-- Table: customer
-- ============================================================
IF OBJECT_ID('dbo.customer', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.customer (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        codigo NVARCHAR(50) NOT NULL UNIQUE,
        razon_social NVARCHAR(200) NOT NULL,
        ruc NVARCHAR(20) NULL,
        tipo_documento NVARCHAR(30) NOT NULL,
        numero_documento NVARCHAR(50) NOT NULL,
        pais NVARCHAR(100) NOT NULL,
        ciudad NVARCHAR(100) NULL,
        direccion NVARCHAR(200) NULL,
        telefono NVARCHAR(30) NULL,
        email NVARCHAR(100) NULL,
        contacto_principal NVARCHAR(150) NULL,
        tipo_cliente NVARCHAR(30) NOT NULL,
        active BIT NOT NULL,
        created_at DATETIME2 NULL,
        updated_at DATETIME2 NULL,
        deleted_at DATETIME2 NULL,
        restored_at DATETIME2 NULL
    );
END
GO

-- ============================================================
-- Table: productos
-- ============================================================
IF OBJECT_ID('dbo.productos', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.productos (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        codigo NVARCHAR(50) NOT NULL UNIQUE,
        nombre NVARCHAR(200) NOT NULL,
        descripcion NVARCHAR(500) NULL,
        categoria NVARCHAR(50) NOT NULL,
        unidad_medida NVARCHAR(30) NOT NULL,
        precio_unitario DECIMAL(10,2) NOT NULL,
        stock_actual INT NOT NULL,
        stock_minimo INT NOT NULL,
        origen NVARCHAR(200) NOT NULL,
        estado BIT NOT NULL,
        created_at DATETIME2 NULL,
        updated_at DATETIME2 NULL,
        deleted_at DATETIME2 NULL,
        restored_at DATETIME2 NULL
    );
END
GO

-- ============================================================
-- Table: productores
-- ============================================================
IF OBJECT_ID('dbo.productores', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.productores (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        nombre_completo NVARCHAR(255) NULL,
        dni NVARCHAR(255) NULL,
        telefono NVARCHAR(255) NULL,
        correo NVARCHAR(255) NULL,
        ubicacion NVARCHAR(255) NULL,
        distrito NVARCHAR(255) NULL,
        estado BIT NULL,
        fecha_creacion DATETIME2 NULL,
        fecha_actualizacion DATETIME2 NULL,
        fecha_eliminacion DATETIME2 NULL,
        fecha_restauracion DATETIME2 NULL,
        active BIT NOT NULL,
        deleted_at DATETIME2 NULL,
        restored_at DATETIME2 NULL
    );
END
GO

-- ============================================================
-- Defaults for active
-- ============================================================
IF OBJECT_ID('DF_agrochemical_active', 'D') IS NULL
    ALTER TABLE dbo.agrochemical ADD CONSTRAINT DF_agrochemical_active DEFAULT 1 FOR active;
IF OBJECT_ID('DF_app_users_active', 'D') IS NULL
    ALTER TABLE dbo.app_users ADD CONSTRAINT DF_app_users_active DEFAULT 1 FOR active;
IF OBJECT_ID('DF_producers_active', 'D') IS NULL
    ALTER TABLE dbo.producers ADD CONSTRAINT DF_producers_active DEFAULT 1 FOR active;
IF OBJECT_ID('DF_fields_active', 'D') IS NULL
    ALTER TABLE dbo.fields ADD CONSTRAINT DF_fields_active DEFAULT 1 FOR active;
IF OBJECT_ID('DF_production_lots_active', 'D') IS NULL
    ALTER TABLE dbo.production_lots ADD CONSTRAINT DF_production_lots_active DEFAULT 1 FOR active;
IF OBJECT_ID('DF_field_applications_active', 'D') IS NULL
    ALTER TABLE dbo.field_applications ADD CONSTRAINT DF_field_applications_active DEFAULT 1 FOR active;
IF OBJECT_ID('DF_inventory_deliveries_active', 'D') IS NULL
    ALTER TABLE dbo.inventory_deliveries ADD CONSTRAINT DF_inventory_deliveries_active DEFAULT 1 FOR active;
IF OBJECT_ID('DF_alerts_active', 'D') IS NULL
    ALTER TABLE dbo.alerts ADD CONSTRAINT DF_alerts_active DEFAULT 1 FOR active;
IF OBJECT_ID('DF_audit_logs_active', 'D') IS NULL
    ALTER TABLE dbo.audit_logs ADD CONSTRAINT DF_audit_logs_active DEFAULT 1 FOR active;
IF OBJECT_ID('DF_customer_active', 'D') IS NULL
    ALTER TABLE dbo.customer ADD CONSTRAINT DF_customer_active DEFAULT 1 FOR active;
IF OBJECT_ID('DF_productos_active', 'D') IS NULL
    ALTER TABLE dbo.productos ADD CONSTRAINT DF_productos_active DEFAULT 1 FOR estado;
IF OBJECT_ID('DF_productores_active', 'D') IS NULL
    ALTER TABLE dbo.productores ADD CONSTRAINT DF_productores_active DEFAULT 1 FOR active;
GO

-- ============================================================
-- Stored Procedures: Soft Delete / Restore
-- ============================================================
IF OBJECT_ID('dbo.sp_soft_delete', 'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_soft_delete;
GO

CREATE PROCEDURE dbo.sp_soft_delete
    @table NVARCHAR(128),
    @id BIGINT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @sql NVARCHAR(MAX);

    SET @sql = N'
        UPDATE ' + QUOTENAME(@table) + N'
        SET active = 0,
            deleted_at = SYSUTCDATETIME(),
            restored_at = NULL
        WHERE id = @id;
    ';

    EXEC sp_executesql @sql, N'@id BIGINT', @id;
END
GO

IF OBJECT_ID('dbo.sp_restore', 'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_restore;
GO

CREATE PROCEDURE dbo.sp_restore
    @table NVARCHAR(128),
    @id BIGINT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @sql NVARCHAR(MAX);

    SET @sql = N'
        UPDATE ' + QUOTENAME(@table) + N'
        SET active = 1,
            restored_at = SYSUTCDATETIME(),
            deleted_at = NULL
        WHERE id = @id;
    ';

    EXEC sp_executesql @sql, N'@id BIGINT', @id;
END
GO

-- ============================================================
-- SEED DATA: 20 rows per table
-- ============================================================

-- ============================================================
-- INSERT: agrochemical (20 rows)
-- ============================================================
INSERT INTO dbo.agrochemical (commercial_name, active_ingredient, category, senasa_registration_number, registration_expiry, max_dose, waiting_period_days, manufacturer, active, created_at)
VALUES
('Confidor 350 SC', 'Imidacloprid', 'INSECTICIDE', 'PQUA-01-2021', '2027-06-15', 0.5000, 14, 'Bayer CropScience', 1, SYSUTCDATETIME()), ('Amistar Top', 'Azoxystrobin + Difenoconazole', 'FUNGICIDE', 'PQUA-02-2021', '2027-08-20', 1.0000, 21, 'Syngenta', 1, SYSUTCDATETIME()), ('Roundup Max', 'Glifosato', 'HERBICIDE', 'PQUA-03-2020', '2026-12-31', 3.0000, 30, 'Monsanto', 1, SYSUTCDATETIME()), ('Karate Zeon', 'Lambda-Cihalotrina', 'INSECTICIDE', 'PQUA-04-2022', '2028-03-10', 0.4000, 7, 'Syngenta', 1, SYSUTCDATETIME()), ('Bayfolan Forte', 'NPK + Microelementos', 'FERTILIZER', 'PQUA-05-2021', '2027-05-01', 5.0000, 0, 'Bayer CropScience', 1, SYSUTCDATETIME()),
('Trichoderma Harzianum', 'Trichoderma harzianum', 'BIOSTIMULANT', 'PQUA-06-2023', '2029-01-15', 2.0000, 0, 'BioAgro Peru', 1, SYSUTCDATETIME()), ('Lannate 90 SP', 'Metomilo', 'INSECTICIDE', 'PQUA-07-2020', '2026-09-30', 0.3000, 14, 'DuPont', 1, SYSUTCDATETIME()), ('Score 250 EC', 'Difenoconazole', 'FUNGICIDE', 'PQUA-08-2022', '2028-07-22', 0.5000, 14, 'Syngenta', 1, SYSUTCDATETIME()), ('Paraquat Gramoxone', 'Paraquat', 'HERBICIDE', 'PQUA-09-2021', '2027-04-18', 2.5000, 21, 'Syngenta', 1, SYSUTCDATETIME()), ('Engeo 247 SC', 'Tiametoxam + Lambda-Cihalotrina', 'INSECTICIDE', 'PQUA-10-2023', '2029-02-28', 0.3000, 10, 'Syngenta', 1, SYSUTCDATETIME()),
('Mancozeb 80 WP', 'Mancozeb', 'FUNGICIDE', 'PQUA-11-2020', '2026-11-15', 2.5000, 14, 'Dow AgroSciences', 1, SYSUTCDATETIME()), ('Glufosinato Basta', 'Glufosinato de amonio', 'HERBICIDE', 'PQUA-12-2022', '2028-06-10', 2.0000, 21, 'BASF', 1, SYSUTCDATETIME()), ('Abamectina 1.8 EC', 'Abamectina', 'INSECTICIDE', 'PQUA-13-2021', '2027-09-05', 0.5000, 7, 'Arysta LifeScience', 1, SYSUTCDATETIME()), ('Carbendazim 500 SC', 'Carbendazim', 'FUNGICIDE', 'PQUA-14-2020', '2026-08-20', 1.0000, 14, 'Nufarm', 1, SYSUTCDATETIME()), ('Stimulate', 'Citoquininas + Giberelinas + Auxinas', 'BIOSTIMULANT', 'PQUA-15-2023', '2029-04-12', 0.5000, 0, 'Stoller', 1, SYSUTCDATETIME()),
('Sulfato de Potasio', 'K2SO4', 'FERTILIZER', 'PQUA-16-2022', '2028-10-30', 10.0000, 0, 'SQM', 1, SYSUTCDATETIME()), ('Acefato 75 SP', 'Acefato', 'INSECTICIDE', 'PQUA-17-2021', '2027-03-25', 1.0000, 14, 'United Phosphorus', 1, SYSUTCDATETIME()), ('Captan 50 WP', 'Captan', 'FUNGICIDE', 'PQUA-18-2022', '2028-12-01', 2.0000, 7, 'Arysta LifeScience', 1, SYSUTCDATETIME()), ('Aceite Agricola Citroliv', 'Aceite mineral', 'OTHER', 'PQUA-19-2021', '2027-07-14', 5.0000, 0, 'Bayer CropScience', 1, SYSUTCDATETIME()), ('Bacillus thuringiensis', 'Bt var. kurstaki', 'BIOSTIMULANT', 'PQUA-20-2023', '2029-05-20', 1.5000, 0, 'BioAgro Peru', 1, SYSUTCDATETIME());
GO

-- ============================================================
-- INSERT: app_users (20 rows)
-- ============================================================
INSERT INTO dbo.app_users (username, password_hash, full_name, email, role, active, created_at)
VALUES
('admin', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Carlos Mendoza Ríos', 'admin@avsa.pe', 'ROLE_ADMIN', 1, SYSUTCDATETIME()), ('supervisor1', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'María Elena Torres', 'mtorres@avsa.pe', 'ROLE_SUPERVISOR', 1, SYSUTCDATETIME()), ('tech_juan', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Juan Pérez Castro', 'jperez@avsa.pe', 'ROLE_TECHNICIAN', 1, SYSUTCDATETIME()), ('tech_rosa', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Rosa Huamán Valdez', 'rhuaman@avsa.pe', 'ROLE_TECHNICIAN', 1, SYSUTCDATETIME()), ('logistica1', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Pedro García Flores', 'pgarcia@avsa.pe', 'ROLE_LOGISTICS', 1, SYSUTCDATETIME()),
('cert_ana', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Ana Quispe Rojas', 'aquispe@avsa.pe', 'ROLE_CERTIFICACIONES', 1, SYSUTCDATETIME()), ('supervisor2', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Luis Fernández Prado', 'lfernandez@avsa.pe', 'ROLE_SUPERVISOR', 1, SYSUTCDATETIME()), ('tech_miguel', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Miguel Ángel Ramos', 'mramos@avsa.pe', 'ROLE_TECHNICIAN', 1, SYSUTCDATETIME()), ('logistica2', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Carmen Díaz Luna', 'cdiaz@avsa.pe', 'ROLE_LOGISTICS', 1, SYSUTCDATETIME()), ('admin2', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Jorge Vargas Medina', 'jvargas@avsa.pe', 'ROLE_ADMIN', 1, SYSUTCDATETIME()),
('tech_sofia', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Sofía Castillo Vera', 'scastillo@avsa.pe', 'ROLE_TECHNICIAN', 1, SYSUTCDATETIME()), ('cert_diego', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Diego Morales Soto', 'dmorales@avsa.pe', 'ROLE_CERTIFICACIONES', 1, SYSUTCDATETIME()), ('supervisor3', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Patricia Salazar Rojas', 'psalazar@avsa.pe', 'ROLE_SUPERVISOR', 1, SYSUTCDATETIME()), ('tech_andres', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Andrés Villanueva Cruz', 'avillanueva@avsa.pe', 'ROLE_TECHNICIAN', 1, SYSUTCDATETIME()), ('logistica3', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Lucía Espinoza Ramos', 'lespinoza@avsa.pe', 'ROLE_LOGISTICS', 1, SYSUTCDATETIME()),
('tech_daniela', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Daniela Herrera Vega', 'dherrera@avsa.pe', 'ROLE_TECHNICIAN', 1, SYSUTCDATETIME()), ('cert_raul', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Raúl Jiménez Paredes', 'rjimenez@avsa.pe', 'ROLE_CERTIFICACIONES', 1, SYSUTCDATETIME()), ('supervisor4', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Verónica Núñez Silva', 'vnunez@avsa.pe', 'ROLE_SUPERVISOR', 1, SYSUTCDATETIME()), ('tech_fernando', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Fernando Rojas Paz', 'frojas@avsa.pe', 'ROLE_TECHNICIAN', 1, SYSUTCDATETIME()), ('logistica4', '$2a$10$dXJ3SW6G7P50lGmMQgel6uN0K8ArLp0nO8PYtVhOHa0T6T1l1Jqr2', 'Isabel Chávez Montoya', 'ichavez@avsa.pe', 'ROLE_LOGISTICS', 1, SYSUTCDATETIME());
GO

-- ============================================================
-- INSERT: producers (20 rows)
-- ============================================================
INSERT INTO dbo.producers (full_name, dni, phone, email, location, district, province, region, active, qr_token, created_at)
VALUES
('Roberto Sánchez Luján', '45678901', '956123456', 'rsanchez@gmail.com', 'Fundo Santa Rosa Km 5', 'San Vicente', 'Cañete', 'Lima', 1, 'QR-PROD-001', SYSUTCDATETIME()), ('María Flores Huamán', '45678902', '956123457', 'mflores@gmail.com', 'Parcela 12 Valle Cañete', 'Imperial', 'Cañete', 'Lima', 1, 'QR-PROD-002', SYSUTCDATETIME()), ('José Luis Quispe Mamani', '45678903', '956123458', 'jquispe@gmail.com', 'Fundo La Esperanza', 'Lunahuaná', 'Cañete', 'Lima', 1, 'QR-PROD-003', SYSUTCDATETIME()), ('Ana Cristina Valdez Rojas', '45678904', '956123459', 'avaldez@gmail.com', 'Sector Herbay Alto', 'San Vicente', 'Cañete', 'Lima', 1, 'QR-PROD-004', SYSUTCDATETIME()), ('Pedro Antonio García López', '45678905', '956123460', 'pgarcia2@gmail.com', 'Fundo San Martín', 'Nuevo Imperial', 'Cañete', 'Lima', 1, 'QR-PROD-005', SYSUTCDATETIME()),
('Carmen Rosa Díaz Vega', '45678906', '956123461', 'cdiaz2@gmail.com', 'Parcela 28 Cerro Azul', 'Cerro Azul', 'Cañete', 'Lima', 1, 'QR-PROD-006', SYSUTCDATETIME()), ('Miguel Ángel Torres Prado', '45678907', '956123462', 'mtorres2@gmail.com', 'Fundo Los Olivos', 'Quilmaná', 'Cañete', 'Lima', 1, 'QR-PROD-007', SYSUTCDATETIME()), ('Lucía Fernández Castro', '45678908', '956123463', 'lfernandez2@gmail.com', 'Sector Hualcará', 'San Vicente', 'Cañete', 'Lima', 1, 'QR-PROD-008', SYSUTCDATETIME()), ('Eduardo Ramos Salazar', '45678909', '956123464', 'eramos@gmail.com', 'Fundo El Carmen', 'Asia', 'Cañete', 'Lima', 1, 'QR-PROD-009', SYSUTCDATETIME()), ('Sofía Castillo Mendoza', '45678910', '956123465', 'scastillo2@gmail.com', 'Parcela 45 Cañete Centro', 'San Vicente', 'Cañete', 'Lima', 1, 'QR-PROD-010', SYSUTCDATETIME()),
('Diego Morales Herrera', '45678911', '956123466', 'dmorales2@gmail.com', 'Fundo Vista Alegre', 'Imperial', 'Cañete', 'Lima', 1, 'QR-PROD-011', SYSUTCDATETIME()), ('Patricia Salazar Núñez', '45678912', '956123467', 'psalazar2@gmail.com', 'Sector Ungará', 'San Vicente', 'Cañete', 'Lima', 1, 'QR-PROD-012', SYSUTCDATETIME()), ('Andrés Villanueva Espinoza', '45678913', '956123468', 'avillanueva2@gmail.com', 'Fundo San Pedro', 'Lunahuaná', 'Cañete', 'Lima', 1, 'QR-PROD-013', SYSUTCDATETIME()), ('Daniela Herrera Jiménez', '45678914', '956123469', 'dherrera2@gmail.com', 'Parcela 67 Nuevo Imperial', 'Nuevo Imperial', 'Cañete', 'Lima', 1, 'QR-PROD-014', SYSUTCDATETIME()), ('Raúl Jiménez Chávez', '45678915', '956123470', 'rjimenez2@gmail.com', 'Fundo Las Palmas', 'Cerro Azul', 'Cañete', 'Lima', 1, 'QR-PROD-015', SYSUTCDATETIME()),
('Verónica Núñez Montoya', '45678916', '956123471', 'vnunez2@gmail.com', 'Sector Conta', 'San Vicente', 'Cañete', 'Lima', 1, 'QR-PROD-016', SYSUTCDATETIME()), ('Fernando Rojas Vega', '45678917', '956123472', 'frojas2@gmail.com', 'Fundo El Paraíso', 'Quilmaná', 'Cañete', 'Lima', 1, 'QR-PROD-017', SYSUTCDATETIME()), ('Isabel Chávez Luna', '45678918', '956123473', 'ichavez2@gmail.com', 'Parcela 89 Herbay Bajo', 'San Vicente', 'Cañete', 'Lima', 1, 'QR-PROD-018', SYSUTCDATETIME()), ('Gustavo Espinoza Ramos', '45678919', '956123474', 'gespinoza@gmail.com', 'Fundo San Juan', 'Imperial', 'Cañete', 'Lima', 1, 'QR-PROD-019', SYSUTCDATETIME()), ('Claudia Medina Torres', '45678920', '956123475', 'cmedina@gmail.com', 'Sector Cantagallo', 'San Vicente', 'Cañete', 'Lima', 1, 'QR-PROD-020', SYSUTCDATETIME());
GO

-- ============================================================
-- INSERT: customer (20 rows)
-- ============================================================
INSERT INTO dbo.customer (codigo, razon_social, ruc, tipo_documento, numero_documento, pais, ciudad, direccion, telefono, email, contacto_principal, tipo_cliente, active, created_at)
VALUES
('CLI-001', 'Agroindustrias del Sur S.A.C.', '20512345678', 'RUC', '20512345678', 'Perú', 'Lima', 'Av. La Marina 1234', '01-4567890', 'ventas@agroindsur.pe', 'Luis Paredes', 'NACIONAL', 1, SYSUTCDATETIME()), ('CLI-002', 'Fresh Fruits Export Ltd.', NULL, 'TAX_ID', 'US-987654321', 'Estados Unidos', 'Miami', '1200 Brickell Ave', '+1-305-1234567', 'imports@freshfruits.com', 'John Smith', 'INTERNACIONAL', 1, SYSUTCDATETIME()), ('CLI-003', 'Supermercados Peruanos S.A.', '20101234567', 'RUC', '20101234567', 'Perú', 'Lima', 'Calle Morelli 181 San Borja', '01-6180000', 'compras@spsa.pe', 'Martha Gutiérrez', 'NACIONAL', 1, SYSUTCDATETIME()), ('CLI-004', 'European Organic Traders GmbH', NULL, 'TAX_ID', 'DE-123456789', 'Alemania', 'Hamburgo', 'Hafenstraße 45', '+49-40-12345', 'procurement@eot.de', 'Hans Mueller', 'INTERNACIONAL', 1, SYSUTCDATETIME()), ('CLI-005', 'Mercado Mayorista Cañete E.I.R.L.', '20567890123', 'RUC', '20567890123', 'Perú', 'Cañete', 'Jr. Bolognesi 456', '01-5812345', 'info@mercadocanete.pe', 'Rosa Mendoza', 'NACIONAL', 1, SYSUTCDATETIME()),
('CLI-006', 'Asia Pacific Foods Co.', NULL, 'TAX_ID', 'JP-456789012', 'Japón', 'Tokio', 'Chiyoda-ku 3-1-1', '+81-3-1234567', 'buy@apfoods.jp', 'Yuki Tanaka', 'INTERNACIONAL', 1, SYSUTCDATETIME()), ('CLI-007', 'Distribuidora Agrícola Lima S.A.C.', '20234567890', 'RUC', '20234567890', 'Perú', 'Lima', 'Av. Argentina 2345', '01-3456789', 'pedidos@distagri.pe', 'Carlos Vega', 'NACIONAL', 1, SYSUTCDATETIME()), ('CLI-008', 'Canadian Harvest Imports Inc.', NULL, 'TAX_ID', 'CA-789012345', 'Canadá', 'Toronto', '100 Queen St West', '+1-416-9876543', 'sourcing@canharvest.ca', 'Emma Wilson', 'INTERNACIONAL', 1, SYSUTCDATETIME()), ('CLI-009', 'Cooperativa Agraria Cañete', '20345678901', 'RUC', '20345678901', 'Perú', 'Cañete', 'Av. Mariscal Benavides 789', '01-5813456', 'cooperativa@coopcanete.pe', 'Jorge Salinas', 'NACIONAL', 1, SYSUTCDATETIME()), ('CLI-010', 'UK Fresh Produce Ltd.', NULL, 'TAX_ID', 'GB-321654987', 'Reino Unido', 'Londres', '10 Downing Market', '+44-20-7654321', 'orders@ukfresh.co.uk', 'James Brown', 'INTERNACIONAL', 1, SYSUTCDATETIME()),
('CLI-011', 'Restaurant Chain Peru S.A.C.', '20456789012', 'RUC', '20456789012', 'Perú', 'Lima', 'Av. Javier Prado 567', '01-2345678', 'compras@restchain.pe', 'Andrea Paz', 'NACIONAL', 1, SYSUTCDATETIME()), ('CLI-012', 'Chilean Agricultural Trading SpA', NULL, 'TAX_ID', 'CL-765432109', 'Chile', 'Santiago', 'Av. Providencia 1234', '+56-2-9876543', 'imports@chiletrade.cl', 'Pablo Muñoz', 'INTERNACIONAL', 1, SYSUTCDATETIME()), ('CLI-013', 'Exportadora Valle Sur S.A.', '20567890234', 'RUC', '20567890234', 'Perú', 'Ica', 'Panamericana Sur Km 300', '056-234567', 'export@vallesur.pe', 'Diana Flores', 'NACIONAL', 1, SYSUTCDATETIME()), ('CLI-014', 'Netherlands Tropical Fruits BV', NULL, 'TAX_ID', 'NL-876543210', 'Países Bajos', 'Rotterdam', 'Europoort 789', '+31-10-1234567', 'purchase@nltropical.nl', 'Pieter van Dijk', 'INTERNACIONAL', 1, SYSUTCDATETIME()), ('CLI-015', 'Agrícola Don Bosco S.A.C.', '20678901234', 'RUC', '20678901234', 'Perú', 'Lima', 'Av. Colonial 3456', '01-5678901', 'gerencia@donboscoagri.pe', 'Manuel Ríos', 'NACIONAL', 1, SYSUTCDATETIME()),
('CLI-016', 'Dubai Fresh Markets LLC', NULL, 'TAX_ID', 'AE-543210987', 'Emiratos Árabes', 'Dubái', 'Jebel Ali Free Zone', '+971-4-1234567', 'import@dubaifresh.ae', 'Ahmed Al-Rashid', 'INTERNACIONAL', 1, SYSUTCDATETIME()), ('CLI-017', 'Procesadora de Alimentos Lima S.A.', '20789012345', 'RUC', '20789012345', 'Perú', 'Lima', 'Av. Venezuela 890', '01-4321098', 'logistica@proalim.pe', 'Sandra Morales', 'NACIONAL', 1, SYSUTCDATETIME()), ('CLI-018', 'Brazilian Agro Commerce Ltda.', NULL, 'TAX_ID', 'BR-210987654', 'Brasil', 'São Paulo', 'Rua Augusta 1500', '+55-11-98765432', 'compras@brazilagro.com.br', 'Ricardo Silva', 'INTERNACIONAL', 1, SYSUTCDATETIME()), ('CLI-019', 'Minimarket La Cosecha E.I.R.L.', '10234567890', 'RUC', '10234567890', 'Perú', 'Cañete', 'Calle Lima 234 Imperial', '01-5814567', 'lacosecha@gmail.com', 'Julio Espinoza', 'NACIONAL', 1, SYSUTCDATETIME()), ('CLI-020', 'South Korea Organic Co. Ltd.', NULL, 'TAX_ID', 'KR-654321098', 'Corea del Sur', 'Seúl', 'Gangnam-gu 123', '+82-2-1234567', 'organic@skorganic.kr', 'Min-Jun Park', 'INTERNACIONAL', 1, SYSUTCDATETIME());
GO

-- ============================================================
-- INSERT: productos (20 rows)
-- ============================================================
INSERT INTO dbo.productos (codigo, nombre, descripcion, categoria, unidad_medida, precio_unitario, stock_actual, stock_minimo, origen, estado, created_at)
VALUES
('PROD-001', 'Mandarina Satsuma', 'Mandarina variedad Satsuma de primera calidad', 'Cítricos', 'Caja', 85.00, 500, 50, 'Cañete - San Vicente', 1, SYSUTCDATETIME()), ('PROD-002', 'Palta Hass', 'Palta Hass calibre 16-20', 'Frutas', 'Caja', 120.00, 350, 40, 'Cañete - Lunahuaná', 1, SYSUTCDATETIME()), ('PROD-003', 'Uva Red Globe', 'Uva de mesa variedad Red Globe', 'Frutas', 'Caja', 95.00, 280, 30, 'Cañete - Imperial', 1, SYSUTCDATETIME()), ('PROD-004', 'Espárrago Verde', 'Espárrago verde fresco para exportación', 'Hortalizas', 'Kg', 18.50, 1200, 100, 'Cañete - Quilmaná', 1, SYSUTCDATETIME()), ('PROD-005', 'Camote Amarillo', 'Camote amarillo tipo exportación', 'Tubérculos', 'Saco', 45.00, 600, 60, 'Cañete - Cerro Azul', 1, SYSUTCDATETIME()),
('PROD-006', 'Mango Kent', 'Mango variedad Kent calibre 8-10', 'Frutas', 'Caja', 110.00, 420, 50, 'Cañete - San Vicente', 1, SYSUTCDATETIME()), ('PROD-007', 'Maíz Morado', 'Maíz morado orgánico', 'Granos', 'Saco', 65.00, 300, 30, 'Cañete - Nuevo Imperial', 1, SYSUTCDATETIME()), ('PROD-008', 'Algodón Tangüis', 'Algodón Tangüis fibra larga', 'Fibras', 'Quintal', 250.00, 150, 20, 'Cañete - San Vicente', 1, SYSUTCDATETIME()), ('PROD-009', 'Limón Sutil', 'Limón sutil para industria y mesa', 'Cítricos', 'Caja', 55.00, 800, 80, 'Cañete - Imperial', 1, SYSUTCDATETIME()), ('PROD-010', 'Naranja Valencia', 'Naranja Valencia jugo concentrado', 'Cítricos', 'Caja', 70.00, 450, 50, 'Cañete - Quilmaná', 1, SYSUTCDATETIME()),
('PROD-011', 'Arándano Biloxi', 'Arándano fresco variedad Biloxi', 'Frutas', 'Clamshell', 35.00, 900, 100, 'Cañete - San Vicente', 1, SYSUTCDATETIME()), ('PROD-012', 'Quinua Blanca', 'Quinua blanca orgánica lavada', 'Granos', 'Saco', 180.00, 200, 25, 'Cañete - Lunahuaná', 1, SYSUTCDATETIME()), ('PROD-013', 'Tomate Cherry', 'Tomate cherry hidropónico', 'Hortalizas', 'Kg', 12.00, 650, 70, 'Cañete - Imperial', 1, SYSUTCDATETIME()), ('PROD-014', 'Pimiento Piquillo', 'Pimiento piquillo rojo fresco', 'Hortalizas', 'Kg', 14.50, 400, 40, 'Cañete - Nuevo Imperial', 1, SYSUTCDATETIME()), ('PROD-015', 'Granadilla', 'Granadilla fresca calibre AA', 'Frutas', 'Caja', 90.00, 250, 30, 'Cañete - Lunahuaná', 1, SYSUTCDATETIME()),
('PROD-016', 'Frijol Canario', 'Frijol canario seleccionado', 'Legumbres', 'Saco', 75.00, 350, 35, 'Cañete - Cerro Azul', 1, SYSUTCDATETIME()), ('PROD-017', 'Yuca Amarilla', 'Yuca amarilla para procesamiento', 'Tubérculos', 'Saco', 38.00, 500, 50, 'Cañete - Quilmaná', 1, SYSUTCDATETIME()), ('PROD-018', 'Chirimoya Cumbe', 'Chirimoya variedad Cumbe exportación', 'Frutas', 'Caja', 130.00, 180, 20, 'Cañete - Lunahuaná', 1, SYSUTCDATETIME()), ('PROD-019', 'Ají Amarillo', 'Ají amarillo fresco seleccionado', 'Hortalizas', 'Kg', 8.50, 700, 70, 'Cañete - San Vicente', 1, SYSUTCDATETIME()), ('PROD-020', 'Higo Negro', 'Higo negro maduro para exportación', 'Frutas', 'Caja', 100.00, 220, 25, 'Cañete - Imperial', 1, SYSUTCDATETIME());
GO

-- ============================================================
-- INSERT: productores (20 rows)
-- ============================================================
INSERT INTO dbo.productores (nombre_completo, dni, telefono, correo, ubicacion, distrito, estado, fecha_creacion, active)
VALUES
('Alberto Ramos Solís', '71234501', '987654321', 'aramos@gmail.com', 'Fundo El Ángel', 'San Vicente', 1, SYSUTCDATETIME(), 1), ('Beatriz Luna Paredes', '71234502', '987654322', 'bluna@gmail.com', 'Parcela 15 Imperial', 'Imperial', 1, SYSUTCDATETIME(), 1), ('César Montoya Ríos', '71234503', '987654323', 'cmontoya@gmail.com', 'Fundo Santa Clara', 'Lunahuaná', 1, SYSUTCDATETIME(), 1), ('Diana Flores Espinoza', '71234504', '987654324', 'dflores@gmail.com', 'Sector Los Ángeles', 'Nuevo Imperial', 1, SYSUTCDATETIME(), 1), ('Ernesto Paz Villanueva', '71234505', '987654325', 'epaz@gmail.com', 'Fundo La Victoria', 'Cerro Azul', 1, SYSUTCDATETIME(), 1),
('Francisca Rojas Medina', '71234506', '987654326', 'frojas3@gmail.com', 'Parcela 30 Quilmaná', 'Quilmaná', 1, SYSUTCDATETIME(), 1), ('Gonzalo Vega Torres', '71234507', '987654327', 'gvega@gmail.com', 'Fundo San Isidro', 'San Vicente', 1, SYSUTCDATETIME(), 1), ('Helena Castro Jiménez', '71234508', '987654328', 'hcastro@gmail.com', 'Sector Herbay Bajo', 'San Vicente', 1, SYSUTCDATETIME(), 1), ('Iván Salinas Chávez', '71234509', '987654329', 'isalinas@gmail.com', 'Fundo El Progreso', 'Imperial', 1, SYSUTCDATETIME(), 1), ('Julia Espinoza Morales', '71234510', '987654330', 'jespinoza@gmail.com', 'Parcela 50 Cañete', 'San Vicente', 1, SYSUTCDATETIME(), 1),
('Kevin Herrera Díaz', '71234511', '987654331', 'kherrera@gmail.com', 'Fundo Los Ficus', 'Lunahuaná', 1, SYSUTCDATETIME(), 1), ('Liliana Núñez Valdez', '71234512', '987654332', 'lnunez@gmail.com', 'Sector Conta Alto', 'San Vicente', 1, SYSUTCDATETIME(), 1), ('Manuel García Huamán', '71234513', '987654333', 'mgarcia@gmail.com', 'Fundo La Merced', 'Quilmaná', 1, SYSUTCDATETIME(), 1), ('Natalia Prado Cruz', '71234514', '987654334', 'nprado@gmail.com', 'Parcela 72 Imperial', 'Imperial', 1, SYSUTCDATETIME(), 1), ('Oscar Mendoza Fernández', '71234515', '987654335', 'omendoza@gmail.com', 'Fundo Buena Vista', 'Cerro Azul', 1, SYSUTCDATETIME(), 1),
('Paola Castillo Ramos', '71234516', '987654336', 'pcastillo@gmail.com', 'Sector Ungará Norte', 'San Vicente', 1, SYSUTCDATETIME(), 1), ('Quintín Vargas Salazar', '71234517', '987654337', 'qvargas@gmail.com', 'Fundo San Andrés', 'Nuevo Imperial', 1, SYSUTCDATETIME(), 1), ('Rocío Torres Luna', '71234518', '987654338', 'rtorres@gmail.com', 'Parcela 91 Asia', 'Asia', 1, SYSUTCDATETIME(), 1), ('Samuel Díaz Espinoza', '71234519', '987654339', 'sdiaz@gmail.com', 'Fundo El Rosario', 'San Vicente', 1, SYSUTCDATETIME(), 1), ('Teresa Jiménez Morales', '71234520', '987654340', 'tjimenez@gmail.com', 'Sector Cantagallo Sur', 'San Vicente', 1, SYSUTCDATETIME(), 1);
GO

-- ============================================================
-- INSERT: fields (20 rows)
-- ============================================================
INSERT INTO dbo.fields (producer_id, name, area_hectares, active_crop, location, active, qr_token, created_at)
VALUES
(1, 'Parcela Norte Santa Rosa', 5.5, 'Mandarina', 'Fundo Santa Rosa - Sector Norte', 1, 'QR-FLD-001', SYSUTCDATETIME()), (1, 'Parcela Sur Santa Rosa', 3.2, 'Palta', 'Fundo Santa Rosa - Sector Sur', 1, 'QR-FLD-002', SYSUTCDATETIME()), (2, 'Campo Principal Valle', 8.0, 'Uva', 'Parcela 12 - Campo Central', 1, 'QR-FLD-003', SYSUTCDATETIME()), (3, 'Terreno La Esperanza', 4.5, 'Espárrago', 'Fundo La Esperanza - Lote 1', 1, 'QR-FLD-004', SYSUTCDATETIME()), (4, 'Campo Herbay Sector A', 6.0, 'Mango', 'Herbay Alto - Sector A', 1, 'QR-FLD-005', SYSUTCDATETIME()),
(5, 'Parcela San Martín Central', 7.5, 'Maíz Morado', 'Fundo San Martín - Centro', 1, 'QR-FLD-006', SYSUTCDATETIME()), (6, 'Terreno Cerro Azul Bajo', 3.8, 'Camote', 'Cerro Azul - Zona Baja', 1, 'QR-FLD-007', SYSUTCDATETIME()), (7, 'Campo Los Olivos Norte', 9.0, 'Algodón', 'Fundo Los Olivos - Norte', 1, 'QR-FLD-008', SYSUTCDATETIME()), (8, 'Parcela Hualcará Este', 4.0, 'Limón', 'Sector Hualcará - Este', 1, 'QR-FLD-009', SYSUTCDATETIME()), (9, 'Campo El Carmen Central', 6.5, 'Arándano', 'Fundo El Carmen - Centro', 1, 'QR-FLD-010', SYSUTCDATETIME()),
(10, 'Terreno Cañete Centro', 2.5, 'Tomate Cherry', 'Cañete Centro - Lote 45', 1, 'QR-FLD-011', SYSUTCDATETIME()), (11, 'Campo Vista Alegre Sur', 5.0, 'Naranja', 'Vista Alegre - Sector Sur', 1, 'QR-FLD-012', SYSUTCDATETIME()), (12, 'Parcela Ungará Oeste', 3.5, 'Quinua', 'Sector Ungará - Oeste', 1, 'QR-FLD-013', SYSUTCDATETIME()), (13, 'Terreno San Pedro Alto', 7.0, 'Frijol', 'Fundo San Pedro - Zona Alta', 1, 'QR-FLD-014', SYSUTCDATETIME()), (14, 'Campo Nuevo Imperial Este', 4.8, 'Pimiento', 'Nuevo Imperial - Sector Este', 1, 'QR-FLD-015', SYSUTCDATETIME()),
(15, 'Parcela Las Palmas Central', 6.2, 'Granadilla', 'Fundo Las Palmas - Centro', 1, 'QR-FLD-016', SYSUTCDATETIME()), (16, 'Terreno Conta Norte', 3.0, 'Ají Amarillo', 'Sector Conta - Norte', 1, 'QR-FLD-017', SYSUTCDATETIME()), (17, 'Campo Paraíso Bajo', 8.5, 'Mandarina', 'Fundo El Paraíso - Zona Baja', 1, 'QR-FLD-018', SYSUTCDATETIME()), (18, 'Parcela Herbay Bajo Sur', 4.2, 'Yuca', 'Herbay Bajo - Sector Sur', 1, 'QR-FLD-019', SYSUTCDATETIME()), (19, 'Campo San Juan Oriental', 5.8, 'Higo', 'Fundo San Juan - Oriental', 1, 'QR-FLD-020', SYSUTCDATETIME());
GO

-- ============================================================
-- INSERT: production_lots (20 rows)
-- ============================================================
INSERT INTO dbo.production_lots (field_id, code, start_date, end_date, crop, status, observations, active, created_at)
VALUES
(1, 'LOT-2026-001', '2026-01-15', NULL, 'Mandarina', 'ACTIVE', 'Temporada principal mandarina Satsuma', 1, SYSUTCDATETIME()), (2, 'LOT-2026-002', '2026-01-20', NULL, 'Palta', 'ACTIVE', 'Cosecha palta Hass primera campaña', 1, SYSUTCDATETIME()), (3, 'LOT-2026-003', '2026-02-01', NULL, 'Uva', 'ACTIVE', 'Temporada uva Red Globe para exportación', 1, SYSUTCDATETIME()), (4, 'LOT-2026-004', '2026-02-10', '2026-05-10', 'Espárrago', 'CLOSED', 'Campaña espárrago finalizada con éxito', 1, SYSUTCDATETIME()), (5, 'LOT-2026-005', '2026-03-01', NULL, 'Mango', 'ACTIVE', 'Inicio temporada mango Kent', 1, SYSUTCDATETIME()),
(6, 'LOT-2026-006', '2026-03-15', NULL, 'Maíz Morado', 'ACTIVE', 'Siembra maíz morado orgánico', 1, SYSUTCDATETIME()), (7, 'LOT-2026-007', '2026-01-10', '2026-04-20', 'Camote', 'CLOSED', 'Cosecha camote completada', 1, SYSUTCDATETIME()), (8, 'LOT-2026-008', '2026-02-20', NULL, 'Algodón', 'ACTIVE', 'Campaña algodón Tangüis 2026', 1, SYSUTCDATETIME()), (9, 'LOT-2026-009', '2026-03-05', NULL, 'Limón', 'ACTIVE', 'Producción continua limón sutil', 1, SYSUTCDATETIME()), (10, 'LOT-2026-010', '2026-04-01', NULL, 'Arándano', 'ACTIVE', 'Primera campaña arándano Biloxi', 1, SYSUTCDATETIME()),
(11, 'LOT-2026-011', '2026-01-25', NULL, 'Tomate Cherry', 'ACTIVE', 'Producción hidropónica continua', 1, SYSUTCDATETIME()), (12, 'LOT-2026-012', '2026-02-15', NULL, 'Naranja', 'ACTIVE', 'Temporada naranja Valencia', 1, SYSUTCDATETIME()), (13, 'LOT-2026-013', '2026-03-10', '2026-05-15', 'Quinua', 'CLOSED', 'Cosecha quinua orgánica completada', 1, SYSUTCDATETIME()), (14, 'LOT-2026-014', '2026-04-05', NULL, 'Frijol', 'ACTIVE', 'Siembra frijol canario segunda campaña', 1, SYSUTCDATETIME()), (15, 'LOT-2026-015', '2026-02-28', NULL, 'Pimiento', 'ACTIVE', 'Producción pimiento piquillo', 1, SYSUTCDATETIME()),
(16, 'LOT-2026-016', '2026-03-20', NULL, 'Granadilla', 'ACTIVE', 'Temporada granadilla AA', 1, SYSUTCDATETIME()), (17, 'LOT-2026-017', '2026-01-05', '2026-03-30', 'Ají Amarillo', 'CANCELLED', 'Cancelado por plaga no controlada', 1, SYSUTCDATETIME()), (18, 'LOT-2026-018', '2026-04-10', NULL, 'Mandarina', 'ACTIVE', 'Segunda temporada mandarina fundo Paraíso', 1, SYSUTCDATETIME()), (19, 'LOT-2026-019', '2026-03-25', NULL, 'Yuca', 'ACTIVE', 'Siembra yuca amarilla procesamiento', 1, SYSUTCDATETIME()), (20, 'LOT-2026-020', '2026-05-01', NULL, 'Higo', 'ACTIVE', 'Inicio temporada higo negro', 1, SYSUTCDATETIME());
GO

-- ============================================================
-- INSERT: field_applications (20 rows)
-- ============================================================
INSERT INTO dbo.field_applications (producer_id, field_id, lot_id, agrochemical_id, dose, dose_unit, application_date, technician_id, observations, senasa_valid, dose_exceeded, active, created_at)
VALUES
(1, 1, 1, 1, 0.4, 'L/ha', '2026-02-10', 3, 'Aplicación preventiva contra pulgones en mandarina', 1, 0, 1, SYSUTCDATETIME()), (1, 2, 2, 2, 0.8, 'L/ha', '2026-02-15', 3, 'Control de antracnosis en palta', 1, 0, 1, SYSUTCDATETIME()), (2, 3, 3, 8, 0.5, 'L/ha', '2026-03-01', 4, 'Tratamiento fungicida preventivo en uva', 1, 0, 1, SYSUTCDATETIME()), (3, 4, 4, 4, 0.3, 'L/ha', '2026-03-05', 8, 'Control de gusano cogollero en espárrago', 1, 0, 1, SYSUTCDATETIME()), (4, 5, 5, 5, 4.0, 'L/ha', '2026-03-20', 3, 'Fertilización foliar mango en floración', 1, 0, 1, SYSUTCDATETIME()),
(5, 6, 6, 6, 1.5, 'kg/ha', '2026-04-01', 4, 'Aplicación Trichoderma al suelo para maíz', 1, 0, 1, SYSUTCDATETIME()), (6, 7, 7, 7, 0.3, 'kg/ha', '2026-02-05', 8, 'Control de trips en camote', 1, 0, 1, SYSUTCDATETIME()), (7, 8, 8, 10, 0.25, 'L/ha', '2026-03-15', 11, 'Aplicación insecticida en algodón contra picudo', 1, 0, 1, SYSUTCDATETIME()), (8, 9, 9, 13, 0.4, 'L/ha', '2026-04-02', 14, 'Control de ácaro en limón', 1, 0, 1, SYSUTCDATETIME()), (9, 10, 10, 15, 0.3, 'L/ha', '2026-04-20', 3, 'Bioestimulante para cuajado en arándano', 1, 0, 1, SYSUTCDATETIME()),
(10, 11, 11, 11, 2.0, 'kg/ha', '2026-02-20', 4, 'Prevención mildiu en tomate cherry', 1, 0, 1, SYSUTCDATETIME()), (11, 12, 12, 19, 3.0, 'L/ha', '2026-03-10', 8, 'Aceite agrícola contra cochinilla en naranja', 1, 0, 1, SYSUTCDATETIME()), (12, 13, 13, 16, 8.0, 'kg/ha', '2026-04-05', 11, 'Fertilización potásica quinua en llenado de grano', 1, 0, 1, SYSUTCDATETIME()), (13, 14, 14, 20, 1.0, 'L/ha', '2026-04-15', 14, 'Aplicación Bt contra gusano en frijol', 1, 0, 1, SYSUTCDATETIME()), (14, 15, 15, 14, 0.8, 'L/ha', '2026-03-25', 16, 'Fungicida preventivo pimiento', 1, 0, 1, SYSUTCDATETIME()),
(15, 16, 16, 1, 0.5, 'L/ha', '2026-04-08', 19, 'Control de mosca blanca en granadilla', 1, 1, 1, SYSUTCDATETIME()), (16, 17, 17, 17, 0.8, 'kg/ha', '2026-02-12', 3, 'Control de gusano en ají amarillo', 1, 0, 1, SYSUTCDATETIME()), (17, 18, 18, 2, 0.9, 'L/ha', '2026-04-25', 4, 'Fungicida contra gomosis mandarina', 1, 0, 1, SYSUTCDATETIME()), (18, 19, 19, 3, 2.5, 'L/ha', '2026-04-10', 8, 'Herbicida pre-emergente yuca', 1, 0, 1, SYSUTCDATETIME()), (19, 20, 20, 18, 1.5, 'kg/ha', '2026-05-05', 11, 'Fungicida preventivo higo', 1, 0, 1, SYSUTCDATETIME());
GO

-- ============================================================
-- INSERT: inventory_deliveries (20 rows)
-- ============================================================
INSERT INTO dbo.inventory_deliveries (producer_id, agrochemical_id, quantity_delivered, unit, delivery_date, responsible_id, observations, active, created_at)
VALUES
(1, 1, 5.0, 'Litros', '2026-02-01', 5, 'Entrega Confidor para campaña mandarina', 1, SYSUTCDATETIME()), (1, 2, 3.0, 'Litros', '2026-02-01', 5, 'Amistar Top para control palta', 1, SYSUTCDATETIME()), (2, 8, 4.0, 'Litros', '2026-02-20', 9, 'Score para tratamiento preventivo uva', 1, SYSUTCDATETIME()), (3, 4, 2.0, 'Litros', '2026-02-25', 5, 'Karate Zeon para espárrago', 1, SYSUTCDATETIME()), (4, 5, 20.0, 'Litros', '2026-03-10', 9, 'Bayfolan para fertilización mango', 1, SYSUTCDATETIME()),
(5, 6, 10.0, 'Kg', '2026-03-20', 15, 'Trichoderma para aplicación suelo maíz', 1, SYSUTCDATETIME()), (6, 7, 3.0, 'Kg', '2026-01-28', 5, 'Lannate para control trips camote', 1, SYSUTCDATETIME()), (7, 10, 2.5, 'Litros', '2026-03-05', 9, 'Engeo para algodón contra picudo', 1, SYSUTCDATETIME()), (8, 13, 4.0, 'Litros', '2026-03-25', 15, 'Abamectina para ácaro limón', 1, SYSUTCDATETIME()), (9, 15, 3.0, 'Litros', '2026-04-10', 5, 'Stimulate para arándano', 1, SYSUTCDATETIME()),
(10, 11, 10.0, 'Kg', '2026-02-10', 9, 'Mancozeb para tomate cherry', 1, SYSUTCDATETIME()), (11, 19, 15.0, 'Litros', '2026-03-01', 15, 'Aceite agrícola para naranja', 1, SYSUTCDATETIME()), (12, 16, 50.0, 'Kg', '2026-03-28', 5, 'Sulfato de potasio para quinua', 1, SYSUTCDATETIME()), (13, 20, 5.0, 'Litros', '2026-04-05', 9, 'Bt para control gusano frijol', 1, SYSUTCDATETIME()), (14, 14, 4.0, 'Litros', '2026-03-15', 15, 'Carbendazim para pimiento', 1, SYSUTCDATETIME()),
(15, 1, 3.0, 'Litros', '2026-03-30', 5, 'Confidor para granadilla', 1, SYSUTCDATETIME()), (16, 17, 5.0, 'Kg', '2026-02-05', 20, 'Acefato para control ají amarillo', 1, SYSUTCDATETIME()), (17, 2, 4.0, 'Litros', '2026-04-15', 9, 'Amistar Top mandarina segundo lote', 1, SYSUTCDATETIME()), (18, 3, 10.0, 'Litros', '2026-04-01', 15, 'Roundup para limpieza terreno yuca', 1, SYSUTCDATETIME()), (19, 18, 8.0, 'Kg', '2026-04-28', 20, 'Captan para higo preventivo', 1, SYSUTCDATETIME());
GO

-- ============================================================
-- INSERT: alerts (20 rows)
-- ============================================================
INSERT INTO dbo.alerts (type, severity, title, description, producer_id, field_id, agrochemical_id, status, active, created_at)
VALUES
('DOSE_EXCEEDED', 'HIGH', 'Dosis excedida - Confidor en Granadilla', 'El productor Raúl Jiménez aplicó 0.5 L/ha de Confidor 350 SC, excediendo la dosis máxima de 0.5 L/ha', 15, 16, 1, 'PENDING', 1, SYSUTCDATETIME()), ('SENASA_EXPIRING', 'MEDIUM', 'Registro SENASA próximo a vencer - Roundup', 'El registro PQUA-03-2020 de Roundup Max vence el 2026-12-31', NULL, NULL, 3, 'PENDING', 1, SYSUTCDATETIME()), ('NO_APPLICATION_REGISTERED', 'LOW', 'Sin aplicación registrada - Campo Vista Alegre', 'El campo Vista Alegre Sur no tiene aplicaciones registradas en los últimos 30 días', 11, 12, NULL, 'PENDING', 1, SYSUTCDATETIME()), ('LOW_STOCK', 'MEDIUM', 'Stock bajo - Karate Zeon', 'El inventario de Karate Zeon está por debajo del mínimo requerido', NULL, NULL, 4, 'REVIEWED', 1, SYSUTCDATETIME()), ('SENASA_EXPIRED', 'HIGH', 'Registro SENASA vencido - Lannate 90', 'El registro PQUA-07-2020 de Lannate 90 SP venció el 2026-09-30', NULL, NULL, 7, 'PENDING', 1, SYSUTCDATETIME()),
('DOSE_EXCEEDED', 'HIGH', 'Dosis excedida - Herbicida en Yuca', 'Aplicación de 2.5 L/ha de Roundup en yuca, cerca del límite máximo', 18, 19, 3, 'REVIEWED', 1, SYSUTCDATETIME()), ('NO_APPLICATION_REGISTERED', 'LOW', 'Sin aplicación registrada - Terreno Conta', 'El terreno Conta Norte no registra aplicaciones este mes', 16, 17, NULL, 'DISMISSED', 1, SYSUTCDATETIME()), ('SENASA_EXPIRING', 'MEDIUM', 'Registro próximo a vencer - Mancozeb', 'El registro PQUA-11-2020 vence el 2026-11-15', NULL, NULL, 11, 'PENDING', 1, SYSUTCDATETIME()), ('LOW_STOCK', 'HIGH', 'Stock crítico - Abamectina', 'Solo quedan 2 litros de Abamectina en almacén', NULL, NULL, 13, 'PENDING', 1, SYSUTCDATETIME()), ('DOSE_EXCEEDED', 'MEDIUM', 'Dosis alta - Mancozeb en Tomate', 'Aplicación de 2.0 kg/ha de Mancozeb, dentro del rango pero cercano al máximo', 10, 11, 11, 'REVIEWED', 1, SYSUTCDATETIME()),
('SENASA_EXPIRED', 'HIGH', 'Registro vencido - Carbendazim', 'El registro PQUA-14-2020 de Carbendazim 500 SC venció el 2026-08-20', NULL, NULL, 14, 'PENDING', 1, SYSUTCDATETIME()), ('NO_APPLICATION_REGISTERED', 'LOW', 'Sin aplicación - Parcela Ungará', 'La parcela Ungará Oeste no tiene aplicaciones en 45 días', 12, 13, NULL, 'PENDING', 1, SYSUTCDATETIME()), ('LOW_STOCK', 'MEDIUM', 'Stock bajo - Stimulate', 'El inventario de Stimulate está por debajo de 5 litros', NULL, NULL, 15, 'DISMISSED', 1, SYSUTCDATETIME()), ('SENASA_EXPIRING', 'MEDIUM', 'Registro próximo - Paraquat Gramoxone', 'El registro PQUA-09-2021 vence el 2027-04-18', NULL, NULL, 9, 'PENDING', 1, SYSUTCDATETIME()), ('DOSE_EXCEEDED', 'HIGH', 'Sobredosis - Sulfato de Potasio en Quinua', 'Se aplicó 8.0 kg/ha de Sulfato de Potasio, dentro del rango permitido', 12, 13, 16, 'REVIEWED', 1, SYSUTCDATETIME()),
('NO_APPLICATION_REGISTERED', 'LOW', 'Sin aplicación - Campo San Juan', 'El campo San Juan Oriental no registra aplicaciones en 20 días', 19, 20, NULL, 'PENDING', 1, SYSUTCDATETIME()), ('LOW_STOCK', 'HIGH', 'Stock crítico - Engeo 247 SC', 'Solo quedan 1.5 litros de Engeo en inventario', NULL, NULL, 10, 'PENDING', 1, SYSUTCDATETIME()), ('SENASA_EXPIRING', 'MEDIUM', 'Registro próximo - Acefato 75 SP', 'El registro PQUA-17-2021 vence el 2027-03-25', NULL, NULL, 17, 'PENDING', 1, SYSUTCDATETIME()), ('DOSE_EXCEEDED', 'MEDIUM', 'Dosis límite - Aceite Agrícola en Naranja', 'Aplicación de 3.0 L/ha de Aceite Citroliv, dentro del rango', 11, 12, 19, 'DISMISSED', 1, SYSUTCDATETIME()), ('LOW_STOCK', 'MEDIUM', 'Stock bajo - Bacillus thuringiensis', 'Inventario de Bt por debajo del mínimo operativo', NULL, NULL, 20, 'PENDING', 1, SYSUTCDATETIME());
GO

-- ============================================================
-- INSERT: audit_logs (20 rows)
-- ============================================================
INSERT INTO dbo.audit_logs (user_id, user_name, action, module, entity_id, details, ip_address, [timestamp], active)
VALUES
(1, 'admin', 'CREATE', 'AGROCHEMICAL', 1, 'Creación de Confidor 350 SC', '192.168.1.10', SYSUTCDATETIME(), 1), (1, 'admin', 'CREATE', 'AGROCHEMICAL', 2, 'Creación de Amistar Top', '192.168.1.10', SYSUTCDATETIME(), 1), (2, 'supervisor1', 'CREATE', 'PRODUCER', 1, 'Registro de productor Roberto Sánchez', '192.168.1.20', SYSUTCDATETIME(), 1), (3, 'tech_juan', 'CREATE', 'FIELD_APPLICATION', 1, 'Aplicación Confidor en mandarina', '192.168.1.30', SYSUTCDATETIME(), 1), (5, 'logistica1', 'CREATE', 'INVENTORY_DELIVERY', 1, 'Entrega 5L Confidor a productor 1', '192.168.1.40', SYSUTCDATETIME(), 1),
(1, 'admin', 'UPDATE', 'APP_USER', 3, 'Actualización rol técnico Juan Pérez', '192.168.1.10', SYSUTCDATETIME(), 1), (6, 'cert_ana', 'REVIEW', 'ALERT', 4, 'Revisión alerta stock bajo Karate Zeon', '192.168.1.50', SYSUTCDATETIME(), 1), (2, 'supervisor1', 'CREATE', 'FIELD', 1, 'Registro Parcela Norte Santa Rosa', '192.168.1.20', SYSUTCDATETIME(), 1), (1, 'admin', 'CREATE', 'PRODUCTO', 1, 'Creación producto Mandarina Satsuma', '192.168.1.10', SYSUTCDATETIME(), 1), (7, 'supervisor2', 'CREATE', 'PRODUCTION_LOT', 1, 'Creación lote LOT-2026-001 mandarina', '192.168.1.60', SYSUTCDATETIME(), 1),
(4, 'tech_rosa', 'CREATE', 'FIELD_APPLICATION', 3, 'Aplicación Score en uva Red Globe', '192.168.1.35', SYSUTCDATETIME(), 1), (9, 'logistica2', 'CREATE', 'INVENTORY_DELIVERY', 3, 'Entrega 4L Score a productor 2', '192.168.1.45', SYSUTCDATETIME(), 1), (1, 'admin', 'DELETE', 'AGROCHEMICAL', 7, 'Eliminación lógica Lannate 90 SP', '192.168.1.10', SYSUTCDATETIME(), 1), (1, 'admin', 'RESTORE', 'AGROCHEMICAL', 7, 'Restauración Lannate 90 SP', '192.168.1.10', SYSUTCDATETIME(), 1), (10, 'admin2', 'CREATE', 'CUSTOMER', 1, 'Registro cliente Agroindustrias del Sur', '192.168.1.70', SYSUTCDATETIME(), 1),
(13, 'supervisor3', 'UPDATE', 'PRODUCTION_LOT', 4, 'Cierre lote LOT-2026-004 espárrago', '192.168.1.80', SYSUTCDATETIME(), 1), (5, 'logistica1', 'CREATE', 'INVENTORY_DELIVERY', 10, 'Entrega 3L Stimulate a productor 9', '192.168.1.40', SYSUTCDATETIME(), 1), (12, 'cert_diego', 'DISMISS', 'ALERT', 7, 'Descarte alerta sin aplicación Conta', '192.168.1.55', SYSUTCDATETIME(), 1), (1, 'admin', 'CREATE', 'PRODUCTO', 10, 'Creación producto Naranja Valencia', '192.168.1.10', SYSUTCDATETIME(), 1), (8, 'tech_miguel', 'CREATE', 'FIELD_APPLICATION', 8, 'Aplicación Engeo en algodón', '192.168.1.38', SYSUTCDATETIME(), 1);
GO

