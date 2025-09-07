package br.com.stockqueryservice.infrastructure.adapters.in.web;

import br.com.stockqueryservice.application.port.in.StockQueryUseCase;
import br.com.stockqueryservice.domain.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Stock Controller Tests")
class StockControllerTest {

    @Mock
    private StockQueryUseCase stockQueryUseCase;

    @InjectMocks
    private StockController stockController;

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
    @DisplayName("Get Stock Endpoint")
    class GetStockEndpoint {

        @Nested
        @DisplayName("With Product ID Only")
        class WithProductIdOnly {

            @Test
            @DisplayName("Should return stock list when product exists in multiple stores")
            void shouldReturnStockListWhenProductExistsInMultipleStores() {
                // Given
                Long productId = 100L;
                List<Stock> expectedStocks = Arrays.asList(stock1, stock2);
                when(stockQueryUseCase.getStockByProduct(productId)).thenReturn(expectedStocks);

                // When
                ResponseEntity<List<Stock>> response = stockController.getStock(productId, null);

                // Then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).hasSize(2);
                assertThat(response.getBody()).containsExactlyInAnyOrder(stock1, stock2);
                verify(stockQueryUseCase).getStockByProduct(productId);
            }

            @Test
            @DisplayName("Should return empty list when product has no inventory")
            void shouldReturnEmptyListWhenProductHasNoInventory() {
                // Given
                Long productId = 999L;
                when(stockQueryUseCase.getStockByProduct(productId)).thenReturn(List.of());

                // When
                ResponseEntity<List<Stock>> response = stockController.getStock(productId, null);

                // Then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).isEmpty();
                verify(stockQueryUseCase).getStockByProduct(productId);
            }

            @Test
            @DisplayName("Should return single stock when product exists in one store only")
            void shouldReturnSingleStockWhenProductExistsInOneStoreOnly() {
                // Given
                Long productId = 200L;
                List<Stock> expectedStocks = List.of(stock3);
                when(stockQueryUseCase.getStockByProduct(productId)).thenReturn(expectedStocks);

                // When
                ResponseEntity<List<Stock>> response = stockController.getStock(productId, null);

                // Then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).hasSize(1);
                assertThat(response.getBody()).contains(stock3);
                verify(stockQueryUseCase).getStockByProduct(productId);
            }
        }

        @Nested
        @DisplayName("With Product ID and Store ID")
        class WithProductIdAndStoreId {

            @Test
            @DisplayName("Should return stock list with single item when product exists in specific store")
            void shouldReturnStockListWithSingleItemWhenProductExistsInSpecificStore() {
                // Given
                Long productId = 100L;
                Long storeId = 10L;
                when(stockQueryUseCase.getStockByProductAndStore(productId, storeId)).thenReturn(Optional.of(stock1));

                // When
                ResponseEntity<List<Stock>> response = stockController.getStock(productId, storeId);

                // Then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).hasSize(1);
                assertThat(response.getBody()).contains(stock1);
                verify(stockQueryUseCase).getStockByProductAndStore(productId, storeId);
            }

            @Test
            @DisplayName("Should return empty list when product does not exist in specific store")
            void shouldReturnEmptyListWhenProductDoesNotExistInSpecificStore() {
                // Given
                Long productId = 100L;
                Long storeId = 999L;
                when(stockQueryUseCase.getStockByProductAndStore(productId, storeId)).thenReturn(Optional.empty());

                // When
                ResponseEntity<List<Stock>> response = stockController.getStock(productId, storeId);

                // Then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).isEmpty();
                verify(stockQueryUseCase).getStockByProductAndStore(productId, storeId);
            }

            @Test
            @DisplayName("Should return empty list when product does not exist at all")
            void shouldReturnEmptyListWhenProductDoesNotExistAtAll() {
                // Given
                Long productId = 999L;
                Long storeId = 10L;
                when(stockQueryUseCase.getStockByProductAndStore(productId, storeId)).thenReturn(Optional.empty());

                // When
                ResponseEntity<List<Stock>> response = stockController.getStock(productId, storeId);

                // Then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).isEmpty();
                verify(stockQueryUseCase).getStockByProductAndStore(productId, storeId);
            }
        }
    }

    @Nested
    @DisplayName("Get Stock by Store Endpoint")
    class GetStockByStoreEndpoint {

        @Test
        @DisplayName("Should return all products in store when store has multiple products")
        void shouldReturnAllProductsInStoreWhenStoreHasMultipleProducts() {
            // Given
            Long storeId = 10L;
            List<Stock> expectedStocks = Arrays.asList(stock1, stock3);
            when(stockQueryUseCase.getStockByStore(storeId)).thenReturn(expectedStocks);

            // When
            ResponseEntity<List<Stock>> response = stockController.getStockByStore(storeId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(2);
            assertThat(response.getBody()).containsExactlyInAnyOrder(stock1, stock3);
            verify(stockQueryUseCase).getStockByStore(storeId);
        }

        @Test
        @DisplayName("Should return single product when store has only one product")
        void shouldReturnSingleProductWhenStoreHasOnlyOneProduct() {
            // Given
            Long storeId = 20L;
            List<Stock> expectedStocks = List.of(stock2);
            when(stockQueryUseCase.getStockByStore(storeId)).thenReturn(expectedStocks);

            // When
            ResponseEntity<List<Stock>> response = stockController.getStockByStore(storeId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(1);
            assertThat(response.getBody()).contains(stock2);
            verify(stockQueryUseCase).getStockByStore(storeId);
        }

        @Test
        @DisplayName("Should return empty list when store has no inventory")
        void shouldReturnEmptyListWhenStoreHasNoInventory() {
            // Given
            Long storeId = 999L;
            when(stockQueryUseCase.getStockByStore(storeId)).thenReturn(List.of());

            // When
            ResponseEntity<List<Stock>> response = stockController.getStockByStore(storeId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();
            verify(stockQueryUseCase).getStockByStore(storeId);
        }

        @Test
        @DisplayName("Should return empty list when store does not exist")
        void shouldReturnEmptyListWhenStoreDoesNotExist() {
            // Given
            Long storeId = 0L;
            when(stockQueryUseCase.getStockByStore(storeId)).thenReturn(List.of());

            // When
            ResponseEntity<List<Stock>> response = stockController.getStockByStore(storeId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();
            verify(stockQueryUseCase).getStockByStore(storeId);
        }
    }

    @Nested
    @DisplayName("Get All Stock Endpoint")
    class GetAllStockEndpoint {

        @Test
        @DisplayName("Should return all stock from all stores and products")
        void shouldReturnAllStockFromAllStoresAndProducts() {
            // Given
            List<Stock> expectedStocks = Arrays.asList(stock1, stock2, stock3);
            when(stockQueryUseCase.getAllStock()).thenReturn(expectedStocks);

            // When
            ResponseEntity<List<Stock>> response = stockController.getAllStock();

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(3);
            assertThat(response.getBody()).containsExactlyInAnyOrder(stock1, stock2, stock3);
            verify(stockQueryUseCase).getAllStock();
        }

        @Test
        @DisplayName("Should return empty list when no stock exists in system")
        void shouldReturnEmptyListWhenNoStockExistsInSystem() {
            // Given
            when(stockQueryUseCase.getAllStock()).thenReturn(List.of());

            // When
            ResponseEntity<List<Stock>> response = stockController.getAllStock();

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();
            verify(stockQueryUseCase).getAllStock();
        }
    }
}