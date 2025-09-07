package br.com.orderservice.infrastructure.adapters.in.web.integration;

import br.com.orderservice.application.port.in.OrderUseCase;
import br.com.orderservice.domain.enuns.OrderStatus;
import br.com.orderservice.domain.model.Order;
import br.com.orderservice.domain.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@Testcontainers
@DisplayName("OrderController Integration Tests")
class OrderControllerIntegrationTest {

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("apache/kafka"));

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private OrderUseCase orderUseCase;

    private MockMvc mockMvc;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.embedded.kafka.brokers", kafkaContainer::getBootstrapServers);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Should return order when valid ID is provided")
    void shouldReturnOrderWhenValidIdProvided() throws Exception {
        // Given
        Order testOrder = createTestOrder();
        Order savedOrder = orderUseCase.createOrder(testOrder);

        // When & Then
        mockMvc.perform(get("/api/orders/{id}", savedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(savedOrder.getId().intValue())))
                .andExpect(jsonPath("$.numero", is(testOrder.getNumero())))
                .andExpect(jsonPath("$.customerId", is(testOrder.getCustomerId())))
                .andExpect(jsonPath("$.storeId", is(testOrder.getStoreId().intValue())))
                .andExpect(jsonPath("$.status", is(testOrder.getStatus().toString())))
                .andExpect(jsonPath("$.items", hasSize(testOrder.getItems().size())));
    }

    @Test
    @DisplayName("Should return 404 when order ID does not exist")
    void shouldReturn404WhenOrderIdDoesNotExist() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/orders/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return orders by customer ID")
    void shouldReturnOrdersByCustomerId() throws Exception {
        // Given
        String customerId = "CUSTOMER-TEST-123";
        Order order1 = createTestOrderForCustomer(customerId, "ORDER-001");
        Order order2 = createTestOrderForCustomer(customerId, "ORDER-002");

        orderUseCase.createOrder(order1);
        orderUseCase.createOrder(order2);

        // When & Then
        mockMvc.perform(get("/api/orders")
                        .param("customerId", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].customerId", everyItem(is(customerId))))
                .andExpect(jsonPath("$[*].numero", containsInAnyOrder("ORDER-001", "ORDER-002")));
    }

    @Test
    @DisplayName("Should return empty list when no orders found for customer")
    void shouldReturnEmptyListWhenNoOrdersFoundForCustomer() throws Exception {
        // Given
        String nonExistentCustomerId = "CUSTOMER-NONEXISTENT";

        // When & Then
        mockMvc.perform(get("/api/orders")
                        .param("customerId", nonExistentCustomerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should handle multiple customers with different orders")
    void shouldHandleMultipleCustomersWithDifferentOrders() throws Exception {
        // Given
        String customer1 = "CUSTOMER-001";
        String customer2 = "CUSTOMER-002";

        Order order1 = createTestOrderForCustomer(customer1, "ORDER-C1-001");
        Order order2 = createTestOrderForCustomer(customer1, "ORDER-C1-002");
        Order order3 = createTestOrderForCustomer(customer2, "ORDER-C2-001");

        orderUseCase.createOrder(order1);
        orderUseCase.createOrder(order2);
        orderUseCase.createOrder(order3);

        // When & Then - Customer 1 should have 2 orders
        mockMvc.perform(get("/api/orders")
                        .param("customerId", customer1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].customerId", everyItem(is(customer1))));

        // When & Then - Customer 2 should have 1 order
        mockMvc.perform(get("/api/orders")
                        .param("customerId", customer2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerId", is(customer2)))
                .andExpect(jsonPath("$[0].numero", is("ORDER-C2-001")));
    }

    @Test
    @DisplayName("Should return order with complex item structure")
    void shouldReturnOrderWithComplexItemStructure() throws Exception {
        // Given
        Order complexOrder = createComplexTestOrder();
        Order savedOrder = orderUseCase.createOrder(complexOrder);

        // When & Then
        mockMvc.perform(get("/api/orders/{id}", savedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[*].sku", containsInAnyOrder("SKU-001", "SKU-002", "SKU-003")))
                .andExpect(jsonPath("$.items[*].quantity", containsInAnyOrder(1, 2, 3)))
                .andExpect(jsonPath("$.items[*].priceCentavos", containsInAnyOrder(1000, 2500, 3750)));
    }

    private Order createTestOrder() {
        return createTestOrderForCustomer("CUSTOMER-TEST", "ORDER-TEST");
    }

    private Order createTestOrderForCustomer(String customerId, String orderNumber) {
        OrderItem item = OrderItem.builder()
                .sku("SKU-TEST")
                .quantity(1L)
                .priceCentavos(1000L)
                .build();

        return Order.builder()
                .numero(orderNumber)
                .customerId(customerId)
                .storeId(1L)
                .status(OrderStatus.COMPLETED)
                .totalCentavos(1000L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(Collections.singletonList(item))
                .build();
    }

    private Order createComplexTestOrder() {
        OrderItem item1 = OrderItem.builder()
                .sku("SKU-001")
                .quantity(1L)
                .priceCentavos(1000L)
                .build();

        OrderItem item2 = OrderItem.builder()
                .sku("SKU-002")
                .quantity(2L)
                .priceCentavos(2500L)
                .build();

        OrderItem item3 = OrderItem.builder()
                .sku("SKU-003")
                .quantity(3L)
                .priceCentavos(3750L)
                .build();

        return Order.builder()
                .numero("COMPLEX-ORDER")
                .customerId("CUSTOMER-COMPLEX")
                .storeId(1L)
                .status(OrderStatus.COMPLETED)
                .totalCentavos(7250L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(Arrays.asList(item1, item2, item3))
                .build();
    }
}