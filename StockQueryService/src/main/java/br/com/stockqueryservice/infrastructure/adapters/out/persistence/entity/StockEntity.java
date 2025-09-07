package br.com.stockqueryservice.infrastructure.adapters.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "estoque")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "produto_id", nullable = false)
    private Long productId;

    @Column(name = "loja_id", nullable = false)
    private Long storeId;

    @Column(name = "quantidade", nullable = false)
    private Integer quantity;

    @Column(name = "data_atualizacao")
    private LocalDateTime lastUpdated;

    @Column(name = "nome_produto")
    private String productName;

    @Column(name = "nome_loja")
    private String storeName;
}