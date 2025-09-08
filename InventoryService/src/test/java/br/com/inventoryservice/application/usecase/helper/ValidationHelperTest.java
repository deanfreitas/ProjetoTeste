package br.com.inventoryservice.application.usecase.helper;

import br.com.inventoryservice.application.port.out.ProductReadPort;
import br.com.inventoryservice.application.port.out.StoreReadPort;
import br.com.inventoryservice.domain.model.SalesItem;
import br.com.inventoryservice.domain.model.SalesModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidationHelper Tests")
class ValidationHelperTest {

    private static final String TOPIC = "test-topic";

    @Mock
    private ProductReadPort productReadPort;

    @Mock
    private StoreReadPort storeReadPort;

    @InjectMocks
    private ValidationHelper validationHelper;

    @Nested
    @DisplayName("Sales Event Validation Tests")
    class SalesEventValidationTests {

        @Test
        @DisplayName("Should validate valid sales event")
        void shouldValidateValidSalesEvent() {
            // Given
            SalesItem item = new SalesItem("PROD-001", 5);
            SalesModel salesEvent = new SalesModel("event-123", "STORE-001", List.of(item));

            // When
            boolean result = validationHelper.isValidSalesEvent(salesEvent, TOPIC);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should invalidate null sales event")
        void shouldInvalidateNullSalesEvent() {
            // When
            boolean result = validationHelper.isValidSalesEvent(null, TOPIC);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should invalidate sales event with null store")
        void shouldInvalidateSalesEventWithNullStore() {
            // Given
            SalesItem item = new SalesItem("PROD-001", 5);
            SalesModel salesEvent = new SalesModel("event-123", null, List.of(item));

            // When
            boolean result = validationHelper.isValidSalesEvent(salesEvent, TOPIC);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should invalidate sales event with null items")
        void shouldInvalidateSalesEventWithNullItems() {
            // Given
            SalesModel salesEvent = new SalesModel("event-123", "STORE-001", null);

            // When
            boolean result = validationHelper.isValidSalesEvent(salesEvent, TOPIC);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should invalidate sales event with empty items")
        void shouldInvalidateSalesEventWithEmptyItems() {
            // Given
            SalesModel salesEvent = new SalesModel("event-123", "STORE-001", Collections.emptyList());

            // When
            boolean result = validationHelper.isValidSalesEvent(salesEvent, TOPIC);

            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("Sales Item Validation Tests")
    class SalesItemValidationTests {

        @Test
        @DisplayName("Should validate valid sales item")
        void shouldValidateValidSalesItem() {
            // Given
            SalesItem salesItem = new SalesItem("PROD-001", 5);

            // When
            boolean result = validationHelper.isValidSalesItem(salesItem, TOPIC);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should invalidate null sales item")
        void shouldInvalidateNullSalesItem() {
            // When
            boolean result = validationHelper.isValidSalesItem(null, TOPIC);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should invalidate sales item with null SKU")
        void shouldInvalidateSalesItemWithNullSku() {
            // Given
            SalesItem salesItem = new SalesItem(null, 5);

            // When
            boolean result = validationHelper.isValidSalesItem(salesItem, TOPIC);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should invalidate sales item with null quantity")
        void shouldInvalidateSalesItemWithNullQuantity() {
            // Given
            SalesItem salesItem = new SalesItem("PROD-001", null);

            // When
            boolean result = validationHelper.isValidSalesItem(salesItem, TOPIC);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should invalidate sales item with zero quantity")
        void shouldInvalidateSalesItemWithZeroQuantity() {
            // Given
            SalesItem salesItem = new SalesItem("PROD-001", 0);

            // When
            boolean result = validationHelper.isValidSalesItem(salesItem, TOPIC);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should invalidate sales item with negative quantity")
        void shouldInvalidateSalesItemWithNegativeQuantity() {
            // Given
            SalesItem salesItem = new SalesItem("PROD-001", -5);

            // When
            boolean result = validationHelper.isValidSalesItem(salesItem, TOPIC);

            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("Product Existence Tests")
    class ProductExistenceTests {

        @Test
        @DisplayName("Should return true when product exists")
        void shouldReturnTrueWhenProductExists() {
            // Given
            String sku = "PROD-001";
            when(productReadPort.existsBySku(sku)).thenReturn(true);

            // When
            boolean result = validationHelper.productExists(sku);

            // Then
            assertTrue(result);
            verify(productReadPort).existsBySku(sku);
        }

        @Test
        @DisplayName("Should return false when product does not exist")
        void shouldReturnFalseWhenProductDoesNotExist() {
            // Given
            String sku = "PROD-999";
            when(productReadPort.existsBySku(sku)).thenReturn(false);

            // When
            boolean result = validationHelper.productExists(sku);

            // Then
            assertFalse(result);
            verify(productReadPort).existsBySku(sku);
        }
    }

    @Nested
    @DisplayName("Store Existence Tests")
    class StoreExistenceTests {

        @Test
        @DisplayName("Should return true when store exists")
        void shouldReturnTrueWhenStoreExists() {
            // Given
            String codigo = "STORE-001";
            when(storeReadPort.existsByCodigo(codigo)).thenReturn(true);

            // When
            boolean result = validationHelper.storeExists(codigo);

            // Then
            assertTrue(result);
            verify(storeReadPort).existsByCodigo(codigo);
        }

        @Test
        @DisplayName("Should return false when store does not exist")
        void shouldReturnFalseWhenStoreDoesNotExist() {
            // Given
            String codigo = "STORE-999";
            when(storeReadPort.existsByCodigo(codigo)).thenReturn(false);

            // When
            boolean result = validationHelper.storeExists(codigo);

            // Then
            assertFalse(result);
            verify(storeReadPort).existsByCodigo(codigo);
        }
    }

    @Nested
    @DisplayName("Product Active Status Tests")
    class ProductActiveStatusTests {

        @Test
        @DisplayName("Should return true when product is active")
        void shouldReturnTrueWhenProductIsActive() {
            // Given
            String sku = "PROD-001";
            when(productReadPort.isAtivo(sku)).thenReturn(Optional.of(true));

            // When
            boolean result = validationHelper.isActiveProduct(sku, TOPIC);

            // Then
            assertTrue(result);
            verify(productReadPort).isAtivo(sku);
        }

        @Test
        @DisplayName("Should return false when product is inactive")
        void shouldReturnFalseWhenProductIsInactive() {
            // Given
            String sku = "PROD-001";
            when(productReadPort.isAtivo(sku)).thenReturn(Optional.of(false));

            // When
            boolean result = validationHelper.isActiveProduct(sku, TOPIC);

            // Then
            assertFalse(result);
            verify(productReadPort).isAtivo(sku);
        }

        @Test
        @DisplayName("Should return false when product status is not found")
        void shouldReturnFalseWhenProductStatusIsNotFound() {
            // Given
            String sku = "PROD-999";
            when(productReadPort.isAtivo(sku)).thenReturn(Optional.empty());

            // When
            boolean result = validationHelper.isActiveProduct(sku, TOPIC);

            // Then
            assertFalse(result);
            verify(productReadPort).isAtivo(sku);
        }
    }

    @Nested
    @DisplayName("Product Event Validation Tests")
    class ProductEventValidationTests {

        @Test
        @DisplayName("Should validate product event when product exists")
        void shouldValidateProductEventWhenProductExists() {
            // Given
            String sku = "PROD-001";
            when(productReadPort.existsBySku(sku)).thenReturn(true);

            // When
            boolean result = validationHelper.validateProductEvent(sku, TOPIC);

            // Then
            assertTrue(result);
            verify(productReadPort).existsBySku(sku);
        }

        @Test
        @DisplayName("Should validate product event when product does not exist")
        void shouldValidateProductEventWhenProductDoesNotExist() {
            // Given
            String sku = "PROD-999";
            when(productReadPort.existsBySku(sku)).thenReturn(false);

            // When
            boolean result = validationHelper.validateProductEvent(sku, TOPIC);

            // Then
            assertTrue(result); // Still returns true for idempotence
            verify(productReadPort).existsBySku(sku);
        }

        @Test
        @DisplayName("Should return false when SKU is null")
        void shouldReturnFalseWhenSkuIsNull() {
            // When
            boolean result = validationHelper.validateProductEvent(null, TOPIC);

            // Then
            assertFalse(result);
            verify(productReadPort, never()).existsBySku(anyString());
        }
    }

    @Nested
    @DisplayName("Store Event Validation Tests")
    class StoreEventValidationTests {

        @Test
        @DisplayName("Should validate store event when store exists")
        void shouldValidateStoreEventWhenStoreExists() {
            // Given
            String codigo = "STORE-001";
            when(storeReadPort.existsByCodigo(codigo)).thenReturn(true);

            // When
            boolean result = validationHelper.validateStoreEvent(codigo, TOPIC);

            // Then
            assertTrue(result);
            verify(storeReadPort).existsByCodigo(codigo);
        }

        @Test
        @DisplayName("Should validate store event when store does not exist")
        void shouldValidateStoreEventWhenStoreDoesNotExist() {
            // Given
            String codigo = "STORE-999";
            when(storeReadPort.existsByCodigo(codigo)).thenReturn(false);

            // When
            boolean result = validationHelper.validateStoreEvent(codigo, TOPIC);

            // Then
            assertTrue(result); // Still returns true for idempotence
            verify(storeReadPort).existsByCodigo(codigo);
        }

        @Test
        @DisplayName("Should return false when code is null")
        void shouldReturnFalseWhenCodeIsNull() {
            // When
            boolean result = validationHelper.validateStoreEvent(null, TOPIC);

            // Then
            assertFalse(result);
            verify(storeReadPort, never()).existsByCodigo(anyString());
        }
    }
}