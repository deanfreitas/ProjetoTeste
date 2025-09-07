package br.com.inventoryservice.application.usecase.helper;

import br.com.inventoryservice.application.port.out.StockAdjustmentPort;
import br.com.inventoryservice.application.port.out.StockPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StockManager Tests")
class StockManagerTest {

    @Mock
    private StockPort stockPort;

    @Mock
    private StockAdjustmentPort stockAdjustmentPort;

    @InjectMocks
    private StockManager stockManager;

    private static final String STORE_CODE = "STORE-001";
    private static final String SKU = "PROD-001";
    private static final String TOPIC = "test-topic";

    @Nested
    @DisplayName("Adjust Stock Tests")
    class AdjustStockTests {

        @Test
        @DisplayName("Should adjust stock with positive delta")
        void shouldAdjustStockWithPositiveDelta() {
            // Given
            int currentStock = 10;
            int delta = 5;
            int expectedNewStock = 15;
            
            ReflectionTestUtils.setField(stockManager, "allowNegative", false);
            when(stockPort.getQuantity(STORE_CODE, SKU)).thenReturn(currentStock);
            doNothing().when(stockPort).upsertQuantity(STORE_CODE, SKU, expectedNewStock);

            // When
            stockManager.adjustStock(STORE_CODE, SKU, delta, TOPIC, false);

            // Then
            verify(stockPort).getQuantity(STORE_CODE, SKU);
            verify(stockPort).upsertQuantity(STORE_CODE, SKU, expectedNewStock);
        }

        @Test
        @DisplayName("Should adjust stock with negative delta when result is positive")
        void shouldAdjustStockWithNegativeDeltaWhenResultIsPositive() {
            // Given
            int currentStock = 10;
            int delta = -3;
            int expectedNewStock = 7;
            
            ReflectionTestUtils.setField(stockManager, "allowNegative", false);
            when(stockPort.getQuantity(STORE_CODE, SKU)).thenReturn(currentStock);
            doNothing().when(stockPort).upsertQuantity(STORE_CODE, SKU, expectedNewStock);

            // When
            stockManager.adjustStock(STORE_CODE, SKU, delta, TOPIC, false);

            // Then
            verify(stockPort).getQuantity(STORE_CODE, SKU);
            verify(stockPort).upsertQuantity(STORE_CODE, SKU, expectedNewStock);
        }

        @Test
        @DisplayName("Should block negative stock when allowNegative is false")
        void shouldBlockNegativeStockWhenAllowNegativeIsFalse() {
            // Given
            int currentStock = 5;
            int delta = -10;
            
            ReflectionTestUtils.setField(stockManager, "allowNegative", false);
            when(stockPort.getQuantity(STORE_CODE, SKU)).thenReturn(currentStock);

            // When
            stockManager.adjustStock(STORE_CODE, SKU, delta, TOPIC, false);

            // Then
            verify(stockPort).getQuantity(STORE_CODE, SKU);
            verify(stockPort, never()).upsertQuantity(anyString(), anyString(), anyInt());
        }

        @Test
        @DisplayName("Should allow negative stock when allowNegative is true")
        void shouldAllowNegativeStockWhenAllowNegativeIsTrue() {
            // Given
            int currentStock = 5;
            int delta = -10;
            int expectedNewStock = -5;
            
            ReflectionTestUtils.setField(stockManager, "allowNegative", true);
            when(stockPort.getQuantity(STORE_CODE, SKU)).thenReturn(currentStock);
            doNothing().when(stockPort).upsertQuantity(STORE_CODE, SKU, expectedNewStock);

            // When
            stockManager.adjustStock(STORE_CODE, SKU, delta, TOPIC, false);

            // Then
            verify(stockPort).getQuantity(STORE_CODE, SKU);
            verify(stockPort).upsertQuantity(STORE_CODE, SKU, expectedNewStock);
        }

        @Test
        @DisplayName("Should skip adjustment when delta is zero and allowZeroDelta is false")
        void shouldSkipAdjustmentWhenDeltaIsZeroAndAllowZeroDeltaIsFalse() {
            // Given
            int delta = 0;

            // When
            stockManager.adjustStock(STORE_CODE, SKU, delta, TOPIC, false);

            // Then
            verify(stockPort, never()).getQuantity(anyString(), anyString());
            verify(stockPort, never()).upsertQuantity(anyString(), anyString(), anyInt());
        }

        @Test
        @DisplayName("Should process adjustment when delta is zero and allowZeroDelta is true")
        void shouldProcessAdjustmentWhenDeltaIsZeroAndAllowZeroDeltaIsTrue() {
            // Given
            int currentStock = 10;
            int delta = 0;
            int expectedNewStock = 10;
            
            ReflectionTestUtils.setField(stockManager, "allowNegative", false);
            when(stockPort.getQuantity(STORE_CODE, SKU)).thenReturn(currentStock);
            doNothing().when(stockPort).upsertQuantity(STORE_CODE, SKU, expectedNewStock);

            // When
            stockManager.adjustStock(STORE_CODE, SKU, delta, TOPIC, true);

            // Then
            verify(stockPort).getQuantity(STORE_CODE, SKU);
            verify(stockPort).upsertQuantity(STORE_CODE, SKU, expectedNewStock);
        }

