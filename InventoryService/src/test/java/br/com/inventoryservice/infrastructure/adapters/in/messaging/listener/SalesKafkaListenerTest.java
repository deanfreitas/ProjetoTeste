package br.com.inventoryservice.infrastructure.adapters.in.messaging.listener;

import br.com.inventoryservice.application.port.in.InventoryEventUseCase;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.SalesItem;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.SalesEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("SalesKafkaListener")
class SalesKafkaListenerTest {

    @Mock
    private InventoryEventUseCase inventoryEventUseCase;

    @Mock
    private ConsumerRecord<String, String> consumerRecord;

    @InjectMocks
    private SalesKafkaListener salesKafkaListener;

    @Nested
    @DisplayName("Normal Data Handling")
    class NormalDataHandling {

        @Test
        @DisplayName("Should handle sales event with complete data")
        void shouldHandleSalesEventWithCompleteData() {
            // Given
            List<SalesItem> itens = List.of(
                    SalesItem.builder().sku("SKU001").quantidade(2).build(),
                    SalesItem.builder().sku("SKU002").quantidade(1).build()
            );

            SalesEvent event = SalesEvent.builder()
                    .eventId("sale123")
                    .loja("L001")
                    .itens(itens)
                    .build();

            String topic = "vendas";
            Integer partition = 0;
            Long offset = 123L;

            doNothing().when(inventoryEventUseCase).handleSalesEvent(any(), eq(topic), eq(partition), eq(offset));

            // When
            salesKafkaListener.onVenda(event, topic, partition, offset, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleSalesEvent(any(), eq(topic), eq(partition), eq(offset));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle sales event with null items")
        void shouldHandleSalesEventWithNullItems() {
            // Given
            SalesEvent event = SalesEvent.builder()
                    .eventId("sale456")
                    .loja("L002")
                    .itens(null)
                    .build();

            String topic = "vendas";
            Integer partition = 1;
            Long offset = 456L;

            doNothing().when(inventoryEventUseCase).handleSalesEvent(any(), eq(topic), eq(partition), eq(offset));

            // When
            salesKafkaListener.onVenda(event, topic, partition, offset, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleSalesEvent(any(), eq(topic), eq(partition), eq(offset));
        }

        @Test
        @DisplayName("Should handle sales event with empty items list")
        void shouldHandleSalesEventWithEmptyItemsList() {
            // Given
            SalesEvent event = SalesEvent.builder()
                    .eventId("sale789")
                    .loja("L003")
                    .itens(List.of())
                    .build();

            String topic = "vendas";
            Integer partition = 2;
            Long offset = 789L;

            doNothing().when(inventoryEventUseCase).handleSalesEvent(any(), eq(topic), eq(partition), eq(offset));

            // When
            salesKafkaListener.onVenda(event, topic, partition, offset, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleSalesEvent(any(), eq(topic), eq(partition), eq(offset));
        }

        @Test
        @DisplayName("Should handle sales event with null store")
        void shouldHandleSalesEventWithNullStore() {
            // Given
            List<SalesItem> itens = List.of(
                    SalesItem.builder().sku("SKU003").quantidade(3).build()
            );

            SalesEvent event = SalesEvent.builder()
                    .eventId("sale999")
                    .loja(null)
                    .itens(itens)
                    .build();

            String topic = "vendas";
            Integer partition = 0;
            Long offset = 999L;

            doNothing().when(inventoryEventUseCase).handleSalesEvent(any(), eq(topic), eq(partition), eq(offset));

            // When
            salesKafkaListener.onVenda(event, topic, partition, offset, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleSalesEvent(any(), eq(topic), eq(partition), eq(offset));
        }

        @Test
        @DisplayName("Should handle sales event with null headers")
        void shouldHandleSalesEventWithNullHeaders() {
            // Given
            List<SalesItem> itens = List.of(
                    SalesItem.builder().sku("SKU004").quantidade(1).build()
            );

            SalesEvent event = SalesEvent.builder()
                    .eventId("sale111")
                    .loja("L004")
                    .itens(itens)
                    .build();

            doNothing().when(inventoryEventUseCase).handleSalesEvent(any(), eq(null), eq(null), eq(null));

            // When
            salesKafkaListener.onVenda(event, null, null, null, consumerRecord);

            // Then
            verify(inventoryEventUseCase).handleSalesEvent(any(), eq(null), eq(null), eq(null));
        }
    }
}