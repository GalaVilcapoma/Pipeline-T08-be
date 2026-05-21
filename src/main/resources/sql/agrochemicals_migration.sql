-- ============================================================
-- Migración: Agregar columnas de auditoría a agrochemicals
-- Ejecutar en avsa_db si la tabla ya existe sin estas columnas
-- ============================================================

USE avsa_db;
GO

-- Agregar deleted_at si no existe
IF NOT EXISTS (
    SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'agrochemicals' AND COLUMN_NAME = 'deleted_at'
)
BEGIN
    ALTER TABLE agrochemicals ADD deleted_at DATETIME2 NULL;
    PRINT 'Columna deleted_at agregada.';
END
GO

-- Agregar restored_at si no existe
IF NOT EXISTS (
    SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'agrochemicals' AND COLUMN_NAME = 'restored_at'
)
BEGIN
    ALTER TABLE agrochemicals ADD restored_at DATETIME2 NULL;
    PRINT 'Columna restored_at agregada.';
END
GO

-- Verificar estructura final
SELECT
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'agrochemicals'
ORDER BY ORDINAL_POSITION;
GO

PRINT 'Migración completada. Tabla agrochemicals lista para auditoría completa.';
GO
