package br.com.inventoryservice.infrastructure.adapters.in.messaging.mapper;

import br.com.inventoryservice.domain.model.SalesModel;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.SalesItem;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.SalesEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SalesEventMapper Tests")
class SalesEventMapperTest {

    @Nested
    @DisplayName("Valid Conversions")
    class ValidConversions {

        @Test
        @DisplayName("Should convert valid SalesEvent to domain model")
        void shouldConvertValidSalesEventToDomainModel() {
            // Given
            SalesItem item1 =
                    SalesItem.builder()
                            .sku("PROD-001")
                            .quantidade(5)
                            .build();

            SalesItem item2 =
                    SalesItem.builder()
                            .sku("PROD-002")
                            .quantidade(3)
                            .build();

            SalesEvent salesEvent = SalesEvent.builder()
                    .eventId("event-123")
                    .loja("STORE-001")
                    .itens(Arrays.asList(item1, item2))
                    .build();

            // When
            SalesModel result = SalesEventMapper.toDomain(salesEvent);

            // Then
            assertNotNull(result);
            assertEquals("event-123", result.eventId());
            assertEquals("STORE-001", result.loja());
            assertNotNull(result.itens());
            assertEquals(2, result.itens().size());
            assertEquals("PROD-001", result.itens().get(0).sku());
            assertEquals(5, result.itens().get(0).quantidade());
            assertEquals("PROD-002", result.itens().get(1).sku());
            assertEquals(3, result.itens().get(1).quantidade());
        }
    }

    @Nested
    @DisplayName("Null Input Handling")
    class NullInputHandling {

        @Test
        @DisplayName("Should return null when SalesEvent is null")
        void shouldReturnNullWhenSalesEventIsNull() {
            // When
            SalesModel result = SalesEventMapper.toDomain(null);

            // Then
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle SalesEvent with null itens")
        void shouldHandleSalesEventWithNullItens() {
            // Given
            SalesEvent salesEvent = SalesEvent.builder()
                    .eventId("event-456")
                    .loja("STORE-002")
                    .itens(null)
                    .build();

            // When
            SalesModel result = SalesEventMapper.toDomain(salesEvent);

            // Then
            assertNotNull(result);
            assertEquals("event-456", result.eventId());
            assertEquals("STORE-002", result.loja());
            assertNull(result.itens());
        }

        @Test
        @DisplayName("Should handle SalesEvent with empty itens list")
        void shouldHandleSalesEventWithEmptyItensList() {
            // Given
            SalesEvent salesEvent = SalesEvent.builder()
                    .eventId("event-789")
                    .loja("STORE-003")
                    .itens(Collections.emptyList())
                    .build();

            // When
            SalesModel result = SalesEventMapper.toDomain(salesEvent);

            // Then
            assertNotNull(result);
            assertEquals("event-789", result.eventId());
            assertEquals("STORE-003", result.loja());
            assertNotNull(result.itens());
            assertTrue(result.itens().isEmpty());
        }
    }
}