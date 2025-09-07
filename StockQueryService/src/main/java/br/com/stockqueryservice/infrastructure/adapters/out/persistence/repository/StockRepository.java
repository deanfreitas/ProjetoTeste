package br.com.stockqueryservice.infrastructure.adapters.out.persistence.repository;

import br.com.stockqueryservice.infrastructure.adapters.out.persistence.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {

    @Query("SELECT s FROM StockEntity s WHERE s.productId = :productId")
    List<StockEntity> findByProductId(@Param("productId") Long productId);

    @Query("SELECT s FROM StockEntity s WHERE s.productId = :productId AND s.storeId = :storeId")
    Optional<StockEntity> findByProductIdAndStoreId(@Param("productId") Long productId, @Param("storeId") Long storeId);

    @Query("SELECT s FROM StockEntity s WHERE s.storeId = :storeId")
    List<StockEntity> findByStoreId(@Param("storeId") Long storeId);
}