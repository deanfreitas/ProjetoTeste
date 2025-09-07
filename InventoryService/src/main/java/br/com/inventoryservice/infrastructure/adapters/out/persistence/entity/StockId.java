package br.com.inventoryservice.infrastructure.adapters.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * Composite key for estoque (loja + sku).
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StockId implements Serializable {

    @Column(name = "loja_codigo", nullable = false, length = 64)
    private String lojaCodigo;

    @Column(name = "produto_sku", nullable = false, length = 64)
    private String produtoSku;
}
