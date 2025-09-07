package br.com.inventoryservice.adapters.out.persistence.repository;

import br.com.inventoryservice.adapters.out.persistence.entity.StockAdjustmentEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockAdjustmentRepository extends JpaRepository<StockAdjustmentEntity, UUID> {
}
