-- ============================================================
-- AVSA — SCRIPT COMPLETO: Clientes + Productos + Pedidos
-- Ejecutar en SQL Server Management Studio o Azure Data Studio
-- Idempotente: solo inserta si la tabla está vacía
-- ============================================================

USE avsa_db;
GO

-- ============================================================
-- 0. SEED CLIENTES (si la tabla está vacía)
-- ============================================================
IF NOT EXISTS (SELECT 1 FROM dbo.customer)
BEGIN
    INSERT INTO dbo.customer (codigo, razon_social, ruc, tipo_documento, numero_documento, pais, ciudad, direccion, telefono, email, contacto_principal, tipo_cliente, active, created_at)
    VALUES
    ('CLI-001','Agroindustrias del Sur S.A.C.','20512345678','RUC','20512345678','Perú','Lima','Av. La Marina 1234','01-4567890','ventas@agroindsur.pe','Luis Paredes','NACIONAL',1,SYSUTCDATETIME()),
    ('CLI-002','Fresh Fruits Export Ltd.',NULL,'TAX_ID','US-987654321','Estados Unidos','Miami','1200 Brickell Ave','+1-305-1234567','imports@freshfruits.com','John Smith','INTERNACIONAL',1,SYSUTCDATETIME()),
    ('CLI-003','Supermercados Peruanos S.A.','20101234567','RUC','20101234567','Perú','Lima','Calle Morelli 181 San Borja','01-6180000','compras@spsa.pe','Martha Gutiérrez','NACIONAL',1,SYSUTCDATETIME()),
    ('CLI-004','European Organic Traders GmbH',NULL,'TAX_ID','DE-123456789','Alemania','Hamburgo','Hafenstraße 45','+49-40-12345','procurement@eot.de','Hans Mueller','INTERNACIONAL',1,SYSUTCDATETIME()),
    ('CLI-005','Mercado Mayorista Cañete E.I.R.L.','20567890123','RUC','20567890123','Perú','Cañete','Jr. Bolognesi 456','01-5812345','info@mercadocanete.pe','Rosa Mendoza','NACIONAL',1,SYSUTCDATETIME()),
    ('CLI-006','Asia Pacific Foods Co.',NULL,'TAX_ID','JP-456789012','Japón','Tokio','Chiyoda-ku 3-1-1','+81-3-1234567','buy@apfoods.jp','Yuki Tanaka','INTERNACIONAL',1,SYSUTCDATETIME()),
    ('CLI-007','Distribuidora Agrícola Lima S.A.C.','20234567890','RUC','20234567890','Perú','Lima','Av. Argentina 2345','01-3456789','pedidos@distagri.pe','Carlos Vega','NACIONAL',1,SYSUTCDATETIME()),
    ('CLI-008','Canadian Harvest Imports Inc.',NULL,'TAX_ID','CA-789012345','Canadá','Toronto','100 Queen St West','+1-416-9876543','sourcing@canharvest.ca','Emma Wilson','INTERNACIONAL',1,SYSUTCDATETIME()),
    ('CLI-009','Cooperativa Agraria Cañete','20345678901','RUC','20345678901','Perú','Cañete','Av. Mariscal Benavides 789','01-5813456','cooperativa@coopcanete.pe','Jorge Salinas','NACIONAL',1,SYSUTCDATETIME()),
    ('CLI-010','UK Fresh Produce Ltd.',NULL,'TAX_ID','GB-321654987','Reino Unido','Londres','10 Downing Market','+44-20-7654321','orders@ukfresh.co.uk','James Brown','INTERNACIONAL',1,SYSUTCDATETIME()),
    ('CLI-011','Restaurant Chain Peru S.A.C.','20456789012','RUC','20456789012','Perú','Lima','Av. Javier Prado 567','01-2345678','compras@restchain.pe','Andrea Paz','NACIONAL',1,SYSUTCDATETIME()),
    ('CLI-012','Chilean Agricultural Trading SpA',NULL,'TAX_ID','CL-765432109','Chile','Santiago','Av. Providencia 1234','+56-2-9876543','imports@chiletrade.cl','Pablo Muñoz','INTERNACIONAL',1,SYSUTCDATETIME()),
    ('CLI-013','Exportadora Valle Sur S.A.','20567890234','RUC','20567890234','Perú','Ica','Panamericana Sur Km 300','056-234567','export@vallesur.pe','Diana Flores','NACIONAL',1,SYSUTCDATETIME()),
    ('CLI-014','Netherlands Tropical Fruits BV',NULL,'TAX_ID','NL-876543210','Países Bajos','Rotterdam','Europoort 789','+31-10-1234567','purchase@nltropical.nl','Pieter van Dijk','INTERNACIONAL',1,SYSUTCDATETIME()),
    ('CLI-015','Agrícola Don Bosco S.A.C.','20678901234','RUC','20678901234','Perú','Lima','Av. Colonial 3456','01-5678901','gerencia@donboscoagri.pe','Manuel Ríos','NACIONAL',1,SYSUTCDATETIME()),
    ('CLI-016','Dubai Fresh Markets LLC',NULL,'TAX_ID','AE-543210987','Emiratos Árabes','Dubái','Jebel Ali Free Zone','+971-4-1234567','import@dubaifresh.ae','Ahmed Al-Rashid','INTERNACIONAL',1,SYSUTCDATETIME()),
    ('CLI-017','Procesadora de Alimentos Lima S.A.','20789012345','RUC','20789012345','Perú','Lima','Av. Venezuela 890','01-4321098','logistica@proalim.pe','Sandra Morales','NACIONAL',1,SYSUTCDATETIME()),
    ('CLI-018','Brazilian Agro Commerce Ltda.',NULL,'TAX_ID','BR-210987654','Brasil','São Paulo','Rua Augusta 1500','+55-11-98765432','compras@brazilagro.com.br','Ricardo Silva','INTERNACIONAL',1,SYSUTCDATETIME()),
    ('CLI-019','Minimarket La Cosecha E.I.R.L.','10234567890','RUC','10234567890','Perú','Cañete','Calle Lima 234 Imperial','01-5814567','lacosecha@gmail.com','Julio Espinoza','NACIONAL',1,SYSUTCDATETIME()),
    ('CLI-020','South Korea Organic Co. Ltd.',NULL,'TAX_ID','KR-654321098','Corea del Sur','Seúl','Gangnam-gu 123','+82-2-1234567','organic@skorganic.kr','Min-Jun Park','INTERNACIONAL',1,SYSUTCDATETIME());
    PRINT '20 clientes insertados.';
