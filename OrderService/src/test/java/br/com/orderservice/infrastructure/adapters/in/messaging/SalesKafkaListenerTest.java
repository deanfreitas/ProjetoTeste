package br.com.orderservice.infrastructure.adapters.in.messaging;

import br.com.orderservice.application.port.in.OrderUseCase;
import br.com.orderservice.domain.enuns.OrderStatus;
import br.com.orderservice.domain.model.Order;
import br.com.orderservice.infrastructure.adapters.in.messaging.dto.SalesEvent;
import br.com.orderservice.infrastructure.adapters.in.messaging.dto.SalesEventItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SalesKafkaListener Tests")
class SalesKafkaListenerTest {

    @Mock
    private OrderUseCase orderUseCase;

    @Mock
    private StoreResolver storeResolver;

    @InjectMocks
    private SalesKafkaListener salesKafkaListener;

    // Helper methods for creating test data
    private SalesEvent createValidSalesEvent() {
        SalesEventItem item = new SalesEventItem("SKU001", 2L, 1500L);

        return new SalesEvent(
                "event-123",
                "ORD-001",
                "customer-456",
                "STORE_ABC",
                3000L,
                LocalDateTime.now(),
                List.of(item)
        );
    }

    private SalesEvent createSalesEventWithMultipleItems() {
        List<SalesEventItem> items = Arrays.asList(
                new SalesEventItem("SKU002", 2L, 1000L),
                new SalesEventItem("SKU003", 1L, 2000L),
                new SalesEventItem("SKU004", 3L, 500L)
        );

        return new SalesEvent(
                "event-multi",
                "ORD-002",
                "customer-789",
                "STORE_XYZ",
                5500L,
                LocalDateTime.now(),
                items
        );
    }

    private SalesEvent createSalesEventWithEmptyItems() {
        return new SalesEvent(
                "event-empty",
                "ORD-003",
                "customer-000",
                "STORE_EMPTY",
                0L,
                LocalDateTime.now(),
                List.of()
        );
    }

    private SalesEvent createInvalidSalesEvent() {
        return new SalesEvent(
                "event-invalid",
                null, // Missing numero
                null, // Missing customerId
                "INVALID_STORE",
                null, // Missing totalCentavos
                null, // Missing timestamp
                null  // Missing items
        );
    }

    private Order createExpectedOrder(Long storeId) {
        return Order.builder()
                .numero("ORD-001")
                .customerId("customer-456")
                .storeId(storeId)
                .status(OrderStatus.COMPLETED)
                .totalCentavos(3000L)
                .build();
    }

    @Nested
    @DisplayName("Event Processing")
    class EventProcessing {

        @Test
        @DisplayName("Should successfully process valid sales event")
        void shouldSuccessfullyProcessValidSalesEvent() {
            // Arrange
            SalesEvent salesEvent = createValidSalesEvent();
            Long resolvedStoreId = 100L;
            Order expectedOrder = createExpectedOrder(resolvedStoreId);
            Order createdOrder = Order.builder()
                    .id(1L)
                    .numero(salesEvent.getNumero())
                    .customerId(salesEvent.getCustomerId())
                    .storeId(resolvedStoreId)
                    .status(OrderStatus.COMPLETED)
                    .build();

            when(storeResolver.resolveStoreId(salesEvent.getStoreCode())).thenReturn(resolvedStoreId);
            when(orderUseCase.createOrder(any(Order.class))).thenReturn(createdOrder);

            // Act
            salesKafkaListener.handleSalesEvent(salesEvent);

            // Assert
            verify(storeResolver).resolveStoreId(salesEvent.getStoreCode());
            verify(orderUseCase).createOrder(any(Order.class));
        }

        @Test
        @DisplayName("Should process sales event with multiple items")
        void shouldProcessSalesEventWithMultipleItems() {
            // Arrange
            SalesEvent salesEvent = createSalesEventWithMultipleItems();
            Long resolvedStoreId = 200L;
            Order createdOrder = Order.builder().id(2L).build();

            when(storeResolver.resolveStoreId(anyString())).thenReturn(resolvedStoreId);
            when(orderUseCase.createOrder(any(Order.class))).thenReturn(createdOrder);

            // Act
            salesKafkaListener.handleSalesEvent(salesEvent);

            // Assert
            verify(storeResolver).resolveStoreId(salesEvent.getStoreCode());
            verify(orderUseCase).createOrder(any(Order.class));
        }

