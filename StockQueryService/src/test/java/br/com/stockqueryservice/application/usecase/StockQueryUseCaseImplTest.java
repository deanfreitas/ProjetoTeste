package br.com.stockqueryservice.application.usecase;

import br.com.stockqueryservice.application.port.out.StockPort;
import br.com.stockqueryservice.domain.model.Stock;
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
@DisplayName("Stock Query Use Case Implementation Tests")
class StockQueryUseCaseImplTest {

    @Mock
    private StockPort stockPort;

    @InjectMocks
    private StockQueryUseCaseImpl stockQueryUseCase;

    private Stock stock1;
    private Stock stock2;
    private Stock stock3;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        stock1 = new Stock(1L, 100L, 10L, 50, now, "Product 1", "Store A");
        stock2 = new Stock(2L, 100L, 20L, 25, now, "Product 1", "Store B");
        stock3 = new Stock(3L, 200L, 10L, 75, now, "Product 2", "Store A");
    }

    @Nested
    @DisplayName("Get Stock by Product")
    class GetStockByProduct {

        @Test
        @DisplayName("Should return stock list when product has inventory in multiple stores")
        void shouldReturnStockListWhenProductHasInventoryInMultipleStores() {
            // Given
            Long productId = 100L;
            List<Stock> expectedStocks = Arrays.asList(stock1, stock2);
            when(stockPort.findByProductId(productId)).thenReturn(expectedStocks);

            // When
            List<Stock> result = stockQueryUseCase.getStockByProduct(productId);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactlyInAnyOrder(stock1, stock2);
            verify(stockPort).findByProductId(productId);
        }

        @Test
        @DisplayName("Should return empty list when product has no inventory")
        void shouldReturnEmptyListWhenProductHasNoInventory() {
            // Given
            Long productId = 999L;
            when(stockPort.findByProductId(productId)).thenReturn(List.of());

            // When
            List<Stock> result = stockQueryUseCase.getStockByProduct(productId);

            // Then
            assertThat(result).isEmpty();
            verify(stockPort).findByProductId(productId);
        }

        @Test
        @DisplayName("Should return single stock when product exists in only one store")
        void shouldReturnSingleStockWhenProductExistsInOnlyOneStore() {
            // Given
            Long productId = 200L;
            List<Stock> expectedStocks = List.of(stock3);
            when(stockPort.findByProductId(productId)).thenReturn(expectedStocks);

            // When
            List<Stock> result = stockQueryUseCase.getStockByProduct(productId);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result).contains(stock3);
            verify(stockPort).findByProductId(productId);
        }
    }

    @Nested
    @DisplayName("Get Stock by Product and Store")
    class GetStockByProductAndStore {

        @Test
        @DisplayName("Should return stock when product exists in specific store")
        void shouldReturnStockWhenProductExistsInSpecificStore() {
            // Given
            Long productId = 100L;
            Long storeId = 10L;
            when(stockPort.findByProductIdAndStoreId(productId, storeId)).thenReturn(Optional.of(stock1));

            // When
            Optional<Stock> result = stockQueryUseCase.getStockByProductAndStore(productId, storeId);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(stock1);
            verify(stockPort).findByProductIdAndStoreId(productId, storeId);
        }

        @Test
        @DisplayName("Should return empty when product does not exist in specific store")
        void shouldReturnEmptyWhenProductDoesNotExistInSpecificStore() {
            // Given
            Long productId = 100L;
            Long storeId = 999L;
            when(stockPort.findByProductIdAndStoreId(productId, storeId)).thenReturn(Optional.empty());

            // When
            Optional<Stock> result = stockQueryUseCase.getStockByProductAndStore(productId, storeId);

            // Then
            assertThat(result).isEmpty();
            verify(stockPort).findByProductIdAndStoreId(productId, storeId);
        }

        @Test
        @DisplayName("Should return empty when product does not exist")
        void shouldReturnEmptyWhenProductDoesNotExist() {
            // Given
            Long productId = 999L;
            Long storeId = 10L;
            when(stockPort.findByProductIdAndStoreId(productId, storeId)).thenReturn(Optional.empty());

            // When
            Optional<Stock> result = stockQueryUseCase.getStockByProductAndStore(productId, storeId);

            // Then
            assertThat(result).isEmpty();
            verify(stockPort).findByProductIdAndStoreId(productId, storeId);
        }
    }

    @Nested
    @DisplayName("Get Stock by Store")
    class GetStockByStore {

        @Test
        @DisplayName("Should return all stock items when store has multiple products")
        void shouldReturnAllStockItemsWhenStoreHasMultipleProducts() {
            // Given
            Long storeId = 10L;
            List<Stock> expectedStocks = Arrays.asList(stock1, stock3);
            when(stockPort.findByStoreId(storeId)).thenReturn(expectedStocks);

            // When
            List<Stock> result = stockQueryUseCase.getStockByStore(storeId);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactlyInAnyOrder(stock1, stock3);
            verify(stockPort).findByStoreId(storeId);
        }

        @Test
        @DisplayName("Should return empty list when store has no inventory")
        void shouldReturnEmptyListWhenStoreHasNoInventory() {
            // Given
            Long storeId = 999L;
            when(stockPort.findByStoreId(storeId)).thenReturn(List.of());

            // When
            List<Stock> result = stockQueryUseCase.getStockByStore(storeId);

            // Then
            assertThat(result).isEmpty();
            verify(stockPort).findByStoreId(storeId);
        }

        @Test
        @DisplayName("Should return single stock when store has only one product")
        void shouldReturnSingleStockWhenStoreHasOnlyOneProduct() {
            // Given
            Long storeId = 20L;
            List<Stock> expectedStocks = List.of(stock2);
            when(stockPort.findByStoreId(storeId)).thenReturn(expectedStocks);

            // When
            List<Stock> result = stockQueryUseCase.getStockByStore(storeId);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result).contains(stock2);
            verify(stockPort).findByStoreId(storeId);
        }
    }

    @Nested
    @DisplayName("Get All Stock")
    class GetAllStock {

        @Test
        @DisplayName("Should return all stock items from all stores and products")
        void shouldReturnAllStockItemsFromAllStoresAndProducts() {
            // Given
            List<Stock> expectedStocks = Arrays.asList(stock1, stock2, stock3);
            when(stockPort.findAll()).thenReturn(expectedStocks);

            // When
            List<Stock> result = stockQueryUseCase.getAllStock();

            // Then
            assertThat(result).hasSize(3);
            assertThat(result).containsExactlyInAnyOrder(stock1, stock2, stock3);
            verify(stockPort).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no stock exists")
        void shouldReturnEmptyListWhenNoStockExists() {
            // Given
            when(stockPort.findAll()).thenReturn(List.of());

            // When
            List<Stock> result = stockQueryUseCase.getAllStock();

            // Then
            assertThat(result).isEmpty();
            verify(stockPort).findAll();
        }
    }
}