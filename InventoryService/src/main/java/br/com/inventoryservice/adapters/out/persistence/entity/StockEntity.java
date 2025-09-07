package br.com.inventoryservice.adapters.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

/**
 * Entity for table 'estoque' using composite key StockId.
 */
@Entity
@Table(name = "estoque")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StockEntity {

    @EmbeddedId
    private StockId id;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;
}
