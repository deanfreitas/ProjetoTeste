package br.com.inventoryservice.application.service.mapper;

import br.com.inventoryservice.domain.model.ProductModel;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.ProductData;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.ProductEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductEventMapper Tests")
class ProductEventMapperTest {

    @Nested
    @DisplayName("Valid Conversions")
    class ValidConversions {
        
        @Test
        @DisplayName("Should convert valid ProductEvent to domain model")
        void shouldConvertValidProductEventToDomainModel() {
            // Given
            ProductData productData = ProductData.builder()
                    .sku("PROD-001")
                    .nome("Test Product")
                    .ativo(true)
                    .build();
            
            ProductEvent productEvent = ProductEvent.builder()
                    .eventId("event-123")
                    .dados(productData)
                    .build();

            // When
            ProductModel result = ProductEventMapper.toDomain(productEvent);

            // Then
            assertNotNull(result);
            assertEquals("event-123", result.eventId());
            assertEquals("PROD-001", result.sku());
        }
    }

    @Nested
    @DisplayName("Null Input Handling")
    class NullInputHandling {
        
        @Test
        @DisplayName("Should return null when ProductEvent is null")
        void shouldReturnNullWhenProductEventIsNull() {
            // When
            ProductModel result = ProductEventMapper.toDomain(null);

            // Then
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {
        
        @Test
        @DisplayName("Should handle ProductEvent with null dados")
        void shouldHandleProductEventWithNullDados() {
            // Given
            ProductEvent productEvent = ProductEvent.builder()
                    .eventId("event-456")
                    .dados(null)
                    .build();

            // When
            ProductModel result = ProductEventMapper.toDomain(productEvent);

            // Then
            assertNotNull(result);
            assertEquals("event-456", result.eventId());
            assertNull(result.sku());
        }

        @Test
        @DisplayName("Should handle ProductEvent with dados containing null sku")
        void shouldHandleProductEventWithDadosContainingNullSku() {
            // Given
            ProductData productData = ProductData.builder()
                    .sku(null)
                    .nome("Test Product")
                    .ativo(true)
                    .build();
            
            ProductEvent productEvent = ProductEvent.builder()
                    .eventId("event-789")
                    .dados(productData)
                    .build();

            // When
            ProductModel result = ProductEventMapper.toDomain(productEvent);

            // Then
            assertNotNull(result);
            assertEquals("event-789", result.eventId());
            assertNull(result.sku());
        }
    }
}