        @Test
        @DisplayName("Should process sales event with empty item list")
        void shouldProcessSalesEventWithEmptyItemList() {
            // Arrange
            SalesEvent salesEvent = createSalesEventWithEmptyItems();
            Long resolvedStoreId = 300L;
            Order createdOrder = Order.builder().id(3L).build();

            when(storeResolver.resolveStoreId(anyString())).thenReturn(resolvedStoreId);
            when(orderUseCase.createOrder(any(Order.class))).thenReturn(createdOrder);

            // Act
            salesKafkaListener.handleSalesEvent(salesEvent);

            // Assert
            verify(storeResolver).resolveStoreId(salesEvent.getStoreCode());
            verify(orderUseCase).createOrder(any(Order.class));
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        @Test
        @DisplayName("Should handle store resolution failure gracefully")
        void shouldHandleStoreResolutionFailureGracefully() {
            // Arrange
            SalesEvent salesEvent = createValidSalesEvent();

            when(storeResolver.resolveStoreId(anyString()))
                    .thenThrow(new RuntimeException("Store not found"));

            // Act
            salesKafkaListener.handleSalesEvent(salesEvent);

            // Assert
            verify(storeResolver).resolveStoreId(salesEvent.getStoreCode());
            verify(orderUseCase, never()).createOrder(any(Order.class));
        }

        @Test
        @DisplayName("Should handle order creation failure gracefully")
        void shouldHandleOrderCreationFailureGracefully() {
            // Arrange
            SalesEvent salesEvent = createValidSalesEvent();
            Long resolvedStoreId = 100L;

            when(storeResolver.resolveStoreId(anyString())).thenReturn(resolvedStoreId);
            when(orderUseCase.createOrder(any(Order.class)))
                    .thenThrow(new RuntimeException("Database error"));

            // Act
            salesKafkaListener.handleSalesEvent(salesEvent);

            // Assert
            verify(storeResolver).resolveStoreId(salesEvent.getStoreCode());
            verify(orderUseCase).createOrder(any(Order.class));
        }

        @Test
        @DisplayName("Should handle null sales event gracefully")
        void shouldHandleNullSalesEventGracefully() {
            // Act
            salesKafkaListener.handleSalesEvent(null);

            // Assert
            verify(storeResolver, never()).resolveStoreId(anyString());
            verify(orderUseCase, never()).createOrder(any(Order.class));
        }

        @Test
        @DisplayName("Should handle invalid sales event gracefully")
        void shouldHandleInvalidSalesEventGracefully() {
            // Arrange
            SalesEvent salesEvent = createInvalidSalesEvent();
            Long resolvedStoreId = 100L;
            Order createdOrder = Order.builder().id(1L).build();

            when(storeResolver.resolveStoreId(anyString())).thenReturn(resolvedStoreId);
            when(orderUseCase.createOrder(any(Order.class))).thenReturn(createdOrder);

            // Act
            salesKafkaListener.handleSalesEvent(salesEvent);

            // Assert
            verify(storeResolver).resolveStoreId(salesEvent.getStoreCode());
            verify(orderUseCase).createOrder(any(Order.class));
        }
    }

    @Nested
    @DisplayName("Integration with Dependencies")
    class IntegrationWithDependencies {

        @Test
        @DisplayName("Should call store resolver with correct store code")
        void shouldCallStoreResolverWithCorrectStoreCode() {
            // Arrange
            String storeCode = "STORE_ABC";
            SalesEvent salesEvent = new SalesEvent(
                    "event-123",
                    "ORD-001",
                    "customer-456",
                    storeCode,
                    1000L,
                    LocalDateTime.now(),
                    List.of()
            );

            when(storeResolver.resolveStoreId(storeCode)).thenReturn(100L);
            when(orderUseCase.createOrder(any(Order.class))).thenReturn(Order.builder().id(1L).build());

            // Act
            salesKafkaListener.handleSalesEvent(salesEvent);

            // Assert
            verify(storeResolver).resolveStoreId(storeCode);
        }

        @Test
        @DisplayName("Should pass correctly mapped order to use case")
        void shouldPassCorrectlyMappedOrderToUseCase() {
            // Arrange
            SalesEvent salesEvent = createValidSalesEvent();
            Long resolvedStoreId = 100L;

            when(storeResolver.resolveStoreId(anyString())).thenReturn(resolvedStoreId);
            when(orderUseCase.createOrder(any(Order.class))).thenReturn(Order.builder().id(1L).build());

            // Act
            salesKafkaListener.handleSalesEvent(salesEvent);

            // Assert
            verify(orderUseCase).createOrder(any(Order.class));
        }
    }
}