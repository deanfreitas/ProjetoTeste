package br.com.inventoryservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SalesItem")
class SalesItemTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create SalesItem with valid parameters")
        void shouldCreateSalesItemWithValidParameters() {
            // Given
            String sku = "SKU001";
            Integer quantidade = 5;

            // When
            SalesItem salesItem = new SalesItem(sku, quantidade);

            // Then
            assertNotNull(salesItem);
            assertEquals(sku, salesItem.sku());
            assertEquals(quantidade, salesItem.quantidade());
        }
    }

    @Nested
    @DisplayName("Quantity Value Scenarios")
    class QuantityValueScenarios {

        @Test
        @DisplayName("Should create SalesItem with zero quantidade")
        void shouldCreateSalesItemWithZeroQuantidade() {
            // Given
            String sku = "SKU001";
            Integer quantidade = 0;

            // When
            SalesItem salesItem = new SalesItem(sku, quantidade);

            // Then
            assertNotNull(salesItem);
            assertEquals(sku, salesItem.sku());
            assertEquals(quantidade, salesItem.quantidade());
        }

        @Test
        @DisplayName("Should create SalesItem with negative quantidade")
        void shouldCreateSalesItemWithNegativeQuantidade() {
            // Given
            String sku = "SKU001";
            Integer quantidade = -1;

            // When
            SalesItem salesItem = new SalesItem(sku, quantidade);

            // Then
            assertNotNull(salesItem);
            assertEquals(sku, salesItem.sku());
            assertEquals(quantidade, salesItem.quantidade());
        }
    }

    @Nested
    @DisplayName("Null Parameter Handling")
    class NullParameterHandling {

        @Test
        @DisplayName("Should create SalesItem with null sku")
        void shouldCreateSalesItemWithNullSku() {
            // Given
            Integer quantidade = 5;

            // When
            SalesItem salesItem = new SalesItem(null, quantidade);

            // Then
            assertNotNull(salesItem);
            assertNull(salesItem.sku());
            assertEquals(quantidade, salesItem.quantidade());
        }

        @Test
        @DisplayName("Should create SalesItem with null quantidade")
        void shouldCreateSalesItemWithNullQuantidade() {
            // Given
            String sku = "SKU001";

            // When
            SalesItem salesItem = new SalesItem(sku, null);

            // Then
            assertNotNull(salesItem);
            assertEquals(sku, salesItem.sku());
            assertNull(salesItem.quantidade());
        }
    }

    @Nested
    @DisplayName("Object Behavior")
    class ObjectBehavior {

        @Test
        @DisplayName("Should verify equals and hashCode")
        void shouldVerifyEqualsAndHashCode() {
            // Given
            SalesItem item1 = new SalesItem("SKU001", 5);
            SalesItem item2 = new SalesItem("SKU001", 5);
            SalesItem item3 = new SalesItem("SKU002", 3);

            // Then
            assertEquals(item1, item2);
            assertEquals(item1.hashCode(), item2.hashCode());
            assertNotEquals(item1, item3);
            assertNotEquals(item1.hashCode(), item3.hashCode());
        }

        @Test
        @DisplayName("Should verify toString")
        void shouldVerifyToString() {
            // Given
            SalesItem salesItem = new SalesItem("SKU001", 5);

            // When
            String toString = salesItem.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("SKU001"));
            assertTrue(toString.contains("5"));
        }
    }
}