-- Create pedidos table
CREATE TABLE pedidos
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    numero         VARCHAR(255) NOT NULL UNIQUE,
    cliente_id     VARCHAR(255),
    loja_id        BIGINT,
    status         VARCHAR(50)  NOT NULL CHECK (status IN ('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED',
                                                           'COMPLETED', 'CANCELLED')),
    total_centavos BIGINT       NOT NULL,
    criado_em      TIMESTAMP,
    atualizado_em  TIMESTAMP
);

-- Create itens_pedido table
CREATE TABLE itens_pedido
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pedido_id      BIGINT       NOT NULL,
    sku            VARCHAR(255) NOT NULL,
    quantidade     BIGINT       NOT NULL,
    preco_centavos BIGINT       NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos (id) ON DELETE CASCADE
);

-- Create indexes for performance
CREATE INDEX idx_pedidos_numero ON pedidos (numero);
CREATE INDEX idx_pedidos_cliente_id ON pedidos (cliente_id);
CREATE INDEX idx_pedidos_loja_id ON pedidos (loja_id);
CREATE INDEX idx_pedidos_status ON pedidos (status);
CREATE INDEX idx_itens_pedido_pedido_id ON itens_pedido (pedido_id);
CREATE INDEX idx_itens_pedido_sku ON itens_pedido (sku);