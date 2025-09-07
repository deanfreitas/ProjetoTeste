package br.com.inventoryservice.adapters.in.messaging.dto.event;

import br.com.inventoryservice.adapters.in.messaging.dto.data.SalesItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SalesEvent Tests")
class SalesEventTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create SalesEvent with no-args constructor")
        void shouldCreateSalesEventWithNoArgsConstructor() {
            // Given & When
            SalesEvent salesEvent = new SalesEvent();

            // Then
            assertNotNull(salesEvent);
            assertNull(salesEvent.getEventId());
            assertNull(salesEvent.getTipo());
            assertNull(salesEvent.getTimestamp());
            assertNull(salesEvent.getLoja());
            assertNull(salesEvent.getPedidoId());
            assertNull(salesEvent.getItens());
        }

        @Test
        @DisplayName("Should create SalesEvent with all-args constructor")
        void shouldCreateSalesEventWithAllArgsConstructor() {
            // Given
            String eventId = "sales-event-123";
            String tipo = "venda_registrada";
            OffsetDateTime timestamp = OffsetDateTime.now();
            String loja = "SP-01";
            String pedidoId = "order-456";
            List<SalesItem> itens = Arrays.asList(
                    SalesItem.builder().sku("SKU-001").quantidade(2).build(),
                    SalesItem.builder().sku("SKU-002").quantidade(1).build()
            );

            // When
            SalesEvent salesEvent = new SalesEvent(eventId, tipo, timestamp, loja, pedidoId, itens);

            // Then
            assertNotNull(salesEvent);
            assertEquals(eventId, salesEvent.getEventId());
            assertEquals(tipo, salesEvent.getTipo());
            assertEquals(timestamp, salesEvent.getTimestamp());
            assertEquals(loja, salesEvent.getLoja());
            assertEquals(pedidoId, salesEvent.getPedidoId());
            assertEquals(itens, salesEvent.getItens());
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should create SalesEvent using builder pattern")
        void shouldCreateSalesEventUsingBuilder() {
            // Given
            String eventId = "sales-event-789";
            String tipo = "venda_registrada";
            OffsetDateTime timestamp = OffsetDateTime.now();
            String loja = "RJ-02";
            String pedidoId = "order-789";
            List<SalesItem> itens = Arrays.asList(
                    SalesItem.builder().sku("SKU-003").quantidade(3).build()
            );

            // When
            SalesEvent salesEvent = SalesEvent.builder()
                    .eventId(eventId)
                    .tipo(tipo)
                    .timestamp(timestamp)
                    .loja(loja)
                    .pedidoId(pedidoId)
                    .itens(itens)
                    .build();

            // Then
            assertNotNull(salesEvent);
            assertEquals(eventId, salesEvent.getEventId());
            assertEquals(tipo, salesEvent.getTipo());
            assertEquals(timestamp, salesEvent.getTimestamp());
            assertEquals(loja, salesEvent.getLoja());
            assertEquals(pedidoId, salesEvent.getPedidoId());
            assertEquals(itens, salesEvent.getItens());
        }

        @Test
        @DisplayName("Should create SalesEvent with partial builder data")
        void shouldCreateSalesEventWithPartialBuilderData() {
            // Given
            String eventId = "sales-event-partial";
            String loja = "MG-03";

            // When
            SalesEvent salesEvent = SalesEvent.builder()
                    .eventId(eventId)
                    .loja(loja)
                    .build();

            // Then
            assertNotNull(salesEvent);
            assertEquals(eventId, salesEvent.getEventId());
            assertEquals(loja, salesEvent.getLoja());
            assertNull(salesEvent.getTipo());
            assertNull(salesEvent.getTimestamp());
            assertNull(salesEvent.getPedidoId());
            assertNull(salesEvent.getItens());
        }

        @Test
        @DisplayName("Should test builder toString method")
        void shouldTestBuilderToStringMethod() {
            // Given & When
            String builderToString = SalesEvent.builder()
                    .eventId("test-event")
                    .tipo("venda_teste")
                    .loja("SP-01")
                    .toString();

            // Then
            assertNotNull(builderToString);
            assertTrue(builderToString.contains("SalesEvent.SalesEventBuilder"));
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get eventId correctly")
        void shouldSetAndGetEventIdCorrectly() {
            // Given
            SalesEvent salesEvent = new SalesEvent();
            String eventId = "test-sales-event-id";

            // When
            salesEvent.setEventId(eventId);

            // Then
            assertEquals(eventId, salesEvent.getEventId());
        }

        @Test
        @DisplayName("Should set and get tipo correctly")
        void shouldSetAndGetTipoCorrectly() {
            // Given
            SalesEvent salesEvent = new SalesEvent();
            String tipo = "venda_registrada";

            // When
            salesEvent.setTipo(tipo);

            // Then
            assertEquals(tipo, salesEvent.getTipo());
        }

        @Test
        @DisplayName("Should set and get timestamp correctly")
        void shouldSetAndGetTimestampCorrectly() {
            // Given
            SalesEvent salesEvent = new SalesEvent();
            OffsetDateTime timestamp = OffsetDateTime.now();

            // When
            salesEvent.setTimestamp(timestamp);

            // Then
            assertEquals(timestamp, salesEvent.getTimestamp());
        }

        @Test
        @DisplayName("Should set and get loja correctly")
        void shouldSetAndGetLojaCorrectly() {
            // Given
            SalesEvent salesEvent = new SalesEvent();
            String loja = "PR-04";

            // When
            salesEvent.setLoja(loja);

            // Then
            assertEquals(loja, salesEvent.getLoja());
        }

        @Test
        @DisplayName("Should set and get pedidoId correctly")
        void shouldSetAndGetPedidoIdCorrectly() {
            // Given
            SalesEvent salesEvent = new SalesEvent();
            String pedidoId = "order-test-123";

            // When
            salesEvent.setPedidoId(pedidoId);

            // Then
            assertEquals(pedidoId, salesEvent.getPedidoId());
        }

        @Test
        @DisplayName("Should set and get itens correctly")
        void shouldSetAndGetItensCorrectly() {
            // Given
            SalesEvent salesEvent = new SalesEvent();
            List<SalesItem> itens = Arrays.asList(
                    SalesItem.builder().sku("SKU-004").quantidade(5).build(),
                    SalesItem.builder().sku("SKU-005").quantidade(2).build()
            );

            // When
            salesEvent.setItens(itens);

            // Then
            assertEquals(itens, salesEvent.getItens());
            assertEquals(2, salesEvent.getItens().size());
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
            List<SalesItem> itens = Arrays.asList(
                    SalesItem.builder().sku("SKU-001").quantidade(2).build()
            );

            SalesEvent salesEvent1 = SalesEvent.builder()
                    .eventId("sales-123")
                    .tipo("venda_registrada")
                    .timestamp(timestamp)
                    .loja("SP-01")
                    .pedidoId("order-456")
                    .itens(itens)
                    .build();

            SalesEvent salesEvent2 = SalesEvent.builder()
                    .eventId("sales-123")
                    .tipo("venda_registrada")
                    .timestamp(timestamp)
                    .loja("SP-01")
                    .pedidoId("order-456")
                    .itens(itens)
                    .build();

            // When & Then
            assertEquals(salesEvent1, salesEvent2);
            assertEquals(salesEvent1.hashCode(), salesEvent2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when pedidoId is different")
        void shouldNotBeEqualWhenPedidoIdIsDifferent() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            List<SalesItem> itens = Arrays.asList(
                    SalesItem.builder().sku("SKU-001").quantidade(2).build()
            );

            SalesEvent salesEvent1 = SalesEvent.builder()
                    .eventId("sales-123")
                    .tipo("venda_registrada")
                    .timestamp(timestamp)
                    .loja("SP-01")
                    .pedidoId("order-456")
                    .itens(itens)
                    .build();

            SalesEvent salesEvent2 = SalesEvent.builder()
                    .eventId("sales-123")
                    .tipo("venda_registrada")
                    .timestamp(timestamp)
                    .loja("SP-01")
                    .pedidoId("order-789")
                    .itens(itens)
                    .build();

            // When & Then
            assertNotEquals(salesEvent1, salesEvent2);
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
            List<SalesItem> itens = Arrays.asList(
                    SalesItem.builder().sku("SKU-001").quantidade(2).build()
            );

            SalesEvent salesEvent = SalesEvent.builder()
                    .eventId("sales-123")
                    .tipo("venda_registrada")
                    .timestamp(timestamp)
                    .loja("SP-01")
                    .pedidoId("order-456")
                    .itens(itens)
                    .build();

            // When
            String toString = salesEvent.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("eventId=sales-123"));
            assertTrue(toString.contains("tipo=venda_registrada"));
            assertTrue(toString.contains("loja=SP-01"));
            assertTrue(toString.contains("pedidoId=order-456"));
            assertTrue(toString.contains("itens="));
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
                SalesEvent salesEvent = SalesEvent.builder()
                        .eventId("event-" + storeCode)
                        .loja(storeCode)
                        .build();

                // Then
                assertEquals(storeCode, salesEvent.getLoja());
            }
        }
    }

    @Nested
    @DisplayName("Sales Items Validation Tests")
    class SalesItemsValidationTests {

        @Test
        @DisplayName("Should handle empty items list")
        void shouldHandleEmptyItemsList() {
            // Given
            List<SalesItem> emptyItens = Arrays.asList();

            // When
            SalesEvent salesEvent = SalesEvent.builder()
                    .eventId("empty-items-event")
                    .itens(emptyItens)
                    .build();

            // Then
            assertEquals(emptyItens, salesEvent.getItens());
            assertTrue(salesEvent.getItens().isEmpty());
        }

        @Test
        @DisplayName("Should handle multiple sales items")
        void shouldHandleMultipleSalesItems() {
            // Given
            List<SalesItem> multipleItens = Arrays.asList(
                    SalesItem.builder().sku("SKU-001").quantidade(2).build(),
                    SalesItem.builder().sku("SKU-002").quantidade(1).build(),
                    SalesItem.builder().sku("SKU-003").quantidade(5).build(),
                    SalesItem.builder().sku("SKU-004").quantidade(3).build()
            );

            // When
            SalesEvent salesEvent = SalesEvent.builder()
                    .eventId("multiple-items-event")
                    .itens(multipleItens)
                    .build();

            // Then
            assertEquals(multipleItens, salesEvent.getItens());
            assertEquals(4, salesEvent.getItens().size());
        }
    }

    @Nested
    @DisplayName("Timestamp Validation Tests")
    class TimestampValidationTests {

        @Test
        @DisplayName("Should handle null timestamp")
        void shouldHandleNullTimestamp() {
            // Given & When
            SalesEvent salesEvent = SalesEvent.builder()
                    .eventId("null-timestamp-event")
                    .timestamp(null)
                    .build();

            // Then
            assertNull(salesEvent.getTimestamp());
        }

        @Test
        @DisplayName("Should handle future timestamp")
        void shouldHandleFutureTimestamp() {
            // Given
            OffsetDateTime futureTimestamp = OffsetDateTime.now().plusDays(1);

            // When
            SalesEvent salesEvent = SalesEvent.builder()
                    .eventId("future-timestamp-event")
                    .timestamp(futureTimestamp)
                    .build();

            // Then
            assertEquals(futureTimestamp, salesEvent.getTimestamp());
            assertTrue(salesEvent.getTimestamp().isAfter(OffsetDateTime.now()));
        }

        @Test
        @DisplayName("Should handle past timestamp")
        void shouldHandlePastTimestamp() {
            // Given
            OffsetDateTime pastTimestamp = OffsetDateTime.now().minusDays(1);

            // When
            SalesEvent salesEvent = SalesEvent.builder()
                    .eventId("past-timestamp-event")
                    .timestamp(pastTimestamp)
                    .build();

            // Then
            assertEquals(pastTimestamp, salesEvent.getTimestamp());
            assertTrue(salesEvent.getTimestamp().isBefore(OffsetDateTime.now()));
        }
    }
}