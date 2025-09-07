package br.com.orderservice.infrastructure.adapters.in.messaging.mapper;

import br.com.orderservice.domain.enuns.OrderStatus;
import br.com.orderservice.domain.model.Order;
import br.com.orderservice.domain.model.OrderItem;
import br.com.orderservice.infrastructure.adapters.in.messaging.dto.SalesEvent;
import br.com.orderservice.infrastructure.adapters.in.messaging.dto.SalesEventItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SalesEventMapper Tests")
class SalesEventMapperTest {

    // Helper methods
    private SalesEvent createValidSalesEvent(LocalDateTime timestamp) {
        List<SalesEventItem> items = Arrays.asList(
                new SalesEventItem("SKU-001", 2L, 5000L),
                new SalesEventItem("SKU-002", 1L, 10000L)
        );

        return new SalesEvent(
                "EVENT-001",
                "ORDER-001",
                "CUSTOMER-123",
                "STORE-001",
                15000L,
                timestamp,
                items
        );
    }

    @Nested
    @DisplayName("toDomain() method tests")
    class ToDomainTests {

        @Test
        @DisplayName("Should convert valid SalesEvent to Order domain object")
        void shouldConvertValidSalesEventToOrder() {
            // Given
            LocalDateTime timestamp = LocalDateTime.of(2025, 9, 7, 15, 30);
            SalesEvent salesEvent = createValidSalesEvent(timestamp);
            Long storeId = 123L;

            // When
            Order result = SalesEventMapper.toDomain(salesEvent, storeId);

            // Then
            assertNotNull(result);
            assertEquals("ORDER-001", result.getNumero());
            assertEquals("CUSTOMER-123", result.getCustomerId());
            assertEquals(storeId, result.getStoreId());
            assertEquals(OrderStatus.COMPLETED, result.getStatus());
            assertEquals(15000L, result.getTotalCentavos());
            assertEquals(timestamp, result.getCreatedAt());
            assertNotNull(result.getUpdatedAt());
            assertEquals(2, result.getItems().size());

            // Verify first item
            OrderItem firstItem = result.getItems().get(0);
            assertEquals("SKU-001", firstItem.getSku());
            assertEquals(2L, firstItem.getQuantity());
            assertEquals(5000L, firstItem.getPriceCentavos());

            // Verify second item
            OrderItem secondItem = result.getItems().get(1);
            assertEquals("SKU-002", secondItem.getSku());
            assertEquals(1L, secondItem.getQuantity());
            assertEquals(10000L, secondItem.getPriceCentavos());
        }

        @Test
        @DisplayName("Should return null when SalesEvent is null")
        void shouldReturnNullWhenSalesEventIsNull() {
            // Given
            SalesEvent salesEvent = null;
            Long storeId = 123L;

            // When
            Order result = SalesEventMapper.toDomain(salesEvent, storeId);

            // Then
            assertNull(result);
        }

        @Test
        @DisplayName("Should handle SalesEvent with null timestamp by using current time")
        void shouldHandleNullTimestamp() {
            // Given
            SalesEvent salesEvent = createValidSalesEvent(null);
            Long storeId = 123L;
            LocalDateTime beforeTest = LocalDateTime.now();

            // When
            Order result = SalesEventMapper.toDomain(salesEvent, storeId);

            // Then
            assertNotNull(result);
            assertNotNull(result.getCreatedAt());
            assertTrue(result.getCreatedAt().isAfter(beforeTest) || result.getCreatedAt().isEqual(beforeTest));
        }

        @Test
        @DisplayName("Should handle SalesEvent with null items list")
        void shouldHandleNullItemsList() {
            // Given
            SalesEvent salesEvent = new SalesEvent(
                    "EVENT-001",
                    "ORDER-001",
                    "CUSTOMER-123",
                    "STORE-001",
                    15000L,
                    LocalDateTime.now(),
                    null // null items
            );
            Long storeId = 123L;

            // When
            Order result = SalesEventMapper.toDomain(salesEvent, storeId);

            // Then
            assertNotNull(result);
            assertNull(result.getItems());
        }

        @Test
        @DisplayName("Should handle SalesEvent with empty items list")
        void shouldHandleEmptyItemsList() {
            // Given
            SalesEvent salesEvent = new SalesEvent(
                    "EVENT-001",
                    "ORDER-001",
                    "CUSTOMER-123",
                    "STORE-001",
                    15000L,
                    LocalDateTime.now(),
                    Collections.emptyList()
            );
            Long storeId = 123L;

            // When
            Order result = SalesEventMapper.toDomain(salesEvent, storeId);

            // Then
            assertNotNull(result);
            assertNotNull(result.getItems());
            assertTrue(result.getItems().isEmpty());
        }

        @Test
        @DisplayName("Should handle SalesEvent with single item")
        void shouldHandleSingleItem() {
            // Given
            SalesEventItem item = new SalesEventItem("SKU-001", 3L, 2500L);
            SalesEvent salesEvent = new SalesEvent(
                    "EVENT-001",
                    "ORDER-001",
                    "CUSTOMER-123",
                    "STORE-001",
                    7500L,
                    LocalDateTime.now(),
                    Collections.singletonList(item)
            );
            Long storeId = 123L;

            // When
            Order result = SalesEventMapper.toDomain(salesEvent, storeId);

            // Then
            assertNotNull(result);
            assertEquals(1, result.getItems().size());
            OrderItem orderItem = result.getItems().get(0);
            assertEquals("SKU-001", orderItem.getSku());
            assertEquals(3L, orderItem.getQuantity());
            assertEquals(2500L, orderItem.getPriceCentavos());
        }
    }

