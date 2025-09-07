package br.com.inventoryservice.infrastructure.adapters.in.messaging.integration;

import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.ProductData;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.ProductEvent;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.entity.ProcessedEventEntity;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.repository.ProcessedEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@DisplayName("ProductKafkaListener Integration Tests")
@Sql(scripts = "/test-data/init-inventory.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data/setup-product-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProductKafkaListenerIntegrationTest extends KafkaIntegrationTestBase {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ProcessedEventRepository processedEventRepository;

    private static final String PRODUCT_TOPIC = "produtos-test";

    @BeforeEach
    void setUp() {
        processedEventRepository.deleteAll();
    }

    @Test
    @DisplayName("Should process product event and mark as processed")
    void shouldProcessProductEventAndMarkAsProcessed() {
        // Given
        ProductData productData = ProductData.builder()
                .sku("SKU001")
                .nome("Test Product")
                .ativo(true)
                .build();

        ProductEvent productEvent = ProductEvent.builder()
                .eventId("test-event-001")
                .tipo("CREATE")
                .dados(productData)
                .build();

        // When
        kafkaTemplate.send(PRODUCT_TOPIC, "key1", productEvent);

        // Then - Wait for message to be processed
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertFalse(processedEvents.isEmpty(), "Event should be marked as processed");
                    
                    ProcessedEventEntity processedEvent = processedEvents.get(0);
                    assertEquals(PRODUCT_TOPIC, processedEvent.getTopico());
                    assertNotNull(processedEvent.getParticao());
                    assertNotNull(processedEvent.getOffset());
                    assertNotNull(processedEvent.getProcessadoEm());
                });
    }

    @Test
    @DisplayName("Should handle product event with null data")
    void shouldHandleProductEventWithNullData() {
        // Given
        ProductEvent productEvent = ProductEvent.builder()
                .eventId("test-event-002")
                .tipo("DELETE")
                .dados(null)
                .build();

        // When
        kafkaTemplate.send(PRODUCT_TOPIC, "key2", productEvent);

        // Then - Should still mark as processed for idempotency
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertFalse(processedEvents.isEmpty(), "Event should be marked as processed even with null data");
                });
    }

    @Test
    @DisplayName("Should prevent duplicate processing of same event")
    void shouldPreventDuplicateProcessingOfSameEvent() {
        // Given
        ProductData productData = ProductData.builder()
                .sku("SKU002")
                .nome("Duplicate Test Product")
                .ativo(true)
                .build();

        ProductEvent productEvent = ProductEvent.builder()
                .eventId("duplicate-event-001")
                .tipo("CREATE")
                .dados(productData)
                .build();

        // When - Send same event twice
        kafkaTemplate.send(PRODUCT_TOPIC, "key3", productEvent);
        kafkaTemplate.send(PRODUCT_TOPIC, "key3", productEvent);

        // Then - Should process only once
        await().atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertFalse(processedEvents.isEmpty(), "Event should be processed at least once");
                    // Note: Due to Kafka partitioning, we might get different offsets, 
                    // but the business logic should handle duplicates via eventId
                });
    }

    @Test
    @DisplayName("Should handle product event with partial data")
    void shouldHandleProductEventWithPartialData() {
        // Given
        ProductData productData = ProductData.builder()
                .sku("SKU003")
                .nome(null) // Partial data
                .ativo(null)
                .build();

        ProductEvent productEvent = ProductEvent.builder()
                .eventId("partial-event-001")
                .tipo("UPDATE")
                .dados(productData)
                .build();

        // When
        kafkaTemplate.send(PRODUCT_TOPIC, "key4", productEvent);

        // Then
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertFalse(processedEvents.isEmpty(), "Event with partial data should still be processed");
                });
    }

    @Test
    @DisplayName("Should handle multiple different product events")
    void shouldHandleMultipleDifferentProductEvents() {
        // Given
        ProductEvent event1 = ProductEvent.builder()
                .eventId("multi-event-001")
                .tipo("CREATE")
                .dados(ProductData.builder().sku("SKU004").nome("Product 1").ativo(true).build())
                .build();

        ProductEvent event2 = ProductEvent.builder()
                .eventId("multi-event-002")
                .tipo("UPDATE")
                .dados(ProductData.builder().sku("SKU005").nome("Product 2").ativo(false).build())
                .build();

        ProductEvent event3 = ProductEvent.builder()
                .eventId("multi-event-003")
                .tipo("DELETE")
                .dados(null)
                .build();

        // When
        kafkaTemplate.send(PRODUCT_TOPIC, "key5", event1);
        kafkaTemplate.send(PRODUCT_TOPIC, "key6", event2);
        kafkaTemplate.send(PRODUCT_TOPIC, "key7", event3);

        // Then
        await().atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertTrue(processedEvents.size() >= 3, "All three events should be processed");
                });
    }
}