END
ELSE
    PRINT 'Clientes ya existen — se omite seed.';
GO

-- ============================================================
-- 0b. SEED PRODUCTOS (si la tabla está vacía)
-- ============================================================
IF NOT EXISTS (SELECT 1 FROM dbo.productos)
BEGIN
    INSERT INTO dbo.productos (codigo, nombre, descripcion, categoria, unidad_medida, precio_unitario, stock_actual, stock_minimo, origen, estado, created_at)
    VALUES
    ('PROD-001','Mandarina Satsuma','Mandarina variedad Satsuma de primera calidad','Cítricos','Caja',85.00,500,50,'Cañete - San Vicente',1,SYSUTCDATETIME()),
    ('PROD-002','Palta Hass','Palta Hass calibre 16-20','Frutas','Caja',120.00,350,40,'Cañete - Lunahuaná',1,SYSUTCDATETIME()),
    ('PROD-003','Uva Red Globe','Uva de mesa variedad Red Globe','Frutas','Caja',95.00,280,30,'Cañete - Imperial',1,SYSUTCDATETIME()),
    ('PROD-004','Espárrago Verde','Espárrago verde fresco para exportación','Hortalizas','Kg',18.50,1200,100,'Cañete - Quilmaná',1,SYSUTCDATETIME()),
    ('PROD-005','Camote Amarillo','Camote amarillo tipo exportación','Tubérculos','Saco',45.00,600,60,'Cañete - Cerro Azul',1,SYSUTCDATETIME()),
    ('PROD-006','Mango Kent','Mango variedad Kent calibre 8-10','Frutas','Caja',110.00,420,50,'Cañete - San Vicente',1,SYSUTCDATETIME()),
    ('PROD-007','Maíz Morado','Maíz morado orgánico','Granos','Saco',65.00,300,30,'Cañete - Nuevo Imperial',1,SYSUTCDATETIME()),
    ('PROD-008','Algodón Tangüis','Algodón Tangüis fibra larga','Fibras','Quintal',250.00,150,20,'Cañete - San Vicente',1,SYSUTCDATETIME()),
    ('PROD-009','Limón Sutil','Limón sutil para industria y mesa','Cítricos','Caja',55.00,800,80,'Cañete - Imperial',1,SYSUTCDATETIME()),
    ('PROD-010','Naranja Valencia','Naranja Valencia jugo concentrado','Cítricos','Caja',70.00,450,50,'Cañete - Quilmaná',1,SYSUTCDATETIME()),
    ('PROD-011','Arándano Biloxi','Arándano fresco variedad Biloxi','Frutas','Clamshell',35.00,900,100,'Cañete - San Vicente',1,SYSUTCDATETIME()),
    ('PROD-012','Quinua Blanca','Quinua blanca orgánica lavada','Granos','Saco',180.00,200,25,'Cañete - Lunahuaná',1,SYSUTCDATETIME()),
    ('PROD-013','Tomate Cherry','Tomate cherry hidropónico','Hortalizas','Kg',12.00,650,70,'Cañete - Imperial',1,SYSUTCDATETIME()),
    ('PROD-014','Pimiento Piquillo','Pimiento piquillo rojo fresco','Hortalizas','Kg',14.50,400,40,'Cañete - Nuevo Imperial',1,SYSUTCDATETIME()),
    ('PROD-015','Granadilla','Granadilla fresca calibre AA','Frutas','Caja',90.00,250,30,'Cañete - Lunahuaná',1,SYSUTCDATETIME()),
    ('PROD-016','Frijol Canario','Frijol canario seleccionado','Legumbres','Saco',75.00,350,35,'Cañete - Cerro Azul',1,SYSUTCDATETIME()),
    ('PROD-017','Yuca Amarilla','Yuca amarilla para procesamiento','Tubérculos','Saco',38.00,500,50,'Cañete - Quilmaná',1,SYSUTCDATETIME()),
    ('PROD-018','Chirimoya Cumbe','Chirimoya variedad Cumbe exportación','Frutas','Caja',130.00,180,20,'Cañete - Lunahuaná',1,SYSUTCDATETIME()),
    ('PROD-019','Ají Amarillo','Ají amarillo fresco seleccionado','Hortalizas','Kg',8.50,700,70,'Cañete - San Vicente',1,SYSUTCDATETIME()),
    ('PROD-020','Higo Negro','Higo negro maduro para exportación','Frutas','Caja',100.00,220,25,'Cañete - Imperial',1,SYSUTCDATETIME());
    PRINT '20 productos insertados.';
