package br.com.orderservice.infrastructure.adapters.in.messaging.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SalesEvent")
class SalesEventTest {

    @Test
    @DisplayName("Should create SalesEvent with constructor and verify all getters")
    void testSalesEventConstructorAndGetters() {
        // Given
        String eventId = "event123";
        String numero = "12345";
        String customerId = "customer456";
        String storeCode = "STORE001";
        Long totalCentavos = 5000L;
        LocalDateTime timestamp = LocalDateTime.now();
        List<SalesEventItem> items = Arrays.asList(
                new SalesEventItem("SKU1", 2L, 1500L),
                new SalesEventItem("SKU2", 3L, 3500L)
        );

        // When
        SalesEvent salesEvent = new SalesEvent(eventId, numero, customerId, storeCode, totalCentavos, timestamp, items);

        // Then
        assertEquals(eventId, salesEvent.getEventId());
        assertEquals(numero, salesEvent.getNumero());
        assertEquals(customerId, salesEvent.getCustomerId());
        assertEquals(storeCode, salesEvent.getStoreCode());
        assertEquals(totalCentavos, salesEvent.getTotalCentavos());
        assertEquals(timestamp, salesEvent.getTimestamp());
        assertEquals(items, salesEvent.getItems());
        assertEquals(2, salesEvent.getItems().size());
    }

    @Test
    void testSalesEventNoArgsConstructor() {
        // When
        SalesEvent salesEvent = new SalesEvent();

        // Then
        assertNull(salesEvent.getEventId());
        assertNull(salesEvent.getNumero());
        assertNull(salesEvent.getCustomerId());
        assertNull(salesEvent.getStoreCode());
        assertNull(salesEvent.getTotalCentavos());
        assertNull(salesEvent.getTimestamp());
        assertNull(salesEvent.getItems());
    }

    @Test
    void testSalesEventSetters() {
        // Given
        SalesEvent salesEvent = new SalesEvent();
        String eventId = "newEvent789";
        String numero = "67890";
        String customerId = "newCustomer123";
        String storeCode = "STORE002";
        Long totalCentavos = 7500L;
        LocalDateTime timestamp = LocalDateTime.of(2023, 12, 25, 10, 30);
        List<SalesEventItem> items = List.of(new SalesEventItem("SKU3", 1L, 7500L));

        // When
        salesEvent.setEventId(eventId);
        salesEvent.setNumero(numero);
        salesEvent.setCustomerId(customerId);
        salesEvent.setStoreCode(storeCode);
        salesEvent.setTotalCentavos(totalCentavos);
        salesEvent.setTimestamp(timestamp);
        salesEvent.setItems(items);

        // Then
        assertEquals(eventId, salesEvent.getEventId());
        assertEquals(numero, salesEvent.getNumero());
        assertEquals(customerId, salesEvent.getCustomerId());
        assertEquals(storeCode, salesEvent.getStoreCode());
        assertEquals(totalCentavos, salesEvent.getTotalCentavos());
        assertEquals(timestamp, salesEvent.getTimestamp());
        assertEquals(items, salesEvent.getItems());
    }

    @Test
    void testSalesEventEqualsAndHashCode() {
        // Given
        LocalDateTime timestamp = LocalDateTime.now();
        List<SalesEventItem> items = List.of(new SalesEventItem("SKU1", 1L, 1000L));

        SalesEvent event1 = new SalesEvent("event1", "123", "customer1", "STORE1", 1000L, timestamp, items);
        SalesEvent event2 = new SalesEvent("event1", "123", "customer1", "STORE1", 1000L, timestamp, items);
        SalesEvent event3 = new SalesEvent("event2", "456", "customer2", "STORE2", 2000L, timestamp, items);

        // Then
        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1, event3);
        assertNotEquals(event1.hashCode(), event3.hashCode());
    }

    @Test
    void testSalesEventToString() {
        // Given
        LocalDateTime timestamp = LocalDateTime.of(2023, 12, 25, 15, 45);
        List<SalesEventItem> items = List.of(new SalesEventItem("SKU1", 1L, 1000L));
        SalesEvent salesEvent = new SalesEvent("event123", "12345", "customer456", "STORE001", 5000L, timestamp, items);

        // When
        String toString = salesEvent.toString();

        // Then
        assertTrue(toString.contains("SalesEvent"));
        assertTrue(toString.contains("eventId=event123"));
        assertTrue(toString.contains("numero=12345"));
        assertTrue(toString.contains("customerId=customer456"));
        assertTrue(toString.contains("storeCode=STORE001"));
        assertTrue(toString.contains("totalCentavos=5000"));
    }

    @Test
    void testSalesEventWithNullValues() {
        // When
        SalesEvent salesEvent = new SalesEvent(null, null, null, null, null, null, null);

        // Then
        assertNull(salesEvent.getEventId());
        assertNull(salesEvent.getNumero());
        assertNull(salesEvent.getCustomerId());
        assertNull(salesEvent.getStoreCode());
        assertNull(salesEvent.getTotalCentavos());
        assertNull(salesEvent.getTimestamp());
        assertNull(salesEvent.getItems());
    }

    @Test
    void testSalesEventWithEmptyItemsList() {
        // Given
        LocalDateTime timestamp = LocalDateTime.now();
        List<SalesEventItem> emptyItems = List.of();

        // When
        SalesEvent salesEvent = new SalesEvent("event1", "123", "customer1", "STORE1", 0L, timestamp, emptyItems);

        // Then
        assertEquals("event1", salesEvent.getEventId());
        assertEquals("123", salesEvent.getNumero());
        assertEquals("customer1", salesEvent.getCustomerId());
        assertEquals("STORE1", salesEvent.getStoreCode());
        assertEquals(0L, salesEvent.getTotalCentavos());
        assertEquals(timestamp, salesEvent.getTimestamp());
        assertEquals(emptyItems, salesEvent.getItems());
        assertTrue(salesEvent.getItems().isEmpty());
    }

    @Test
    void testSalesEventWithEmptyStrings() {
        // Given
        LocalDateTime timestamp = LocalDateTime.now();
        List<SalesEventItem> items = List.of(new SalesEventItem("", 0L, 0L));

        // When
        SalesEvent salesEvent = new SalesEvent("", "", "", "", 0L, timestamp, items);

        // Then
        assertEquals("", salesEvent.getEventId());
        assertEquals("", salesEvent.getNumero());
        assertEquals("", salesEvent.getCustomerId());
        assertEquals("", salesEvent.getStoreCode());
        assertEquals(0L, salesEvent.getTotalCentavos());
        assertEquals(timestamp, salesEvent.getTimestamp());
        assertEquals(items, salesEvent.getItems());
    }
}