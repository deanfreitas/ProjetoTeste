package br.com.inventoryservice.adapters.in.messaging;

import br.com.inventoryservice.adapters.in.messaging.dto.data.ProductData;
import br.com.inventoryservice.adapters.in.messaging.dto.event.ProductEvent;
import br.com.inventoryservice.application.port.in.InventoryEventUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductKafkaListener")
class ProductKafkaListenerTest {

    @Mock
    private InventoryEventUseCase inventoryEventUseCase;

    @Mock
    private ConsumerRecord<String, String> consumerRecord;

    @InjectMocks
    private ProductKafkaListener productKafkaListener;

    @Nested
    @DisplayName("Normal Data Handling")
    class NormalDataHandling {

        @Test
        @DisplayName("Should handle product event with complete data")
        void shouldHandleProductEventWithCompleteData() {
            // Given
            ProductData productData = new ProductData("SKU001", "Product 1", true);
            ProductEvent event = new ProductEvent("event123", "CREATE", productData);

            String topic = "produtos";
            Integer partition = 0;
            Long offset = 123L;

            doNothing().when(inventoryEventUseCase).handleProductEvent(any(), eq(topic), eq(partition), eq(offset));

            // When
            productKafkaListener.onProdutoEvento(event, topic, partition, offset, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleProductEvent(any(), eq(topic), eq(partition), eq(offset));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle product event with null data")
        void shouldHandleProductEventWithNullData() {
            // Given
            ProductEvent event = new ProductEvent("event123", "DELETE", null);

            String topic = "produtos";
            Integer partition = 1;
            Long offset = 456L;

            doNothing().when(inventoryEventUseCase).handleProductEvent(any(), eq(topic), eq(partition), eq(offset));

            // When
            productKafkaListener.onProdutoEvento(event, topic, partition, offset, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleProductEvent(any(), eq(topic), eq(partition), eq(offset));
        }

        @Test
        @DisplayName("Should handle product event with partial data")
        void shouldHandleProductEventWithPartialData() {
            // Given
            ProductData productData = ProductData.builder()
                    .sku("SKU002")
                    .nome(null)
                    .ativo(null)
                    .build();

            ProductEvent event = ProductEvent.builder()
                    .eventId("event456")
                    .tipo("UPDATE")
                    .dados(productData)
                    .build();

            String topic = "produtos";
            Integer partition = 2;
            Long offset = 789L;

            doNothing().when(inventoryEventUseCase).handleProductEvent(any(), eq(topic), eq(partition), eq(offset));

            // When
            productKafkaListener.onProdutoEvento(event, topic, partition, offset, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleProductEvent(any(), eq(topic), eq(partition), eq(offset));
        }

        @Test
        @DisplayName("Should handle product event with null headers")
        void shouldHandleProductEventWithNullHeaders() {
            // Given
            ProductData productData = ProductData.builder()
                    .sku("SKU003")
                    .nome("Product 3")
                    .ativo(false)
                    .build();

            ProductEvent event = ProductEvent.builder()
                    .eventId("event789")
                    .tipo("CREATE")
                    .dados(productData)
                    .build();

            doNothing().when(inventoryEventUseCase).handleProductEvent(any(), eq(null), eq(null), eq(null));

            // When
            productKafkaListener.onProdutoEvento(event, null, null, null, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleProductEvent(any(), eq(null), eq(null), eq(null));
        }

        @Test
        @DisplayName("Should handle product event with empty event ID")
        void shouldHandleProductEventWithEmptyEventId() {
            // Given
            ProductData productData = ProductData.builder()
                    .sku("SKU004")
                    .nome("Product 4")
                    .ativo(true)
                    .build();

            ProductEvent event = ProductEvent.builder()
                    .eventId("")
                    .tipo("UPDATE")
                    .dados(productData)
                    .build();

            String topic = "produtos";
            Integer partition = 0;
            Long offset = 999L;

            doNothing().when(inventoryEventUseCase).handleProductEvent(any(), eq(topic), eq(partition), eq(offset));

            // When
            productKafkaListener.onProdutoEvento(event, topic, partition, offset, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleProductEvent(any(), eq(topic), eq(partition), eq(offset));
        }
    }
}