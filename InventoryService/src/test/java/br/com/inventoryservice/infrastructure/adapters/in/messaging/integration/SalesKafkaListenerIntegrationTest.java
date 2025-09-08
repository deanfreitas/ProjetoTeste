package br.com.inventoryservice.infrastructure.adapters.in.messaging.integration;

import br.com.inventoryservice.application.port.in.InventoryEventUseCase;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.SalesItem;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.SalesEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Integration tests for SalesKafkaListener using TestContainers.
 * Tests the complete flow from Kafka message consumption to service processing.
 */
@DisplayName("SalesKafkaListener Integration Tests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SalesKafkaListenerIntegrationTest extends KafkaIntegrationTestBase {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private InventoryEventUseCase inventoryEventUseCase;

    @Test
    @DisplayName("Should process valid sales event successfully")
    void shouldProcessValidSalesEvent() {
        // Arrange
        SalesEvent salesEvent = createValidSalesEvent("VENDA", "LOJA001", "PEDIDO001");
        String topic = "vendas-test";

        // Act
        kafkaTemplate.send(topic, salesEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleSalesEvent(
                                    argThat(sales ->
                                            "LOJA001".equals(sales.loja()) &&
                                                    sales.eventId() != null &&
                                                    sales.itens() != null &&
                                                    sales.itens().size() == 2
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should process sales event with single item")
    void shouldProcessSalesEventWithSingleItem() {
        // Arrange
        SalesEvent salesEvent = createSalesEventWithSingleItem("VENDA", "LOJA002", "PEDIDO002", "PRODUTO001", 5);
        String topic = "vendas-test";

        // Act
        kafkaTemplate.send(topic, salesEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleSalesEvent(
                                    argThat(sales ->
                                            "LOJA002".equals(sales.loja()) &&
                                                    sales.eventId() != null &&
                                                    sales.itens() != null &&
                                                    sales.itens().size() == 1 &&
                                                    "PRODUTO001".equals(sales.itens().get(0).sku()) &&
                                                    sales.itens().get(0).quantidade().equals(5)
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should handle multiple sales events concurrently")
    void shouldHandleMultipleSalesEventsConcurrently() {
        // Arrange
        String topic = "vendas-test";
        SalesEvent event1 = createValidSalesEvent("VENDA", "LOJA003", "PEDIDO003");
        SalesEvent event2 = createValidSalesEvent("VENDA", "LOJA004", "PEDIDO004");
        SalesEvent event3 = createValidSalesEvent("VENDA", "LOJA005", "PEDIDO005");

        // Act
        kafkaTemplate.send(topic, event1);
        kafkaTemplate.send(topic, event2);
        kafkaTemplate.send(topic, event3);

        // Assert
        await().atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, times(3))
                            .handleSalesEvent(any(), eq(topic), any(Integer.class), any(Long.class));
                });
    }

    @Test
    @DisplayName("Should handle sales event with empty items list")
    void shouldHandleSalesEventWithEmptyItems() {
        // Arrange
        SalesEvent salesEvent = createSalesEventWithEmptyItems("VENDA", "LOJA006", "PEDIDO006");
        String topic = "vendas-test";

        // Act
        kafkaTemplate.send(topic, salesEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleSalesEvent(
                                    argThat(sales ->
                                            "LOJA006".equals(sales.loja()) &&
                                                    sales.eventId() != null &&
                                                    sales.itens() != null &&
                                                    sales.itens().isEmpty()
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should handle sales event with different store codes")
    void shouldHandleSalesEventWithDifferentStoreCodes() {
        // Arrange
        SalesEvent salesEvent1 = createValidSalesEvent("VENDA", "STORE_A", "ORDER_A");
        SalesEvent salesEvent2 = createValidSalesEvent("VENDA", "STORE_B", "ORDER_B");
        String topic = "vendas-test";

        // Act
        kafkaTemplate.send(topic, salesEvent1);
        kafkaTemplate.send(topic, salesEvent2);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleSalesEvent(
                                    argThat(sales -> "STORE_A".equals(sales.loja())),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleSalesEvent(
                                    argThat(sales -> "STORE_B".equals(sales.loja())),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should handle sales cancellation event")
    void shouldHandleSalesCancellationEvent() {
        // Arrange
        SalesEvent salesEvent = createValidSalesEvent("CANCELAMENTO", "LOJA007", "PEDIDO007");
        String topic = "vendas-test";

        // Act
        kafkaTemplate.send(topic, salesEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleSalesEvent(
                                    argThat(sales ->
                                            "LOJA007".equals(sales.loja()) &&
                                                    sales.eventId() != null
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    /**
     * Creates a valid SalesEvent for testing purposes with multiple items.
     */
    private SalesEvent createValidSalesEvent(String tipo, String loja, String pedidoId) {
        SalesItem item1 = SalesItem.builder()
                .sku("PRODUTO001")
                .quantidade(2)
                .build();

        SalesItem item2 = SalesItem.builder()
                .sku("PRODUTO002")
                .quantidade(1)
                .build();

        return SalesEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tipo(tipo)
                .timestamp(OffsetDateTime.now())
                .loja(loja)
                .pedidoId(pedidoId)
                .itens(List.of(item1, item2))
                .build();
    }

    /**
     * Creates a SalesEvent with a single item for testing purposes.
     */
    private SalesEvent createSalesEventWithSingleItem(String tipo, String loja, String pedidoId, String sku, Integer quantidade) {
        SalesItem item = SalesItem.builder()
                .sku(sku)
                .quantidade(quantidade)
                .build();

        return SalesEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tipo(tipo)
                .timestamp(OffsetDateTime.now())
                .loja(loja)
                .pedidoId(pedidoId)
                .itens(List.of(item))
                .build();
    }

    /**
     * Creates a SalesEvent with empty items list for testing purposes.
     */
    private SalesEvent createSalesEventWithEmptyItems(String tipo, String loja, String pedidoId) {
        return SalesEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tipo(tipo)
                .timestamp(OffsetDateTime.now())
                .loja(loja)
                .pedidoId(pedidoId)
                .itens(List.of())
                .build();
    }
}