package br.com.inventoryservice.adapters.in.messaging.dto.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StockAdjustmentEvent Tests")
class StockAdjustmentEventTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create StockAdjustmentEvent with no-args constructor")
        void shouldCreateStockAdjustmentEventWithNoArgsConstructor() {
            // Given & When
            StockAdjustmentEvent stockEvent = new StockAdjustmentEvent();

            // Then
            assertNotNull(stockEvent);
            assertNull(stockEvent.getEventId());
            assertNull(stockEvent.getTipo());
            assertNull(stockEvent.getTimestamp());
            assertNull(stockEvent.getLoja());
            assertNull(stockEvent.getSku());
            assertNull(stockEvent.getDelta());
            assertNull(stockEvent.getMotivo());
        }

        @Test
        @DisplayName("Should create StockAdjustmentEvent with all-args constructor")
        void shouldCreateStockAdjustmentEventWithAllArgsConstructor() {
            // Given
            String eventId = "stock-event-123";
            String tipo = "ajuste_aplicado";
            OffsetDateTime timestamp = OffsetDateTime.now();
            String loja = "SP-01";
            String sku = "SKU-001";
            Integer delta = 10;
            String motivo = "Inventory correction";

            // When
            StockAdjustmentEvent stockEvent = new StockAdjustmentEvent(eventId, tipo, timestamp, loja, sku, delta, motivo);

            // Then
            assertNotNull(stockEvent);
            assertEquals(eventId, stockEvent.getEventId());
            assertEquals(tipo, stockEvent.getTipo());
            assertEquals(timestamp, stockEvent.getTimestamp());
            assertEquals(loja, stockEvent.getLoja());
            assertEquals(sku, stockEvent.getSku());
            assertEquals(delta, stockEvent.getDelta());
            assertEquals(motivo, stockEvent.getMotivo());
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should create StockAdjustmentEvent using builder pattern")
        void shouldCreateStockAdjustmentEventUsingBuilder() {
            // Given
            String eventId = "stock-event-456";
            String tipo = "ajuste_aplicado";
            OffsetDateTime timestamp = OffsetDateTime.now();
            String loja = "RJ-02";
            String sku = "SKU-002";
            Integer delta = -5;
            String motivo = "Product damaged";

            // When
            StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                    .eventId(eventId)
                    .tipo(tipo)
                    .timestamp(timestamp)
                    .loja(loja)
                    .sku(sku)
                    .delta(delta)
                    .motivo(motivo)
                    .build();

            // Then
            assertNotNull(stockEvent);
            assertEquals(eventId, stockEvent.getEventId());
            assertEquals(tipo, stockEvent.getTipo());
            assertEquals(timestamp, stockEvent.getTimestamp());
            assertEquals(loja, stockEvent.getLoja());
            assertEquals(sku, stockEvent.getSku());
            assertEquals(delta, stockEvent.getDelta());
            assertEquals(motivo, stockEvent.getMotivo());
        }

        @Test
        @DisplayName("Should create StockAdjustmentEvent with partial builder data")
        void shouldCreateStockAdjustmentEventWithPartialBuilderData() {
            // Given
            String eventId = "stock-event-partial";
            String sku = "SKU-003";
            Integer delta = 15;

            // When
            StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                    .eventId(eventId)
                    .sku(sku)
                    .delta(delta)
                    .build();

            // Then
            assertNotNull(stockEvent);
            assertEquals(eventId, stockEvent.getEventId());
            assertEquals(sku, stockEvent.getSku());
            assertEquals(delta, stockEvent.getDelta());
            assertNull(stockEvent.getTipo());
            assertNull(stockEvent.getTimestamp());
            assertNull(stockEvent.getLoja());
            assertNull(stockEvent.getMotivo());
        }

        @Test
        @DisplayName("Should test builder toString method")
        void shouldTestBuilderToStringMethod() {
            // Given & When
            String builderToString = StockAdjustmentEvent.builder()
                    .eventId("test-event")
                    .tipo("ajuste_teste")
                    .sku("SKU-001")
                    .delta(10)
                    .toString();

            // Then
            assertNotNull(builderToString);
            assertTrue(builderToString.contains("StockAdjustmentEvent.StockAdjustmentEventBuilder"));
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get eventId correctly")
        void shouldSetAndGetEventIdCorrectly() {
            // Given
            StockAdjustmentEvent stockEvent = new StockAdjustmentEvent();
            String eventId = "test-stock-event-id";

            // When
            stockEvent.setEventId(eventId);

            // Then
            assertEquals(eventId, stockEvent.getEventId());
        }

        @Test
        @DisplayName("Should set and get tipo correctly")
        void shouldSetAndGetTipoCorrectly() {
            // Given
            StockAdjustmentEvent stockEvent = new StockAdjustmentEvent();
            String tipo = "ajuste_aplicado";

            // When
            stockEvent.setTipo(tipo);

            // Then
            assertEquals(tipo, stockEvent.getTipo());
        }

        @Test
        @DisplayName("Should set and get timestamp correctly")
        void shouldSetAndGetTimestampCorrectly() {
            // Given
            StockAdjustmentEvent stockEvent = new StockAdjustmentEvent();
            OffsetDateTime timestamp = OffsetDateTime.now();

            // When
            stockEvent.setTimestamp(timestamp);

            // Then
            assertEquals(timestamp, stockEvent.getTimestamp());
        }

        @Test
        @DisplayName("Should set and get loja correctly")
        void shouldSetAndGetLojaCorrectly() {
            // Given
            StockAdjustmentEvent stockEvent = new StockAdjustmentEvent();
            String loja = "MG-03";

            // When
            stockEvent.setLoja(loja);

            // Then
            assertEquals(loja, stockEvent.getLoja());
        }

        @Test
        @DisplayName("Should set and get sku correctly")
        void shouldSetAndGetSkuCorrectly() {
            // Given
            StockAdjustmentEvent stockEvent = new StockAdjustmentEvent();
            String sku = "SKU-TEST-001";

            // When
            stockEvent.setSku(sku);

            // Then
            assertEquals(sku, stockEvent.getSku());
        }

        @Test
        @DisplayName("Should set and get delta correctly")
        void shouldSetAndGetDeltaCorrectly() {
            // Given
            StockAdjustmentEvent stockEvent = new StockAdjustmentEvent();
            Integer delta = 25;

            // When
            stockEvent.setDelta(delta);

            // Then
            assertEquals(delta, stockEvent.getDelta());
        }

        @Test
        @DisplayName("Should set and get motivo correctly")
        void shouldSetAndGetMotivoCorrectly() {
            // Given
            StockAdjustmentEvent stockEvent = new StockAdjustmentEvent();
            String motivo = "Manual inventory adjustment";

            // When
            stockEvent.setMotivo(motivo);

            // Then
            assertEquals(motivo, stockEvent.getMotivo());
        }
    }

    @Nested
    @DisplayName("Equality and HashCode Tests")
    class EqualityHashCodeTests {

        @Test
        @DisplayName("Should be equal when all fields are the same")
        void shouldBeEqualWhenAllFieldsAreTheSame() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();

            StockAdjustmentEvent stockEvent1 = StockAdjustmentEvent.builder()
                    .eventId("stock-123")
                    .tipo("ajuste_aplicado")
                    .timestamp(timestamp)
                    .loja("SP-01")
                    .sku("SKU-001")
                    .delta(10)
                    .motivo("Test adjustment")
                    .build();

            StockAdjustmentEvent stockEvent2 = StockAdjustmentEvent.builder()
                    .eventId("stock-123")
                    .tipo("ajuste_aplicado")
                    .timestamp(timestamp)
                    .loja("SP-01")
                    .sku("SKU-001")
                    .delta(10)
                    .motivo("Test adjustment")
                    .build();

            // When & Then
            assertEquals(stockEvent1, stockEvent2);
            assertEquals(stockEvent1.hashCode(), stockEvent2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when sku is different")
        void shouldNotBeEqualWhenSkuIsDifferent() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();

            StockAdjustmentEvent stockEvent1 = StockAdjustmentEvent.builder()
                    .eventId("stock-123")
                    .tipo("ajuste_aplicado")
                    .timestamp(timestamp)
                    .loja("SP-01")
                    .sku("SKU-001")
                    .delta(10)
                    .motivo("Test adjustment")
                    .build();

            StockAdjustmentEvent stockEvent2 = StockAdjustmentEvent.builder()
                    .eventId("stock-123")
                    .tipo("ajuste_aplicado")
                    .timestamp(timestamp)
                    .loja("SP-01")
                    .sku("SKU-002")
                    .delta(10)
                    .motivo("Test adjustment")
                    .build();

            // When & Then
            assertNotEquals(stockEvent1, stockEvent2);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should generate toString with all fields")
        void shouldGenerateToStringWithAllFields() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();

            StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                    .eventId("stock-123")
                    .tipo("ajuste_aplicado")
                    .timestamp(timestamp)
                    .loja("SP-01")
                    .sku("SKU-001")
                    .delta(10)
                    .motivo("Test adjustment")
                    .build();

            // When
            String toString = stockEvent.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("eventId=stock-123"));
            assertTrue(toString.contains("tipo=ajuste_aplicado"));
            assertTrue(toString.contains("loja=SP-01"));
            assertTrue(toString.contains("sku=SKU-001"));
            assertTrue(toString.contains("delta=10"));
            assertTrue(toString.contains("motivo=Test adjustment"));
        }
    }

    @Nested
    @DisplayName("Delta Validation Tests")
    class DeltaValidationTests {

        @Test
        @DisplayName("Should handle positive delta values")
        void shouldHandlePositiveDeltaValues() {
            // Given
            Integer positiveDelta = 50;

            // When
            StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                    .eventId("positive-delta-event")
                    .sku("SKU-001")
                    .delta(positiveDelta)
                    .build();

            // Then
            assertEquals(positiveDelta, stockEvent.getDelta());
            assertTrue(stockEvent.getDelta() > 0);
        }

        @Test
        @DisplayName("Should handle negative delta values")
        void shouldHandleNegativeDeltaValues() {
            // Given
            Integer negativeDelta = -20;

            // When
            StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                    .eventId("negative-delta-event")
                    .sku("SKU-002")
                    .delta(negativeDelta)
                    .build();

            // Then
            assertEquals(negativeDelta, stockEvent.getDelta());
            assertTrue(stockEvent.getDelta() < 0);
        }

        @Test
        @DisplayName("Should handle zero delta values")
        void shouldHandleZeroDeltaValues() {
            // Given
            Integer zeroDelta = 0;

            // When
            StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                    .eventId("zero-delta-event")
                    .sku("SKU-003")
                    .delta(zeroDelta)
                    .build();

            // Then
            assertEquals(zeroDelta, stockEvent.getDelta());
            assertEquals(0, stockEvent.getDelta().intValue());
        }

        @Test
        @DisplayName("Should handle null delta values")
        void shouldHandleNullDeltaValues() {
            // Given & When
            StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                    .eventId("null-delta-event")
                    .sku("SKU-004")
                    .delta(null)
                    .build();

            // Then
            assertNull(stockEvent.getDelta());
        }
    }

    @Nested
    @DisplayName("Store Code Validation Tests")
    class StoreCodeValidationTests {

        @Test
        @DisplayName("Should handle different store code formats")
        void shouldHandleDifferentStoreCodeFormats() {
            // Given
            String[] storeCodes = {"SP-01", "RJ-02", "MG-03", "PR-04", "RS-05"};

            for (String storeCode : storeCodes) {
                // When
                StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                        .eventId("event-" + storeCode)
                        .loja(storeCode)
                        .sku("SKU-001")
                        .delta(10)
                        .build();

                // Then
                assertEquals(storeCode, stockEvent.getLoja());
            }
        }
    }

    @Nested
    @DisplayName("SKU Validation Tests")
    class SkuValidationTests {

        @Test
        @DisplayName("Should handle different SKU formats")
        void shouldHandleDifferentSkuFormats() {
            // Given
            String[] skus = {"SKU-001", "PROD-ABC-123", "ITEM_XYZ_456", "12345", "TEST-SKU"};

            for (String sku : skus) {
                // When
                StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                        .eventId("event-" + sku)
                        .sku(sku)
                        .delta(5)
                        .build();

                // Then
                assertEquals(sku, stockEvent.getSku());
            }
        }

        @Test
        @DisplayName("Should handle empty SKU")
        void shouldHandleEmptySku() {
            // Given
            String emptySku = "";

            // When
            StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                    .eventId("empty-sku-event")
                    .sku(emptySku)
                    .delta(1)
                    .build();

            // Then
            assertEquals(emptySku, stockEvent.getSku());
            assertTrue(stockEvent.getSku().isEmpty());
        }
    }

    @Nested
    @DisplayName("Reason Validation Tests")
    class ReasonValidationTests {

        @Test
        @DisplayName("Should handle different reason types")
        void shouldHandleDifferentReasonTypes() {
            // Given
            String[] reasons = {
                    "Inventory correction",
                    "Product damaged",
                    "Expired items",
                    "Manual adjustment",
                    "System correction",
                    "Theft/Loss",
                    "Return processing"
            };

            for (String reason : reasons) {
                // When
                StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                        .eventId("reason-event-" + reason.hashCode())
                        .sku("SKU-001")
                        .delta(1)
                        .motivo(reason)
                        .build();

                // Then
                assertEquals(reason, stockEvent.getMotivo());
            }
        }

        @Test
        @DisplayName("Should handle null reason")
        void shouldHandleNullReason() {
            // Given & When
            StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                    .eventId("null-reason-event")
                    .sku("SKU-001")
                    .delta(1)
                    .motivo(null)
                    .build();

            // Then
            assertNull(stockEvent.getMotivo());
        }

        @Test
        @DisplayName("Should handle empty reason")
        void shouldHandleEmptyReason() {
            // Given
            String emptyReason = "";

            // When
            StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                    .eventId("empty-reason-event")
                    .sku("SKU-001")
                    .delta(1)
                    .motivo(emptyReason)
                    .build();

            // Then
            assertEquals(emptyReason, stockEvent.getMotivo());
            assertTrue(stockEvent.getMotivo().isEmpty());
        }
    }

    @Nested
    @DisplayName("Timestamp Validation Tests")
    class TimestampValidationTests {

        @Test
        @DisplayName("Should handle null timestamp")
        void shouldHandleNullTimestamp() {
            // Given & When
            StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                    .eventId("null-timestamp-event")
                    .sku("SKU-001")
                    .delta(1)
                    .timestamp(null)
                    .build();

            // Then
            assertNull(stockEvent.getTimestamp());
        }

        @Test
        @DisplayName("Should handle future timestamp")
        void shouldHandleFutureTimestamp() {
            // Given
            OffsetDateTime futureTimestamp = OffsetDateTime.now().plusDays(1);

            // When
            StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                    .eventId("future-timestamp-event")
                    .sku("SKU-001")
                    .delta(1)
                    .timestamp(futureTimestamp)
                    .build();

            // Then
            assertEquals(futureTimestamp, stockEvent.getTimestamp());
            assertTrue(stockEvent.getTimestamp().isAfter(OffsetDateTime.now()));
        }

        @Test
        @DisplayName("Should handle past timestamp")
        void shouldHandlePastTimestamp() {
            // Given
            OffsetDateTime pastTimestamp = OffsetDateTime.now().minusDays(1);

            // When
            StockAdjustmentEvent stockEvent = StockAdjustmentEvent.builder()
                    .eventId("past-timestamp-event")
                    .sku("SKU-001")
                    .delta(1)
                    .timestamp(pastTimestamp)
                    .build();

            // Then
            assertEquals(pastTimestamp, stockEvent.getTimestamp());
            assertTrue(stockEvent.getTimestamp().isBefore(OffsetDateTime.now()));
        }
    }
}