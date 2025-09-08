package br.com.inventoryservice.infrastructure.adapters.in.messaging.integration;

import br.com.inventoryservice.application.port.in.InventoryEventUseCase;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.StoreData;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.StoreEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Integration tests for StoreKafkaListener using TestContainers.
 * Tests the complete flow from Kafka message consumption to service processing.
 */
@DisplayName("StoreKafkaListener Integration Tests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StoreKafkaListenerIntegrationTest extends KafkaIntegrationTestBase {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private InventoryEventUseCase inventoryEventUseCase;

    @Test
    @DisplayName("Should process valid store creation event successfully")
    void shouldProcessValidStoreCreationEvent() {
        // Arrange
        StoreEvent storeEvent = createValidStoreEvent("LOJA_CRIADA", "LOJA001", "Loja Centro");
        String topic = "lojas-test";

        // Act
        kafkaTemplate.send(topic, storeEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStoreEvent(
                                    argThat(store ->
                                            "LOJA001".equals(store.codigo()) &&
                                                    store.eventId() != null
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should process valid store update event successfully")
    void shouldProcessValidStoreUpdateEvent() {
        // Arrange
        StoreEvent storeEvent = createValidStoreEvent("LOJA_ATUALIZADA", "LOJA002", "Loja Shopping Atualizada");
        String topic = "lojas-test";

        // Act
        kafkaTemplate.send(topic, storeEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStoreEvent(
                                    argThat(store ->
                                            "LOJA002".equals(store.codigo()) &&
                                                    store.eventId() != null
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should process store deactivation event successfully")
    void shouldProcessStoreDeactivationEvent() {
        // Arrange
        StoreEvent storeEvent = createValidStoreEvent("LOJA_DESATIVADA", "LOJA003", "Loja Desativada");
        String topic = "lojas-test";

        // Act
        kafkaTemplate.send(topic, storeEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStoreEvent(
                                    argThat(store ->
                                            "LOJA003".equals(store.codigo()) &&
                                                    store.eventId() != null
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should handle multiple store events concurrently")
    void shouldHandleMultipleStoreEventsConcurrently() {
        // Arrange
        String topic = "lojas-test";
        StoreEvent event1 = createValidStoreEvent("LOJA_CRIADA", "LOJA004", "Loja Norte");
        StoreEvent event2 = createValidStoreEvent("LOJA_CRIADA", "LOJA005", "Loja Sul");
        StoreEvent event3 = createValidStoreEvent("LOJA_CRIADA", "LOJA006", "Loja Leste");

        // Act
        kafkaTemplate.send(topic, event1);
        kafkaTemplate.send(topic, event2);
        kafkaTemplate.send(topic, event3);

        // Assert
        await().atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, times(3))
                            .handleStoreEvent(any(), eq(topic), any(Integer.class), any(Long.class));
                });
    }

    @Test
    @DisplayName("Should handle store event with null data gracefully")
    void shouldHandleStoreEventWithNullDataGracefully() {
        // Arrange
        StoreEvent storeEvent = StoreEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tipo("LOJA_CRIADA")
                .dados(null)
                .build();
        String topic = "lojas-test";

        // Act
        kafkaTemplate.send(topic, storeEvent);

        // Assert - Should not throw exception and should still call the service
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStoreEvent(any(), eq(topic), any(Integer.class), any(Long.class));
                });
    }

    @Test
    @DisplayName("Should process store event with empty store code")
    void shouldProcessStoreEventWithEmptyStoreCode() {
        // Arrange
        StoreEvent storeEvent = createValidStoreEvent("LOJA_CRIADA", "", "Loja Sem Código");
        String topic = "lojas-test";

        // Act
        kafkaTemplate.send(topic, storeEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStoreEvent(
                                    argThat(store ->
                                            "".equals(store.codigo()) &&
                                                    store.eventId() != null
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should handle different store event types")
    void shouldHandleDifferentStoreEventTypes() {
        // Arrange
        StoreEvent creationEvent = createValidStoreEvent("LOJA_CRIADA", "STORE_A", "Store A");
        StoreEvent updateEvent = createValidStoreEvent("LOJA_ATUALIZADA", "STORE_B", "Store B Updated");
        StoreEvent deactivationEvent = createValidStoreEvent("LOJA_DESATIVADA", "STORE_C", "Store C Deactivated");
        String topic = "lojas-test";

        // Act
        kafkaTemplate.send(topic, creationEvent);
        kafkaTemplate.send(topic, updateEvent);
        kafkaTemplate.send(topic, deactivationEvent);

        // Assert
        await().atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStoreEvent(
                                    argThat(store -> "STORE_A".equals(store.codigo())),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStoreEvent(
                                    argThat(store -> "STORE_B".equals(store.codigo())),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStoreEvent(
                                    argThat(store -> "STORE_C".equals(store.codigo())),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should process store events with special characters in store codes")
    void shouldProcessStoreEventsWithSpecialCharacters() {
        // Arrange
        StoreEvent event1 = createValidStoreEvent("LOJA_CRIADA", "LOJA-001", "Loja com Hífen");
        StoreEvent event2 = createValidStoreEvent("LOJA_CRIADA", "LOJA_002", "Loja com Underscore");
        StoreEvent event3 = createValidStoreEvent("LOJA_CRIADA", "LOJA.003", "Loja com Ponto");
        String topic = "lojas-test";

        // Act
        kafkaTemplate.send(topic, event1);
        kafkaTemplate.send(topic, event2);
        kafkaTemplate.send(topic, event3);

        // Assert
        await().atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStoreEvent(
                                    argThat(store -> "LOJA-001".equals(store.codigo())),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStoreEvent(
                                    argThat(store -> "LOJA_002".equals(store.codigo())),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStoreEvent(
                                    argThat(store -> "LOJA.003".equals(store.codigo())),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    /**
     * Creates a valid StoreEvent for testing purposes.
     */
    private StoreEvent createValidStoreEvent(String tipo, String codigo, String nome) {
        StoreData storeData = StoreData.builder()
                .codigo(codigo)
                .nome(nome)
                .build();

        return StoreEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tipo(tipo)
                .dados(storeData)
                .build();
    }
}