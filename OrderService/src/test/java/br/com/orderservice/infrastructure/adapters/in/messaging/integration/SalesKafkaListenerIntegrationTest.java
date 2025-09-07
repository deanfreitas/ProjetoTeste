package br.com.orderservice.infrastructure.adapters.in.messaging.integration;

import br.com.orderservice.application.port.in.OrderUseCase;
import br.com.orderservice.domain.model.Order;
import br.com.orderservice.infrastructure.adapters.in.messaging.dto.SalesEvent;
import br.com.orderservice.infrastructure.adapters.in.messaging.dto.SalesEventItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@DisplayName("SalesKafkaListener Integration Tests")
class SalesKafkaListenerIntegrationTest {

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("apache/kafka"));

    @Autowired
    private OrderUseCase orderUseCase;

    @Autowired
    private KafkaTemplate<String, SalesEvent> kafkaTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.embedded.kafka.brokers", kafkaContainer::getBootstrapServers);
    }

    @Test
    @DisplayName("Should successfully process valid sales event and create order")
    void shouldProcessValidSalesEventAndCreateOrder() {
        // Given
        SalesEvent salesEvent = createValidSalesEvent();

        // When
        kafkaTemplate.send("vendas-test", salesEvent);

        // Then - Wait for message processing
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    List<Order> orders = orderUseCase.findOrdersByCustomerId(salesEvent.getCustomerId());
                    assertFalse(orders.isEmpty());

                    Order createdOrder = orders.get(0);
                    assertEquals(salesEvent.getNumero(), createdOrder.getNumero());
                    assertEquals(salesEvent.getCustomerId(), createdOrder.getCustomerId());
                    assertEquals(salesEvent.getItems().size(), createdOrder.getItems().size());

                    // Verify order items
                    assertEquals(salesEvent.getItems().get(0).getSku(), createdOrder.getItems().get(0).getSku());
                    assertEquals(salesEvent.getItems().get(0).getQuantidade(), createdOrder.getItems().get(0).getQuantity());
                    assertEquals(salesEvent.getItems().get(0).getPrecoCentavos(), createdOrder.getItems().get(0).getPriceCentavos());
                });
    }

    @Test
    @DisplayName("Should handle multiple sales events concurrently")
    void shouldHandleMultipleSalesEventsConcurrently() {
        // Given
        SalesEvent event1 = createValidSalesEvent("CUSTOMER-001", "ORDER-001");
        SalesEvent event2 = createValidSalesEvent("CUSTOMER-002", "ORDER-002");
        SalesEvent event3 = createValidSalesEvent("CUSTOMER-003", "ORDER-003");

        // When
        kafkaTemplate.send("vendas-test", event1);
        kafkaTemplate.send("vendas-test", event2);
        kafkaTemplate.send("vendas-test", event3);

        // Then
        await().atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    List<Order> orders1 = orderUseCase.findOrdersByCustomerId("CUSTOMER-001");
                    List<Order> orders2 = orderUseCase.findOrdersByCustomerId("CUSTOMER-002");
                    List<Order> orders3 = orderUseCase.findOrdersByCustomerId("CUSTOMER-003");

                    assertFalse(orders1.isEmpty());
                    assertFalse(orders2.isEmpty());
                    assertFalse(orders3.isEmpty());

                    assertEquals("ORDER-001", orders1.get(0).getNumero());
                    assertEquals("ORDER-002", orders2.get(0).getNumero());
                    assertEquals("ORDER-003", orders3.get(0).getNumero());
                });
    }

    @Test
    @DisplayName("Should handle sales event with multiple items")
    void shouldHandleSalesEventWithMultipleItems() {
        // Given
        SalesEvent salesEvent = createSalesEventWithMultipleItems();

        // When
        kafkaTemplate.send("vendas-test", salesEvent);

        // Then
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    List<Order> orders = orderUseCase.findOrdersByCustomerId(salesEvent.getCustomerId());
                    assertFalse(orders.isEmpty());

                    Order createdOrder = orders.get(0);
                    assertEquals(3, createdOrder.getItems().size());

                    // Verify all items are present
                    assertTrue(createdOrder.getItems().stream().anyMatch(item -> "PROD-001".equals(item.getSku())));
                    assertTrue(createdOrder.getItems().stream().anyMatch(item -> "PROD-002".equals(item.getSku())));
                    assertTrue(createdOrder.getItems().stream().anyMatch(item -> "PROD-003".equals(item.getSku())));
                });
    }

    @Test
    @DisplayName("Should handle sales event with different store codes")
    void shouldHandleSalesEventWithDifferentStoreCodes() {
        // Given
        SalesEvent eventStore1 = createValidSalesEvent("CUSTOMER-001", "ORDER-001");
        eventStore1.setStoreCode("STORE-001");

        SalesEvent eventStore2 = createValidSalesEvent("CUSTOMER-002", "ORDER-002");
        eventStore2.setStoreCode("STORE-002");

        // When
        kafkaTemplate.send("vendas-test", eventStore1);
        kafkaTemplate.send("vendas-test", eventStore2);

        // Then
        await().atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    List<Order> orders1 = orderUseCase.findOrdersByCustomerId("CUSTOMER-001");
                    List<Order> orders2 = orderUseCase.findOrdersByCustomerId("CUSTOMER-002");

                    assertFalse(orders1.isEmpty());
                    assertFalse(orders2.isEmpty());

                    // Both orders should be created successfully despite different store codes
                    assertNotNull(orders1.get(0).getStoreId());
                    assertNotNull(orders2.get(0).getStoreId());
                });
    }

    @Test
    @DisplayName("Should gracefully handle invalid sales events")
    void shouldGracefullyHandleInvalidSalesEvents() {
        // Given
        SalesEvent invalidEvent = new SalesEvent();
        invalidEvent.setEventId("INVALID-EVENT");
        // Missing required fields

        SalesEvent validEvent = createValidSalesEvent("CUSTOMER-VALID", "ORDER-VALID");

        // When
        kafkaTemplate.send("vendas-test", invalidEvent);
        kafkaTemplate.send("vendas-test", validEvent);

        // Then - Valid event should still be processed
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    List<Order> orders = orderUseCase.findOrdersByCustomerId("CUSTOMER-VALID");
                    assertFalse(orders.isEmpty());
                    assertEquals("ORDER-VALID", orders.get(0).getNumero());
                });
    }

    private SalesEvent createValidSalesEvent() {
        return createValidSalesEvent("CUSTOMER-TEST", "ORDER-TEST");
    }

    private SalesEvent createValidSalesEvent(String customerId, String orderNumber) {
        SalesEvent salesEvent = new SalesEvent();
        salesEvent.setEventId("EVENT-" + System.currentTimeMillis());
        salesEvent.setNumero(orderNumber);
        salesEvent.setCustomerId(customerId);
        salesEvent.setStoreCode("STORE-001");
        salesEvent.setTimestamp(LocalDateTime.now());
        salesEvent.setTotalCentavos(9999L);

        SalesEventItem item = new SalesEventItem();
        item.setSku("PROD-001");
        item.setQuantidade(2L);
        item.setPrecoCentavos(4999L);

        salesEvent.setItems(Collections.singletonList(item));
        return salesEvent;
    }

    private SalesEvent createSalesEventWithMultipleItems() {
        SalesEvent salesEvent = new SalesEvent();
        salesEvent.setEventId("EVENT-MULTI-" + System.currentTimeMillis());
        salesEvent.setNumero("ORDER-MULTI");
        salesEvent.setCustomerId("CUSTOMER-MULTI");
        salesEvent.setStoreCode("STORE-001");
        salesEvent.setTimestamp(LocalDateTime.now());
        salesEvent.setTotalCentavos(29997L);

        SalesEventItem item1 = new SalesEventItem();
        item1.setSku("PROD-001");
        item1.setQuantidade(1L);
        item1.setPrecoCentavos(9999L);

        SalesEventItem item2 = new SalesEventItem();
        item2.setSku("PROD-002");
        item2.setQuantidade(2L);
        item2.setPrecoCentavos(4999L);

        SalesEventItem item3 = new SalesEventItem();
        item3.setSku("PROD-003");
        item3.setQuantidade(1L);
        item3.setPrecoCentavos(10000L);

        salesEvent.setItems(List.of(item1, item2, item3));
        return salesEvent;
    }
}