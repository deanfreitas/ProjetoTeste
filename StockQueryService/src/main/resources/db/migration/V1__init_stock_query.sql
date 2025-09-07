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
    loja_codigo
    VARCHAR
(
    64
) NOT NULL,
    produto_sku VARCHAR
(
    64
) NOT NULL,
    quantidade INT NOT NULL,
    PRIMARY KEY
(
    loja_codigo,
    produto_sku
),
    CONSTRAINT fk_estoque_loja FOREIGN KEY
(
    loja_codigo
) REFERENCES lojas
(
    codigo
) ON DELETE RESTRICT
  ON UPDATE CASCADE,
    CONSTRAINT fk_estoque_produto FOREIGN KEY
(
    produto_sku
) REFERENCES produtos
(
    sku
)
  ON DELETE RESTRICT
  ON UPDATE CASCADE
    );

-- Read-optimized views as specified in README section 4.2
CREATE
OR REPLACE VIEW stock_view AS
SELECT p.sku,
       p.nome            as produto_nome,
       SUM(e.quantidade) as quantidade_total
FROM produtos p
         LEFT JOIN estoque e ON p.sku = e.produto_sku
WHERE p.ativo = TRUE
GROUP BY p.sku, p.nome;

CREATE
OR REPLACE VIEW stock_by_store_view AS
SELECT e.loja_codigo,
       l.nome as loja_nome,
       e.produto_sku,
       p.nome as produto_nome,
       e.quantidade
FROM estoque e
         JOIN lojas l ON e.loja_codigo = l.codigo
         JOIN produtos p ON e.produto_sku = p.sku
WHERE p.ativo = TRUE;

CREATE
OR REPLACE VIEW product_store_view AS
SELECT p.sku                     as produto_sku,
       p.nome                    as produto_nome,
       l.codigo                  as loja_codigo,
       l.nome                    as loja_nome,
       COALESCE(e.quantidade, 0) as quantidade
FROM produtos p
         CROSS JOIN lojas l
         LEFT JOIN estoque e ON p.sku = e.produto_sku AND l.codigo = e.loja_codigo
WHERE p.ativo = TRUE;

-- Indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_estoque_produto ON estoque(produto_sku);
CREATE INDEX IF NOT EXISTS idx_estoque_loja ON estoque(loja_codigo);