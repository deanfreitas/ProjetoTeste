package br.com.orderservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderItem")
@ExtendWith(MockitoExtension.class)
class OrderItemTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create OrderItem with constructor and verify all getters")
        void testOrderItemConstructorAndGetters() {
            // Given
            Long id = 1L;
            Long orderId = 100L;
            String sku = "SKU123";
            Long quantity = 5L;
            Long priceCentavos = 2500L;

            // When
            OrderItem orderItem = new OrderItem(id, orderId, sku, quantity, priceCentavos);

            // Then
            assertEquals(id, orderItem.getId());
            assertEquals(orderId, orderItem.getOrderId());
            assertEquals(sku, orderItem.getSku());
            assertEquals(quantity, orderItem.getQuantity());
            assertEquals(priceCentavos, orderItem.getPriceCentavos());
        }

        @Test
        @DisplayName("Should create OrderItem with no-args constructor and have null values")
        void testOrderItemNoArgsConstructor() {
            // When
            OrderItem orderItem = new OrderItem();

            // Then
            assertNull(orderItem.getId());
            assertNull(orderItem.getOrderId());
            assertNull(orderItem.getSku());
            assertNull(orderItem.getQuantity());
            assertNull(orderItem.getPriceCentavos());
        }
    }

    @Nested
    @DisplayName("Setter Tests")
    class SetterTests {

        @Test
        @DisplayName("Should set all properties using setters")
        void testOrderItemSetters() {
            // Given
            OrderItem orderItem = new OrderItem();
            Long id = 2L;
            Long orderId = 200L;
            String sku = "SKU456";
            Long quantity = 10L;
            Long priceCentavos = 5000L;

            // When
            orderItem.setId(id);
            orderItem.setOrderId(orderId);
            orderItem.setSku(sku);
            orderItem.setQuantity(quantity);
            orderItem.setPriceCentavos(priceCentavos);

            // Then
            assertEquals(id, orderItem.getId());
            assertEquals(orderId, orderItem.getOrderId());
            assertEquals(sku, orderItem.getSku());
            assertEquals(quantity, orderItem.getQuantity());
            assertEquals(priceCentavos, orderItem.getPriceCentavos());
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should create OrderItem using builder pattern with all properties")
        void testOrderItemBuilder() {
            // Given
            Long id = 3L;
            Long orderId = 300L;
            String sku = "SKU789";
            Long quantity = 15L;
            Long priceCentavos = 7500L;

            // When
            OrderItem orderItem = OrderItem.builder()
                    .id(id)
                    .orderId(orderId)
                    .sku(sku)
                    .quantity(quantity)
                    .priceCentavos(priceCentavos)
                    .build();

            // Then
            assertEquals(id, orderItem.getId());
            assertEquals(orderId, orderItem.getOrderId());
            assertEquals(sku, orderItem.getSku());
            assertEquals(quantity, orderItem.getQuantity());
            assertEquals(priceCentavos, orderItem.getPriceCentavos());
        }

        @Test
        @DisplayName("Should create OrderItem using builder pattern with partial properties")
        void testOrderItemBuilderPartial() {
            // When
            OrderItem orderItem = OrderItem.builder()
                    .sku("PARTIAL_SKU")
                    .quantity(3L)
                    .build();

            // Then
            assertNull(orderItem.getId());
            assertNull(orderItem.getOrderId());
            assertEquals("PARTIAL_SKU", orderItem.getSku());
            assertEquals(3L, orderItem.getQuantity());
            assertNull(orderItem.getPriceCentavos());
        }
    }

    @Nested
    @DisplayName("Utility Method Tests")
    class UtilityMethodTests {

        @Test
        @DisplayName("Should properly compare OrderItems using equals and hashCode")
        void testOrderItemEqualsAndHashCode() {
            // Given
            OrderItem orderItem1 = new OrderItem(1L, 100L, "SKU123", 5L, 2500L);
            OrderItem orderItem2 = new OrderItem(1L, 100L, "SKU123", 5L, 2500L);
            OrderItem orderItem3 = new OrderItem(2L, 200L, "SKU456", 10L, 5000L);

            // Then
            assertEquals(orderItem1, orderItem2);
            assertEquals(orderItem1.hashCode(), orderItem2.hashCode());
            assertNotEquals(orderItem1, orderItem3);
            assertNotEquals(orderItem1.hashCode(), orderItem3.hashCode());
        }

        @Test
        @DisplayName("Should generate meaningful toString representation")
        void testOrderItemToString() {
            // Given
            OrderItem orderItem = new OrderItem(1L, 100L, "SKU123", 5L, 2500L);

            // When
            String toString = orderItem.toString();

            // Then
            assertTrue(toString.contains("OrderItem"));
            assertTrue(toString.contains("id=1"));
            assertTrue(toString.contains("orderId=100"));
            assertTrue(toString.contains("sku=SKU123"));
            assertTrue(toString.contains("quantity=5"));
            assertTrue(toString.contains("priceCentavos=2500"));
        }
    }
}