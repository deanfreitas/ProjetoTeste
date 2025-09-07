package br.com.stockqueryservice.infrastructure.adapters.out.persistence.portimpl;

import br.com.stockqueryservice.domain.model.Stock;
import br.com.stockqueryservice.infrastructure.adapters.out.persistence.entity.StockEntity;
import br.com.stockqueryservice.infrastructure.adapters.out.persistence.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Stock Port Adapter Tests")
class StockPortAdapterTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockPortAdapter stockPortAdapter;

    private StockEntity stockEntity1;
    private StockEntity stockEntity2;
    private StockEntity stockEntity3;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        stockEntity1 = createStockEntity(1L, 100L, 10L, 50, now, "Product 1", "Store A");
        stockEntity2 = createStockEntity(2L, 100L, 20L, 25, now, "Product 1", "Store B");
        stockEntity3 = createStockEntity(3L, 200L, 10L, 75, now, "Product 2", "Store A");
    }

    private StockEntity createStockEntity(Long id, Long productId, Long storeId, Integer quantity,
                                          LocalDateTime lastUpdated, String productName, String storeName) {
        StockEntity entity = new StockEntity();
        entity.setId(id);
        entity.setProductId(productId);
        entity.setStoreId(storeId);
        entity.setQuantity(quantity);
        entity.setLastUpdated(lastUpdated);
        entity.setProductName(productName);
        entity.setStoreName(storeName);
        return entity;
    }

    @Nested
    @DisplayName("Find by Product ID")
    class FindByProductId {

        @Test
        @DisplayName("Should return domain models when product exists in multiple stores")
        void shouldReturnDomainModelsWhenProductExistsInMultipleStores() {
            // Given
            Long productId = 100L;
            List<StockEntity> entities = Arrays.asList(stockEntity1, stockEntity2);
            when(stockRepository.findByProductId(productId)).thenReturn(entities);

            // When
            List<Stock> result = stockPortAdapter.findByProductId(productId);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allSatisfy(stock -> {
                assertThat(stock.productId()).isEqualTo(productId);
                assertThat(stock.productName()).isEqualTo("Product 1");
            });
            verify(stockRepository).findByProductId(productId);
        }

        @Test
        @DisplayName("Should return empty list when product has no inventory")
        void shouldReturnEmptyListWhenProductHasNoInventory() {
            // Given
            Long productId = 999L;
            when(stockRepository.findByProductId(productId)).thenReturn(List.of());

            // When
            List<Stock> result = stockPortAdapter.findByProductId(productId);

            // Then
            assertThat(result).isEmpty();
            verify(stockRepository).findByProductId(productId);
        }

        @Test
        @DisplayName("Should return single domain model when product exists in one store")
        void shouldReturnSingleDomainModelWhenProductExistsInOneStore() {
            // Given
            Long productId = 200L;
            List<StockEntity> entities = List.of(stockEntity3);
            when(stockRepository.findByProductId(productId)).thenReturn(entities);

            // When
            List<Stock> result = stockPortAdapter.findByProductId(productId);

            // Then
            assertThat(result).hasSize(1);
            Stock stock = result.get(0);
            assertThat(stock.id()).isEqualTo(stockEntity3.getId());
            assertThat(stock.productId()).isEqualTo(stockEntity3.getProductId());
            assertThat(stock.storeId()).isEqualTo(stockEntity3.getStoreId());
            assertThat(stock.quantity()).isEqualTo(stockEntity3.getQuantity());
            assertThat(stock.productName()).isEqualTo(stockEntity3.getProductName());
            assertThat(stock.storeName()).isEqualTo(stockEntity3.getStoreName());
            verify(stockRepository).findByProductId(productId);
        }
    }

    @Nested
    @DisplayName("Find by Product ID and Store ID")
    class FindByProductIdAndStoreId {

        @Test
        @DisplayName("Should return domain model when product exists in specific store")
        void shouldReturnDomainModelWhenProductExistsInSpecificStore() {
            // Given
            Long productId = 100L;
            Long storeId = 10L;
            when(stockRepository.findByProductIdAndStoreId(productId, storeId))
                    .thenReturn(Optional.of(stockEntity1));

            // When
            Optional<Stock> result = stockPortAdapter.findByProductIdAndStoreId(productId, storeId);

            // Then
            assertThat(result).isPresent();
            Stock stock = result.get();
            assertThat(stock.id()).isEqualTo(stockEntity1.getId());
            assertThat(stock.productId()).isEqualTo(stockEntity1.getProductId());
            assertThat(stock.storeId()).isEqualTo(stockEntity1.getStoreId());
            assertThat(stock.quantity()).isEqualTo(stockEntity1.getQuantity());
            assertThat(stock.productName()).isEqualTo(stockEntity1.getProductName());
            assertThat(stock.storeName()).isEqualTo(stockEntity1.getStoreName());
            verify(stockRepository).findByProductIdAndStoreId(productId, storeId);
        }

        @Test
        @DisplayName("Should return empty when product does not exist in specific store")
        void shouldReturnEmptyWhenProductDoesNotExistInSpecificStore() {
            // Given
            Long productId = 100L;
            Long storeId = 999L;
            when(stockRepository.findByProductIdAndStoreId(productId, storeId))
                    .thenReturn(Optional.empty());

            // When
            Optional<Stock> result = stockPortAdapter.findByProductIdAndStoreId(productId, storeId);

            // Then
            assertThat(result).isEmpty();
            verify(stockRepository).findByProductIdAndStoreId(productId, storeId);
        }

        @Test
        @DisplayName("Should return empty when product does not exist at all")
        void shouldReturnEmptyWhenProductDoesNotExistAtAll() {
            // Given
            Long productId = 999L;
            Long storeId = 10L;
            when(stockRepository.findByProductIdAndStoreId(productId, storeId))
                    .thenReturn(Optional.empty());

            // When
            Optional<Stock> result = stockPortAdapter.findByProductIdAndStoreId(productId, storeId);

            // Then
            assertThat(result).isEmpty();
            verify(stockRepository).findByProductIdAndStoreId(productId, storeId);
        }
    }

    @Nested
    @DisplayName("Find by Store ID")
    class FindByStoreId {

        @Test
        @DisplayName("Should return all products when store has multiple products")
        void shouldReturnAllProductsWhenStoreHasMultipleProducts() {
            // Given
            Long storeId = 10L;
            List<StockEntity> entities = Arrays.asList(stockEntity1, stockEntity3);
            when(stockRepository.findByStoreId(storeId)).thenReturn(entities);

            // When
            List<Stock> result = stockPortAdapter.findByStoreId(storeId);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allSatisfy(stock -> {
                assertThat(stock.storeId()).isEqualTo(storeId);
                assertThat(stock.storeName()).isEqualTo("Store A");
            });
            assertThat(result).extracting(Stock::productId).containsExactlyInAnyOrder(100L, 200L);
            verify(stockRepository).findByStoreId(storeId);
        }

        @Test
        @DisplayName("Should return empty list when store has no inventory")
        void shouldReturnEmptyListWhenStoreHasNoInventory() {
            // Given
            Long storeId = 999L;
            when(stockRepository.findByStoreId(storeId)).thenReturn(List.of());

            // When
            List<Stock> result = stockPortAdapter.findByStoreId(storeId);

            // Then
            assertThat(result).isEmpty();
            verify(stockRepository).findByStoreId(storeId);
        }

        @Test
        @DisplayName("Should return single product when store has only one product")
        void shouldReturnSingleProductWhenStoreHasOnlyOneProduct() {
            // Given
            Long storeId = 20L;
            List<StockEntity> entities = List.of(stockEntity2);
            when(stockRepository.findByStoreId(storeId)).thenReturn(entities);

            // When
            List<Stock> result = stockPortAdapter.findByStoreId(storeId);

            // Then
            assertThat(result).hasSize(1);
            Stock stock = result.get(0);
            assertThat(stock.storeId()).isEqualTo(20L);
            assertThat(stock.storeName()).isEqualTo("Store B");
            assertThat(stock.productId()).isEqualTo(100L);
            verify(stockRepository).findByStoreId(storeId);
        }
    }

    @Nested
    @DisplayName("Find All")
    class FindAll {

        @Test
        @DisplayName("Should return all stock from all stores and products")
        void shouldReturnAllStockFromAllStoresAndProducts() {
            // Given
            List<StockEntity> entities = Arrays.asList(stockEntity1, stockEntity2, stockEntity3);
            when(stockRepository.findAll()).thenReturn(entities);

            // When
            List<Stock> result = stockPortAdapter.findAll();

            // Then
            assertThat(result).hasSize(3);
            assertThat(result).extracting(Stock::id).containsExactlyInAnyOrder(1L, 2L, 3L);
            assertThat(result).extracting(Stock::productId).containsExactlyInAnyOrder(100L, 100L, 200L);
            assertThat(result).extracting(Stock::storeId).containsExactlyInAnyOrder(10L, 20L, 10L);
            verify(stockRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no stock exists")
        void shouldReturnEmptyListWhenNoStockExists() {
            // Given
            when(stockRepository.findAll()).thenReturn(List.of());

            // When
            List<Stock> result = stockPortAdapter.findAll();

            // Then
            assertThat(result).isEmpty();
            verify(stockRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Entity to Domain Model Mapping")
    class EntityToDomainModelMapping {

        @Test
        @DisplayName("Should correctly map all entity fields to domain model")
        void shouldCorrectlyMapAllEntityFieldsToDomainModel() {
            // Given
            Long storeId = 10L;
            List<StockEntity> entities = List.of(stockEntity1);
            when(stockRepository.findByStoreId(storeId)).thenReturn(entities);

            // When
            List<Stock> result = stockPortAdapter.findByStoreId(storeId);

            // Then
            assertThat(result).hasSize(1);
            Stock stock = result.get(0);

            // Verify all fields are correctly mapped
            assertThat(stock.id()).isEqualTo(stockEntity1.getId());
            assertThat(stock.productId()).isEqualTo(stockEntity1.getProductId());
            assertThat(stock.storeId()).isEqualTo(stockEntity1.getStoreId());
            assertThat(stock.quantity()).isEqualTo(stockEntity1.getQuantity());
            assertThat(stock.lastUpdated()).isEqualTo(stockEntity1.getLastUpdated());
            assertThat(stock.productName()).isEqualTo(stockEntity1.getProductName());
            assertThat(stock.storeName()).isEqualTo(stockEntity1.getStoreName());
        }

        @Test
        @DisplayName("Should handle null values gracefully in entity mapping")
        void shouldHandleNullValuesGracefullyInEntityMapping() {
            // Given
            StockEntity entityWithNulls = createStockEntity(4L, 300L, 30L, 0, null, null, null);
            Long storeId = 30L;
            when(stockRepository.findByStoreId(storeId)).thenReturn(List.of(entityWithNulls));

            // When
            List<Stock> result = stockPortAdapter.findByStoreId(storeId);

            // Then
            assertThat(result).hasSize(1);
            Stock stock = result.get(0);
            assertThat(stock.id()).isEqualTo(4L);
            assertThat(stock.productId()).isEqualTo(300L);
            assertThat(stock.storeId()).isEqualTo(30L);
            assertThat(stock.quantity()).isEqualTo(0);
            assertThat(stock.lastUpdated()).isNull();
            assertThat(stock.productName()).isNull();
            assertThat(stock.storeName()).isNull();
        }
    }
}