package br.com.orderservice.application.port.out;

import br.com.orderservice.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderPort {
    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findByCustomerId(String customerId);

    List<Order> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);
}