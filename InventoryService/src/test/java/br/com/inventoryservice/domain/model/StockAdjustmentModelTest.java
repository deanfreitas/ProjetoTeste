package br.com.inventoryservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StockAdjustmentModel")
class StockAdjustmentModelTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create StockAdjustmentModel with valid parameters")
        void shouldCreateStockAdjustmentModelWithValidParameters() {
            // Given
            String eventId = "event123";
            String loja = "L001";
            String sku = "SKU001";
            Integer delta = 10;
            String motivo = "inventario";
            OffsetDateTime timestamp = OffsetDateTime.now();

            // When
            StockAdjustmentModel model = new StockAdjustmentModel(eventId, loja, sku, delta, motivo, timestamp);

            // Then
            assertNotNull(model);
            assertEquals(eventId, model.eventId());
            assertEquals(loja, model.loja());
            assertEquals(sku, model.sku());
            assertEquals(delta, model.delta());
            assertEquals(motivo, model.motivo());
            assertEquals(timestamp, model.timestamp());
        }
    }

    @Nested
    @DisplayName("Delta Value Scenarios")
    class DeltaValueScenarios {

        @Test
        @DisplayName("Should create StockAdjustmentModel with negative delta")
        void shouldCreateStockAdjustmentModelWithNegativeDelta() {
            // Given
            String eventId = "event123";
            String loja = "L001";
            String sku = "SKU001";
            Integer delta = -5;
            String motivo = "venda";
            OffsetDateTime timestamp = OffsetDateTime.now();

            // When
            StockAdjustmentModel model = new StockAdjustmentModel(eventId, loja, sku, delta, motivo, timestamp);

            // Then
            assertNotNull(model);
            assertEquals(delta, model.delta());
        }

        @Test
        @DisplayName("Should create StockAdjustmentModel with zero delta")
        void shouldCreateStockAdjustmentModelWithZeroDelta() {
            // Given
            String eventId = "event123";
            String loja = "L001";
            String sku = "SKU001";
            Integer delta = 0;
            String motivo = "ajuste";
            OffsetDateTime timestamp = OffsetDateTime.now();

            // When
            StockAdjustmentModel model = new StockAdjustmentModel(eventId, loja, sku, delta, motivo, timestamp);

            // Then
            assertNotNull(model);
            assertEquals(delta, model.delta());
        }
    }

    @Nested
    @DisplayName("Null Parameter Handling")
    class NullParameterHandling {

        @Test
        @DisplayName("Should create StockAdjustmentModel with null parameters")
        void shouldCreateStockAdjustmentModelWithNullParameters() {
            // When
            StockAdjustmentModel model = new StockAdjustmentModel(null, null, null, null, null, null);

            // Then
            assertNotNull(model);
            assertNull(model.eventId());
            assertNull(model.loja());
            assertNull(model.sku());
            assertNull(model.delta());
            assertNull(model.motivo());
            assertNull(model.timestamp());
        }

        @Test
        @DisplayName("Should create StockAdjustmentModel with mixed null and valid parameters")
        void shouldCreateStockAdjustmentModelWithMixedNullAndValidParameters() {
            // Given
            String sku = "SKU001";
            Integer delta = 10;
            OffsetDateTime timestamp = OffsetDateTime.now();

            // When
            StockAdjustmentModel model = new StockAdjustmentModel(null, null, sku, delta, null, timestamp);

            // Then
            assertNotNull(model);
            assertNull(model.eventId());
            assertNull(model.loja());
            assertEquals(sku, model.sku());
            assertEquals(delta, model.delta());
            assertNull(model.motivo());
            assertEquals(timestamp, model.timestamp());
        }
    }

    @Nested
    @DisplayName("Object Behavior")
    class ObjectBehavior {

        @Test
        @DisplayName("Should verify equals and hashCode")
        void shouldVerifyEqualsAndHashCode() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentModel model1 = new StockAdjustmentModel("event123", "L001", "SKU001", 10, "inventario", timestamp);
            StockAdjustmentModel model2 = new StockAdjustmentModel("event123", "L001", "SKU001", 10, "inventario", timestamp);
            StockAdjustmentModel model3 = new StockAdjustmentModel("event456", "L002", "SKU002", 5, "venda", timestamp);

            // Then
            assertEquals(model1, model2);
            assertEquals(model1.hashCode(), model2.hashCode());
            assertNotEquals(model1, model3);
            assertNotEquals(model1.hashCode(), model3.hashCode());
        }

        @Test
        @DisplayName("Should verify toString")
        void shouldVerifyToString() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentModel model = new StockAdjustmentModel("event123", "L001", "SKU001", 10, "inventario", timestamp);

            // When
            String toString = model.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("event123"));
            assertTrue(toString.contains("L001"));
            assertTrue(toString.contains("SKU001"));
            assertTrue(toString.contains("10"));
            assertTrue(toString.contains("inventario"));
        }
    }
}