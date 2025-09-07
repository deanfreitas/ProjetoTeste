-- Setup test data for product integration tests
-- Create some products for testing purposes

INSERT INTO produtos (sku, nome, ativo) VALUES 
('SKU001', 'Test Product 1', true),
('SKU002', 'Test Product 2', true),
('SKU003', 'Test Product 3', false),
('SKU004', 'Test Product 4', true),
('SKU005', 'Test Product 5', false);

-- Create some stores for testing purposes
INSERT INTO lojas (codigo, nome) VALUES 
('STORE001', 'Test Store 1'),
('STORE002', 'Test Store 2');

-- Initialize stock for some products (using foreign key codes)
INSERT INTO estoque (produto_sku, loja_codigo, quantidade) VALUES
('SKU001', 'STORE001', 100),  -- SKU001 at STORE001
('SKU002', 'STORE001', 50),   -- SKU002 at STORE001
('SKU001', 'STORE002', 75),   -- SKU001 at STORE002
('SKU004', 'STORE002', 200);  -- SKU004 at STORE002