        @Test
        @DisplayName("Should handle zero current stock with positive delta")
        void shouldHandleZeroCurrentStockWithPositiveDelta() {
            // Given
            int currentStock = 0;
            int delta = 5;
            int expectedNewStock = 5;
            
            ReflectionTestUtils.setField(stockManager, "allowNegative", false);
            when(stockPort.getQuantity(STORE_CODE, SKU)).thenReturn(currentStock);
            doNothing().when(stockPort).upsertQuantity(STORE_CODE, SKU, expectedNewStock);

            // When
            stockManager.adjustStock(STORE_CODE, SKU, delta, TOPIC, false);

            // Then
            verify(stockPort).getQuantity(STORE_CODE, SKU);
            verify(stockPort).upsertQuantity(STORE_CODE, SKU, expectedNewStock);
        }

        @Test
        @DisplayName("Should handle zero current stock with negative delta when allowNegative is true")
        void shouldHandleZeroCurrentStockWithNegativeDeltaWhenAllowNegativeIsTrue() {
            // Given
            int currentStock = 0;
            int delta = -3;
            int expectedNewStock = -3;
            
            ReflectionTestUtils.setField(stockManager, "allowNegative", true);
            when(stockPort.getQuantity(STORE_CODE, SKU)).thenReturn(currentStock);
            doNothing().when(stockPort).upsertQuantity(STORE_CODE, SKU, expectedNewStock);

            // When
            stockManager.adjustStock(STORE_CODE, SKU, delta, TOPIC, false);

            // Then
            verify(stockPort).getQuantity(STORE_CODE, SKU);
            verify(stockPort).upsertQuantity(STORE_CODE, SKU, expectedNewStock);
        }

        @Test
        @DisplayName("Should block zero current stock with negative delta when allowNegative is false")
        void shouldBlockZeroCurrentStockWithNegativeDeltaWhenAllowNegativeIsFalse() {
            // Given
            int currentStock = 0;
            int delta = -3;
            
            ReflectionTestUtils.setField(stockManager, "allowNegative", false);
            when(stockPort.getQuantity(STORE_CODE, SKU)).thenReturn(currentStock);

            // When
            stockManager.adjustStock(STORE_CODE, SKU, delta, TOPIC, false);

            // Then
            verify(stockPort).getQuantity(STORE_CODE, SKU);
            verify(stockPort, never()).upsertQuantity(anyString(), anyString(), anyInt());
        }

        @Test
        @DisplayName("Should handle exact zero result")
        void shouldHandleExactZeroResult() {
            // Given
            int currentStock = 5;
            int delta = -5;
            int expectedNewStock = 0;
            
            ReflectionTestUtils.setField(stockManager, "allowNegative", false);
            when(stockPort.getQuantity(STORE_CODE, SKU)).thenReturn(currentStock);
            doNothing().when(stockPort).upsertQuantity(STORE_CODE, SKU, expectedNewStock);

            // When
            stockManager.adjustStock(STORE_CODE, SKU, delta, TOPIC, false);

            // Then
            verify(stockPort).getQuantity(STORE_CODE, SKU);
            verify(stockPort).upsertQuantity(STORE_CODE, SKU, expectedNewStock);
        }
    }

    @Nested
    @DisplayName("Save Stock Adjustment Tests")
    class SaveStockAdjustmentTests {

        @Test
        @DisplayName("Should save stock adjustment with all parameters")
        void shouldSaveStockAdjustmentWithAllParameters() {
            // Given
            String loja = "STORE-001";
            String sku = "PROD-001";
            int delta = 10;
            String motivo = "Reposição";
            Instant timestamp = Instant.now();
            
            doNothing().when(stockAdjustmentPort).saveAdjustment(loja, sku, delta, motivo, timestamp);

            // When
            stockManager.saveStockAdjustment(loja, sku, delta, motivo, timestamp);

            // Then
            verify(stockAdjustmentPort).saveAdjustment(loja, sku, delta, motivo, timestamp);
        }

        @Test
        @DisplayName("Should save stock adjustment with negative delta")
        void shouldSaveStockAdjustmentWithNegativeDelta() {
            // Given
            String loja = "STORE-002";
            String sku = "PROD-002";
            int delta = -5;
            String motivo = "Correção";
            Instant timestamp = Instant.now();
            
            doNothing().when(stockAdjustmentPort).saveAdjustment(loja, sku, delta, motivo, timestamp);

            // When
            stockManager.saveStockAdjustment(loja, sku, delta, motivo, timestamp);

            // Then
            verify(stockAdjustmentPort).saveAdjustment(loja, sku, delta, motivo, timestamp);
        }