END
ELSE
    PRINT 'Productos ya existen — se omite seed.';
GO

-- ============================================================
-- 1. CREAR TABLAS (solo si no existen)
-- ============================================================

IF OBJECT_ID('dbo.customer_orders', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.customer_orders (
        id                      BIGINT IDENTITY(1,1) PRIMARY KEY,
        numero_pedido           NVARCHAR(50)  NOT NULL UNIQUE,
        customer_id             BIGINT        NOT NULL,
        fecha_pedido            DATE          NOT NULL,
        fecha_entrega_estimada  DATE          NULL,
        estado                  NVARCHAR(30)  NOT NULL DEFAULT 'PENDIENTE',
        observaciones           NVARCHAR(500) NULL,
        subtotal                DECIMAL(12,2) NOT NULL DEFAULT 0.00,
        igv                     DECIMAL(12,2) NOT NULL DEFAULT 0.00,
        total                   DECIMAL(12,2) NOT NULL DEFAULT 0.00,
        active                  BIT           NOT NULL DEFAULT 1,
        created_at              DATETIME2     NULL,
        updated_at              DATETIME2     NULL,
        CONSTRAINT FK_co_customer  FOREIGN KEY (customer_id) REFERENCES dbo.customer(id),
        CONSTRAINT CK_co_estado    CHECK (estado IN ('PENDIENTE','CONFIRMADO','EN_PROCESO','ENTREGADO','CANCELADO'))
    );
    PRINT 'Tabla customer_orders creada.';
END
ELSE
    PRINT 'Tabla customer_orders ya existe — se omite creación.';
GO

IF OBJECT_ID('dbo.customer_order_details', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.customer_order_details (
        id              BIGINT IDENTITY(1,1) PRIMARY KEY,
        order_id        BIGINT        NOT NULL,
        producto_id     BIGINT        NOT NULL,
        cantidad        INT           NOT NULL,
        precio_unitario DECIMAL(10,2) NOT NULL,
        subtotal        DECIMAL(12,2) NOT NULL,
        observacion     NVARCHAR(300) NULL,
        created_at      DATETIME2     NULL,
        CONSTRAINT FK_cod_order    FOREIGN KEY (order_id)    REFERENCES dbo.customer_orders(id),
        CONSTRAINT FK_cod_producto FOREIGN KEY (producto_id) REFERENCES dbo.productos(id),
        CONSTRAINT CK_cod_cantidad CHECK (cantidad > 0)
    );
    PRINT 'Tabla customer_order_details creada.';
END
ELSE
    PRINT 'Tabla customer_order_details ya existe — se omite creación.';
GO

-- Índices (solo si no existen)
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_co_customer_id' AND object_id = OBJECT_ID('dbo.customer_orders'))
    CREATE INDEX IX_co_customer_id  ON dbo.customer_orders(customer_id);
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_co_fecha'       AND object_id = OBJECT_ID('dbo.customer_orders'))
    CREATE INDEX IX_co_fecha        ON dbo.customer_orders(fecha_pedido);
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_cod_order_id'   AND object_id = OBJECT_ID('dbo.customer_order_details'))
    CREATE INDEX IX_cod_order_id    ON dbo.customer_order_details(order_id);
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_cod_producto_id' AND object_id = OBJECT_ID('dbo.customer_order_details'))
    CREATE INDEX IX_cod_producto_id ON dbo.customer_order_details(producto_id);
GO

-- ============================================================
-- 2. SEED DATA — Solo insertar si la tabla está vacía
-- ============================================================
IF EXISTS (SELECT 1 FROM dbo.customer_orders)
BEGIN
    PRINT 'Ya existen pedidos — se omite el seed.';
END
ELSE
BEGIN

    DECLARE @oid BIGINT;

    -- ── PEDIDO 1: Agroindustrias del Sur (customer_id=1) — CONFIRMADO ──────
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260501-001', 1, '2026-05-01', '2026-05-10', 'CONFIRMADO',
            'Entrega en almacén central Lima', 30925.00, 5566.50, 36491.50, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid, 1, 200,  85.00, 17000.00, 'Mandarina Satsuma calibre A',  SYSUTCDATETIME()),
        (@oid, 2, 100, 120.00, 12000.00, 'Palta Hass calibre 16',        SYSUTCDATETIME()),
        (@oid, 9,  35,  55.00,  1925.00, NULL,                            SYSUTCDATETIME());

    -- ── PEDIDO 2: Fresh Fruits Export Ltd. (customer_id=2) — ENTREGADO ─────
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260503-001', 2, '2026-05-03', '2026-05-20', 'ENTREGADO',
            'Exportación Miami - contenedor refrigerado 40HC', 47500.00, 8550.00, 56050.00, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid, 2, 150, 120.00, 18000.00, 'Palta Hass exportación',       SYSUTCDATETIME()),
        (@oid, 3, 100,  95.00,  9500.00, 'Uva Red Globe premium',         SYSUTCDATETIME()),
        (@oid, 6, 100, 110.00, 11000.00, 'Mango Kent calibre 8',          SYSUTCDATETIME()),
        (@oid,11, 257,  35.00,  8999.50, 'Arándano Biloxi fresco',        SYSUTCDATETIME());

    -- ── PEDIDO 3: Supermercados Peruanos (customer_id=3) — EN_PROCESO ───────
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260505-001', 3, '2026-05-05', '2026-05-12', 'EN_PROCESO',
            'Distribución a 15 tiendas Lima Metropolitana', 19435.00, 3498.30, 22933.30, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid,  1,  80,  85.00,  6800.00, NULL,                           SYSUTCDATETIME()),
        (@oid, 13, 200,  12.00,  2400.00, 'Tomate cherry hidropónico',    SYSUTCDATETIME()),
        (@oid, 14, 150,  14.50,  2175.00, NULL,                           SYSUTCDATETIME()),
        (@oid, 19, 500,   8.50,  4250.00, 'Ají amarillo seleccionado',    SYSUTCDATETIME()),
        (@oid, 10,  53,  70.00,  3710.00, NULL,                           SYSUTCDATETIME());

    -- ── PEDIDO 4: European Organic Traders (customer_id=4) — CONFIRMADO ────
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260506-001', 4, '2026-05-06', '2026-06-01', 'CONFIRMADO',
            'Exportación Hamburgo - certificación orgánica requerida', 54000.00, 9720.00, 63720.00, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid, 12, 150, 180.00, 27000.00, 'Quinua orgánica certificada',  SYSUTCDATETIME()),
        (@oid,  7, 200,  65.00, 13000.00, 'Maíz morado orgánico',         SYSUTCDATETIME()),
        (@oid, 18, 107, 130.00, 13910.00, 'Chirimoya Cumbe exportación',  SYSUTCDATETIME());

    -- ── PEDIDO 5: Mercado Mayorista Cañete (customer_id=5) — PENDIENTE ──────
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260507-001', 5, '2026-05-07', '2026-05-09', 'PENDIENTE',
            'Entrega directa en mercado Cañete', 8750.00, 1575.00, 10325.00, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid,  5, 100,  45.00,  4500.00, 'Camote amarillo exportación',  SYSUTCDATETIME()),
        (@oid, 17, 100,  38.00,  3800.00, NULL,                           SYSUTCDATETIME()),
        (@oid, 16,   6,  75.00,   450.00, NULL,                           SYSUTCDATETIME());

    -- ── PEDIDO 6: Asia Pacific Foods (customer_id=6) — CONFIRMADO ───────────
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260508-001', 6, '2026-05-08', '2026-06-15', 'CONFIRMADO',
            'Exportación Tokio - embalaje especial japonés', 38510.00, 6931.80, 45441.80, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid, 15, 200,  90.00, 18000.00, 'Granadilla calibre AA',        SYSUTCDATETIME()),
        (@oid, 20, 100, 100.00, 10000.00, 'Higo negro maduro',            SYSUTCDATETIME()),
        (@oid, 18,  81, 130.00, 10530.00, NULL,                           SYSUTCDATETIME());

    -- ── PEDIDO 7: Distribuidora Agrícola Lima (customer_id=7) — CANCELADO ───
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260509-001', 7, '2026-05-09', '2026-05-15', 'CANCELADO',
            'Cancelado por cliente - cambio de proveedor', 14247.50, 2564.55, 16812.05, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid,  4, 500,  18.50,  9250.00, 'Espárrago verde exportación',  SYSUTCDATETIME()),
        (@oid, 13, 250,  12.00,  3000.00, NULL,                           SYSUTCDATETIME()),
        (@oid, 19, 235,   8.50,  1997.50, NULL,                           SYSUTCDATETIME());

    -- ── PEDIDO 8: Canadian Harvest Imports (customer_id=8) — ENTREGADO ──────
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260510-001', 8, '2026-05-10', '2026-05-28', 'ENTREGADO',
            'Exportación Toronto - contenedor reefer', 32415.00, 5834.70, 38249.70, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid,  3, 200,  95.00, 19000.00, 'Uva Red Globe premium',        SYSUTCDATETIME()),
        (@oid,  6, 100, 110.00, 11000.00, 'Mango Kent calibre 10',        SYSUTCDATETIME()),
        (@oid, 11,  69,  35.00,  2415.00, NULL,                           SYSUTCDATETIME());

    -- ── PEDIDO 9: Cooperativa Agraria Cañete (customer_id=9) — EN_PROCESO ───
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260512-001', 9, '2026-05-12', '2026-05-18', 'EN_PROCESO',
            'Distribución interna cooperativa', 11200.00, 2016.00, 13216.00, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid,  7, 100,  65.00,  6500.00, 'Maíz morado procesamiento',    SYSUTCDATETIME()),
        (@oid, 16,  50,  75.00,  3750.00, NULL,                           SYSUTCDATETIME()),
        (@oid, 17,  25,  38.00,   950.00, NULL,                           SYSUTCDATETIME());

    -- ── PEDIDO 10: UK Fresh Produce (customer_id=10) — CONFIRMADO ───────────
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260513-001', 10, '2026-05-13', '2026-06-10', 'CONFIRMADO',
            'Exportación Londres - certificado GlobalGAP', 42000.00, 7560.00, 49560.00, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid,  2, 200, 120.00, 24000.00, 'Palta Hass calibre 16-20',    SYSUTCDATETIME()),
        (@oid, 11, 200,  35.00,  7000.00, 'Arándano Biloxi fresco',       SYSUTCDATETIME()),
        (@oid, 15, 122,  90.00, 10980.00, NULL,                           SYSUTCDATETIME());

    -- ── PEDIDO 11: Agroindustrias del Sur (customer_id=1) — PENDIENTE ────────
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260514-001', 1, '2026-05-14', '2026-05-22', 'PENDIENTE',
            'Segundo pedido del mes - urgente', 16490.00, 2968.20, 19458.20, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid,  1, 100,  85.00,  8500.00, NULL,                           SYSUTCDATETIME()),
        (@oid, 10, 100,  70.00,  7000.00, NULL,                           SYSUTCDATETIME()),
        (@oid,  9,  18,  55.00,   990.00, NULL,                           SYSUTCDATETIME());

    -- ── PEDIDO 12: Supermercados Peruanos (customer_id=3) — CONFIRMADO ───────
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260515-001', 3, '2026-05-15', '2026-05-20', 'CONFIRMADO',
            'Reposición semanal tiendas Lima Norte', 9602.50, 1728.45, 11330.95, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid, 13, 300,  12.00,  3600.00, NULL,                           SYSUTCDATETIME()),
        (@oid, 14, 200,  14.50,  2900.00, NULL,                           SYSUTCDATETIME()),
        (@oid, 19, 365,   8.50,  3102.50, NULL,                           SYSUTCDATETIME());

    -- ── PEDIDO 13: Mercado Mayorista Cañete (customer_id=5) — ENTREGADO ──────
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260516-001', 5, '2026-05-16', '2026-05-17', 'ENTREGADO',
            'Entrega express mercado local', 6750.00, 1215.00, 7965.00, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid,  5,  50,  45.00,  2250.00, NULL,                           SYSUTCDATETIME()),
        (@oid, 16,  30,  75.00,  2250.00, NULL,                           SYSUTCDATETIME()),
        (@oid,  7,  34,  65.00,  2210.00, NULL,                           SYSUTCDATETIME());

    -- ── PEDIDO 14: European Organic Traders (customer_id=4) — EN_PROCESO ────
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260517-001', 4, '2026-05-17', '2026-06-20', 'EN_PROCESO',
            'Segundo contenedor Hamburgo - orgánico certificado', 36000.00, 6480.00, 42480.00, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid,  8,  60, 250.00, 15000.00, 'Algodón Tangüis fibra larga',  SYSUTCDATETIME()),
        (@oid, 12,  50, 180.00,  9000.00, 'Quinua blanca orgánica',       SYSUTCDATETIME()),
        (@oid, 20, 120, 100.00, 12000.00, NULL,                           SYSUTCDATETIME());

    -- ── PEDIDO 15: Fresh Fruits Export (customer_id=2) — CONFIRMADO ─────────
    INSERT INTO dbo.customer_orders
        (numero_pedido, customer_id, fecha_pedido, fecha_entrega_estimada, estado, observaciones, subtotal, igv, total, active, created_at)
    VALUES ('PED-20260519-001', 2, '2026-05-19', '2026-06-05', 'CONFIRMADO',
            'Segundo envío Miami - temporada alta', 52500.00, 9450.00, 61950.00, 1, SYSUTCDATETIME());
    SET @oid = SCOPE_IDENTITY();
    INSERT INTO dbo.customer_order_details (order_id, producto_id, cantidad, precio_unitario, subtotal, observacion, created_at) VALUES
        (@oid,  2, 150, 120.00, 18000.00, 'Palta Hass calibre 20',        SYSUTCDATETIME()),
        (@oid,  3, 100,  95.00,  9500.00, NULL,                           SYSUTCDATETIME()),
        (@oid,  6, 100, 110.00, 11000.00, NULL,                           SYSUTCDATETIME()),
        (@oid, 15, 100,  90.00,  9000.00, 'Granadilla AA exportación',    SYSUTCDATETIME()),
        (@oid, 20,  50, 100.00,  5000.00, NULL,                           SYSUTCDATETIME());

    PRINT 'Seed completado: 15 pedidos y sus detalles insertados correctamente.';

