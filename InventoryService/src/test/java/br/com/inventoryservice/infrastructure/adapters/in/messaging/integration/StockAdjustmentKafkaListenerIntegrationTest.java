package br.com.inventoryservice.infrastructure.adapters.in.messaging.integration;

import br.com.inventoryservice.application.port.in.InventoryEventUseCase;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.StockAdjustmentEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Integration tests for StockAdjustmentKafkaListener using TestContainers.
 * Tests the complete flow from Kafka message consumption to service processing.
 */
@DisplayName("StockAdjustmentKafkaListener Integration Tests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StockAdjustmentKafkaListenerIntegrationTest extends KafkaIntegrationTestBase {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private InventoryEventUseCase inventoryEventUseCase;

    @Test
    @DisplayName("Should process positive stock adjustment successfully")
    void shouldProcessPositiveStockAdjustment() {
        // Arrange
        StockAdjustmentEvent adjustmentEvent = createStockAdjustmentEvent(
                "AJUSTE_APLICADO", "LOJA001", "PRODUTO001", 10, "Reposição de estoque"
        );
        String topic = "ajustes_estoque-test";

        // Act
        kafkaTemplate.send(topic, adjustmentEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStockAdjustmentEvent(
                                    argThat(adjustment ->
                                            "LOJA001".equals(adjustment.loja()) &&
                                                    "PRODUTO001".equals(adjustment.sku()) &&
                                                    adjustment.delta().equals(10) &&
                                                    "Reposição de estoque".equals(adjustment.motivo()) &&
                                                    adjustment.eventId() != null
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should process negative stock adjustment successfully")
    void shouldProcessNegativeStockAdjustment() {
        // Arrange
        StockAdjustmentEvent adjustmentEvent = createStockAdjustmentEvent(
                "AJUSTE_APLICADO", "LOJA002", "PRODUTO002", -5, "Produto danificado"
        );
        String topic = "ajustes_estoque-test";

        // Act
        kafkaTemplate.send(topic, adjustmentEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStockAdjustmentEvent(
                                    argThat(adjustment ->
                                            "LOJA002".equals(adjustment.loja()) &&
                                                    "PRODUTO002".equals(adjustment.sku()) &&
                                                    adjustment.delta().equals(-5) &&
                                                    "Produto danificado".equals(adjustment.motivo()) &&
                                                    adjustment.eventId() != null
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should process stock adjustment without reason")
    void shouldProcessStockAdjustmentWithoutReason() {
        // Arrange
        StockAdjustmentEvent adjustmentEvent = createStockAdjustmentEvent(
                "AJUSTE_APLICADO", "LOJA003", "PRODUTO003", 3, null
        );
        String topic = "ajustes_estoque-test";

        // Act
        kafkaTemplate.send(topic, adjustmentEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStockAdjustmentEvent(
                                    argThat(adjustment ->
                                            "LOJA003".equals(adjustment.loja()) &&
                                                    "PRODUTO003".equals(adjustment.sku()) &&
                                                    adjustment.delta().equals(3) &&
                                                    adjustment.motivo() == null &&
                                                    adjustment.eventId() != null
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should handle multiple stock adjustments concurrently")
    void shouldHandleMultipleStockAdjustmentsConcurrently() {
        // Arrange
        String topic = "ajustes_estoque-test";
        StockAdjustmentEvent event1 = createStockAdjustmentEvent("AJUSTE_APLICADO", "LOJA004", "PRODUTO004", 15, "Inventário");
        StockAdjustmentEvent event2 = createStockAdjustmentEvent("AJUSTE_APLICADO", "LOJA005", "PRODUTO005", -8, "Perda");
        StockAdjustmentEvent event3 = createStockAdjustmentEvent("AJUSTE_APLICADO", "LOJA006", "PRODUTO006", 22, "Transferência");

        // Act
        kafkaTemplate.send(topic, event1);
        kafkaTemplate.send(topic, event2);
        kafkaTemplate.send(topic, event3);

        // Assert
        await().atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, times(3))
                            .handleStockAdjustmentEvent(any(), eq(topic), any(Integer.class), any(Long.class));
                });
    }

    @Test
    @DisplayName("Should handle stock adjustment with zero delta")
    void shouldHandleStockAdjustmentWithZeroDelta() {
        // Arrange
        StockAdjustmentEvent adjustmentEvent = createStockAdjustmentEvent(
                "AJUSTE_APLICADO", "LOJA007", "PRODUTO007", 0, "Correção de contagem"
        );
        String topic = "ajustes_estoque-test";

        // Act
        kafkaTemplate.send(topic, adjustmentEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStockAdjustmentEvent(
                                    argThat(adjustment ->
                                            "LOJA007".equals(adjustment.loja()) &&
                                                    "PRODUTO007".equals(adjustment.sku()) &&
                                                    adjustment.delta().equals(0) &&
                                                    "Correção de contagem".equals(adjustment.motivo())
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should handle large positive stock adjustment")
    void shouldHandleLargePositiveStockAdjustment() {
        // Arrange
        StockAdjustmentEvent adjustmentEvent = createStockAdjustmentEvent(
                "AJUSTE_APLICADO", "LOJA008", "PRODUTO008", 1000, "Reposição massiva"
        );
        String topic = "ajustes_estoque-test";

        // Act
        kafkaTemplate.send(topic, adjustmentEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStockAdjustmentEvent(
                                    argThat(adjustment ->
                                            "LOJA008".equals(adjustment.loja()) &&
                                                    "PRODUTO008".equals(adjustment.sku()) &&
                                                    adjustment.delta().equals(1000) &&
                                                    adjustment.eventId() != null
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should handle different adjustment types")
    void shouldHandleDifferentAdjustmentTypes() {
        // Arrange
        StockAdjustmentEvent inventoryEvent = createStockAdjustmentEvent(
                "INVENTARIO", "LOJA009", "PRODUTO009", 50, "Contagem física"
        );
        StockAdjustmentEvent correctionEvent = createStockAdjustmentEvent(
                "CORRECAO", "LOJA010", "PRODUTO010", -12, "Correção de erro"
        );
        String topic = "ajustes_estoque-test";

        // Act
        kafkaTemplate.send(topic, inventoryEvent);
        kafkaTemplate.send(topic, correctionEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStockAdjustmentEvent(
                                    argThat(adjustment ->
                                            "LOJA009".equals(adjustment.loja()) &&
                                                    adjustment.delta().equals(50)
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleStockAdjustmentEvent(
                                    argThat(adjustment ->
                                            "LOJA010".equals(adjustment.loja()) &&
                                                    adjustment.delta().equals(-12)
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    /**
     * Creates a StockAdjustmentEvent for testing purposes.
     */
    private StockAdjustmentEvent createStockAdjustmentEvent(String tipo, String loja, String sku, Integer delta, String motivo) {
        return StockAdjustmentEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tipo(tipo)
                .timestamp(OffsetDateTime.now())
                .loja(loja)
                .sku(sku)
                .delta(delta)
                .motivo(motivo)
                .build();
    }
}