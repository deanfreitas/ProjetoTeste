package br.com.inventoryservice.adapters.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity for table 'ajustes_estoque' to track administrative stock adjustments.
 */
@Entity
@Table(name = "ajustes_estoque")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StockAdjustmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(name = "loja_codigo", nullable = false, length = 64)
    private String lojaCodigo;

    @Column(name = "produto_sku", nullable = false, length = 64)
    private String produtoSku;

    @Column(name = "delta", nullable = false)
    private Integer delta;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;
}
