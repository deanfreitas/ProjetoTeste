package br.com.inventoryservice.adapters.in.messaging;

import br.com.inventoryservice.adapters.in.messaging.dto.event.StockAdjustmentEvent;
import br.com.inventoryservice.application.port.in.InventoryEventUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("StockAdjustmentKafkaListener")
class StockAdjustmentKafkaListenerTest {

    @Mock
    private InventoryEventUseCase inventoryEventUseCase;

    @Mock
    private ConsumerRecord<String, String> consumerRecord;

    @InjectMocks
    private StockAdjustmentKafkaListener stockAdjustmentKafkaListener;

    @Nested
    @DisplayName("Normal Data Handling")
    class NormalDataHandling {

        @Test
        @DisplayName("Should handle stock adjustment event with complete data")
        void shouldHandleStockAdjustmentEventWithCompleteData() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentEvent event = StockAdjustmentEvent.builder()
                    .eventId("adj123")
                    .loja("L001")
                    .sku("SKU001")
                    .delta(10)
                    .motivo("inventario")
                    .timestamp(timestamp)
                    .build();

            String topic = "ajustes_estoque";
            Integer partition = 0;
            Long offset = 123L;

            doNothing().when(inventoryEventUseCase).handleStockAdjustmentEvent(any(), eq(topic), eq(partition), eq(offset));

            // When
            stockAdjustmentKafkaListener.onAjusteEstoque(event, topic, partition, offset, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleStockAdjustmentEvent(any(), eq(topic), eq(partition), eq(offset));
        }

        @Test
        @DisplayName("Should handle stock adjustment event with negative delta")
        void shouldHandleStockAdjustmentEventWithNegativeDelta() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentEvent event = StockAdjustmentEvent.builder()
                    .eventId("adj456")
                    .loja("L002")
                    .sku("SKU002")
                    .delta(-5)
                    .motivo("venda")
                    .timestamp(timestamp)
                    .build();

            String topic = "ajustes_estoque";
            Integer partition = 1;
            Long offset = 456L;

            doNothing().when(inventoryEventUseCase).handleStockAdjustmentEvent(any(), eq(topic), eq(partition), eq(offset));

            // When
            stockAdjustmentKafkaListener.onAjusteEstoque(event, topic, partition, offset, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleStockAdjustmentEvent(any(), eq(topic), eq(partition), eq(offset));
        }

        @Test
        @DisplayName("Should handle stock adjustment event with zero delta")
        void shouldHandleStockAdjustmentEventWithZeroDelta() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentEvent event = StockAdjustmentEvent.builder()
                    .eventId("adj789")
                    .loja("L003")
                    .sku("SKU003")
                    .delta(0)
                    .motivo("ajuste")
                    .timestamp(timestamp)
                    .build();

            String topic = "ajustes_estoque";
            Integer partition = 2;
            Long offset = 789L;

            doNothing().when(inventoryEventUseCase).handleStockAdjustmentEvent(any(), eq(topic), eq(partition), eq(offset));

            // When
            stockAdjustmentKafkaListener.onAjusteEstoque(event, topic, partition, offset, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleStockAdjustmentEvent(any(), eq(topic), eq(partition), eq(offset));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle stock adjustment event with null fields")
        void shouldHandleStockAdjustmentEventWithNullFields() {
            // Given
            StockAdjustmentEvent event = StockAdjustmentEvent.builder()
                    .eventId("adj999")
                    .loja(null)
                    .sku(null)
                    .delta(null)
                    .motivo(null)
                    .timestamp(null)
                    .build();

            String topic = "ajustes_estoque";
            Integer partition = 0;
            Long offset = 999L;

            doNothing().when(inventoryEventUseCase).handleStockAdjustmentEvent(any(), eq(topic), eq(partition), eq(offset));

            // When
            stockAdjustmentKafkaListener.onAjusteEstoque(event, topic, partition, offset, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleStockAdjustmentEvent(any(), eq(topic), eq(partition), eq(offset));
        }

        @Test
        @DisplayName("Should handle stock adjustment event with null headers")
        void shouldHandleStockAdjustmentEventWithNullHeaders() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentEvent event = StockAdjustmentEvent.builder()
                    .eventId("adj111")
                    .loja("L004")
                    .sku("SKU004")
                    .delta(15)
                    .motivo("reposicao")
                    .timestamp(timestamp)
                    .build();

            doNothing().when(inventoryEventUseCase).handleStockAdjustmentEvent(any(), eq(null), eq(null), eq(null));

            // When
            stockAdjustmentKafkaListener.onAjusteEstoque(event, null, null, null, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleStockAdjustmentEvent(any(), eq(null), eq(null), eq(null));
        }
    }
}