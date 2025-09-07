package br.com.inventoryservice.infrastructure.adapters.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

/**
 * Entity for table 'lojas'. Owned by Cadastro service; Inventory reads/updates via events.
 */
@Entity
@Table(name = "lojas")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StoreEntity {

    @Id
    @Column(name = "codigo", nullable = false, length = 64)
    private String codigo;

    @Column(name = "nome")
    private String nome;
}
