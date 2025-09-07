package br.com.inventoryservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SalesModel")
class SalesModelTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create SalesModel with valid parameters")
        void shouldCreateSalesModelWithValidParameters() {
            // Given
            String eventId = "event123";
            String loja = "L001";
            List<SalesItem> itens = List.of(new SalesItem("SKU001", 5));

            // When
            SalesModel salesModel = new SalesModel(eventId, loja, itens);

            // Then
            assertNotNull(salesModel);
            assertEquals(eventId, salesModel.eventId());
            assertEquals(loja, salesModel.loja());
            assertEquals(itens, salesModel.itens());
        }

        @Test
        @DisplayName("Should create SalesModel with multiple items")
        void shouldCreateSalesModelWithMultipleItems() {
            // Given
            String eventId = "event123";
            String loja = "L001";
            List<SalesItem> itens = List.of(
                new SalesItem("SKU001", 5),
                new SalesItem("SKU002", 3),
                new SalesItem("SKU003", 1)
            );

            // When
            SalesModel salesModel = new SalesModel(eventId, loja, itens);

            // Then
            assertNotNull(salesModel);
            assertEquals(eventId, salesModel.eventId());
            assertEquals(loja, salesModel.loja());
            assertEquals(itens, salesModel.itens());
            assertEquals(3, salesModel.itens().size());
        }
    }

    @Nested
    @DisplayName("Items List Handling")
    class ItemsListHandling {

        @Test
        @DisplayName("Should create SalesModel with empty items list")
        void shouldCreateSalesModelWithEmptyItemsList() {
            // Given
            String eventId = "event123";
            String loja = "L001";
            List<SalesItem> itens = new ArrayList<>();

            // When
            SalesModel salesModel = new SalesModel(eventId, loja, itens);

            // Then
            assertNotNull(salesModel);
            assertEquals(eventId, salesModel.eventId());
            assertEquals(loja, salesModel.loja());
            assertEquals(itens, salesModel.itens());
            assertTrue(salesModel.itens().isEmpty());
        }

        @Test
        @DisplayName("Should create SalesModel with null items")
        void shouldCreateSalesModelWithNullItems() {
            // Given
            String eventId = "event123";
            String loja = "L001";

            // When
            SalesModel salesModel = new SalesModel(eventId, loja, null);

            // Then
            assertNotNull(salesModel);
            assertEquals(eventId, salesModel.eventId());
            assertEquals(loja, salesModel.loja());
            assertNull(salesModel.itens());
        }
    }

    @Nested
    @DisplayName("Null Parameter Handling")
    class NullParameterHandling {

        @Test
        @DisplayName("Should create SalesModel with null eventId")
        void shouldCreateSalesModelWithNullEventId() {
            // Given
            String loja = "L001";
            List<SalesItem> itens = List.of(new SalesItem("SKU001", 5));

            // When
            SalesModel salesModel = new SalesModel(null, loja, itens);

            // Then
            assertNotNull(salesModel);
            assertNull(salesModel.eventId());
            assertEquals(loja, salesModel.loja());
            assertEquals(itens, salesModel.itens());
        }

        @Test
        @DisplayName("Should create SalesModel with null loja")
        void shouldCreateSalesModelWithNullLoja() {
            // Given
            String eventId = "event123";
            List<SalesItem> itens = List.of(new SalesItem("SKU001", 5));

            // When
            SalesModel salesModel = new SalesModel(eventId, null, itens);

            // Then
            assertNotNull(salesModel);
            assertEquals(eventId, salesModel.eventId());
            assertNull(salesModel.loja());
            assertEquals(itens, salesModel.itens());
        }
    }

    @Nested
    @DisplayName("Object Behavior")
    class ObjectBehavior {

        @Test
        @DisplayName("Should verify equals and hashCode")
        void shouldVerifyEqualsAndHashCode() {
            // Given
            List<SalesItem> itens = List.of(new SalesItem("SKU001", 5));
            SalesModel sales1 = new SalesModel("event123", "L001", itens);
            SalesModel sales2 = new SalesModel("event123", "L001", itens);
            SalesModel sales3 = new SalesModel("event456", "L002", List.of(new SalesItem("SKU002", 3)));

            // Then
            assertEquals(sales1, sales2);
            assertEquals(sales1.hashCode(), sales2.hashCode());
            assertNotEquals(sales1, sales3);
            assertNotEquals(sales1.hashCode(), sales3.hashCode());
        }

        @Test
        @DisplayName("Should verify toString")
        void shouldVerifyToString() {
            // Given
            List<SalesItem> itens = List.of(new SalesItem("SKU001", 5));
            SalesModel salesModel = new SalesModel("event123", "L001", itens);

            // When
            String toString = salesModel.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("event123"));
            assertTrue(toString.contains("L001"));
        }
    }
}