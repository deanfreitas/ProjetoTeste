package br.com.inventoryservice.infrastructure.adapters.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

/**
 * Entity for table 'produtos'.
 */
@Entity
@Table(name = "produtos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductEntity {

    @Id
    @Column(name = "sku", nullable = false, length = 64)
    private String sku;

    @Column(name = "nome")
    private String nome;

    @Column(name = "ativo")
    private Boolean ativo;
}
