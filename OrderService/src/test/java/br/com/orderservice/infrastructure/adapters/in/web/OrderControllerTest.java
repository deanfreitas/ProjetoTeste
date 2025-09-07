package br.com.orderservice.infrastructure.adapters.in.web;

import br.com.orderservice.application.port.in.OrderUseCase;
import br.com.orderservice.domain.enuns.OrderStatus;
import br.com.orderservice.domain.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Order Controller Tests")
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderUseCase orderUseCase;

    @InjectMocks
    private OrderController orderController;

    @Nested
    @DisplayName("Get Order By ID")
    class GetOrderById {

        @Test
        @DisplayName("Should return order when valid ID is provided")
        void shouldReturnOrderWhenValidIdIsProvided() {
            // Given
            Long orderId = 1L;
            Order order = Order.builder()
                    .id(orderId)
                    .numero("ORD-001")
                    .customerId("customer-123")
                    .storeId(100L)
                    .status(OrderStatus.COMPLETED)
                    .totalCentavos(5000L)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .items(Collections.emptyList())
                    .build();

            when(orderUseCase.findOrderById(orderId)).thenReturn(Optional.of(order));

            // When
            ResponseEntity<Order> response = orderController.getOrderById(orderId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(orderId);
            assertThat(response.getBody().getNumero()).isEqualTo("ORD-001");
            assertThat(response.getBody().getCustomerId()).isEqualTo("customer-123");
            assertThat(response.getBody().getStoreId()).isEqualTo(100L);
            assertThat(response.getBody().getStatus()).isEqualTo(OrderStatus.COMPLETED);
            assertThat(response.getBody().getTotalCentavos()).isEqualTo(5000L);
        }

        @Test
        @DisplayName("Should return 404 when order is not found")
        void shouldReturn404WhenOrderIsNotFound() {
            // Given
            Long orderId = 999L;
            when(orderUseCase.findOrderById(orderId)).thenReturn(Optional.empty());

            // When
            ResponseEntity<Order> response = orderController.getOrderById(orderId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNull();
        }

        @Test
        @DisplayName("Should handle null ID")
        void shouldHandleNullId() {
            // Given
            when(orderUseCase.findOrderById(null)).thenReturn(Optional.empty());

            // When
            ResponseEntity<Order> response = orderController.getOrderById(null);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNull();
        }
    }

    @Nested
    @DisplayName("Get Orders By Customer ID")
    class GetOrdersByCustomerId {

        @Test
        @DisplayName("Should return orders when valid customer ID is provided")
        void shouldReturnOrdersWhenValidCustomerIdIsProvided() {
            // Given
            String customerId = "customer-123";
            List<Order> orders = Arrays.asList(
                    Order.builder()
                            .id(1L)
                            .numero("ORD-001")
                            .customerId(customerId)
                            .storeId(100L)
                            .status(OrderStatus.COMPLETED)
                            .totalCentavos(5000L)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .items(Collections.emptyList())
                            .build(),
                    Order.builder()
                            .id(2L)
                            .numero("ORD-002")
                            .customerId(customerId)
                            .storeId(200L)
                            .status(OrderStatus.COMPLETED)
                            .totalCentavos(3000L)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .items(Collections.emptyList())
                            .build()
            );

            when(orderUseCase.findOrdersByCustomerId(customerId)).thenReturn(orders);

            // When
            ResponseEntity<List<Order>> response = orderController.getOrdersByCustomerId(customerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).hasSize(2);
            assertThat(response.getBody().get(0).getId()).isEqualTo(1L);
            assertThat(response.getBody().get(0).getNumero()).isEqualTo("ORD-001");
            assertThat(response.getBody().get(0).getCustomerId()).isEqualTo(customerId);
            assertThat(response.getBody().get(0).getStatus()).isEqualTo(OrderStatus.COMPLETED);
            assertThat(response.getBody().get(1).getId()).isEqualTo(2L);
            assertThat(response.getBody().get(1).getNumero()).isEqualTo("ORD-002");
            assertThat(response.getBody().get(1).getCustomerId()).isEqualTo(customerId);
            assertThat(response.getBody().get(1).getStatus()).isEqualTo(OrderStatus.COMPLETED);
        }

        @Test
        @DisplayName("Should return empty list when customer has no orders")
        void shouldReturnEmptyListWhenCustomerHasNoOrders() {
            // Given
            String customerId = "customer-without-orders";
            when(orderUseCase.findOrdersByCustomerId(customerId)).thenReturn(Collections.emptyList());

            // When
            ResponseEntity<List<Order>> response = orderController.getOrdersByCustomerId(customerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty customer ID parameter")
        void shouldHandleEmptyCustomerIdParameter() {
            // Given
            String customerId = "";
            when(orderUseCase.findOrdersByCustomerId(customerId)).thenReturn(Collections.emptyList());

            // When
            ResponseEntity<List<Order>> response = orderController.getOrdersByCustomerId(customerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null customer ID parameter")
        void shouldHandleNullCustomerIdParameter() {
            // Given
            when(orderUseCase.findOrdersByCustomerId(null)).thenReturn(Collections.emptyList());

            // When
            ResponseEntity<List<Order>> response = orderController.getOrdersByCustomerId(null);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).isEmpty();
        }
    }
}