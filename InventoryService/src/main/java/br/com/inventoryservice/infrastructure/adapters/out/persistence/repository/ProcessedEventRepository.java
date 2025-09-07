package br.com.inventoryservice.infrastructure.adapters.out.persistence.repository;

import br.com.inventoryservice.infrastructure.adapters.out.persistence.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEventEntity, UUID> {
    Optional<ProcessedEventEntity> findByTopicoAndParticaoAndOffset(String topico, Integer particao, Long offset);
}
