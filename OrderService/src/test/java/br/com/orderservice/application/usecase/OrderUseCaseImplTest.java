package br.com.orderservice.application.usecase;

import br.com.orderservice.application.port.out.OrderPort;
import br.com.orderservice.domain.enuns.OrderStatus;
import br.com.orderservice.domain.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderUseCaseImpl")
class OrderUseCaseImplTest {

    @Mock
    private OrderPort orderPort;

    @InjectMocks
    private OrderUseCaseImpl orderUseCase;

    private Order validOrder;
    private Order savedOrder;

    @BeforeEach
    void setUp() {
        validOrder = Order.builder()
                .numero("ORD-001")
                .customerId("customer123")
                .storeId(1L)
                .status(OrderStatus.COMPLETED)
                .totalCentavos(10000L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();

        savedOrder = Order.builder()
                .id(1L)
                .numero("ORD-001")
                .customerId("customer123")
                .storeId(1L)
                .status(OrderStatus.COMPLETED)
                .totalCentavos(10000L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();
    }

    @Nested
    @DisplayName("createOrder")
    class CreateOrderTests {

        @Test
        @DisplayName("Should create order successfully when valid order is provided")
        void shouldCreateOrderSuccessfully() {
            // Given
            when(orderPort.save(any(Order.class))).thenReturn(savedOrder);

            // When
            Order result = orderUseCase.createOrder(validOrder);

            // Then
            assertNotNull(result);
            assertEquals(savedOrder.getId(), result.getId());
            assertEquals(savedOrder.getNumero(), result.getNumero());
            assertEquals(savedOrder.getCustomerId(), result.getCustomerId());
            assertEquals(savedOrder.getStoreId(), result.getStoreId());
            assertEquals(savedOrder.getStatus(), result.getStatus());
            assertEquals(savedOrder.getTotalCentavos(), result.getTotalCentavos());
            verify(orderPort).save(validOrder);
        }

        @Test
        @DisplayName("Should delegate to orderPort.save when creating order")
        void shouldDelegateToOrderPortSave() {
            // Given
            when(orderPort.save(any(Order.class))).thenReturn(savedOrder);

            // When
            orderUseCase.createOrder(validOrder);

            // Then
            verify(orderPort, times(1)).save(validOrder);
        }

        @Test
        @DisplayName("Should return the same order returned by orderPort")
        void shouldReturnOrderFromPort() {
            // Given
            Order expectedOrder = Order.builder()
                    .id(999L)
                    .numero("CUSTOM-ORDER")
                    .customerId("custom-customer")
                    .build();
            when(orderPort.save(any(Order.class))).thenReturn(expectedOrder);

            // When
            Order result = orderUseCase.createOrder(validOrder);

            // Then
            assertSame(expectedOrder, result);
        }
    }

    @Nested
    @DisplayName("findOrderById")
    class FindOrderByIdTests {

        @Test
        @DisplayName("Should return order when order exists")
        void shouldReturnOrderWhenExists() {
            // Given
            Long orderId = 1L;
            when(orderPort.findById(orderId)).thenReturn(Optional.of(savedOrder));

            // When
            Optional<Order> result = orderUseCase.findOrderById(orderId);

            // Then
            assertTrue(result.isPresent());
            assertEquals(savedOrder, result.get());
            verify(orderPort).findById(orderId);
        }

        @Test
        @DisplayName("Should return empty optional when order does not exist")
        void shouldReturnEmptyOptionalWhenOrderNotFound() {
            // Given
            Long nonExistentOrderId = 999L;
            when(orderPort.findById(nonExistentOrderId)).thenReturn(Optional.empty());

            // When
            Optional<Order> result = orderUseCase.findOrderById(nonExistentOrderId);

            // Then
            assertFalse(result.isPresent());
            verify(orderPort).findById(nonExistentOrderId);
        }

        @Test
        @DisplayName("Should delegate to orderPort.findById with correct ID")
        void shouldDelegateToOrderPortFindById() {
            // Given
            Long orderId = 42L;
            when(orderPort.findById(orderId)).thenReturn(Optional.empty());

            // When
            orderUseCase.findOrderById(orderId);

            // Then
            verify(orderPort, times(1)).findById(orderId);
        }

        @Test
        @DisplayName("Should handle null ID gracefully")
        void shouldHandleNullIdGracefully() {
            // Given
            when(orderPort.findById(null)).thenReturn(Optional.empty());

            // When
            Optional<Order> result = orderUseCase.findOrderById(null);

            // Then
            assertFalse(result.isPresent());
            verify(orderPort).findById(null);
        }
    }

    @Nested
    @DisplayName("findOrdersByCustomerId")
    class FindOrdersByCustomerIdTests {

        @Test
        @DisplayName("Should return list of orders when customer has orders")
        void shouldReturnOrdersWhenCustomerHasOrders() {
            // Given
            String customerId = "customer123";
            Order order1 = Order.builder()
                    .id(1L)
                    .customerId(customerId)
                    .numero("ORD-001")
                    .build();
            Order order2 = Order.builder()
                    .id(2L)
                    .customerId(customerId)
                    .numero("ORD-002")
                    .build();
            List<Order> expectedOrders = Arrays.asList(order1, order2);
            when(orderPort.findByCustomerId(customerId)).thenReturn(expectedOrders);

            // When
            List<Order> result = orderUseCase.findOrdersByCustomerId(customerId);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(expectedOrders, result);
            verify(orderPort).findByCustomerId(customerId);
        }

        @Test
        @DisplayName("Should return empty list when customer has no orders")
        void shouldReturnEmptyListWhenCustomerHasNoOrders() {
            // Given
            String customerId = "customerWithNoOrders";
            when(orderPort.findByCustomerId(customerId)).thenReturn(Collections.emptyList());

            // When
            List<Order> result = orderUseCase.findOrdersByCustomerId(customerId);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(orderPort).findByCustomerId(customerId);
        }

        @Test
        @DisplayName("Should delegate to orderPort.findByCustomerId with correct customer ID")
        void shouldDelegateToOrderPortFindByCustomerId() {
            // Given
            String customerId = "testCustomer";
            when(orderPort.findByCustomerId(customerId)).thenReturn(Collections.emptyList());

            // When
            orderUseCase.findOrdersByCustomerId(customerId);

            // Then
            verify(orderPort, times(1)).findByCustomerId(customerId);
        }

        @Test
        @DisplayName("Should handle null customer ID gracefully")
        void shouldHandleNullCustomerIdGracefully() {
            // Given
            when(orderPort.findByCustomerId(null)).thenReturn(Collections.emptyList());

            // When
            List<Order> result = orderUseCase.findOrdersByCustomerId(null);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(orderPort).findByCustomerId(null);
        }

        @Test
        @DisplayName("Should handle empty customer ID gracefully")
        void shouldHandleEmptyCustomerIdGracefully() {
            // Given
            String emptyCustomerId = "";
            when(orderPort.findByCustomerId(emptyCustomerId)).thenReturn(Collections.emptyList());

            // When
            List<Order> result = orderUseCase.findOrdersByCustomerId(emptyCustomerId);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(orderPort).findByCustomerId(emptyCustomerId);
        }

        @Test
        @DisplayName("Should return the same list returned by orderPort")
        void shouldReturnSameListFromPort() {
            // Given
            String customerId = "customer123";
            List<Order> expectedList = Collections.singletonList(savedOrder);
            when(orderPort.findByCustomerId(customerId)).thenReturn(expectedList);

            // When
            List<Order> result = orderUseCase.findOrdersByCustomerId(customerId);

            // Then
            assertSame(expectedList, result);
        }
    }
}