END
GO

-- ============================================================
-- 3. VERIFICACIÓN
-- ============================================================

-- Resumen de pedidos por estado
SELECT
    co.estado,
    COUNT(*)        AS pedidos,
    SUM(co.total)   AS monto_total
FROM dbo.customer_orders co
GROUP BY co.estado
ORDER BY monto_total DESC;
GO

-- Listado completo con cliente y cantidad de ítems
SELECT
    co.numero_pedido,
    c.razon_social      AS cliente,
    c.pais,
    co.fecha_pedido,
    co.estado,
    co.subtotal,
    co.igv,
    co.total,
    COUNT(d.id)         AS items
FROM dbo.customer_orders co
INNER JOIN dbo.customer c                ON c.id       = co.customer_id
INNER JOIN dbo.customer_order_details d  ON d.order_id = co.id
GROUP BY
    co.numero_pedido, c.razon_social, c.pais,
    co.fecha_pedido,  co.estado,
    co.subtotal,      co.igv, co.total
ORDER BY co.fecha_pedido;
GO

-- Conteo final
SELECT
    (SELECT COUNT(*) FROM dbo.customer)              AS total_clientes,
    (SELECT COUNT(*) FROM dbo.productos)             AS total_productos,
    (SELECT COUNT(*) FROM dbo.customer_orders)       AS total_pedidos,
    (SELECT COUNT(*) FROM dbo.customer_order_details) AS total_detalles;
GO
