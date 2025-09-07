package br.com.inventoryservice.application.usecase;

import br.com.inventoryservice.application.usecase.helper.EventProcessor;
import br.com.inventoryservice.application.usecase.helper.StockManager;
import br.com.inventoryservice.application.usecase.helper.ValidationHelper;
import br.com.inventoryservice.domain.model.ProductModel;
import br.com.inventoryservice.domain.model.SalesItem;
import br.com.inventoryservice.domain.model.SalesModel;
import br.com.inventoryservice.domain.model.StockAdjustmentModel;
import br.com.inventoryservice.domain.model.StoreModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InventoryEventUseCaseImpl Tests")
class InventoryEventUseCaseImplTest {

    @Mock
    private EventProcessor eventProcessor;

    @Mock
    private ValidationHelper validationHelper;

    @Mock
    private StockManager stockManager;

    @InjectMocks
    private InventoryEventUseCaseImpl inventoryEventUseCase;

    private static final String TOPIC = "test-topic";
    private static final Integer PARTITION = 0;
    private static final Long OFFSET = 100L;

    @Nested
    @DisplayName("Handle Product Event Tests")
    class HandleProductEventTests {

        @Test
        @DisplayName("Should handle valid product event successfully")
        void shouldHandleValidProductEventSuccessfully() {
            // Given
            ProductModel productEvent = new ProductModel("event-123", "PROD-001");
            when(eventProcessor.markProcessed("event-123", TOPIC, PARTITION, OFFSET)).thenReturn(true);
            when(validationHelper.validateProductEvent("PROD-001", TOPIC)).thenReturn(true);

            // When
            inventoryEventUseCase.handleProductEvent(productEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-123", TOPIC, PARTITION, OFFSET);
            verify(validationHelper).validateProductEvent("PROD-001", TOPIC);
        }

        @Test
        @DisplayName("Should handle duplicate product event and skip processing")
        void shouldHandleDuplicateProductEventAndSkipProcessing() {
            // Given
            ProductModel productEvent = new ProductModel("event-123", "PROD-001");
            when(eventProcessor.markProcessed("event-123", TOPIC, PARTITION, OFFSET)).thenReturn(false);
            doNothing().when(eventProcessor).handleDuplicate("produto", TOPIC, PARTITION, OFFSET);

            // When
            inventoryEventUseCase.handleProductEvent(productEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-123", TOPIC, PARTITION, OFFSET);
            verify(eventProcessor).handleDuplicate("produto", TOPIC, PARTITION, OFFSET);
            verify(validationHelper, never()).validateProductEvent(any(), any());
        }

        @Test
        @DisplayName("Should handle null product event")
        void shouldHandleNullProductEvent() {
            // Given
            when(eventProcessor.markProcessed(null, TOPIC, PARTITION, OFFSET)).thenReturn(true);

            // When
            inventoryEventUseCase.handleProductEvent(null, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed(null, TOPIC, PARTITION, OFFSET);
            verify(validationHelper, never()).validateProductEvent(any(), any());
        }

        @Test
        @DisplayName("Should handle product event with null SKU")
        void shouldHandleProductEventWithNullSku() {
            // Given
            ProductModel productEvent = new ProductModel("event-123", null);
            when(eventProcessor.markProcessed("event-123", TOPIC, PARTITION, OFFSET)).thenReturn(true);

            // When
            inventoryEventUseCase.handleProductEvent(productEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-123", TOPIC, PARTITION, OFFSET);
            verify(validationHelper, never()).validateProductEvent(any(), any());
        }
    }

    @Nested
    @DisplayName("Handle Store Event Tests")
    class HandleStoreEventTests {

        @Test
        @DisplayName("Should handle valid store event successfully")
        void shouldHandleValidStoreEventSuccessfully() {
            // Given
            StoreModel storeEvent = new StoreModel("event-456", "STORE-001");
            when(eventProcessor.markProcessed("event-456", TOPIC, PARTITION, OFFSET)).thenReturn(true);
            when(validationHelper.validateStoreEvent("STORE-001", TOPIC)).thenReturn(true);

            // When
            inventoryEventUseCase.handleStoreEvent(storeEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-456", TOPIC, PARTITION, OFFSET);
            verify(validationHelper).validateStoreEvent("STORE-001", TOPIC);
        }

        @Test
        @DisplayName("Should handle duplicate store event and skip processing")
        void shouldHandleDuplicateStoreEventAndSkipProcessing() {
            // Given
            StoreModel storeEvent = new StoreModel("event-456", "STORE-001");
            when(eventProcessor.markProcessed("event-456", TOPIC, PARTITION, OFFSET)).thenReturn(false);
            doNothing().when(eventProcessor).handleDuplicate("loja", TOPIC, PARTITION, OFFSET);

            // When
            inventoryEventUseCase.handleStoreEvent(storeEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-456", TOPIC, PARTITION, OFFSET);
            verify(eventProcessor).handleDuplicate("loja", TOPIC, PARTITION, OFFSET);
            verify(validationHelper, never()).validateStoreEvent(any(), any());
        }

        @Test
        @DisplayName("Should handle null store event")
        void shouldHandleNullStoreEvent() {
            // Given
            when(eventProcessor.markProcessed(null, TOPIC, PARTITION, OFFSET)).thenReturn(true);

            // When
            inventoryEventUseCase.handleStoreEvent(null, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed(null, TOPIC, PARTITION, OFFSET);
            verify(validationHelper, never()).validateStoreEvent(any(), any());
        }

        @Test
        @DisplayName("Should handle store event with null code")
        void shouldHandleStoreEventWithNullCode() {
            // Given
            StoreModel storeEvent = new StoreModel("event-456", null);
            when(eventProcessor.markProcessed("event-456", TOPIC, PARTITION, OFFSET)).thenReturn(true);

            // When
            inventoryEventUseCase.handleStoreEvent(storeEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-456", TOPIC, PARTITION, OFFSET);
            verify(validationHelper, never()).validateStoreEvent(any(), any());
        }
    }

    @Nested
    @DisplayName("Handle Sales Event Tests")
    class HandleSalesEventTests {

        @Test
        @DisplayName("Should handle valid sales event successfully")
        void shouldHandleValidSalesEventSuccessfully() {
            // Given
            SalesItem item1 = new SalesItem("PROD-001", 5);
            SalesItem item2 = new SalesItem("PROD-002", 3);
            SalesModel salesEvent = new SalesModel("event-789", "STORE-001", Arrays.asList(item1, item2));
            
            when(eventProcessor.markProcessed("event-789", TOPIC, PARTITION, OFFSET)).thenReturn(true);
            when(validationHelper.isValidSalesEvent(salesEvent, TOPIC)).thenReturn(true);
            when(validationHelper.storeExists("STORE-001")).thenReturn(true);
            when(validationHelper.isValidSalesItem(item1, TOPIC)).thenReturn(true);
            when(validationHelper.isValidSalesItem(item2, TOPIC)).thenReturn(true);
            when(validationHelper.isActiveProduct("PROD-001", TOPIC)).thenReturn(true);
            when(validationHelper.isActiveProduct("PROD-002", TOPIC)).thenReturn(true);
            
            doNothing().when(stockManager).adjustStock("STORE-001", "PROD-001", -5, TOPIC, false);
            doNothing().when(stockManager).adjustStock("STORE-001", "PROD-002", -3, TOPIC, false);

            // When
            inventoryEventUseCase.handleSalesEvent(salesEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-789", TOPIC, PARTITION, OFFSET);
            verify(validationHelper).isValidSalesEvent(salesEvent, TOPIC);
            verify(validationHelper).storeExists("STORE-001");
            verify(stockManager).adjustStock("STORE-001", "PROD-001", -5, TOPIC, false);
            verify(stockManager).adjustStock("STORE-001", "PROD-002", -3, TOPIC, false);
        }

        @Test
        @DisplayName("Should handle duplicate sales event and skip processing")
        void shouldHandleDuplicateSalesEventAndSkipProcessing() {
            // Given
            SalesModel salesEvent = new SalesModel("event-789", "STORE-001", Collections.emptyList());
            when(eventProcessor.markProcessed("event-789", TOPIC, PARTITION, OFFSET)).thenReturn(false);
            doNothing().when(eventProcessor).handleDuplicate("venda", TOPIC, PARTITION, OFFSET);

            // When
            inventoryEventUseCase.handleSalesEvent(salesEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-789", TOPIC, PARTITION, OFFSET);
            verify(eventProcessor).handleDuplicate("venda", TOPIC, PARTITION, OFFSET);
            verify(validationHelper, never()).isValidSalesEvent(any(), any());
        }

        @Test
        @DisplayName("Should skip processing when sales event is invalid")
        void shouldSkipProcessingWhenSalesEventIsInvalid() {
            // Given
            SalesModel salesEvent = new SalesModel("event-789", null, null);
            when(eventProcessor.markProcessed("event-789", TOPIC, PARTITION, OFFSET)).thenReturn(true);
            when(validationHelper.isValidSalesEvent(salesEvent, TOPIC)).thenReturn(false);

            // When
            inventoryEventUseCase.handleSalesEvent(salesEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-789", TOPIC, PARTITION, OFFSET);
            verify(validationHelper).isValidSalesEvent(salesEvent, TOPIC);
            verify(validationHelper, never()).storeExists(any());
        }

        @Test
        @DisplayName("Should skip processing when store does not exist")
        void shouldSkipProcessingWhenStoreDoesNotExist() {
            // Given
            SalesItem item = new SalesItem("PROD-001", 5);
            SalesModel salesEvent = new SalesModel("event-789", "STORE-999", List.of(item));
            
            when(eventProcessor.markProcessed("event-789", TOPIC, PARTITION, OFFSET)).thenReturn(true);
            when(validationHelper.isValidSalesEvent(salesEvent, TOPIC)).thenReturn(true);
            when(validationHelper.storeExists("STORE-999")).thenReturn(false);

            // When
            inventoryEventUseCase.handleSalesEvent(salesEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-789", TOPIC, PARTITION, OFFSET);
            verify(validationHelper).isValidSalesEvent(salesEvent, TOPIC);
            verify(validationHelper).storeExists("STORE-999");
            verify(stockManager, never()).adjustStock(any(), any(), anyInt(), any(), anyBoolean());
        }

        @Test
        @DisplayName("Should process only valid items and skip invalid ones")
        void shouldProcessOnlyValidItemsAndSkipInvalidOnes() {
            // Given
            SalesItem validItem = new SalesItem("PROD-001", 5);
            SalesItem invalidItem = new SalesItem("PROD-002", null);
            SalesModel salesEvent = new SalesModel("event-789", "STORE-001", Arrays.asList(validItem, invalidItem));
            
            when(eventProcessor.markProcessed("event-789", TOPIC, PARTITION, OFFSET)).thenReturn(true);
            when(validationHelper.isValidSalesEvent(salesEvent, TOPIC)).thenReturn(true);
            when(validationHelper.storeExists("STORE-001")).thenReturn(true);
            when(validationHelper.isValidSalesItem(validItem, TOPIC)).thenReturn(true);
            when(validationHelper.isValidSalesItem(invalidItem, TOPIC)).thenReturn(false);
            when(validationHelper.isActiveProduct("PROD-001", TOPIC)).thenReturn(true);
            
            doNothing().when(stockManager).adjustStock("STORE-001", "PROD-001", -5, TOPIC, false);

            // When
            inventoryEventUseCase.handleSalesEvent(salesEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(stockManager).adjustStock("STORE-001", "PROD-001", -5, TOPIC, false);
            verify(stockManager, never()).adjustStock(eq("STORE-001"), eq("PROD-002"), anyInt(), any(), anyBoolean());
        }
    }

    @Nested
    @DisplayName("Handle Stock Adjustment Event Tests")
    class HandleStockAdjustmentEventTests {

        @Test
        @DisplayName("Should handle valid stock adjustment event successfully")
        void shouldHandleValidStockAdjustmentEventSuccessfully() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentModel adjustmentEvent = new StockAdjustmentModel("event-999", "STORE-001", "PROD-001", 10, "Reposição", timestamp);
            
            when(eventProcessor.markProcessed("event-999", TOPIC, PARTITION, OFFSET)).thenReturn(true);
            when(validationHelper.storeExists("STORE-001")).thenReturn(true);
            when(validationHelper.productExists("PROD-001")).thenReturn(true);
            when(validationHelper.isActiveProduct("PROD-001", TOPIC)).thenReturn(true);
            
            doNothing().when(stockManager).saveStockAdjustment(eq("STORE-001"), eq("PROD-001"), eq(10), eq("Reposição"), any(Instant.class));
            doNothing().when(stockManager).adjustStock("STORE-001", "PROD-001", 10, TOPIC, true);

            // When
            inventoryEventUseCase.handleStockAdjustmentEvent(adjustmentEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-999", TOPIC, PARTITION, OFFSET);
            verify(validationHelper).storeExists("STORE-001");
            verify(validationHelper).productExists("PROD-001");
            verify(validationHelper).isActiveProduct("PROD-001", TOPIC);
            verify(stockManager).saveStockAdjustment(eq("STORE-001"), eq("PROD-001"), eq(10), eq("Reposição"), any(Instant.class));
            verify(stockManager).adjustStock("STORE-001", "PROD-001", 10, TOPIC, true);
        }

        @Test
        @DisplayName("Should handle duplicate stock adjustment event and skip processing")
        void shouldHandleDuplicateStockAdjustmentEventAndSkipProcessing() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentModel adjustmentEvent = new StockAdjustmentModel("event-999", "STORE-001", "PROD-001", 10, "Reposição", timestamp);
            when(eventProcessor.markProcessed("event-999", TOPIC, PARTITION, OFFSET)).thenReturn(false);
            doNothing().when(eventProcessor).handleDuplicate("ajuste de estoque", TOPIC, PARTITION, OFFSET);

            // When
            inventoryEventUseCase.handleStockAdjustmentEvent(adjustmentEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-999", TOPIC, PARTITION, OFFSET);
            verify(eventProcessor).handleDuplicate("ajuste de estoque", TOPIC, PARTITION, OFFSET);
            verify(validationHelper, never()).storeExists(any());
            verify(stockManager, never()).adjustStock(any(), any(), anyInt(), any(), anyBoolean());
        }

        @Test
        @DisplayName("Should handle null stock adjustment event")
        void shouldHandleNullStockAdjustmentEvent() {
            // Given
            when(eventProcessor.markProcessed(null, TOPIC, PARTITION, OFFSET)).thenReturn(true);

            // When
            inventoryEventUseCase.handleStockAdjustmentEvent(null, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed(null, TOPIC, PARTITION, OFFSET);
            verify(validationHelper, never()).storeExists(any());
            verify(stockManager, never()).adjustStock(any(), any(), anyInt(), any(), anyBoolean());
        }

        @Test
        @DisplayName("Should handle stock adjustment event with null store")
        void shouldHandleStockAdjustmentEventWithNullStore() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentModel adjustmentEvent = new StockAdjustmentModel("event-999", null, "PROD-001", 10, "Reposição", timestamp);
            when(eventProcessor.markProcessed("event-999", TOPIC, PARTITION, OFFSET)).thenReturn(true);

            // When
            inventoryEventUseCase.handleStockAdjustmentEvent(adjustmentEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-999", TOPIC, PARTITION, OFFSET);
            verify(validationHelper, never()).storeExists(any());
            verify(stockManager, never()).adjustStock(any(), any(), anyInt(), any(), anyBoolean());
        }

        @Test
        @DisplayName("Should handle stock adjustment event with null SKU")
        void shouldHandleStockAdjustmentEventWithNullSku() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentModel adjustmentEvent = new StockAdjustmentModel("event-999", "STORE-001", null, 10, "Reposição", timestamp);
            when(eventProcessor.markProcessed("event-999", TOPIC, PARTITION, OFFSET)).thenReturn(true);

            // When
            inventoryEventUseCase.handleStockAdjustmentEvent(adjustmentEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-999", TOPIC, PARTITION, OFFSET);
            verify(validationHelper, never()).storeExists(any());
            verify(stockManager, never()).adjustStock(any(), any(), anyInt(), any(), anyBoolean());
        }

        @Test
        @DisplayName("Should skip processing when store does not exist")
        void shouldSkipProcessingWhenStoreDoesNotExist() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentModel adjustmentEvent = new StockAdjustmentModel("event-999", "STORE-999", "PROD-001", 10, "Reposição", timestamp);
            
            when(eventProcessor.markProcessed("event-999", TOPIC, PARTITION, OFFSET)).thenReturn(true);
            when(validationHelper.storeExists("STORE-999")).thenReturn(false);
            when(validationHelper.productExists("PROD-001")).thenReturn(true);

            // When
            inventoryEventUseCase.handleStockAdjustmentEvent(adjustmentEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-999", TOPIC, PARTITION, OFFSET);
            verify(validationHelper).storeExists("STORE-999");
            verify(validationHelper).productExists("PROD-001");
            verify(stockManager, never()).adjustStock(any(), any(), anyInt(), any(), anyBoolean());
        }

        @Test
        @DisplayName("Should skip processing when product does not exist")
        void shouldSkipProcessingWhenProductDoesNotExist() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentModel adjustmentEvent = new StockAdjustmentModel("event-999", "STORE-001", "PROD-999", 10, "Reposição", timestamp);
            
            when(eventProcessor.markProcessed("event-999", TOPIC, PARTITION, OFFSET)).thenReturn(true);
            when(validationHelper.storeExists("STORE-001")).thenReturn(true);
            when(validationHelper.productExists("PROD-999")).thenReturn(false);

            // When
            inventoryEventUseCase.handleStockAdjustmentEvent(adjustmentEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-999", TOPIC, PARTITION, OFFSET);
            verify(validationHelper).storeExists("STORE-001");
            verify(validationHelper).productExists("PROD-999");
            verify(stockManager, never()).adjustStock(any(), any(), anyInt(), any(), anyBoolean());
        }

        @Test
        @DisplayName("Should skip processing when product is not active")
        void shouldSkipProcessingWhenProductIsNotActive() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentModel adjustmentEvent = new StockAdjustmentModel("event-999", "STORE-001", "PROD-001", 10, "Reposição", timestamp);
            
            when(eventProcessor.markProcessed("event-999", TOPIC, PARTITION, OFFSET)).thenReturn(true);
            when(validationHelper.storeExists("STORE-001")).thenReturn(true);
            when(validationHelper.productExists("PROD-001")).thenReturn(true);
            when(validationHelper.isActiveProduct("PROD-001", TOPIC)).thenReturn(false);

            // When
            inventoryEventUseCase.handleStockAdjustmentEvent(adjustmentEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(eventProcessor).markProcessed("event-999", TOPIC, PARTITION, OFFSET);
            verify(validationHelper).storeExists("STORE-001");
            verify(validationHelper).productExists("PROD-001");
            verify(validationHelper).isActiveProduct("PROD-001", TOPIC);
            verify(stockManager, never()).adjustStock(any(), any(), anyInt(), any(), anyBoolean());
        }

        @Test
        @DisplayName("Should handle stock adjustment event with null delta")
        void shouldHandleStockAdjustmentEventWithNullDelta() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            StockAdjustmentModel adjustmentEvent = new StockAdjustmentModel("event-999", "STORE-001", "PROD-001", null, "Reposição", timestamp);
            
            when(eventProcessor.markProcessed("event-999", TOPIC, PARTITION, OFFSET)).thenReturn(true);
            when(validationHelper.storeExists("STORE-001")).thenReturn(true);
            when(validationHelper.productExists("PROD-001")).thenReturn(true);
            when(validationHelper.isActiveProduct("PROD-001", TOPIC)).thenReturn(true);
            
            doNothing().when(stockManager).saveStockAdjustment(eq("STORE-001"), eq("PROD-001"), eq(0), eq("Reposição"), any(Instant.class));
            doNothing().when(stockManager).adjustStock("STORE-001", "PROD-001", 0, TOPIC, true);

            // When
            inventoryEventUseCase.handleStockAdjustmentEvent(adjustmentEvent, TOPIC, PARTITION, OFFSET);

            // Then
            verify(stockManager).saveStockAdjustment(eq("STORE-001"), eq("PROD-001"), eq(0), eq("Reposição"), any(Instant.class));
            verify(stockManager).adjustStock("STORE-001", "PROD-001", 0, TOPIC, true);
        }
    }
}