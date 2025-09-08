package br.com.inventoryservice.infrastructure.adapters.in.messaging.integration;

import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.ProductData;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.ProductEvent;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.entity.ProductEntity;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Integration tests for ProductKafkaListener using H2 database.
 * Tests the complete flow from Kafka message consumption to service processing.
 */
@DisplayName("ProductKafkaListener Integration Tests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductKafkaListenerIntegrationTest extends KafkaIntegrationTestBase {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void cleanup() {
        productRepository.deleteAll();
    }

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
                    Optional<ProductEntity> savedProduct = productRepository.findById("PRODUTO001");
                    assertThat(savedProduct).isPresent();
                    assertThat(savedProduct.get().getSku()).isEqualTo("PRODUTO001");
                    assertThat(savedProduct.get().getNome()).isEqualTo("Produto Teste");
                    assertThat(savedProduct.get().getAtivo()).isTrue();
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
                    Optional<ProductEntity> savedProduct = productRepository.findById("PRODUTO002");
                    assertThat(savedProduct).isPresent();
                    assertThat(savedProduct.get().getSku()).isEqualTo("PRODUTO002");
                    assertThat(savedProduct.get().getNome()).isEqualTo("Produto Atualizado");
                    assertThat(savedProduct.get().getAtivo()).isTrue();
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
                    Optional<ProductEntity> savedProduct = productRepository.findById("PRODUTO003");
                    assertThat(savedProduct).isPresent();
                    assertThat(savedProduct.get().getSku()).isEqualTo("PRODUTO003");
                    assertThat(savedProduct.get().getNome()).isEqualTo("Produto Desativado");
                    assertThat(savedProduct.get().getAtivo()).isFalse();
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
                    assertThat(productRepository.count()).isEqualTo(3);
                    assertThat(productRepository.findById("PRODUTO004")).isPresent();
                    assertThat(productRepository.findById("PRODUTO005")).isPresent();
                    assertThat(productRepository.findById("PRODUTO006")).isPresent();
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

        // Assert - Should not throw exception and should not save any product
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertThat(productRepository.count()).isEqualTo(0);
                });
    }

    @Test
    @DisplayName("Should process product event with empty sku")
    void shouldProcessProductEventWithEmptySku() {
        // Arrange
        ProductEvent productEvent = ProductEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tipo("CRIAR")
                .dados(ProductData.builder()
                        .sku("")
                        .nome("Produto sem SKU")
                        .ativo(true)
                        .build())
                .build();
        String topic = "produtos-test";

        // Act
        kafkaTemplate.send(topic, productEvent);

        // Assert - Should not save product with empty SKU
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertThat(productRepository.count()).isEqualTo(0);
                });
    }

    private ProductEvent createValidProductEvent(String tipo, String sku, String nome, Boolean ativo) {
        return ProductEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tipo(tipo)
                .dados(ProductData.builder()
                        .sku(sku)
                        .nome(nome)
                        .ativo(ativo)
                        .build())
                .build();
    }
}