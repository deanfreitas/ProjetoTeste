package br.com.orderservice.infrastructure.adapters.out.persistence.entity;

import br.com.orderservice.domain.enuns.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderEntity")
class OrderEntityTest {

    @Test
    @DisplayName("Should create OrderEntity with constructor and verify all getters")
    void testOrderEntityConstructorAndGetters() {
        // Given
        Long id = 1L;
        String numero = "ORDER-123";
        String customerId = "customer456";
        Long storeId = 100L;
        OrderStatus status = OrderStatus.COMPLETED;
        Long totalCentavos = 5000L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        List<OrderItemEntity> items = Collections.singletonList(createSampleOrderItemEntity());

        // When
        OrderEntity orderEntity = new OrderEntity(id, numero, customerId, storeId, status, totalCentavos, createdAt, updatedAt, items);

        // Then
        assertEquals(id, orderEntity.getId());
        assertEquals(numero, orderEntity.getNumero());
        assertEquals(customerId, orderEntity.getCustomerId());
        assertEquals(storeId, orderEntity.getStoreId());
        assertEquals(status, orderEntity.getStatus());
        assertEquals(totalCentavos, orderEntity.getTotalCentavos());
        assertEquals(createdAt, orderEntity.getCreatedAt());
        assertEquals(updatedAt, orderEntity.getUpdatedAt());
        assertEquals(items, orderEntity.getItems());
    }

    @Test
    @DisplayName("Should create OrderEntity with no-args constructor and all fields should be null")
    void testOrderEntityNoArgsConstructor() {
        // When
        OrderEntity orderEntity = new OrderEntity();

        // Then
        assertNull(orderEntity.getId());
        assertNull(orderEntity.getNumero());
        assertNull(orderEntity.getCustomerId());
        assertNull(orderEntity.getStoreId());
        assertNull(orderEntity.getStatus());
        assertNull(orderEntity.getTotalCentavos());
        assertNull(orderEntity.getCreatedAt());
        assertNull(orderEntity.getUpdatedAt());
        assertNull(orderEntity.getItems());
    }

    @Test
    @DisplayName("Should set all OrderEntity fields using setters and verify with getters")
    void testOrderEntitySetters() {
        // Given
        OrderEntity orderEntity = new OrderEntity();
        Long id = 2L;
        String numero = "ORDER-456";
        String customerId = "customer789";
        Long storeId = 200L;
        OrderStatus status = OrderStatus.COMPLETED;
        Long totalCentavos = 7500L;
        LocalDateTime createdAt = LocalDateTime.of(2023, 12, 25, 10, 30);
        LocalDateTime updatedAt = LocalDateTime.of(2023, 12, 25, 11, 0);
        List<OrderItemEntity> items = Collections.singletonList(createSampleOrderItemEntity());

        // When
        orderEntity.setId(id);
        orderEntity.setNumero(numero);
        orderEntity.setCustomerId(customerId);
        orderEntity.setStoreId(storeId);
        orderEntity.setStatus(status);
        orderEntity.setTotalCentavos(totalCentavos);
        orderEntity.setCreatedAt(createdAt);
        orderEntity.setUpdatedAt(updatedAt);
        orderEntity.setItems(items);

        // Then
        assertEquals(id, orderEntity.getId());
        assertEquals(numero, orderEntity.getNumero());
        assertEquals(customerId, orderEntity.getCustomerId());
        assertEquals(storeId, orderEntity.getStoreId());
        assertEquals(status, orderEntity.getStatus());
        assertEquals(totalCentavos, orderEntity.getTotalCentavos());
        assertEquals(createdAt, orderEntity.getCreatedAt());
        assertEquals(updatedAt, orderEntity.getUpdatedAt());
        assertEquals(items, orderEntity.getItems());
    }

    @Test
    @DisplayName("Should build OrderEntity using builder pattern with all fields")
    void testOrderEntityBuilder() {
        // Given
        Long id = 3L;
        String numero = "ORDER-789";
        String customerId = "customer999";
        Long storeId = 300L;
        OrderStatus status = OrderStatus.COMPLETED;
        Long totalCentavos = 10000L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        List<OrderItemEntity> items = Collections.singletonList(createSampleOrderItemEntity());

        // When
        OrderEntity orderEntity = OrderEntity.builder()
                .id(id)
                .numero(numero)
                .customerId(customerId)
                .storeId(storeId)
                .status(status)
                .totalCentavos(totalCentavos)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .items(items)
                .build();

        // Then
        assertEquals(id, orderEntity.getId());
        assertEquals(numero, orderEntity.getNumero());
        assertEquals(customerId, orderEntity.getCustomerId());
        assertEquals(storeId, orderEntity.getStoreId());
        assertEquals(status, orderEntity.getStatus());
        assertEquals(totalCentavos, orderEntity.getTotalCentavos());
        assertEquals(createdAt, orderEntity.getCreatedAt());
        assertEquals(updatedAt, orderEntity.getUpdatedAt());
        assertEquals(items, orderEntity.getItems());
    }

