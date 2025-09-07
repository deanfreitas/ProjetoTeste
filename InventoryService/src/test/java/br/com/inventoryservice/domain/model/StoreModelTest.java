package br.com.inventoryservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StoreModel")
class StoreModelTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create StoreModel with valid parameters")
        void shouldCreateStoreModelWithValidParameters() {
            // Given
            String eventId = "event123";
            String codigo = "L001";

            // When
            StoreModel store = new StoreModel(eventId, codigo);

            // Then
            assertNotNull(store);
            assertEquals(eventId, store.eventId());
            assertEquals(codigo, store.codigo());
        }

        @Test
        @DisplayName("Should create StoreModel with empty strings")
        void shouldCreateStoreModelWithEmptyStrings() {
            // Given
            String eventId = "";
            String codigo = "";

            // When
            StoreModel store = new StoreModel(eventId, codigo);

            // Then
            assertNotNull(store);
            assertEquals(eventId, store.eventId());
            assertEquals(codigo, store.codigo());
        }
    }

    @Nested
    @DisplayName("Null Parameter Handling")
    class NullParameterHandling {

        @Test
        @DisplayName("Should create StoreModel with null eventId")
        void shouldCreateStoreModelWithNullEventId() {
            // Given
            String codigo = "L001";

            // When
            StoreModel store = new StoreModel(null, codigo);

            // Then
            assertNotNull(store);
            assertNull(store.eventId());
            assertEquals(codigo, store.codigo());
        }

        @Test
        @DisplayName("Should create StoreModel with null codigo")
        void shouldCreateStoreModelWithNullCodigo() {
            // Given
            String eventId = "event123";

            // When
            StoreModel store = new StoreModel(eventId, null);

            // Then
            assertNotNull(store);
            assertEquals(eventId, store.eventId());
            assertNull(store.codigo());
        }

        @Test
        @DisplayName("Should create StoreModel with both null parameters")
        void shouldCreateStoreModelWithBothNullParameters() {
            // When
            StoreModel store = new StoreModel(null, null);

            // Then
            assertNotNull(store);
            assertNull(store.eventId());
            assertNull(store.codigo());
        }
    }

    @Nested
    @DisplayName("Object Behavior")
    class ObjectBehavior {

        @Test
        @DisplayName("Should verify equals and hashCode")
        void shouldVerifyEqualsAndHashCode() {
            // Given
            StoreModel store1 = new StoreModel("event123", "L001");
            StoreModel store2 = new StoreModel("event123", "L001");
            StoreModel store3 = new StoreModel("event456", "L002");

            // Then
            assertEquals(store1, store2);
            assertEquals(store1.hashCode(), store2.hashCode());
            assertNotEquals(store1, store3);
            assertNotEquals(store1.hashCode(), store3.hashCode());
        }

        @Test
        @DisplayName("Should verify toString")
        void shouldVerifyToString() {
            // Given
            StoreModel store = new StoreModel("event123", "L001");

            // When
            String toString = store.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("event123"));
            assertTrue(toString.contains("L001"));
        }
    }
}