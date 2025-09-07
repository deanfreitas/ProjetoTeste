package br.com.orderservice.application.port.in;

import br.com.orderservice.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderUseCase {
    Order createOrder(Order order);

    Optional<Order> findOrderById(Long id);

    List<Order> findOrdersByCustomerId(String customerId);
}