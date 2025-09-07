package br.com.orderservice.infrastructure.adapters.in.web;

import br.com.orderservice.application.port.in.OrderUseCase;
import br.com.orderservice.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderUseCase orderUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderUseCase.findOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@RequestParam String customerId) {
        List<Order> orders = orderUseCase.findOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }
}