-- Flyway baseline migration for StockQueryService read model
-- Creates read-optimized views and tables for stock queries

-- Base tables (read-only copies for the read model)
CREATE TABLE IF NOT EXISTS produtos
(
    sku
    VARCHAR
(
    64
) NOT NULL,
    nome VARCHAR
(
    255
),
    ativo BOOLEAN,
    PRIMARY KEY
(
    sku
)
    );

CREATE TABLE IF NOT EXISTS lojas
(
    codigo
    VARCHAR
(
    64
) NOT NULL,
    nome VARCHAR
(
    255
),
    PRIMARY KEY
(
    codigo
)
    );

CREATE TABLE IF NOT EXISTS estoque
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    produto_id
    BIGINT
    NOT
    NULL,
    loja_id
    BIGINT
    NOT
    NULL,
    quantidade INT NOT NULL,
    data_atualizacao
    TIMESTAMP,
    nome_produto
    VARCHAR
(
    255
),
    nome_loja VARCHAR
(
    255
)
    );

CREATE
OR REPLACE VIEW stock_view AS
SELECT produto_id, nome_produto, SUM(quantidade) as quantidade_total
FROM estoque
GROUP BY produto_id, nome_produto;

CREATE
OR REPLACE VIEW stock_by_store_view AS
SELECT loja_id, nome_loja, produto_id, nome_produto, quantidade
FROM estoque;

CREATE
OR REPLACE VIEW product_store_view AS
SELECT produto_id as produto_sku, nome_produto, loja_id as loja_codigo, nome_loja, quantidade
FROM estoque;

-- Indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_estoque_produto ON estoque(produto_id);
CREATE INDEX IF NOT EXISTS idx_estoque_loja ON estoque(loja_id);