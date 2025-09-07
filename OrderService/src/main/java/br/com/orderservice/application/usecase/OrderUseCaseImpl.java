package br.com.orderservice.application.usecase;

import br.com.orderservice.application.port.in.OrderUseCase;
import br.com.orderservice.application.port.out.OrderPort;
import br.com.orderservice.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderUseCaseImpl implements OrderUseCase {

    private final OrderPort orderPort;

    @Override
    public Order createOrder(Order order) {
        return orderPort.save(order);
    }

    @Override
    public Optional<Order> findOrderById(Long id) {
        return orderPort.findById(id);
    }

    @Override
    public List<Order> findOrdersByCustomerId(String customerId) {
        return orderPort.findByCustomerId(customerId);
    }
}