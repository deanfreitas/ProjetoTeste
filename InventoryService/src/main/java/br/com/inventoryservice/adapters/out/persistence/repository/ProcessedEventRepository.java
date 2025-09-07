package br.com.inventoryservice.adapters.out.persistence.repository;

import br.com.inventoryservice.adapters.out.persistence.entity.ProcessedEventEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEventEntity, UUID> {
    Optional<ProcessedEventEntity> findByTopicoAndParticaoAndOffset(String topico, Integer particao, Long offset);
}