    @Test
    @DisplayName("Should verify equals and hashCode contract for OrderEntity")
    void testOrderEntityEqualsAndHashCode() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        List<OrderItemEntity> items = Collections.singletonList(createSampleOrderItemEntity());

        OrderEntity entity1 = new OrderEntity(1L, "ORDER-123", "customer456", 100L, OrderStatus.COMPLETED, 5000L, now, now, items);
        OrderEntity entity2 = new OrderEntity(1L, "ORDER-123", "customer456", 100L, OrderStatus.COMPLETED, 5000L, now, now, items);
        OrderEntity entity3 = new OrderEntity(2L, "ORDER-456", "customer789", 200L, OrderStatus.COMPLETED, 7500L, now, now, items);

        // Then
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1, entity3);
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }

    @Test
    @DisplayName("Should generate proper toString representation for OrderEntity")
    void testOrderEntityToString() {
        // Given
        LocalDateTime createdAt = LocalDateTime.of(2023, 12, 25, 15, 45);
        LocalDateTime updatedAt = LocalDateTime.of(2023, 12, 25, 16, 0);
        List<OrderItemEntity> items = Collections.singletonList(createSampleOrderItemEntity());
        OrderEntity orderEntity = new OrderEntity(1L, "ORDER-123", "customer456", 100L, OrderStatus.COMPLETED, 5000L, createdAt, updatedAt, items);

        // When
        String toString = orderEntity.toString();

        // Then
        assertTrue(toString.contains("OrderEntity"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("numero=ORDER-123"));
        assertTrue(toString.contains("customerId=customer456"));
        assertTrue(toString.contains("storeId=100"));
        assertTrue(toString.contains("status=COMPLETED"));
        assertTrue(toString.contains("totalCentavos=5000"));
    }

    @Test
    @DisplayName("Should handle OrderEntity creation with null values gracefully")
    void testOrderEntityWithNullValues() {
        // When
        OrderEntity orderEntity = new OrderEntity(null, null, null, null, null, null, null, null, null);

        // Then
        assertNull(orderEntity.getId());
        assertNull(orderEntity.getNumero());
        assertNull(orderEntity.getCustomerId());
        assertNull(orderEntity.getStoreId());
        assertNull(orderEntity.getStatus());
        assertNull(orderEntity.getTotalCentavos());
        assertNull(orderEntity.getCreatedAt());
        assertNull(orderEntity.getUpdatedAt());
        assertNull(orderEntity.getItems());
    }

    @Test
    @DisplayName("Should handle OrderEntity with empty items list correctly")
    void testOrderEntityWithEmptyItemsList() {
        // Given
        List<OrderItemEntity> emptyItems = Collections.emptyList();

        // When
        OrderEntity orderEntity = OrderEntity.builder()
                .id(1L)
                .numero("ORDER-EMPTY")
                .customerId("customer123")
                .storeId(100L)
                .status(OrderStatus.COMPLETED)
                .totalCentavos(0L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(emptyItems)
                .build();

        // Then
        assertEquals(1L, orderEntity.getId());
        assertEquals("ORDER-EMPTY", orderEntity.getNumero());
        assertEquals("customer123", orderEntity.getCustomerId());
        assertEquals(100L, orderEntity.getStoreId());
        assertEquals(OrderStatus.COMPLETED, orderEntity.getStatus());
        assertEquals(0L, orderEntity.getTotalCentavos());
        assertEquals(emptyItems, orderEntity.getItems());
        assertTrue(orderEntity.getItems().isEmpty());
    }

    @Test
    @DisplayName("Should build OrderEntity with partial fields using builder pattern")
    void testOrderEntityBuilderPartial() {
        // When
        OrderEntity orderEntity = OrderEntity.builder()
                .numero("PARTIAL-ORDER")
                .status(OrderStatus.COMPLETED)
                .totalCentavos(1000L)
                .build();

        // Then
        assertNull(orderEntity.getId());
        assertEquals("PARTIAL-ORDER", orderEntity.getNumero());
        assertNull(orderEntity.getCustomerId());
        assertNull(orderEntity.getStoreId());
        assertEquals(OrderStatus.COMPLETED, orderEntity.getStatus());
        assertEquals(1000L, orderEntity.getTotalCentavos());
        assertNull(orderEntity.getCreatedAt());
        assertNull(orderEntity.getUpdatedAt());
        assertNull(orderEntity.getItems());
    }

    private OrderItemEntity createSampleOrderItemEntity() {
        return OrderItemEntity.builder()
                .id(1L)
                .sku("SKU123")
                .quantity(2L)
                .priceCentavos(2500L)
                .build();
    }
}