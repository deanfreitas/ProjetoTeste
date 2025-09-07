package br.com.orderservice.infrastructure.adapters.in.messaging.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SalesEventItem")
@ExtendWith(MockitoExtension.class)
class SalesEventItemTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create SalesEventItem with constructor and verify all getters")
        void testSalesEventItemConstructorAndGetters() {
            // Given
            String sku = "SKU123";
            Long quantidade = 5L;
            Long precoCentavos = 2500L;

            // When
            SalesEventItem salesEventItem = new SalesEventItem(sku, quantidade, precoCentavos);

            // Then
            assertEquals(sku, salesEventItem.getSku());
            assertEquals(quantidade, salesEventItem.getQuantidade());
            assertEquals(precoCentavos, salesEventItem.getPrecoCentavos());
        }

        @Test
        @DisplayName("Should create SalesEventItem with no-args constructor and have null values")
        void testSalesEventItemNoArgsConstructor() {
            // When
            SalesEventItem salesEventItem = new SalesEventItem();

            // Then
            assertNull(salesEventItem.getSku());
            assertNull(salesEventItem.getQuantidade());
            assertNull(salesEventItem.getPrecoCentavos());
        }
    }

    @Nested
    @DisplayName("Setter Tests")
    class SetterTests {

        @Test
        @DisplayName("Should set all properties using setters")
        void testSalesEventItemSetters() {
            // Given
            SalesEventItem salesEventItem = new SalesEventItem();
            String sku = "SKU456";
            Long quantidade = 10L;
            Long precoCentavos = 5000L;

            // When
            salesEventItem.setSku(sku);
            salesEventItem.setQuantidade(quantidade);
            salesEventItem.setPrecoCentavos(precoCentavos);

            // Then
            assertEquals(sku, salesEventItem.getSku());
            assertEquals(quantidade, salesEventItem.getQuantidade());
            assertEquals(precoCentavos, salesEventItem.getPrecoCentavos());
        }
    }

    @Nested
    @DisplayName("Utility Method Tests")
    class UtilityMethodTests {

        @Test
        @DisplayName("Should properly compare SalesEventItems using equals and hashCode")
        void testSalesEventItemEqualsAndHashCode() {
            // Given
            SalesEventItem item1 = new SalesEventItem("SKU123", 5L, 2500L);
            SalesEventItem item2 = new SalesEventItem("SKU123", 5L, 2500L);
            SalesEventItem item3 = new SalesEventItem("SKU456", 10L, 5000L);

            // Then
            assertEquals(item1, item2);
            assertEquals(item1.hashCode(), item2.hashCode());
            assertNotEquals(item1, item3);
            assertNotEquals(item1.hashCode(), item3.hashCode());
        }

        @Test
        @DisplayName("Should generate meaningful toString representation")
        void testSalesEventItemToString() {
            // Given
            SalesEventItem salesEventItem = new SalesEventItem("SKU123", 5L, 2500L);

            // When
            String toString = salesEventItem.toString();

            // Then
            assertTrue(toString.contains("SalesEventItem"));
            assertTrue(toString.contains("sku=SKU123"));
            assertTrue(toString.contains("quantidade=5"));
            assertTrue(toString.contains("precoCentavos=2500"));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null values in constructor")
        void testSalesEventItemWithNullValues() {
            // When
            SalesEventItem salesEventItem = new SalesEventItem(null, null, null);

            // Then
            assertNull(salesEventItem.getSku());
            assertNull(salesEventItem.getQuantidade());
            assertNull(salesEventItem.getPrecoCentavos());
        }

        @Test
        @DisplayName("Should handle zero and empty values")
        void testSalesEventItemWithZeroValues() {
            // Given
            String sku = "";
            Long quantidade = 0L;
            Long precoCentavos = 0L;

            // When
            SalesEventItem salesEventItem = new SalesEventItem(sku, quantidade, precoCentavos);

            // Then
            assertEquals("", salesEventItem.getSku());
            assertEquals(0L, salesEventItem.getQuantidade());
            assertEquals(0L, salesEventItem.getPrecoCentavos());
        }

        @Test
        @DisplayName("Should handle negative values")
        void testSalesEventItemWithNegativeValues() {
            // Given
            String sku = "NEGATIVE_SKU";
            Long quantidade = -1L;
            Long precoCentavos = -100L;

            // When
            SalesEventItem salesEventItem = new SalesEventItem(sku, quantidade, precoCentavos);

            // Then
            assertEquals(sku, salesEventItem.getSku());
            assertEquals(-1L, salesEventItem.getQuantidade());
            assertEquals(-100L, salesEventItem.getPrecoCentavos());
        }
    }
}