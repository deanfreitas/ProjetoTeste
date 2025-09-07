package br.com.inventoryservice.infrastructure.adapters.out.persistence.repository;

import br.com.inventoryservice.infrastructure.adapters.out.persistence.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, String> {
}
