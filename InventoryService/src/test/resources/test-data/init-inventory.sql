-- Flyway baseline migration for InventoryService schema
-- Updated to be compatible with H2 (in-memory) and MySQL mode

-- Produtos
CREATE TABLE IF NOT EXISTS produtos (
    sku VARCHAR(64) NOT NULL,
    nome VARCHAR(255),
    ativo BOOLEAN,
    PRIMARY KEY (sku)
);

-- Lojas
CREATE TABLE IF NOT EXISTS lojas (
    codigo VARCHAR(64) NOT NULL,
    nome VARCHAR(255),
    PRIMARY KEY (codigo)
);

-- Estoque (composite key loja + sku)
CREATE TABLE IF NOT EXISTS estoque (
    loja_codigo  VARCHAR(64) NOT NULL,
    produto_sku  VARCHAR(64) NOT NULL,
    quantidade   INT NOT NULL,
    PRIMARY KEY (loja_codigo, produto_sku),
    CONSTRAINT fk_estoque_loja FOREIGN KEY (loja_codigo) REFERENCES lojas(codigo) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_estoque_produto FOREIGN KEY (produto_sku) REFERENCES produtos(sku) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Ajustes de estoque (audit log)
CREATE TABLE IF NOT EXISTS ajustes_estoque (
    id CHAR(36) NOT NULL,
    loja_codigo VARCHAR(64) NOT NULL,
    produto_sku VARCHAR(64) NOT NULL,
    delta INT NOT NULL,
    motivo VARCHAR(255),
    criado_em TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_ajuste_loja FOREIGN KEY (loja_codigo) REFERENCES lojas(codigo) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_ajuste_produto FOREIGN KEY (produto_sku) REFERENCES produtos(sku) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Create indexes separately for H2 compatibility
CREATE INDEX IF NOT EXISTS idx_ajuste_loja ON ajustes_estoque (loja_codigo);
CREATE INDEX IF NOT EXISTS idx_ajuste_produto ON ajustes_estoque (produto_sku);

-- Eventos processados (idempotência)
CREATE TABLE IF NOT EXISTS eventos_processados (
    id CHAR(36) NOT NULL,
    topico VARCHAR(128) NOT NULL,
    particao INT NOT NULL,
    offset_value BIGINT NOT NULL,
    processado_em TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_event UNIQUE (topico, particao, offset_value)
);
