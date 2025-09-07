package br.com.inventoryservice.application.service.mapper;

import br.com.inventoryservice.domain.model.StoreModel;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.StoreData;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.StoreEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StoreEventMapper Tests")
class StoreEventMapperTest {

    @Nested
    @DisplayName("Valid Conversions")
    class ValidConversions {
        
        @Test
        @DisplayName("Should convert valid StoreEvent to domain model")
        void shouldConvertValidStoreEventToDomainModel() {
            // Given
            StoreData storeData = StoreData.builder()
                    .codigo("STORE-001")
                    .nome("Test Store")
                    .build();
            
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId("event-123")
                    .dados(storeData)
                    .build();

            // When
            StoreModel result = StoreEventMapper.toDomain(storeEvent);

            // Then
            assertNotNull(result);
            assertEquals("event-123", result.eventId());
            assertEquals("STORE-001", result.codigo());
        }
    }

    @Nested
    @DisplayName("Null Input Handling")
    class NullInputHandling {
        
        @Test
        @DisplayName("Should return null when StoreEvent is null")
        void shouldReturnNullWhenStoreEventIsNull() {
            // When
            StoreModel result = StoreEventMapper.toDomain(null);

            // Then
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {
        
        @Test
        @DisplayName("Should handle StoreEvent with null dados")
        void shouldHandleStoreEventWithNullDados() {
            // Given
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId("event-456")
                    .dados(null)
                    .build();

            // When
            StoreModel result = StoreEventMapper.toDomain(storeEvent);

            // Then
            assertNotNull(result);
            assertEquals("event-456", result.eventId());
            assertNull(result.codigo());
        }

        @Test
        @DisplayName("Should handle StoreEvent with dados containing null codigo")
        void shouldHandleStoreEventWithDadosContainingNullCodigo() {
            // Given
            StoreData storeData = StoreData.builder()
                    .codigo(null)
                    .nome("Test Store")
                    .build();
            
            StoreEvent storeEvent = StoreEvent.builder()
                    .eventId("event-789")
                    .dados(storeData)
                    .build();

            // When
            StoreModel result = StoreEventMapper.toDomain(storeEvent);

            // Then
            assertNotNull(result);
            assertEquals("event-789", result.eventId());
            assertNull(result.codigo());
        }
    }
}