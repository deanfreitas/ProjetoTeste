package br.com.inventoryservice.infrastructure.adapters.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Tracks processed Kafka events for idempotency, with a uniqueness constraint on (topic, partition, offset).
 */
@Entity
@Table(name = "eventos_processados",
        uniqueConstraints = @UniqueConstraint(name = "uk_event", columnNames = {"topico", "particao", "offset_value"}))
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProcessedEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(name = "topico", nullable = false, length = 128)
    private String topico;

    @Column(name = "particao", nullable = false)
    private Integer particao;

    @Column(name = "offset_value", nullable = false)
    private Long offset;

    @Column(name = "processado_em", nullable = false)
    private Instant processadoEm;
}
