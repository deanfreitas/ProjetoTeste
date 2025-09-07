package br.com.orderservice.domain.model;

import br.com.orderservice.domain.enuns.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Order Domain Model Tests")
@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Nested
    @DisplayName("Object Creation")
    class ObjectCreation {

        @Test
        @DisplayName("Should create order with no-args constructor")
        void shouldCreateOrderWithNoArgsConstructor() {
            Order order = new Order();

            assertNotNull(order);
            assertNull(order.getId());
            assertNull(order.getNumero());
            assertNull(order.getCustomerId());
            assertNull(order.getStoreId());
            assertNull(order.getStatus());
            assertNull(order.getTotalCentavos());
            assertNull(order.getCreatedAt());
            assertNull(order.getUpdatedAt());
            assertNull(order.getItems());
        }

        @Test
        @DisplayName("Should create order with all-args constructor")
        void shouldCreateOrderWithAllArgsConstructor() {
            LocalDateTime now = LocalDateTime.now();
            List<OrderItem> items = Arrays.asList(
                    OrderItem.builder().id(1L).build(),
                    OrderItem.builder().id(2L).build()
            );

            Order order = new Order(
                    1L,
                    "ORD-001",
                    "customer-123",
                    100L,
                    OrderStatus.COMPLETED,
                    5000L,
                    now,
                    now,
                    items
            );

            assertEquals(1L, order.getId());
            assertEquals("ORD-001", order.getNumero());
            assertEquals("customer-123", order.getCustomerId());
            assertEquals(100L, order.getStoreId());
            assertEquals(OrderStatus.COMPLETED, order.getStatus());
            assertEquals(5000L, order.getTotalCentavos());
            assertEquals(now, order.getCreatedAt());
            assertEquals(now, order.getUpdatedAt());
            assertEquals(items, order.getItems());
        }

        @Test
        @DisplayName("Should create order using builder pattern")
        void shouldCreateOrderUsingBuilder() {
            LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
            LocalDateTime updatedAt = LocalDateTime.now();
            List<OrderItem> items = Collections.singletonList(
                    OrderItem.builder().id(1L).build()
            );

            Order order = Order.builder()
                    .id(2L)
                    .numero("ORD-002")
                    .customerId("customer-456")
                    .storeId(200L)
                    .status(OrderStatus.COMPLETED)
                    .totalCentavos(7500L)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .items(items)
                    .build();

            assertEquals(2L, order.getId());
            assertEquals("ORD-002", order.getNumero());
            assertEquals("customer-456", order.getCustomerId());
            assertEquals(200L, order.getStoreId());
            assertEquals(OrderStatus.COMPLETED, order.getStatus());
            assertEquals(7500L, order.getTotalCentavos());
            assertEquals(createdAt, order.getCreatedAt());
            assertEquals(updatedAt, order.getUpdatedAt());
            assertEquals(items, order.getItems());
        }
    }

    @Nested
    @DisplayName("Field Setters and Getters")
    class FieldSettersAndGetters {

        @Test
        @DisplayName("Should set and get id correctly")
        void shouldSetAndGetId() {
            Order order = new Order();
            order.setId(123L);

            assertEquals(123L, order.getId());
        }

        @Test
        @DisplayName("Should set and get numero correctly")
        void shouldSetAndGetNumero() {
            Order order = new Order();
            order.setNumero("ORD-TEST");

            assertEquals("ORD-TEST", order.getNumero());
        }

        @Test
        @DisplayName("Should set and get customerId correctly")
        void shouldSetAndGetCustomerId() {
            Order order = new Order();
            order.setCustomerId("CUST-789");

            assertEquals("CUST-789", order.getCustomerId());
        }

        @Test
        @DisplayName("Should set and get storeId correctly")
        void shouldSetAndGetStoreId() {
            Order order = new Order();
            order.setStoreId(999L);

            assertEquals(999L, order.getStoreId());
        }

        @Test
        @DisplayName("Should set and get status correctly")
        void shouldSetAndGetStatus() {
            Order order = new Order();
            order.setStatus(OrderStatus.COMPLETED);

            assertEquals(OrderStatus.COMPLETED, order.getStatus());
        }

        @Test
        @DisplayName("Should set and get totalCentavos correctly")
        void shouldSetAndGetTotalCentavos() {
            Order order = new Order();
            order.setTotalCentavos(12345L);

            assertEquals(12345L, order.getTotalCentavos());
        }

        @Test
        @DisplayName("Should set and get createdAt correctly")
        void shouldSetAndGetCreatedAt() {
            Order order = new Order();
            LocalDateTime timestamp = LocalDateTime.now();
            order.setCreatedAt(timestamp);

            assertEquals(timestamp, order.getCreatedAt());
        }

        @Test
        @DisplayName("Should set and get updatedAt correctly")
        void shouldSetAndGetUpdatedAt() {
            Order order = new Order();
            LocalDateTime timestamp = LocalDateTime.now();
            order.setUpdatedAt(timestamp);

            assertEquals(timestamp, order.getUpdatedAt());
        }

        @Test
        @DisplayName("Should set and get items correctly")
        void shouldSetAndGetItems() {
            Order order = new Order();
            List<OrderItem> items = Arrays.asList(
                    OrderItem.builder().id(1L).build(),
                    OrderItem.builder().id(2L).build()
            );
            order.setItems(items);

            assertEquals(items, order.getItems());
            assertEquals(2, order.getItems().size());
        }
    }

    @Nested
    @DisplayName("Object Equality and Hash Code")
    class ObjectEqualityAndHashCode {

        @Test
        @DisplayName("Should be equal when all fields are the same")
        void shouldBeEqualWhenAllFieldsAreSame() {
            LocalDateTime now = LocalDateTime.now();
            List<OrderItem> items = Collections.singletonList(OrderItem.builder().id(1L).build());

            Order order1 = Order.builder()
                    .id(1L)
                    .numero("ORD-001")
                    .customerId("customer-123")
                    .storeId(100L)
                    .status(OrderStatus.COMPLETED)
                    .totalCentavos(5000L)
                    .createdAt(now)
                    .updatedAt(now)
                    .items(items)
                    .build();

            Order order2 = Order.builder()
                    .id(1L)
                    .numero("ORD-001")
                    .customerId("customer-123")
                    .storeId(100L)
                    .status(OrderStatus.COMPLETED)
                    .totalCentavos(5000L)
                    .createdAt(now)
                    .updatedAt(now)
                    .items(items)
                    .build();

            assertEquals(order1, order2);
            assertEquals(order1.hashCode(), order2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when ids are different")
        void shouldNotBeEqualWhenIdsAreDifferent() {
            Order order1 = Order.builder().id(1L).numero("ORD-001").build();
            Order order2 = Order.builder().id(2L).numero("ORD-001").build();

            assertNotEquals(order1, order2);
        }

        @Test
        @DisplayName("Should not be equal when comparing with null")
        void shouldNotBeEqualWhenComparingWithNull() {
            Order order = Order.builder().id(1L).build();

            assertNotEquals(order, null);
        }

        @Test
        @DisplayName("Should not be equal when comparing with different class")
        void shouldNotBeEqualWhenComparingWithDifferentClass() {
            Order order = Order.builder().id(1L).build();
            String differentObject = "not an order";

            assertNotEquals(order, differentObject);
        }
    }

    @Nested
    @DisplayName("ToString Method")
    class ToStringMethod {

        @Test
        @DisplayName("Should generate string representation")
        void shouldGenerateStringRepresentation() {
            Order order = Order.builder()
                    .id(1L)
                    .numero("ORD-001")
                    .customerId("customer-123")
                    .status(OrderStatus.COMPLETED)
                    .build();

            String result = order.toString();

            assertNotNull(result);
            assertTrue(result.contains("Order"));
            assertTrue(result.contains("id=1"));
            assertTrue(result.contains("numero=ORD-001"));
            assertTrue(result.contains("customerId=customer-123"));
            assertTrue(result.contains("status=COMPLETED"));
        }
    }
}