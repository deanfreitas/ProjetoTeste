package br.com.inventoryservice.infrastructure.adapters.out.persistence.repository;

import br.com.inventoryservice.infrastructure.adapters.out.persistence.entity.StockAdjustmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockAdjustmentRepository extends JpaRepository<StockAdjustmentEntity, UUID> {
}
