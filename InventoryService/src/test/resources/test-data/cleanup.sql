-- Cleanup test data after integration tests
-- Remove data in correct order to respect foreign key constraints

DELETE FROM eventos_processados;
DELETE FROM ajustes_estoque;
DELETE FROM estoque;
DELETE FROM produtos;
DELETE FROM lojas;