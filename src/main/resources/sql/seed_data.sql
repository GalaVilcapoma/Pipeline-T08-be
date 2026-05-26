SELECT
    (SELECT COUNT(*) FROM dbo.customer)               AS clientes,
    (SELECT COUNT(*) FROM dbo.productos)              AS productos,
    (SELECT COUNT(*) FROM dbo.customer_orders)        AS pedidos,
    (SELECT COUNT(*) FROM dbo.customer_order_details) AS detalles;
GO
