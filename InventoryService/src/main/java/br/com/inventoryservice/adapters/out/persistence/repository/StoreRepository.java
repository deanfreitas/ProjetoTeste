package br.com.inventoryservice.adapters.out.persistence.repository;

import br.com.inventoryservice.adapters.out.persistence.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, String> {
}
