package br.com.inventoryservice.adapters.out.persistence.repository;

import br.com.inventoryservice.adapters.out.persistence.entity.StockEntity;
import br.com.inventoryservice.adapters.out.persistence.entity.StockId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockEntity, StockId> {
}