        @Test
        @DisplayName("Should save stock adjustment with zero delta")
        void shouldSaveStockAdjustmentWithZeroDelta() {
            // Given
            String loja = "STORE-003";
            String sku = "PROD-003";
            int delta = 0;
            String motivo = "Auditoria";
            Instant timestamp = Instant.now();
            
            doNothing().when(stockAdjustmentPort).saveAdjustment(loja, sku, delta, motivo, timestamp);

            // When
            stockManager.saveStockAdjustment(loja, sku, delta, motivo, timestamp);

            // Then
            verify(stockAdjustmentPort).saveAdjustment(loja, sku, delta, motivo, timestamp);
        }

        @Test
        @DisplayName("Should save stock adjustment with null motivo")
        void shouldSaveStockAdjustmentWithNullMotivo() {
            // Given
            String loja = "STORE-004";
            String sku = "PROD-004";
            int delta = 15;
            String motivo = null;
            Instant timestamp = Instant.now();
            
            doNothing().when(stockAdjustmentPort).saveAdjustment(loja, sku, delta, motivo, timestamp);

            // When
            stockManager.saveStockAdjustment(loja, sku, delta, motivo, timestamp);

            // Then
            verify(stockAdjustmentPort).saveAdjustment(loja, sku, delta, motivo, timestamp);
        }

        @Test
        @DisplayName("Should save stock adjustment with empty motivo")
        void shouldSaveStockAdjustmentWithEmptyMotivo() {
            // Given
            String loja = "STORE-005";
            String sku = "PROD-005";
            int delta = 20;
            String motivo = "";
            Instant timestamp = Instant.now();
            
            doNothing().when(stockAdjustmentPort).saveAdjustment(loja, sku, delta, motivo, timestamp);

            // When
            stockManager.saveStockAdjustment(loja, sku, delta, motivo, timestamp);

            // Then
            verify(stockAdjustmentPort).saveAdjustment(loja, sku, delta, motivo, timestamp);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle multiple stock adjustments correctly")
        void shouldHandleMultipleStockAdjustmentsCorrectly() {
            // Given
            ReflectionTestUtils.setField(stockManager, "allowNegative", false);
            
            // First adjustment
            when(stockPort.getQuantity(STORE_CODE, SKU)).thenReturn(10);
            doNothing().when(stockPort).upsertQuantity(STORE_CODE, SKU, 15);

            // When - First adjustment
            stockManager.adjustStock(STORE_CODE, SKU, 5, TOPIC, false);

            // Given - Second adjustment 
            when(stockPort.getQuantity(STORE_CODE, SKU)).thenReturn(15);
            doNothing().when(stockPort).upsertQuantity(STORE_CODE, SKU, 12);

            // When - Second adjustment
            stockManager.adjustStock(STORE_CODE, SKU, -3, TOPIC, false);

            // Then
            verify(stockPort, times(2)).getQuantity(STORE_CODE, SKU);
            verify(stockPort).upsertQuantity(STORE_CODE, SKU, 15);
            verify(stockPort).upsertQuantity(STORE_CODE, SKU, 12);
        }

        @Test
        @DisplayName("Should handle different store and SKU combinations")
        void shouldHandleDifferentStoreAndSkuCombinations() {
            // Given
            String store1 = "STORE-001";
            String store2 = "STORE-002";
            String sku1 = "PROD-001";
            String sku2 = "PROD-002";
            
            ReflectionTestUtils.setField(stockManager, "allowNegative", true);
            
            when(stockPort.getQuantity(store1, sku1)).thenReturn(10);
            when(stockPort.getQuantity(store1, sku2)).thenReturn(5);
            when(stockPort.getQuantity(store2, sku1)).thenReturn(0);
            
            doNothing().when(stockPort).upsertQuantity(store1, sku1, 15);
            doNothing().when(stockPort).upsertQuantity(store1, sku2, 2);
            doNothing().when(stockPort).upsertQuantity(store2, sku1, -2);

            // When
            stockManager.adjustStock(store1, sku1, 5, TOPIC, false);
            stockManager.adjustStock(store1, sku2, -3, TOPIC, false);
            stockManager.adjustStock(store2, sku1, -2, TOPIC, false);

            // Then
            verify(stockPort).getQuantity(store1, sku1);
            verify(stockPort).getQuantity(store1, sku2);
            verify(stockPort).getQuantity(store2, sku1);
            verify(stockPort).upsertQuantity(store1, sku1, 15);
            verify(stockPort).upsertQuantity(store1, sku2, 2);
            verify(stockPort).upsertQuantity(store2, sku1, -2);
        }
    }
}