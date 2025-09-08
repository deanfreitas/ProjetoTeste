package br.com.inventoryservice.infrastructure.adapters.in.messaging.integration;

import br.com.inventoryservice.application.port.in.InventoryEventUseCase;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.ProductData;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.ProductEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Integration tests for ProductKafkaListener using TestContainers.
 * Tests the complete flow from Kafka message consumption to service processing.
 */
@DisplayName("ProductKafkaListener Integration Tests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductKafkaListenerIntegrationTest extends KafkaIntegrationTestBase {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockBean
    private InventoryEventUseCase inventoryEventUseCase;

    @Test
    @DisplayName("Should process valid product creation event successfully")
    void shouldProcessValidProductCreationEvent() {
        // Arrange
        ProductEvent productEvent = createValidProductEvent("CRIAR", "PRODUTO001", "Produto Teste", true);
        String topic = "produtos-test";

        // Act
        kafkaTemplate.send(topic, productEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleProductEvent(
                                    argThat(product ->
                                            "PRODUTO001".equals(product.sku()) &&
                                                    product.eventId() != null
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should process valid product update event successfully")
    void shouldProcessValidProductUpdateEvent() {
        // Arrange
        ProductEvent productEvent = createValidProductEvent("ATUALIZAR", "PRODUTO002", "Produto Atualizado", true);
        String topic = "produtos-test";

        // Act
        kafkaTemplate.send(topic, productEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleProductEvent(
                                    argThat(product ->
                                            "PRODUTO002".equals(product.sku()) &&
                                                    product.eventId() != null
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should process product deactivation event successfully")
    void shouldProcessProductDeactivationEvent() {
        // Arrange
        ProductEvent productEvent = createValidProductEvent("DESATIVAR", "PRODUTO003", "Produto Desativado", false);
        String topic = "produtos-test";

        // Act
        kafkaTemplate.send(topic, productEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleProductEvent(
                                    argThat(product ->
                                            "PRODUTO003".equals(product.sku()) &&
                                                    product.eventId() != null
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    @Test
    @DisplayName("Should handle multiple product events concurrently")
    void shouldHandleMultipleProductEventsConcurrently() {
        // Arrange
        String topic = "produtos-test";
        ProductEvent event1 = createValidProductEvent("CRIAR", "PRODUTO004", "Produto Concorrente 1", true);
        ProductEvent event2 = createValidProductEvent("CRIAR", "PRODUTO005", "Produto Concorrente 2", true);
        ProductEvent event3 = createValidProductEvent("CRIAR", "PRODUTO006", "Produto Concorrente 3", true);

        // Act
        kafkaTemplate.send(topic, event1);
        kafkaTemplate.send(topic, event2);
        kafkaTemplate.send(topic, event3);

        // Assert
        await().atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, times(3))
                            .handleProductEvent(any(), eq(topic), any(Integer.class), any(Long.class));
                });
    }

    @Test
    @DisplayName("Should handle product event with null data gracefully")
    void shouldHandleProductEventWithNullDataGracefully() {
        // Arrange
        ProductEvent productEvent = ProductEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tipo("CRIAR")
                .dados(null)
                .build();
        String topic = "produtos-test";

        // Act
        kafkaTemplate.send(topic, productEvent);

        // Assert - Should not throw exception and should still call the service
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleProductEvent(any(), eq(topic), any(Integer.class), any(Long.class));
                });
    }

    @Test
    @DisplayName("Should process product event with empty SKU")
    void shouldProcessProductEventWithEmptySku() {
        // Arrange
        ProductEvent productEvent = createValidProductEvent("CRIAR", "", "Produto Sem SKU", true);
        String topic = "produtos-test";

        // Act
        kafkaTemplate.send(topic, productEvent);

        // Assert
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(inventoryEventUseCase, atLeastOnce())
                            .handleProductEvent(
                                    argThat(product ->
                                            "".equals(product.sku()) &&
                                                    product.eventId() != null
                                    ),
                                    eq(topic),
                                    any(Integer.class),
                                    any(Long.class)
                            );
                });
    }

    /**
     * Creates a valid ProductEvent for testing purposes.
     */
    private ProductEvent createValidProductEvent(String tipo, String sku, String nome, Boolean ativo) {
        ProductData productData = ProductData.builder()
                .sku(sku)
                .nome(nome)
                .ativo(ativo)
                .build();

        return ProductEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tipo(tipo)
                .dados(productData)
                .build();
    }
}