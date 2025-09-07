package br.com.orderservice.infrastructure.adapters.out.persistence.repository;

import br.com.orderservice.infrastructure.adapters.out.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT o FROM OrderEntity o WHERE o.customerId = :customerId")
    List<OrderEntity> findByCustomerId(@Param("customerId") String customerId);
}