package br.com.inventoryservice.infrastructure.adapters.out.persistence.repository;

import br.com.inventoryservice.infrastructure.adapters.out.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
}
