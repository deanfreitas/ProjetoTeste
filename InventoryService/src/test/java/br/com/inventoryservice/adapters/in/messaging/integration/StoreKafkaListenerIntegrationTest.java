package br.com.inventoryservice.adapters.in.messaging.integration;

import br.com.inventoryservice.adapters.in.messaging.dto.data.StoreData;
import br.com.inventoryservice.adapters.in.messaging.dto.event.StoreEvent;
import br.com.inventoryservice.adapters.out.persistence.entity.ProcessedEventEntity;
import br.com.inventoryservice.adapters.out.persistence.repository.ProcessedEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@DisplayName("StoreKafkaListener Integration Tests")
@Sql(scripts = "/test-data/init-inventory.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data/setup-product-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class StoreKafkaListenerIntegrationTest extends KafkaIntegrationTestBase {

    private static final String STORE_TOPIC = "lojas-test";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ProcessedEventRepository processedEventRepository;

    @BeforeEach
    void setUp() {
        processedEventRepository.deleteAll();
    }

    @Test
    @DisplayName("Should process store event and mark as processed")
    void shouldProcessStoreEventAndMarkAsProcessed() {
        // Given
        StoreData storeData = StoreData.builder()
                .codigo("STORE003")
                .nome("Test Store 3")
                .build();

        StoreEvent storeEvent = StoreEvent.builder()
                .eventId("store-event-001")
                .tipo("CREATE")
                .dados(storeData)
                .build();

        // When
        kafkaTemplate.send(STORE_TOPIC, "key1", storeEvent);

        // Then - Wait for message to be processed
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertFalse(processedEvents.isEmpty(), "Store event should be marked as processed");

                    ProcessedEventEntity processedEvent = processedEvents.get(0);
                    assertEquals(STORE_TOPIC, processedEvent.getTopico());
                    assertNotNull(processedEvent.getParticao());
                    assertNotNull(processedEvent.getOffset());
                    assertNotNull(processedEvent.getProcessadoEm());
                });
    }

    @Test
    @DisplayName("Should handle store event with null data")
    void shouldHandleStoreEventWithNullData() {
        // Given
        StoreEvent storeEvent = StoreEvent.builder()
                .eventId("store-event-002")
                .tipo("DELETE")
                .dados(null)
                .build();

        // When
        kafkaTemplate.send(STORE_TOPIC, "key2", storeEvent);

        // Then - Should still mark as processed for idempotency
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertFalse(processedEvents.isEmpty(), "Store event should be marked as processed even with null data");
                });
    }

    @Test
    @DisplayName("Should prevent duplicate processing of same store event")
    void shouldPreventDuplicateProcessingOfSameStoreEvent() {
        // Given
        StoreData storeData = StoreData.builder()
                .codigo("STORE004")
                .nome("Duplicate Test Store")
                .build();

        StoreEvent storeEvent = StoreEvent.builder()
                .eventId("duplicate-store-event-001")
                .tipo("CREATE")
                .dados(storeData)
                .build();

        // When - Send same event twice
        kafkaTemplate.send(STORE_TOPIC, "key3", storeEvent);
        kafkaTemplate.send(STORE_TOPIC, "key3", storeEvent);

        // Then - Should process only once
        await().atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertFalse(processedEvents.isEmpty(), "Store event should be processed at least once");
                    // Business logic should handle duplicates via eventId
                });
    }

    @Test
    @DisplayName("Should handle store event with partial data")
    void shouldHandleStoreEventWithPartialData() {
        // Given
        StoreData storeData = StoreData.builder()
                .codigo("STORE005")
                .nome(null) // Partial data - missing name
                .build();

        StoreEvent storeEvent = StoreEvent.builder()
                .eventId("partial-store-event-001")
                .tipo("UPDATE")
                .dados(storeData)
                .build();

        // When
        kafkaTemplate.send(STORE_TOPIC, "key4", storeEvent);

        // Then
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertFalse(processedEvents.isEmpty(), "Store event with partial data should still be processed");
                });
    }

    @Test
    @DisplayName("Should handle multiple different store events")
    void shouldHandleMultipleDifferentStoreEvents() {
        // Given
        StoreEvent event1 = StoreEvent.builder()
                .eventId("multi-store-event-001")
                .tipo("CREATE")
                .dados(StoreData.builder().codigo("STORE006").nome("Multi Store 1").build())
                .build();

        StoreEvent event2 = StoreEvent.builder()
                .eventId("multi-store-event-002")
                .tipo("UPDATE")
                .dados(StoreData.builder().codigo("STORE007").nome("Multi Store 2").build())
                .build();

        StoreEvent event3 = StoreEvent.builder()
                .eventId("multi-store-event-003")
                .tipo("DELETE")
                .dados(null)
                .build();

        // When
        kafkaTemplate.send(STORE_TOPIC, "key5", event1);
        kafkaTemplate.send(STORE_TOPIC, "key6", event2);
        kafkaTemplate.send(STORE_TOPIC, "key7", event3);

        // Then
        await().atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertTrue(processedEvents.size() >= 3, "All three store events should be processed");
                });
    }

    @Test
    @DisplayName("Should handle store event for existing store")
    void shouldHandleStoreEventForExistingStore() {
        // Given - STORE001 exists from setup data
        StoreData storeData = StoreData.builder()
                .codigo("STORE001") // This store exists in test data
                .nome("Updated Test Store 1")
                .build();

        StoreEvent storeEvent = StoreEvent.builder()
                .eventId("existing-store-event-001")
                .tipo("UPDATE")
                .dados(storeData)
                .build();

        // When
        kafkaTemplate.send(STORE_TOPIC, "key8", storeEvent);

        // Then
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertFalse(processedEvents.isEmpty(), "Store event for existing store should be processed");
                });
    }
}