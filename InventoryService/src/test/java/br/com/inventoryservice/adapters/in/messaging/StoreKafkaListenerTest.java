package br.com.inventoryservice.adapters.in.messaging;

import br.com.inventoryservice.adapters.in.messaging.dto.data.StoreData;
import br.com.inventoryservice.adapters.in.messaging.dto.event.StoreEvent;
import br.com.inventoryservice.application.port.in.InventoryEventUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("StoreKafkaListener")
class StoreKafkaListenerTest {

    @Mock
    private InventoryEventUseCase inventoryEventUseCase;

    @Mock
    private ConsumerRecord<String, String> consumerRecord;

    @InjectMocks
    private StoreKafkaListener storeKafkaListener;

    @Test
    @DisplayName("Should handle store event with complete data")
    void shouldHandleStoreEventWithCompleteData() {
        // Given
        StoreData storeData = StoreData.builder()
                .codigo("L001")
                .nome("Store 1")
                .build();

        StoreEvent event = StoreEvent.builder()
                .eventId("store123")
                .tipo("CREATE")
                .dados(storeData)
                .build();

        String topic = "lojas";
        Integer partition = 0;
        Long offset = 123L;

        doNothing().when(inventoryEventUseCase).handleStoreEvent(any(), eq(topic), eq(partition), eq(offset));

        // When
        storeKafkaListener.onLojaEvento(event, topic, partition, offset, consumerRecord);

        // Then
        verify(inventoryEventUseCase).handleStoreEvent(any(), eq(topic), eq(partition), eq(offset));
    }

    @Test
    @DisplayName("Should handle store event with null data")
    void shouldHandleStoreEventWithNullData() {
        // Given
        StoreEvent event = StoreEvent.builder()
                .eventId("store456")
                .tipo("DELETE")
                .dados(null)
                .build();

        String topic = "lojas";
        Integer partition = 1;
        Long offset = 456L;

        doNothing().when(inventoryEventUseCase).handleStoreEvent(any(), eq(topic), eq(partition), eq(offset));

        // When
        storeKafkaListener.onLojaEvento(event, topic, partition, offset, consumerRecord);

        // Then
        verify(inventoryEventUseCase).handleStoreEvent(any(), eq(topic), eq(partition), eq(offset));
    }

    @Test
    @DisplayName("Should handle store event with partial data")
    void shouldHandleStoreEventWithPartialData() {
        // Given
        StoreData storeData = StoreData.builder()
                .codigo("L002")
                .nome(null)
                .build();

        StoreEvent event = StoreEvent.builder()
                .eventId("store789")
                .tipo("UPDATE")
                .dados(storeData)
                .build();

        String topic = "lojas";
        Integer partition = 2;
        Long offset = 789L;

        doNothing().when(inventoryEventUseCase).handleStoreEvent(any(), eq(topic), eq(partition), eq(offset));

        // When
        storeKafkaListener.onLojaEvento(event, topic, partition, offset, consumerRecord);

        // Then
        verify(inventoryEventUseCase).handleStoreEvent(any(), eq(topic), eq(partition), eq(offset));
    }

    @Test
    @DisplayName("Should handle store event with empty values")
    void shouldHandleStoreEventWithEmptyValues() {
        // Given
        StoreData storeData = StoreData.builder()
                .codigo("")
                .nome("")
                .build();

        StoreEvent event = StoreEvent.builder()
                .eventId("")
                .tipo("")
                .dados(storeData)
                .build();

        String topic = "lojas";
        Integer partition = 0;
        Long offset = 999L;

        doNothing().when(inventoryEventUseCase).handleStoreEvent(any(), eq(topic), eq(partition), eq(offset));

        // When
        storeKafkaListener.onLojaEvento(event, topic, partition, offset, consumerRecord);

        // Then
        verify(inventoryEventUseCase).handleStoreEvent(any(), eq(topic), eq(partition), eq(offset));
    }

    @Test
    @DisplayName("Should handle store event with null headers")
    void shouldHandleStoreEventWithNullHeaders() {
        // Given
        StoreData storeData = StoreData.builder()
                .codigo("L003")
                .nome("Store 3")
                .build();

        StoreEvent event = StoreEvent.builder()
                .eventId("store111")
                .tipo("CREATE")
                .dados(storeData)
                .build();

        doNothing().when(inventoryEventUseCase).handleStoreEvent(any(), eq(null), eq(null), eq(null));

        // When
        storeKafkaListener.onLojaEvento(event, null, null, null, consumerRecord);

        // Then
        verify(inventoryEventUseCase).handleStoreEvent(any(), eq(null), eq(null), eq(null));
    }
}