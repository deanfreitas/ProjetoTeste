package br.com.inventoryservice.application.service.mapper;

import br.com.inventoryservice.domain.model.StockAdjustmentModel;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.StockAdjustmentEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StockAdjustmentEventMapper Tests")
class StockAdjustmentEventMapperTest {

    @Nested
    @DisplayName("Valid Conversions")
    class ValidConversions {
        
        @Test
        @DisplayName("Should convert valid StockAdjustmentEvent to domain model")
        void shouldConvertValidStockAdjustmentEventToDomainModel() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentEvent event = StockAdjustmentEvent.builder()
                    .eventId("event-123")
                    .loja("STORE-001")
                    .sku("PROD-001")
                    .delta(10)
                    .motivo("Reposição")
                    .timestamp(timestamp)
                    .build();

            // When
            StockAdjustmentModel result = StockAdjustmentEventMapper.toDomain(event);

            // Then
            assertNotNull(result);
            assertEquals("event-123", result.eventId());
            assertEquals("STORE-001", result.loja());
            assertEquals("PROD-001", result.sku());
            assertEquals(10, result.delta());
            assertEquals("Reposição", result.motivo());
            assertEquals(timestamp, result.timestamp());
        }

        @Test
        @DisplayName("Should handle negative delta values")
        void shouldHandleNegativeDeltaValues() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentEvent event = StockAdjustmentEvent.builder()
                    .eventId("event-456")
                    .loja("STORE-002")
                    .sku("PROD-002")
                    .delta(-5)
                    .motivo("Devolução")
                    .timestamp(timestamp)
                    .build();

            // When
            StockAdjustmentModel result = StockAdjustmentEventMapper.toDomain(event);

            // Then
            assertNotNull(result);
            assertEquals("event-456", result.eventId());
            assertEquals("STORE-002", result.loja());
            assertEquals("PROD-002", result.sku());
            assertEquals(-5, result.delta());
            assertEquals("Devolução", result.motivo());
            assertEquals(timestamp, result.timestamp());
        }
    }

    @Nested
    @DisplayName("Null Input Handling")
    class NullInputHandling {
        
        @Test
        @DisplayName("Should return null when StockAdjustmentEvent is null")
        void shouldReturnNullWhenStockAdjustmentEventIsNull() {
            // When
            StockAdjustmentModel result = StockAdjustmentEventMapper.toDomain(null);

            // Then
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {
        
        @Test
        @DisplayName("Should handle StockAdjustmentEvent with null fields")
        void shouldHandleStockAdjustmentEventWithNullFields() {
            // Given
            StockAdjustmentEvent event = StockAdjustmentEvent.builder()
                    .eventId(null)
                    .loja(null)
                    .sku(null)
                    .delta(null)
                    .motivo(null)
                    .timestamp(null)
                    .build();

            // When
            StockAdjustmentModel result = StockAdjustmentEventMapper.toDomain(event);

            // Then
            assertNotNull(result);
            assertNull(result.eventId());
            assertNull(result.loja());
            assertNull(result.sku());
            assertNull(result.delta());
            assertNull(result.motivo());
            assertNull(result.timestamp());
        }
    }
}