package br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event;

import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.StoreData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StoreEvent Tests")
class StoreEventTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create StoreEvent with no-args constructor")
        void shouldCreateStoreEventWithNoArgsConstructor() {
            // Given & When
            StoreEvent storeEvent = new StoreEvent();

            // Then
            assertNotNull(storeEvent);
            assertNull(storeEvent.getEventId());
            assertNull(storeEvent.getTipo());
            assertNull(storeEvent.getDados());
        }

        @Test
        @DisplayName("Should create StoreEvent with all-args constructor")
        void shouldCreateStoreEventWithAllArgsConstructor() {
            // Given
            String eventId = "store-event-123";
            String tipo = "loja_criada";
            StoreData dados = StoreData.builder()
                    .codigo("SP-01")
                    .nome("Store São Paulo 01")
                    .build();

            // When
            StoreEvent storeEvent = new StoreEvent(eventId, tipo, dados);

            // Then
            assertNotNull(storeEvent);
            assertEquals(eventId, storeEvent.getEventId());
            assertEquals(tipo, storeEvent.getTipo());
            assertEquals(dados, storeEvent.getDados());
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should create StoreEvent using builder pattern")
        void shouldCreateStoreEventUsingBuilder() {
            // Given
            String eventId = "store-event-456";
            String tipo = "loja_atualizada";
            StoreData dados = StoreData.builder()
                    .codigo("RJ-02")
                    .nome("Store Rio de Janeiro 02")
                    .build();

            // When
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId(eventId)
                    .tipo(tipo)
                    .dados(dados)
                    .build();

            // Then
            assertNotNull(storeEvent);
            assertEquals(eventId, storeEvent.getEventId());
            assertEquals(tipo, storeEvent.getTipo());
            assertEquals(dados, storeEvent.getDados());
        }

        @Test
        @DisplayName("Should create StoreEvent with partial builder data")
        void shouldCreateStoreEventWithPartialBuilderData() {
            // Given
            String eventId = "store-event-789";

            // When
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId(eventId)
                    .build();

            // Then
            assertNotNull(storeEvent);
            assertEquals(eventId, storeEvent.getEventId());
            assertNull(storeEvent.getTipo());
            assertNull(storeEvent.getDados());
        }

        @Test
        @DisplayName("Should test builder toString method")
        void shouldTestBuilderToStringMethod() {
            // Given & When
            String builderToString = StoreEvent.builder()
                    .eventId("test-event")
                    .tipo("loja_teste")
                    .toString();

            // Then
            assertNotNull(builderToString);
            assertTrue(builderToString.contains("StoreEvent.StoreEventBuilder"));
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get eventId correctly")
        void shouldSetAndGetEventIdCorrectly() {
            // Given
            StoreEvent storeEvent = new StoreEvent();
            String eventId = "test-store-event-id";

            // When
            storeEvent.setEventId(eventId);

            // Then
            assertEquals(eventId, storeEvent.getEventId());
        }

        @Test
        @DisplayName("Should set and get tipo correctly")
        void shouldSetAndGetTipoCorrectly() {
            // Given
            StoreEvent storeEvent = new StoreEvent();
            String tipo = "loja_criada";

            // When
            storeEvent.setTipo(tipo);

            // Then
            assertEquals(tipo, storeEvent.getTipo());
        }

        @Test
        @DisplayName("Should set and get dados correctly")
        void shouldSetAndGetDadosCorrectly() {
            // Given
            StoreEvent storeEvent = new StoreEvent();
            StoreData dados = StoreData.builder()
                    .codigo("MG-03")
                    .nome("Store Minas Gerais 03")
                    .build();

            // When
            storeEvent.setDados(dados);

            // Then
            assertEquals(dados, storeEvent.getDados());
        }
    }

    @Nested
    @DisplayName("Equality and HashCode Tests")
    class EqualityHashCodeTests {

        @Test
        @DisplayName("Should be equal when all fields are the same")
        void shouldBeEqualWhenAllFieldsAreTheSame() {
            // Given
            StoreData dados = StoreData.builder()
                    .codigo("SP-01")
                    .nome("Test Store")
                    .build();

            StoreEvent storeEvent1 = StoreEvent.builder()
                    .eventId("store-123")
                    .tipo("loja_criada")
                    .dados(dados)
                    .build();

            StoreEvent storeEvent2 = StoreEvent.builder()
                    .eventId("store-123")
                    .tipo("loja_criada")
                    .dados(dados)
                    .build();

            // When & Then
            assertEquals(storeEvent1, storeEvent2);
            assertEquals(storeEvent1.hashCode(), storeEvent2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when eventId is different")
        void shouldNotBeEqualWhenEventIdIsDifferent() {
            // Given
            StoreData dados = StoreData.builder()
                    .codigo("SP-01")
                    .nome("Test Store")
                    .build();

            StoreEvent storeEvent1 = StoreEvent.builder()
                    .eventId("store-123")
                    .tipo("loja_criada")
                    .dados(dados)
                    .build();

            StoreEvent storeEvent2 = StoreEvent.builder()
                    .eventId("store-456")
                    .tipo("loja_criada")
                    .dados(dados)
                    .build();

            // When & Then
            assertNotEquals(storeEvent1, storeEvent2);
        }

        @Test
        @DisplayName("Should not be equal when dados is different")
        void shouldNotBeEqualWhenDadosIsDifferent() {
            // Given
            StoreData dados1 = StoreData.builder()
                    .codigo("SP-01")
                    .nome("Test Store 1")
                    .build();

            StoreData dados2 = StoreData.builder()
                    .codigo("SP-02")
                    .nome("Test Store 2")
                    .build();

            StoreEvent storeEvent1 = StoreEvent.builder()
                    .eventId("store-123")
                    .tipo("loja_criada")
                    .dados(dados1)
                    .build();

            StoreEvent storeEvent2 = StoreEvent.builder()
                    .eventId("store-123")
                    .tipo("loja_criada")
                    .dados(dados2)
                    .build();

            // When & Then
            assertNotEquals(storeEvent1, storeEvent2);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should generate toString with all fields")
        void shouldGenerateToStringWithAllFields() {
            // Given
            StoreData dados = StoreData.builder()
                    .codigo("SP-01")
                    .nome("Test Store")
                    .build();

            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId("store-123")
                    .tipo("loja_criada")
                    .dados(dados)
                    .build();

            // When
            String toString = storeEvent.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("eventId=store-123"));
            assertTrue(toString.contains("tipo=loja_criada"));
            assertTrue(toString.contains("dados="));
        }

        @Test
        @DisplayName("Should generate toString with null dados")
        void shouldGenerateToStringWithNullDados() {
            // Given
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId("store-null-dados")
                    .tipo("loja_atualizada")
                    .dados(null)
                    .build();

            // When
            String toString = storeEvent.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("eventId=store-null-dados"));
            assertTrue(toString.contains("tipo=loja_atualizada"));
            assertTrue(toString.contains("dados=null"));
        }
    }

    @Nested
    @DisplayName("Event Type Validation Tests")
    class EventTypeValidationTests {

        @Test
        @DisplayName("Should handle loja_criada event type")
        void shouldHandleLojaCriadaEventType() {
            // Given
            String tipo = "loja_criada";

            // When
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId("event-123")
                    .tipo(tipo)
                    .build();

            // Then
            assertEquals(tipo, storeEvent.getTipo());
        }

        @Test
        @DisplayName("Should handle loja_atualizada event type")
        void shouldHandleLojaAtualizadaEventType() {
            // Given
            String tipo = "loja_atualizada";

            // When
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId("event-456")
                    .tipo(tipo)
                    .build();

            // Then
            assertEquals(tipo, storeEvent.getTipo());
        }

        @Test
        @DisplayName("Should handle custom event types")
        void shouldHandleCustomEventTypes() {
            // Given
            String[] customTypes = {
                    "loja_inativada",
                    "loja_reativada", 
                    "loja_removida",
                    "loja_migrada"
            };

            for (String customType : customTypes) {
                // When
                StoreEvent storeEvent = StoreEvent.builder()
                        .eventId("event-" + customType)
                        .tipo(customType)
                        .build();

                // Then
                assertEquals(customType, storeEvent.getTipo());
            }
        }
    }

    @Nested
    @DisplayName("Store Data Validation Tests")
    class StoreDataValidationTests {

        @Test
        @DisplayName("Should handle null store data")
        void shouldHandleNullStoreData() {
            // Given & When
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId("null-data-event")
                    .tipo("loja_criada")
                    .dados(null)
                    .build();

            // Then
            assertNull(storeEvent.getDados());
        }

        @Test
        @DisplayName("Should handle store data with minimal information")
        void shouldHandleStoreDataWithMinimalInformation() {
            // Given
            StoreData minimalData = StoreData.builder()
                    .codigo("MIN-01")
                    .build();

            // When
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId("minimal-data-event")
                    .tipo("loja_criada")
                    .dados(minimalData)
                    .build();

            // Then
            assertEquals(minimalData, storeEvent.getDados());
            assertEquals("MIN-01", storeEvent.getDados().getCodigo());
        }

        @Test
        @DisplayName("Should handle store data with complete information")
        void shouldHandleStoreDataWithCompleteInformation() {
            // Given
            StoreData completeData = StoreData.builder()
                    .codigo("COMPLETE-01")
                    .nome("Complete Store Name")
                    .build();

            // When
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId("complete-data-event")
                    .tipo("loja_criada")
                    .dados(completeData)
                    .build();

            // Then
            assertEquals(completeData, storeEvent.getDados());
            assertEquals("COMPLETE-01", storeEvent.getDados().getCodigo());
            assertEquals("Complete Store Name", storeEvent.getDados().getNome());
        }
    }

    @Nested
    @DisplayName("Event ID Validation Tests")
    class EventIdValidationTests {

        @Test
        @DisplayName("Should handle different event ID formats")
        void shouldHandleDifferentEventIdFormats() {
            // Given
            String[] eventIds = {
                    "store-event-123",
                    "STORE_EVENT_456",
                    "store.event.789",
                    "12345",
                    "event-uuid-a1b2c3d4",
                    "EVENT-WITH-CAPS"
            };

            for (String eventId : eventIds) {
                // When
                StoreEvent storeEvent = StoreEvent.builder()
                        .eventId(eventId)
                        .tipo("loja_criada")
                        .build();

                // Then
                assertEquals(eventId, storeEvent.getEventId());
            }
        }

        @Test
        @DisplayName("Should handle null event ID")
        void shouldHandleNullEventId() {
            // Given & When
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId(null)
                    .tipo("loja_criada")
                    .build();

            // Then
            assertNull(storeEvent.getEventId());
        }

        @Test
        @DisplayName("Should handle empty event ID")
        void shouldHandleEmptyEventId() {
            // Given
            String emptyEventId = "";

            // When
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId(emptyEventId)
                    .tipo("loja_criada")
                    .build();

            // Then
            assertEquals(emptyEventId, storeEvent.getEventId());
            assertTrue(storeEvent.getEventId().isEmpty());
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
                StoreData storeData = StoreData.builder()
                        .codigo(storeCode)
                        .nome("Store " + storeCode)
                        .build();

                StoreEvent storeEvent = StoreEvent.builder()
                        .eventId("event-" + storeCode)
                        .tipo("loja_criada")
                        .dados(storeData)
                        .build();

                // Then
                assertEquals(storeCode, storeEvent.getDados().getCodigo());
            }
        }
    }

    @Nested
    @DisplayName("Store Name Validation Tests")
    class StoreNameValidationTests {

        @Test
        @DisplayName("Should handle different store name formats")
        void shouldHandleDifferentStoreNameFormats() {
            // Given
            String[] storeNames = {
                    "Store São Paulo Central",
                    "Loja Shopping Rio",
                    "Filial Belo Horizonte",
                    "Store-123",
                    "LOJA PRINCIPAL"
            };

            for (String storeName : storeNames) {
                // When
                StoreData storeData = StoreData.builder()
                        .codigo("CODE-" + storeName.hashCode())
                        .nome(storeName)
                        .build();

                StoreEvent storeEvent = StoreEvent.builder()
                        .eventId("event-" + storeName.hashCode())
                        .tipo("loja_criada")
                        .dados(storeData)
                        .build();

                // Then
                assertEquals(storeName, storeEvent.getDados().getNome());
            }
        }

        @Test
        @DisplayName("Should handle null store name")
        void shouldHandleNullStoreName() {
            // Given
            StoreData storeData = StoreData.builder()
                    .codigo("NULL-NAME-01")
                    .nome(null)
                    .build();

            // When
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId("null-name-event")
                    .tipo("loja_criada")
                    .dados(storeData)
                    .build();

            // Then
            assertNull(storeEvent.getDados().getNome());
        }

        @Test
        @DisplayName("Should handle empty store name")
        void shouldHandleEmptyStoreName() {
            // Given
            String emptyName = "";
            StoreData storeData = StoreData.builder()
                    .codigo("EMPTY-NAME-01")
                    .nome(emptyName)
                    .build();

            // When
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId("empty-name-event")
                    .tipo("loja_criada")
                    .dados(storeData)
                    .build();

            // Then
            assertEquals(emptyName, storeEvent.getDados().getNome());
            assertTrue(storeEvent.getDados().getNome().isEmpty());
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete store creation scenario")
        void shouldHandleCompleteStoreCreationScenario() {
            // Given
            String eventId = "store-creation-integration-test";
            String tipo = "loja_criada";
            StoreData storeData = StoreData.builder()
                    .codigo("INTEGRATION-01")
                    .nome("Integration Test Store")
                    .build();

            // When
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId(eventId)
                    .tipo(tipo)
                    .dados(storeData)
                    .build();

            // Then
            assertNotNull(storeEvent);
            assertEquals(eventId, storeEvent.getEventId());
            assertEquals(tipo, storeEvent.getTipo());
            assertNotNull(storeEvent.getDados());
            assertEquals("INTEGRATION-01", storeEvent.getDados().getCodigo());
            assertEquals("Integration Test Store", storeEvent.getDados().getNome());
        }

        @Test
        @DisplayName("Should handle complete store update scenario")
        void shouldHandleCompleteStoreUpdateScenario() {
            // Given
            String eventId = "store-update-integration-test";
            String tipo = "loja_atualizada";
            StoreData updatedStoreData = StoreData.builder()
                    .codigo("INTEGRATION-01")
                    .nome("Updated Integration Test Store")
                    .build();

            // When
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId(eventId)
                    .tipo(tipo)
                    .dados(updatedStoreData)
                    .build();

            // Then
            assertNotNull(storeEvent);
            assertEquals(eventId, storeEvent.getEventId());
            assertEquals(tipo, storeEvent.getTipo());
            assertNotNull(storeEvent.getDados());
            assertEquals("INTEGRATION-01", storeEvent.getDados().getCodigo());
            assertEquals("Updated Integration Test Store", storeEvent.getDados().getNome());
            assertTrue(storeEvent.getDados().getNome().startsWith("Updated"));
        }

        @Test
        @DisplayName("Should handle store event with only required fields")
        void shouldHandleStoreEventWithOnlyRequiredFields() {
            // Given
            String eventId = "minimal-store-event";
            String tipo = "loja_criada";
            StoreData minimalStoreData = StoreData.builder()
                    .codigo("MINIMAL-01")
                    .build();

            // When
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId(eventId)
                    .tipo(tipo)
                    .dados(minimalStoreData)
                    .build();

            // Then
            assertNotNull(storeEvent);
            assertEquals(eventId, storeEvent.getEventId());
            assertEquals(tipo, storeEvent.getTipo());
            assertNotNull(storeEvent.getDados());
            assertEquals("MINIMAL-01", storeEvent.getDados().getCodigo());
            assertNull(storeEvent.getDados().getNome());
        }
    }
}