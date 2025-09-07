package br.com.inventoryservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductModel")
class ProductModelTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create ProductModel with valid parameters")
        void shouldCreateProductModelWithValidParameters() {
            // Given
            String eventId = "event123";
            String sku = "SKU001";

            // When
            ProductModel product = new ProductModel(eventId, sku);

            // Then
            assertNotNull(product);
            assertEquals(eventId, product.eventId());
            assertEquals(sku, product.sku());
        }
    }

    @Nested
    @DisplayName("Null Parameter Handling")
    class NullParameterHandling {

        @Test
        @DisplayName("Should create ProductModel with null eventId")
        void shouldCreateProductModelWithNullEventId() {
            // Given
            String sku = "SKU001";

            // When
            ProductModel product = new ProductModel(null, sku);

            // Then
            assertNotNull(product);
            assertNull(product.eventId());
            assertEquals(sku, product.sku());
        }

        @Test
        @DisplayName("Should create ProductModel with null sku")
        void shouldCreateProductModelWithNullSku() {
            // Given
            String eventId = "event123";

            // When
            ProductModel product = new ProductModel(eventId, null);

            // Then
            assertNotNull(product);
            assertEquals(eventId, product.eventId());
            assertNull(product.sku());
        }

        @Test
        @DisplayName("Should create ProductModel with both null parameters")
        void shouldCreateProductModelWithBothNullParameters() {
            // When
            ProductModel product = new ProductModel(null, null);

            // Then
            assertNotNull(product);
            assertNull(product.eventId());
            assertNull(product.sku());
        }
    }

    @Nested
    @DisplayName("Object Behavior")
    class ObjectBehavior {

        @Test
        @DisplayName("Should verify equals and hashCode")
        void shouldVerifyEqualsAndHashCode() {
            // Given
            ProductModel product1 = new ProductModel("event123", "SKU001");
            ProductModel product2 = new ProductModel("event123", "SKU001");
            ProductModel product3 = new ProductModel("event456", "SKU002");

            // Then
            assertEquals(product1, product2);
            assertEquals(product1.hashCode(), product2.hashCode());
            assertNotEquals(product1, product3);
            assertNotEquals(product1.hashCode(), product3.hashCode());
        }

        @Test
        @DisplayName("Should verify toString")
        void shouldVerifyToString() {
            // Given
            ProductModel product = new ProductModel("event123", "SKU001");

            // When
            String toString = product.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("event123"));
            assertTrue(toString.contains("SKU001"));
        }
    }
}