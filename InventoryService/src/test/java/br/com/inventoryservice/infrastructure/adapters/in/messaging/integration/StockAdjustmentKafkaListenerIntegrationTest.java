package br.com.inventoryservice.infrastructure.adapters.in.messaging.integration;

import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.StockAdjustmentEvent;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.entity.ProcessedEventEntity;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.entity.StockId;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.repository.ProcessedEventRepository;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.repository.StockAdjustmentRepository;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for StockAdjustmentKafkaListener using TestContainers.
 * Tests the complete flow from Kafka message consumption to service processing.
 */
@DisplayName("StockAdjustmentKafkaListener Integration Tests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "/db/migration/V1__init-inventory.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/migration/setup-product-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/migration/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class StockAdjustmentKafkaListenerIntegrationTest extends KafkaIntegrationTestBase {
    private static final String STOCK_ADJUSTMENT_TOPIC = "ajustes_estoque-test";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ProcessedEventRepository processedEventRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockAdjustmentRepository stockAdjustmentRepository;

    @Test
    @DisplayName("Should process stock adjustment event and update inventory")
    void shouldProcessStockAdjustmentEventAndUpdateInventory() {
        // Given - Initial stock for STORE001/SKU001 is 100 (from setup data)
        StockAdjustmentEvent adjustmentEvent = StockAdjustmentEvent.builder().eventId("adjustment-event-001").loja("STORE001").sku("SKU001").delta(25) // Add 25 units
                .motivo("Inventory recount").timestamp(OffsetDateTime.now()).build();

        // When
        kafkaTemplate.send(STOCK_ADJUSTMENT_TOPIC, "key1", adjustmentEvent);

        // Then - Wait for message to be processed
        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            // Check that event was marked as processed
            var processedEvents = processedEventRepository.findAll();
            assertFalse(processedEvents.isEmpty(), "Stock adjustment event should be marked as processed");

            ProcessedEventEntity processedEvent = processedEvents.get(0);
            assertEquals(STOCK_ADJUSTMENT_TOPIC, processedEvent.getTopico());

            // Check that inventory was adjusted
            var stockId = StockId.builder().lojaCodigo("STORE001").produtoSku("SKU001").build();
            var stock = stockRepository.findById(stockId);

            assertTrue(stock.isPresent(), "Stock for SKU001 should exist");
            assertEquals(125, stock.get().getQuantidade(), "Stock should be increased by 25 (100 + 25)");

            // Check that adjustment record was created
            var adjustments = stockAdjustmentRepository.findAll();
            assertFalse(adjustments.isEmpty(), "Stock adjustment record should be created");
        });
    }

    @Test
    @DisplayName("Should handle negative stock adjustment")
    void shouldHandleNegativeStockAdjustment() {
        // Given
        StockAdjustmentEvent adjustmentEvent = StockAdjustmentEvent.builder().eventId("adjustment-event-002").loja("STORE001").sku("SKU002").delta(-20) // Remove 20 units
                .motivo("Damaged goods").timestamp(OffsetDateTime.now()).build();

        // When
        kafkaTemplate.send(STOCK_ADJUSTMENT_TOPIC, "key2", adjustmentEvent);

        // Then
        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            var processedEvents = processedEventRepository.findAll();
            assertFalse(processedEvents.isEmpty(), "Stock adjustment event should be marked as processed");

            // Check that inventory was decremented
            var stockId = StockId.builder().lojaCodigo("STORE001").produtoSku("SKU002").build();
            var stock = stockRepository.findById(stockId);

            assertTrue(stock.isPresent(), "Stock for SKU002 should exist");
            assertEquals(30, stock.get().getQuantidade(), "Stock should be decreased by 20 (50 - 20)");

            // Check that adjustment record was created
            var adjustments = stockAdjustmentRepository.findAll();
            assertFalse(adjustments.isEmpty(), "Stock adjustment record should be created");
        });
    }

    @Test
    @DisplayName("Should handle zero delta stock adjustment")
    void shouldHandleZeroDeltaStockAdjustment() {
        // Given
        StockAdjustmentEvent adjustmentEvent = StockAdjustmentEvent.builder().eventId("adjustment-event-003").loja("STORE001").sku("SKU001").delta(0) // Zero adjustment
                .motivo("System correction").timestamp(OffsetDateTime.now()).build();

        // When
        kafkaTemplate.send(STOCK_ADJUSTMENT_TOPIC, "key3", adjustmentEvent);

        // Then
        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            var processedEvents = processedEventRepository.findAll();
            assertFalse(processedEvents.isEmpty(), "Stock adjustment event should be marked as processed");

            // Check that inventory remains unchanged
            var stockId = StockId.builder().lojaCodigo("STORE001").produtoSku("SKU001").build();
            var stock = stockRepository.findById(stockId);

            assertTrue(stock.isPresent(), "Stock for SKU001 should exist");
            assertEquals(100, stock.get().getQuantidade(), "Stock should remain unchanged for zero delta");

            // Check that adjustment record was still created
            var adjustments = stockAdjustmentRepository.findAll();
            assertFalse(adjustments.isEmpty(), "Stock adjustment record should be created even for zero delta");
        });
    }

    @Test
    @DisplayName("Should prevent duplicate processing of same adjustment event")
    void shouldPreventDuplicateProcessingOfSameAdjustmentEvent() {
        // Given
        StockAdjustmentEvent adjustmentEvent = StockAdjustmentEvent.builder().eventId("duplicate-adjustment-001").loja("STORE001").sku("SKU001").delta(10).motivo("Duplicate test").timestamp(OffsetDateTime.now()).build();

        // When - Send same event twice
        kafkaTemplate.send(STOCK_ADJUSTMENT_TOPIC, "key4", adjustmentEvent);
        kafkaTemplate.send(STOCK_ADJUSTMENT_TOPIC, "key4", adjustmentEvent);

        // Then - Should process only once
        Awaitility.await().atMost(15, TimeUnit.SECONDS).untilAsserted(() -> {
            var processedEvents = processedEventRepository.findAll();
            assertFalse(processedEvents.isEmpty(), "Adjustment event should be processed at least once");

            // Verify inventory was adjusted only once (should be 110, not 120)
            var stockId = StockId.builder().lojaCodigo("STORE001").produtoSku("SKU001").build();
            var stock = stockRepository.findById(stockId);
            assertTrue(stock.isPresent());
            assertEquals(110, stock.get().getQuantidade(), "Inventory should be adjusted only once");
        });
    }

    @Test
    @DisplayName("Should ignore adjustment for non-existing store")
    void shouldIgnoreAdjustmentForNonExistingStore() {
        // Given
        StockAdjustmentEvent adjustmentEvent = StockAdjustmentEvent.builder().eventId("non-existing-store-adjustment-001").loja("NON_EXISTING_STORE").sku("SKU001").delta(50).motivo("Invalid store test").timestamp(OffsetDateTime.now()).build();

        // When
        kafkaTemplate.send(STOCK_ADJUSTMENT_TOPIC, "key5", adjustmentEvent);

        // Then - Should mark as processed but no inventory changes
        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            var processedEvents = processedEventRepository.findAll();
            assertFalse(processedEvents.isEmpty(), "Adjustment event should be marked as processed");

            // Verify no inventory changes occurred for existing store
            var stockId = StockId.builder().lojaCodigo("STORE001").produtoSku("SKU001").build();
            var stock = stockRepository.findById(stockId);
            assertTrue(stock.isPresent());
            assertEquals(100, stock.get().getQuantidade(), "Inventory should remain unchanged for valid store");

            // Should not create adjustment record for invalid store
            var adjustments = stockAdjustmentRepository.findAll();
            assertTrue(adjustments.isEmpty(), "No adjustment record should be created for invalid store");
        });
    }

    @Test
    @DisplayName("Should ignore adjustment for non-existing product")
    void shouldIgnoreAdjustmentForNonExistingProduct() {
        // Given
        StockAdjustmentEvent adjustmentEvent = StockAdjustmentEvent.builder().eventId("non-existing-product-adjustment-001").loja("STORE001").sku("NON_EXISTING_SKU").delta(50).motivo("Invalid product test").timestamp(OffsetDateTime.now()).build();

        // When
        kafkaTemplate.send(STOCK_ADJUSTMENT_TOPIC, "key6", adjustmentEvent);

        // Then - Should mark as processed but no inventory changes
        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            var processedEvents = processedEventRepository.findAll();
            assertFalse(processedEvents.isEmpty(), "Adjustment event should be marked as processed");

            // Should not create adjustment record for invalid product
            var adjustments = stockAdjustmentRepository.findAll();
            assertTrue(adjustments.isEmpty(), "No adjustment record should be created for invalid product");
        });
    }

    @Test
    @DisplayName("Should ignore adjustment for inactive product")
    void shouldIgnoreAdjustmentForInactiveProduct() {
        // Given - SKU003 is inactive from setup data
        StockAdjustmentEvent adjustmentEvent = StockAdjustmentEvent.builder().eventId("inactive-product-adjustment-001").loja("STORE001").sku("SKU003") // Inactive product
                .delta(25).motivo("Inactive product test").timestamp(OffsetDateTime.now()).build();

        // When
        kafkaTemplate.send(STOCK_ADJUSTMENT_TOPIC, "key7", adjustmentEvent);

        // Then - Should mark as processed but no adjustment should occur
        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            var processedEvents = processedEventRepository.findAll();
            assertFalse(processedEvents.isEmpty(), "Adjustment event should be marked as processed");

            // Should not create adjustment record or stock entry for inactive product
            var adjustments = stockAdjustmentRepository.findAll();
            assertTrue(adjustments.isEmpty(), "No adjustment record should be created for inactive product");

            // Should not create stock entry
            var stockId = StockId.builder().lojaCodigo("STORE001").produtoSku("SKU003").build();
            var stock = stockRepository.findById(stockId);
            assertFalse(stock.isPresent(), "No stock entry should be created for inactive product");
        });
    }

    @Test
    @DisplayName("Should handle adjustment with null event data")
    void shouldHandleAdjustmentWithNullEventData() {
        // Given
        StockAdjustmentEvent adjustmentEvent = StockAdjustmentEvent.builder().eventId("null-data-adjustment-001").loja(null) // Null store
                .sku(null)  // Null SKU
                .delta(10).motivo("Null data test").timestamp(OffsetDateTime.now()).build();

        // When
        kafkaTemplate.send(STOCK_ADJUSTMENT_TOPIC, "key8", adjustmentEvent);

        // Then - Should mark as processed but no adjustments
        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            var processedEvents = processedEventRepository.findAll();
            assertFalse(processedEvents.isEmpty(), "Adjustment event should be marked as processed even with null data");

            // Should not create any adjustment records
            var adjustments = stockAdjustmentRepository.findAll();
            assertTrue(adjustments.isEmpty(), "No adjustment record should be created for null data");
        });
    }
}