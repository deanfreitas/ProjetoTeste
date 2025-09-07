package br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event;

import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.ProductData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductEvent Tests")
class ProductEventTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create ProductEvent with no-args constructor")
        void shouldCreateProductEventWithNoArgsConstructor() {
            // Given & When
            ProductEvent productEvent = new ProductEvent();

            // Then
            assertNotNull(productEvent);
            assertNull(productEvent.getEventId());
            assertNull(productEvent.getTipo());
            assertNull(productEvent.getDados());
        }

        @Test
        @DisplayName("Should create ProductEvent with all-args constructor")
        void shouldCreateProductEventWithAllArgsConstructor() {
            // Given
            String eventId = "event-123";
            String tipo = "produto_criado";
            ProductData dados = ProductData.builder()
                    .sku("SKU-001")
                    .nome("Test Product")
                    .build();

            // When
            ProductEvent productEvent = new ProductEvent(eventId, tipo, dados);

            // Then
            assertNotNull(productEvent);
            assertEquals(eventId, productEvent.getEventId());
            assertEquals(tipo, productEvent.getTipo());
            assertEquals(dados, productEvent.getDados());
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should create ProductEvent using builder pattern")
        void shouldCreateProductEventUsingBuilder() {
            // Given
            String eventId = "event-456";
            String tipo = "produto_atualizado";
            ProductData dados = ProductData.builder()
                    .sku("SKU-002")
                    .nome("Updated Product")
                    .build();

            // When
            ProductEvent productEvent = ProductEvent.builder()
                    .eventId(eventId)
                    .tipo(tipo)
                    .dados(dados)
                    .build();

            // Then
            assertNotNull(productEvent);
            assertEquals(eventId, productEvent.getEventId());
            assertEquals(tipo, productEvent.getTipo());
            assertEquals(dados, productEvent.getDados());
        }

        @Test
        @DisplayName("Should create ProductEvent with partial builder data")
        void shouldCreateProductEventWithPartialBuilderData() {
            // Given
            String eventId = "event-789";

            // When
            ProductEvent productEvent = ProductEvent.builder()
                    .eventId(eventId)
                    .build();

            // Then
            assertNotNull(productEvent);
            assertEquals(eventId, productEvent.getEventId());
            assertNull(productEvent.getTipo());
            assertNull(productEvent.getDados());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get eventId correctly")
        void shouldSetAndGetEventIdCorrectly() {
            // Given
            ProductEvent productEvent = new ProductEvent();
            String eventId = "test-event-id";

            // When
            productEvent.setEventId(eventId);

            // Then
            assertEquals(eventId, productEvent.getEventId());
        }

        @Test
        @DisplayName("Should set and get tipo correctly")
        void shouldSetAndGetTipoCorrectly() {
            // Given
            ProductEvent productEvent = new ProductEvent();
            String tipo = "produto_inativado";

            // When
            productEvent.setTipo(tipo);

            // Then
            assertEquals(tipo, productEvent.getTipo());
        }

        @Test
        @DisplayName("Should set and get dados correctly")
        void shouldSetAndGetDadosCorrectly() {
            // Given
            ProductEvent productEvent = new ProductEvent();
            ProductData dados = ProductData.builder()
                    .sku("SKU-003")
                    .nome("Test Product Data")
                    .build();

            // When
            productEvent.setDados(dados);

            // Then
            assertEquals(dados, productEvent.getDados());
        }
    }

    @Nested
    @DisplayName("Equality and HashCode Tests")
    class EqualityHashCodeTests {

        @Test
        @DisplayName("Should be equal when all fields are the same")
        void shouldBeEqualWhenAllFieldsAreTheSame() {
            // Given
            ProductData dados = ProductData.builder()
                    .sku("SKU-001")
                    .nome("Test Product")
                    .build();

            ProductEvent productEvent1 = ProductEvent.builder()
                    .eventId("event-123")
                    .tipo("produto_criado")
                    .dados(dados)
                    .build();

            ProductEvent productEvent2 = ProductEvent.builder()
                    .eventId("event-123")
                    .tipo("produto_criado")
                    .dados(dados)
                    .build();

            // When & Then
            assertEquals(productEvent1, productEvent2);
            assertEquals(productEvent1.hashCode(), productEvent2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when eventId is different")
        void shouldNotBeEqualWhenEventIdIsDifferent() {
            // Given
            ProductData dados = ProductData.builder()
                    .sku("SKU-001")
                    .nome("Test Product")
                    .build();

            ProductEvent productEvent1 = ProductEvent.builder()
                    .eventId("event-123")
                    .tipo("produto_criado")
                    .dados(dados)
                    .build();

            ProductEvent productEvent2 = ProductEvent.builder()
                    .eventId("event-456")
                    .tipo("produto_criado")
                    .dados(dados)
                    .build();

            // When & Then
            assertNotEquals(productEvent1, productEvent2);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should generate toString with all fields")
        void shouldGenerateToStringWithAllFields() {
            // Given
            ProductData dados = ProductData.builder()
                    .sku("SKU-001")
                    .nome("Test Product")
                    .build();

            ProductEvent productEvent = ProductEvent.builder()
                    .eventId("event-123")
                    .tipo("produto_criado")
                    .dados(dados)
                    .build();

            // When
            String toString = productEvent.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("eventId=event-123"));
            assertTrue(toString.contains("tipo=produto_criado"));
            assertTrue(toString.contains("dados="));
        }
    }

    @Nested
    @DisplayName("Event Type Validation Tests")
    class EventTypeValidationTests {

        @Test
        @DisplayName("Should handle produto_criado event type")
        void shouldHandleProdutoCriadoEventType() {
            // Given
            String tipo = "produto_criado";

            // When
            ProductEvent productEvent = ProductEvent.builder()
                    .eventId("event-123")
                    .tipo(tipo)
                    .build();

            // Then
            assertEquals(tipo, productEvent.getTipo());
        }

        @Test
        @DisplayName("Should handle produto_atualizado event type")
        void shouldHandleProdutoAtualizadoEventType() {
            // Given
            String tipo = "produto_atualizado";

            // When
            ProductEvent productEvent = ProductEvent.builder()
                    .eventId("event-123")
                    .tipo(tipo)
                    .build();

            // Then
            assertEquals(tipo, productEvent.getTipo());
        }

        @Test
        @DisplayName("Should handle produto_inativado event type")
        void shouldHandleProdutoInativadoEventType() {
            // Given
            String tipo = "produto_inativado";

            // When
            ProductEvent productEvent = ProductEvent.builder()
                    .eventId("event-123")
                    .tipo(tipo)
                    .build();

            // Then
            assertEquals(tipo, productEvent.getTipo());
        }
    }
}