package br.com.orderservice.infrastructure.adapters.out.persistence.mapper;

import br.com.orderservice.domain.enuns.OrderStatus;
import br.com.orderservice.domain.model.Order;
import br.com.orderservice.domain.model.OrderItem;
import br.com.orderservice.infrastructure.adapters.out.persistence.entity.OrderEntity;
import br.com.orderservice.infrastructure.adapters.out.persistence.entity.OrderItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderMapper Tests")
class OrderMapperTest {

    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderMapper = new OrderMapper();
    }

    @Nested
    @DisplayName("toEntity() Tests")
    class ToEntityTests {

        @Test
        @DisplayName("Should convert Order to OrderEntity successfully")
        void shouldConvertOrderToOrderEntitySuccessfully() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            OrderItem item1 = OrderItem.builder()
                    .id(1L)
                    .orderId(100L)
                    .sku("SKU001")
                    .quantity(2L)
                    .priceCentavos(1500L)
                    .build();

            OrderItem item2 = OrderItem.builder()
                    .id(2L)
                    .orderId(100L)
                    .sku("SKU002")
                    .quantity(1L)
                    .priceCentavos(3000L)
                    .build();

            Order order = Order.builder()
                    .id(100L)
                    .numero("ORDER-001")
                    .customerId("CUSTOMER-123")
                    .storeId(10L)
                    .status(OrderStatus.COMPLETED)
                    .totalCentavos(6000L)
                    .createdAt(now)
                    .updatedAt(now)
                    .items(Arrays.asList(item1, item2))
                    .build();

            // When
            OrderEntity result = orderMapper.toEntity(order);

            // Then
            assertNotNull(result);
            assertEquals(order.getId(), result.getId());
            assertEquals(order.getNumero(), result.getNumero());
            assertEquals(order.getCustomerId(), result.getCustomerId());
            assertEquals(order.getStoreId(), result.getStoreId());
            assertEquals(order.getStatus(), result.getStatus());
            assertEquals(order.getTotalCentavos(), result.getTotalCentavos());
            assertEquals(order.getCreatedAt(), result.getCreatedAt());
            assertEquals(order.getUpdatedAt(), result.getUpdatedAt());

            assertNotNull(result.getItems());
            assertEquals(2, result.getItems().size());

            OrderItemEntity resultItem1 = result.getItems().get(0);
            assertEquals(item1.getId(), resultItem1.getId());
            assertEquals(item1.getSku(), resultItem1.getSku());
            assertEquals(item1.getQuantity(), resultItem1.getQuantity());
            assertEquals(item1.getPriceCentavos(), resultItem1.getPriceCentavos());
            assertSame(result, resultItem1.getOrder());
        }

        @Test
        @DisplayName("Should return null when Order is null")
        void shouldReturnNullWhenOrderIsNull() {
            // Given
            Order order = null;

            // When
            OrderEntity result = orderMapper.toEntity(order);

            // Then
            assertNull(result);
        }

        @Test
        @DisplayName("Should handle Order with null items list")
        void shouldHandleOrderWithNullItemsList() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            Order order = Order.builder()
                    .id(100L)
                    .numero("ORDER-001")
                    .customerId("CUSTOMER-123")
                    .storeId(10L)
                    .status(OrderStatus.COMPLETED)
                    .totalCentavos(0L)
                    .createdAt(now)
                    .updatedAt(now)
                    .items(null)
                    .build();

            // When
            OrderEntity result = orderMapper.toEntity(order);

            // Then
            assertNotNull(result);
            assertEquals(order.getId(), result.getId());
            assertEquals(order.getNumero(), result.getNumero());
            assertNull(result.getItems());
        }

        @Test
        @DisplayName("Should handle Order with empty items list")
        void shouldHandleOrderWithEmptyItemsList() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            Order order = Order.builder()
                    .id(100L)
                    .numero("ORDER-001")
                    .customerId("CUSTOMER-123")
                    .storeId(10L)
                    .status(OrderStatus.COMPLETED)
                    .totalCentavos(0L)
                    .createdAt(now)
                    .updatedAt(now)
                    .items(new ArrayList<>())
                    .build();

            // When
            OrderEntity result = orderMapper.toEntity(order);

            // Then
            assertNotNull(result);
            assertEquals(order.getId(), result.getId());
            assertEquals(order.getNumero(), result.getNumero());
            assertNotNull(result.getItems());
            assertTrue(result.getItems().isEmpty());
        }
    }

    @Nested
    @DisplayName("toDomain() Tests")
    class ToDomainTests {

        @Test
        @DisplayName("Should convert OrderEntity to Order successfully")
        void shouldConvertOrderEntityToOrderSuccessfully() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            OrderEntity orderEntity = OrderEntity.builder()
                    .id(100L)
                    .numero("ORDER-001")
                    .customerId("CUSTOMER-123")
                    .storeId(10L)
                    .status(OrderStatus.COMPLETED)
                    .totalCentavos(4500L)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            OrderItemEntity itemEntity1 = OrderItemEntity.builder()
                    .id(1L)
                    .order(orderEntity)
                    .sku("SKU001")
                    .quantity(3L)
                    .priceCentavos(1500L)
                    .build();

            OrderItemEntity itemEntity2 = OrderItemEntity.builder()
                    .id(2L)
                    .order(orderEntity)
                    .sku("SKU002")
                    .quantity(1L)
                    .priceCentavos(3000L)
                    .build();

            orderEntity.setItems(Arrays.asList(itemEntity1, itemEntity2));

            // When
            Order result = orderMapper.toDomain(orderEntity);

            // Then
            assertNotNull(result);
            assertEquals(orderEntity.getId(), result.getId());
            assertEquals(orderEntity.getNumero(), result.getNumero());
            assertEquals(orderEntity.getCustomerId(), result.getCustomerId());
            assertEquals(orderEntity.getStoreId(), result.getStoreId());
            assertEquals(orderEntity.getStatus(), result.getStatus());
            assertEquals(orderEntity.getTotalCentavos(), result.getTotalCentavos());
            assertEquals(orderEntity.getCreatedAt(), result.getCreatedAt());
            assertEquals(orderEntity.getUpdatedAt(), result.getUpdatedAt());

            assertNotNull(result.getItems());
            assertEquals(2, result.getItems().size());

            OrderItem resultItem1 = result.getItems().get(0);
            assertEquals(itemEntity1.getId(), resultItem1.getId());
            assertEquals(itemEntity1.getSku(), resultItem1.getSku());
            assertEquals(itemEntity1.getQuantity(), resultItem1.getQuantity());
            assertEquals(itemEntity1.getPriceCentavos(), resultItem1.getPriceCentavos());
            assertEquals(orderEntity.getId(), resultItem1.getOrderId());
        }

        @Test
        @DisplayName("Should return null when OrderEntity is null")
        void shouldReturnNullWhenOrderEntityIsNull() {
            // Given
            OrderEntity orderEntity = null;

            // When
            Order result = orderMapper.toDomain(orderEntity);

            // Then
            assertNull(result);
        }

        @Test
        @DisplayName("Should handle OrderEntity with null items list")
        void shouldHandleOrderEntityWithNullItemsList() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            OrderEntity orderEntity = OrderEntity.builder()
                    .id(100L)
                    .numero("ORDER-001")
                    .customerId("CUSTOMER-123")
                    .storeId(10L)
                    .status(OrderStatus.COMPLETED)
                    .totalCentavos(0L)
                    .createdAt(now)
                    .updatedAt(now)
                    .items(null)
                    .build();

            // When
            Order result = orderMapper.toDomain(orderEntity);

            // Then
            assertNotNull(result);
            assertEquals(orderEntity.getId(), result.getId());
            assertEquals(orderEntity.getNumero(), result.getNumero());
            assertNull(result.getItems());
        }

        @Test
        @DisplayName("Should handle OrderEntity with empty items list")
        void shouldHandleOrderEntityWithEmptyItemsList() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            OrderEntity orderEntity = OrderEntity.builder()
                    .id(100L)
                    .numero("ORDER-001")
                    .customerId("CUSTOMER-123")
                    .storeId(10L)
                    .status(OrderStatus.COMPLETED)
                    .totalCentavos(0L)
                    .createdAt(now)
                    .updatedAt(now)
                    .items(new ArrayList<>())
                    .build();

            // When
            Order result = orderMapper.toDomain(orderEntity);

            // Then
            assertNotNull(result);
            assertEquals(orderEntity.getId(), result.getId());
            assertEquals(orderEntity.getNumero(), result.getNumero());
            assertNotNull(result.getItems());
            assertTrue(result.getItems().isEmpty());
        }

        @Test
        @DisplayName("Should handle OrderItemEntity with null order reference")
        void shouldHandleOrderItemEntityWithNullOrderReference() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            OrderEntity orderEntity = OrderEntity.builder()
                    .id(100L)
                    .numero("ORDER-001")
                    .customerId("CUSTOMER-123")
                    .storeId(10L)
                    .status(OrderStatus.COMPLETED)
                    .totalCentavos(1500L)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            OrderItemEntity itemEntity = OrderItemEntity.builder()
                    .id(1L)
                    .order(null) // null order reference
                    .sku("SKU001")
                    .quantity(1L)
                    .priceCentavos(1500L)
                    .build();

            orderEntity.setItems(Collections.singletonList(itemEntity));

            // When
            Order result = orderMapper.toDomain(orderEntity);

            // Then
            assertNotNull(result);
            assertNotNull(result.getItems());
            assertEquals(1, result.getItems().size());

            OrderItem resultItem = result.getItems().get(0);
            assertEquals(itemEntity.getId(), resultItem.getId());
            assertEquals(itemEntity.getSku(), resultItem.getSku());
            assertEquals(itemEntity.getQuantity(), resultItem.getQuantity());
            assertEquals(itemEntity.getPriceCentavos(), resultItem.getPriceCentavos());
            assertNull(resultItem.getOrderId());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle Order with all null fields except required ones")
        void shouldHandleOrderWithNullFields() {
            // Given
            Order order = Order.builder()
                    .id(null)
                    .numero(null)
                    .customerId(null)
                    .storeId(null)
                    .status(null)
                    .totalCentavos(null)
                    .createdAt(null)
                    .updatedAt(null)
                    .items(null)
                    .build();

            // When
            OrderEntity result = orderMapper.toEntity(order);

            // Then
            assertNotNull(result);
            assertNull(result.getId());
            assertNull(result.getNumero());
            assertNull(result.getCustomerId());
            assertNull(result.getStoreId());
            assertNull(result.getStatus());
            assertNull(result.getTotalCentavos());
            assertNull(result.getCreatedAt());
            assertNull(result.getUpdatedAt());
            assertNull(result.getItems());
        }

        @Test
        @DisplayName("Should handle OrderEntity with all null fields except required ones")
        void shouldHandleOrderEntityWithNullFields() {
            // Given
            OrderEntity orderEntity = OrderEntity.builder()
                    .id(null)
                    .numero(null)
                    .customerId(null)
                    .storeId(null)
                    .status(null)
                    .totalCentavos(null)
                    .createdAt(null)
                    .updatedAt(null)
                    .items(null)
                    .build();

            // When
            Order result = orderMapper.toDomain(orderEntity);

            // Then
            assertNotNull(result);
            assertNull(result.getId());
            assertNull(result.getNumero());
            assertNull(result.getCustomerId());
            assertNull(result.getStoreId());
            assertNull(result.getStatus());
            assertNull(result.getTotalCentavos());
            assertNull(result.getCreatedAt());
            assertNull(result.getUpdatedAt());
            assertNull(result.getItems());
        }

        @Test
        @DisplayName("Should maintain bidirectional relationship in toEntity conversion")
        void shouldMaintainBidirectionalRelationshipInToEntityConversion() {
            // Given
            OrderItem item1 = OrderItem.builder()
                    .id(1L)
                    .orderId(100L)
                    .sku("SKU001")
                    .quantity(1L)
                    .priceCentavos(1000L)
                    .build();

            OrderItem item2 = OrderItem.builder()
                    .id(2L)
                    .orderId(100L)
                    .sku("SKU002")
                    .quantity(2L)
                    .priceCentavos(2000L)
                    .build();

            Order order = Order.builder()
                    .id(100L)
                    .numero("ORDER-001")
                    .customerId("CUSTOMER-123")
                    .storeId(10L)
                    .status(OrderStatus.COMPLETED)
                    .totalCentavos(5000L)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .items(Arrays.asList(item1, item2))
                    .build();

            // When
            OrderEntity result = orderMapper.toEntity(order);

            // Then
            assertNotNull(result);
            assertNotNull(result.getItems());
            assertEquals(2, result.getItems().size());

            // Verify bidirectional relationship
            for (OrderItemEntity itemEntity : result.getItems()) {
                assertNotNull(itemEntity.getOrder());
                assertSame(result, itemEntity.getOrder());
            }
        }
    }
}