    @Nested
    @DisplayName("toOrderItem() method tests (tested indirectly)")
    class ToOrderItemTests {

        @Test
        @DisplayName("Should handle null SalesEventItem in items list")
        void shouldHandleNullSalesEventItemInList() {
            // Given
            List<SalesEventItem> itemsWithNull = Arrays.asList(
                    new SalesEventItem("SKU-001", 2L, 5000L),
                    null,
                    new SalesEventItem("SKU-002", 1L, 10000L)
            );

            SalesEvent salesEvent = new SalesEvent(
                    "EVENT-001",
                    "ORDER-001",
                    "CUSTOMER-123",
                    "STORE-001",
                    15000L,
                    LocalDateTime.now(),
                    itemsWithNull
            );
            Long storeId = 123L;

            // When
            Order result = SalesEventMapper.toDomain(salesEvent, storeId);

            // Then
            assertNotNull(result);
            assertEquals(3, result.getItems().size());

            // First item should be valid
            OrderItem firstItem = result.getItems().get(0);
            assertNotNull(firstItem);
            assertEquals("SKU-001", firstItem.getSku());

            // Second item should be null
            OrderItem secondItem = result.getItems().get(1);
            assertNull(secondItem);

            // Third item should be valid
            OrderItem thirdItem = result.getItems().get(2);
            assertNotNull(thirdItem);
            assertEquals("SKU-002", thirdItem.getSku());
        }

        @Test
        @DisplayName("Should properly map all SalesEventItem fields to OrderItem")
        void shouldMapAllSalesEventItemFields() {
            // Given
            SalesEventItem eventItem = new SalesEventItem("TEST-SKU", 5L, 3000L);
            SalesEvent salesEvent = new SalesEvent(
                    "EVENT-001",
                    "ORDER-001",
                    "CUSTOMER-123",
                    "STORE-001",
                    15000L,
                    LocalDateTime.now(),
                    Collections.singletonList(eventItem)
            );
            Long storeId = 123L;

            // When
            Order result = SalesEventMapper.toDomain(salesEvent, storeId);

            // Then
            assertNotNull(result);
            assertEquals(1, result.getItems().size());
            OrderItem orderItem = result.getItems().get(0);

            assertEquals("TEST-SKU", orderItem.getSku());
            assertEquals(5L, orderItem.getQuantity());
            assertEquals(3000L, orderItem.getPriceCentavos());
        }
    }

    @Nested
    @DisplayName("Edge cases and boundary conditions")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle zero total amount")
        void shouldHandleZeroTotalAmount() {
            // Given
            SalesEvent salesEvent = new SalesEvent(
                    "EVENT-001",
                    "ORDER-001",
                    "CUSTOMER-123",
                    "STORE-001",
                    0L,
                    LocalDateTime.now(),
                    Collections.emptyList()
            );
            Long storeId = 123L;

            // When
            Order result = SalesEventMapper.toDomain(salesEvent, storeId);

            // Then
            assertNotNull(result);
            assertEquals(0L, result.getTotalCentavos());
        }

        @Test
        @DisplayName("Should handle null storeId parameter")
        void shouldHandleNullStoreId() {
            // Given
            SalesEvent salesEvent = createValidSalesEvent(LocalDateTime.now());
            Long storeId = null;

            // When
            Order result = SalesEventMapper.toDomain(salesEvent, storeId);

            // Then
            assertNotNull(result);
            assertNull(result.getStoreId());
        }

        @Test
        @DisplayName("Should always set OrderStatus to COMPLETED")
        void shouldAlwaysSetStatusToCompleted() {
            // Given
            SalesEvent salesEvent = createValidSalesEvent(LocalDateTime.now());
            Long storeId = 123L;

            // When
            Order result = SalesEventMapper.toDomain(salesEvent, storeId);

            // Then
            assertNotNull(result);
            assertEquals(OrderStatus.COMPLETED, result.getStatus());
        }

        @Test
        @DisplayName("Should handle items with zero quantity and price")
        void shouldHandleItemsWithZeroValues() {
            // Given
            SalesEventItem zeroItem = new SalesEventItem("SKU-ZERO", 0L, 0L);
            SalesEvent salesEvent = new SalesEvent(
                    "EVENT-001",
                    "ORDER-001",
                    "CUSTOMER-123",
                    "STORE-001",
                    0L,
                    LocalDateTime.now(),
                    Collections.singletonList(zeroItem)
            );
            Long storeId = 123L;

            // When
            Order result = SalesEventMapper.toDomain(salesEvent, storeId);

            // Then
            assertNotNull(result);
            assertEquals(1, result.getItems().size());
            OrderItem orderItem = result.getItems().get(0);
            assertEquals("SKU-ZERO", orderItem.getSku());
            assertEquals(0L, orderItem.getQuantity());
            assertEquals(0L, orderItem.getPriceCentavos());
        }
    }
}