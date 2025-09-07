package br.com.inventoryservice.adapters.out.persistence.portimpl;

import br.com.inventoryservice.adapters.out.persistence.entity.StockEntity;
import br.com.inventoryservice.adapters.out.persistence.entity.StockId;
import br.com.inventoryservice.adapters.out.persistence.repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StockAdapter Tests")
class StockAdapterTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockAdapter stockAdapter;

    @Nested
    @DisplayName("GetQuantity Method Tests")
    class GetQuantityTests {

        @Test
        @DisplayName("Should return stock quantity when stock exists")
        void shouldReturnStockQuantityWhenStockExists() {
            // Given
            String lojaCodigo = "LOJA001";
            String sku = "PROD001";
            int expectedQuantity = 10;

            StockId stockId = StockId.builder()
                    .lojaCodigo(lojaCodigo)
                    .produtoSku(sku)
                    .build();

            StockEntity stockEntity = StockEntity.builder()
                    .id(stockId)
                    .quantidade(expectedQuantity)
                    .build();

            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.of(stockEntity));

            // When
            int result = stockAdapter.getQuantity(lojaCodigo, sku);

            // Then
            assertEquals(expectedQuantity, result);
            verify(stockRepository).findById(any(StockId.class));
        }

        @Test
        @DisplayName("Should return zero when stock does not exist")
        void shouldReturnZeroWhenStockDoesNotExist() {
            // Given
            String lojaCodigo = "LOJA002";
            String sku = "PROD002";

            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.empty());

            // When
            int result = stockAdapter.getQuantity(lojaCodigo, sku);

            // Then
            assertEquals(0, result);
            verify(stockRepository).findById(any(StockId.class));
        }

        @Test
        @DisplayName("Should return zero when stock quantity is null")
        void shouldReturnZeroWhenStockQuantityIsNull() {
            // Given
            String lojaCodigo = "LOJA003";
            String sku = "PROD003";

            StockId stockId = StockId.builder()
                    .lojaCodigo(lojaCodigo)
                    .produtoSku(sku)
                    .build();

            StockEntity stockEntity = StockEntity.builder()
                    .id(stockId)
                    .quantidade(null)
                    .build();

            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.of(stockEntity));

            // When
            int result = stockAdapter.getQuantity(lojaCodigo, sku);

            // Then
            assertEquals(0, result);
            verify(stockRepository).findById(any(StockId.class));
        }

        @Test
        @DisplayName("Should handle null lojaCodigo parameter")
        void shouldHandleNullLojaCodigoParameter() {
            // Given
            String lojaCodigo = null;
            String sku = "PROD004";

            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.empty());

            // When
            int result = stockAdapter.getQuantity(lojaCodigo, sku);

            // Then
            assertEquals(0, result);
            verify(stockRepository).findById(any(StockId.class));
        }

        @Test
        @DisplayName("Should handle null sku parameter")
        void shouldHandleNullSkuParameter() {
            // Given
            String lojaCodigo = "LOJA005";
            String sku = null;

            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.empty());

            // When
            int result = stockAdapter.getQuantity(lojaCodigo, sku);

            // Then
            assertEquals(0, result);
            verify(stockRepository).findById(any(StockId.class));
        }

        @Test
        @DisplayName("Should return negative quantity when stock has negative value")
        void shouldReturnNegativeQuantityWhenStockHasNegativeValue() {
            // Given
            String lojaCodigo = "LOJA006";
            String sku = "PROD006";
            int expectedQuantity = -5;

            StockId stockId = StockId.builder()
                    .lojaCodigo(lojaCodigo)
                    .produtoSku(sku)
                    .build();

            StockEntity stockEntity = StockEntity.builder()
                    .id(stockId)
                    .quantidade(expectedQuantity)
                    .build();

            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.of(stockEntity));

            // When
            int result = stockAdapter.getQuantity(lojaCodigo, sku);

            // Then
            assertEquals(expectedQuantity, result);
            verify(stockRepository).findById(any(StockId.class));
        }
    }

    @Nested
    @DisplayName("UpsertQuantity Method Tests")
    class UpsertQuantityTests {

        @Test
        @DisplayName("Should update existing stock quantity")
        void shouldUpdateExistingStockQuantity() {
            // Given
            String lojaCodigo = "LOJA001";
            String sku = "PROD001";
            int newQuantity = 20;

            StockId stockId = StockId.builder()
                    .lojaCodigo(lojaCodigo)
                    .produtoSku(sku)
                    .build();

            StockEntity existingStock = StockEntity.builder()
                    .id(stockId)
                    .quantidade(10)
                    .build();

            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.of(existingStock));
            when(stockRepository.save(any(StockEntity.class))).thenReturn(existingStock);

            // When
            stockAdapter.upsertQuantity(lojaCodigo, sku, newQuantity);

            // Then
            ArgumentCaptor<StockEntity> stockCaptor = ArgumentCaptor.forClass(StockEntity.class);
            verify(stockRepository).save(stockCaptor.capture());
            StockEntity savedStock = stockCaptor.getValue();
            assertEquals(newQuantity, savedStock.getQuantidade());
            assertEquals(stockId, savedStock.getId());
        }

        @Test
        @DisplayName("Should create new stock when stock does not exist")
        void shouldCreateNewStockWhenStockDoesNotExist() {
            // Given
            String lojaCodigo = "LOJA002";
            String sku = "PROD002";
            int quantity = 15;

            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.empty());
            when(stockRepository.save(any(StockEntity.class))).thenReturn(any(StockEntity.class));

            // When
            stockAdapter.upsertQuantity(lojaCodigo, sku, quantity);

            // Then
            ArgumentCaptor<StockEntity> stockCaptor = ArgumentCaptor.forClass(StockEntity.class);
            verify(stockRepository).save(stockCaptor.capture());
            StockEntity savedStock = stockCaptor.getValue();
            assertEquals(quantity, savedStock.getQuantidade());
            assertEquals(lojaCodigo, savedStock.getId().getLojaCodigo());
            assertEquals(sku, savedStock.getId().getProdutoSku());
        }

        @Test
        @DisplayName("Should handle zero quantity")
        void shouldHandleZeroQuantity() {
            // Given
            String lojaCodigo = "LOJA003";
            String sku = "PROD003";
            int quantity = 0;

            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.empty());
            when(stockRepository.save(any(StockEntity.class))).thenReturn(any(StockEntity.class));

            // When
            stockAdapter.upsertQuantity(lojaCodigo, sku, quantity);

            // Then
            ArgumentCaptor<StockEntity> stockCaptor = ArgumentCaptor.forClass(StockEntity.class);
            verify(stockRepository).save(stockCaptor.capture());
            StockEntity savedStock = stockCaptor.getValue();
            assertEquals(quantity, savedStock.getQuantidade());
        }

        @Test
        @DisplayName("Should handle negative quantity")
        void shouldHandleNegativeQuantity() {
            // Given
            String lojaCodigo = "LOJA004";
            String sku = "PROD004";
            int quantity = -10;

            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.empty());
            when(stockRepository.save(any(StockEntity.class))).thenReturn(any(StockEntity.class));

            // When
            stockAdapter.upsertQuantity(lojaCodigo, sku, quantity);

            // Then
            ArgumentCaptor<StockEntity> stockCaptor = ArgumentCaptor.forClass(StockEntity.class);
            verify(stockRepository).save(stockCaptor.capture());
            StockEntity savedStock = stockCaptor.getValue();
            assertEquals(quantity, savedStock.getQuantidade());
        }

        @Test
        @DisplayName("Should handle null lojaCodigo when upserting")
        void shouldHandleNullLojaCodigoWhenUpserting() {
            // Given
            String lojaCodigo = null;
            String sku = "PROD005";
            int quantity = 5;

            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.empty());
            when(stockRepository.save(any(StockEntity.class))).thenReturn(any(StockEntity.class));

            // When
            assertDoesNotThrow(() -> stockAdapter.upsertQuantity(lojaCodigo, sku, quantity));

            // Then
            verify(stockRepository).save(any(StockEntity.class));
        }

        @Test
        @DisplayName("Should handle null sku when upserting")
        void shouldHandleNullSkuWhenUpserting() {
            // Given
            String lojaCodigo = "LOJA006";
            String sku = null;
            int quantity = 5;

            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.empty());
            when(stockRepository.save(any(StockEntity.class))).thenReturn(any(StockEntity.class));

            // When
            assertDoesNotThrow(() -> stockAdapter.upsertQuantity(lojaCodigo, sku, quantity));

            // Then
            verify(stockRepository).save(any(StockEntity.class));
        }

        @Test
        @DisplayName("Should replace existing quantity completely")
        void shouldReplaceExistingQuantityCompletely() {
            // Given
            String lojaCodigo = "LOJA007";
            String sku = "PROD007";
            int originalQuantity = 100;
            int newQuantity = 1;

            StockId stockId = StockId.builder()
                    .lojaCodigo(lojaCodigo)
                    .produtoSku(sku)
                    .build();

            StockEntity existingStock = StockEntity.builder()
                    .id(stockId)
                    .quantidade(originalQuantity)
                    .build();

            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.of(existingStock));
            when(stockRepository.save(any(StockEntity.class))).thenReturn(existingStock);

            // When
            stockAdapter.upsertQuantity(lojaCodigo, sku, newQuantity);

            // Then
            ArgumentCaptor<StockEntity> stockCaptor = ArgumentCaptor.forClass(StockEntity.class);
            verify(stockRepository).save(stockCaptor.capture());
            StockEntity savedStock = stockCaptor.getValue();
            assertEquals(newQuantity, savedStock.getQuantidade());
            assertNotEquals(originalQuantity, savedStock.getQuantidade());
        }
    }
}