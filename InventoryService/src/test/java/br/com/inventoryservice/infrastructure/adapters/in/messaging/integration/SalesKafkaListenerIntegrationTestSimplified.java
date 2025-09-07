package br.com.inventoryservice.infrastructure.adapters.in.messaging.integration;

import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.SalesItem;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.SalesEvent;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.entity.ProcessedEventEntity;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.entity.StockId;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.repository.ProcessedEventRepository;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@DisplayName("SalesKafkaListener Integration Tests (Simplified)")
@Sql(scripts = "/test-data/init-inventory.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data/setup-product-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SalesKafkaListenerIntegrationTestSimplified extends KafkaIntegrationTestBase {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ProcessedEventRepository processedEventRepository;

    @Autowired
    private StockRepository stockRepository;

    private static final String SALES_TOPIC = "vendas-test";

    @BeforeEach
    void setUp() {
        processedEventRepository.deleteAll();
    }

    @Test
    @DisplayName("Should process sales event and decrease inventory")
    void shouldProcessSalesEventAndDecreaseInventory() {
        // Given - Initial stock for STORE001/SKU001 is 100 (from setup data)
        List<SalesItem> items = Collections.singletonList(
                SalesItem.builder().sku("SKU001").quantidade(10).build()
        );

        SalesEvent salesEvent = SalesEvent.builder()
                .eventId("sales-event-001")
                .loja("STORE001")
                .itens(items)
                .build();

        // When
        kafkaTemplate.send(SALES_TOPIC, "key1", salesEvent);

        // Then - Wait for message to be processed
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // Check that event was marked as processed
                    var processedEvents = processedEventRepository.findAll();
                    assertFalse(processedEvents.isEmpty(), "Sales event should be marked as processed");

                    ProcessedEventEntity processedEvent = processedEvents.get(0);
                    assertEquals(SALES_TOPIC, processedEvent.getTopico());
                    
                    // Check that inventory was decremented
                    var stockId001 = StockId.builder().lojaCodigo("STORE001").produtoSku("SKU001").build();
                    var stockSku001 = stockRepository.findById(stockId001);
                    
                    assertTrue(stockSku001.isPresent(), "Stock for SKU001 should exist");
                    assertEquals(90, stockSku001.get().getQuantidade(), "SKU001 stock should be decremented by 10");
                });
    }

    @Test
    @DisplayName("Should handle sales event with null items")
    void shouldHandleSalesEventWithNullItems() {
        // Given
        SalesEvent salesEvent = SalesEvent.builder()
                .eventId("sales-event-002")
                .loja("STORE001")
                .itens(null)
                .build();

        // When
        kafkaTemplate.send(SALES_TOPIC, "key2", salesEvent);

        // Then - Should still mark as processed but no inventory changes
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertFalse(processedEvents.isEmpty(), "Sales event should be marked as processed even with null items");
                    
                    // Verify no inventory changes occurred
                    var stockId001 = StockId.builder().lojaCodigo("STORE001").produtoSku("SKU001").build();
                    var stockSku001 = stockRepository.findById(stockId001);
                    assertTrue(stockSku001.isPresent());
                    assertEquals(100, stockSku001.get().getQuantidade(), "Inventory should remain unchanged");
                });
    }

    @Test
    @DisplayName("Should prevent duplicate processing of same sales event")
    void shouldPreventDuplicateProcessingOfSameSalesEvent() {
        // Given
        List<SalesItem> items = Collections.singletonList(
                SalesItem.builder().sku("SKU001").quantidade(5).build()
        );

        SalesEvent salesEvent = SalesEvent.builder()
                .eventId("duplicate-sales-event-001")
                .loja("STORE001")
                .itens(items)
                .build();

        // When - Send same event twice
        kafkaTemplate.send(SALES_TOPIC, "key3", salesEvent);
        kafkaTemplate.send(SALES_TOPIC, "key3", salesEvent);

        // Then - Should process only once
        await().atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertFalse(processedEvents.isEmpty(), "Sales event should be processed at least once");
                    
                    // Verify inventory was decremented only once (should be 95, not 90)
                    var stockId001 = StockId.builder().lojaCodigo("STORE001").produtoSku("SKU001").build();
                    var stockSku001 = stockRepository.findById(stockId001);
                    assertTrue(stockSku001.isPresent());
                    assertEquals(95, stockSku001.get().getQuantidade(), "Inventory should be decremented only once");
                });
    }

    @Test
    @DisplayName("Should ignore sales for non-existing store")
    void shouldIgnoreSalesForNonExistingStore() {
        // Given
        List<SalesItem> items = Collections.singletonList(
                SalesItem.builder().sku("SKU001").quantidade(10).build()
        );

        SalesEvent salesEvent = SalesEvent.builder()
                .eventId("non-existing-store-event-001")
                .loja("NON_EXISTING_STORE")
                .itens(items)
                .build();

        // When
        kafkaTemplate.send(SALES_TOPIC, "key4", salesEvent);

        // Then - Should mark as processed but no inventory changes
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertFalse(processedEvents.isEmpty(), "Sales event should be marked as processed");
                    
                    // Verify no inventory changes occurred for existing store
                    var stockId001 = StockId.builder().lojaCodigo("STORE001").produtoSku("SKU001").build();
                    var stockSku001 = stockRepository.findById(stockId001);
                    assertTrue(stockSku001.isPresent());
                    assertEquals(100, stockSku001.get().getQuantidade(), "Inventory should remain unchanged for valid store");
                });
    }

    @Test
    @DisplayName("Should handle multiple sales events for different stores")
    void shouldHandleMultipleSalesEventsForDifferentStores() {
        // Given
        SalesEvent store1Event = SalesEvent.builder()
                .eventId("multi-store-sales-001")
                .loja("STORE001")
                .itens(Collections.singletonList(SalesItem.builder().sku("SKU001").quantidade(10).build()))
                .build();

        SalesEvent store2Event = SalesEvent.builder()
                .eventId("multi-store-sales-002")
                .loja("STORE002")
                .itens(Collections.singletonList(SalesItem.builder().sku("SKU001").quantidade(15).build()))
                .build();

        // When
        kafkaTemplate.send(SALES_TOPIC, "key7", store1Event);
        kafkaTemplate.send(SALES_TOPIC, "key8", store2Event);

        // Then - Both stores should have inventory decremented
        await().atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var processedEvents = processedEventRepository.findAll();
                    assertTrue(processedEvents.size() >= 2, "Both sales events should be processed");
                    
                    // Check both stores
                    var store1StockId = StockId.builder().lojaCodigo("STORE001").produtoSku("SKU001").build();
                    var store2StockId = StockId.builder().lojaCodigo("STORE002").produtoSku("SKU001").build();
                    var store1Stock = stockRepository.findById(store1StockId);
                    var store2Stock = stockRepository.findById(store2StockId);
                    
                    assertTrue(store1Stock.isPresent());
                    assertTrue(store2Stock.isPresent());
                    
                    assertEquals(90, store1Stock.get().getQuantidade(), "STORE001 should have 90 units");
                    assertEquals(60, store2Stock.get().getQuantidade(), "STORE002 should have 60 units (75-15)");
                });
    }
}