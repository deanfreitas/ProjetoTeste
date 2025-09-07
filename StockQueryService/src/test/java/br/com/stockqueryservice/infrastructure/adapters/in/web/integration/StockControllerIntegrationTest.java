package br.com.stockqueryservice.infrastructure.adapters.in.web.integration;

import br.com.stockqueryservice.domain.model.Stock;
import br.com.stockqueryservice.infrastructure.adapters.out.persistence.entity.StockEntity;
import br.com.stockqueryservice.infrastructure.adapters.out.persistence.repository.StockRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("StockController Integration Tests with Testcontainers")
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StockControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        // Clean up data before each test using repository
        stockRepository.deleteAll();
        setupTestData();
    }

    private void setupTestData() {
        // Create test stock entities with proper Long IDs and names
        StockEntity stock1 = StockEntity.builder()
                .productId(1L)
                .storeId(1L)
                .quantity(10)
                .productName("Produto 1")
                .storeName("Loja Centro")
                .lastUpdated(LocalDateTime.now())
                .build();

        StockEntity stock2 = StockEntity.builder()
                .productId(1L)
                .storeId(2L)
                .quantity(15)
                .productName("Produto 1")
                .storeName("Loja Shopping")
                .lastUpdated(LocalDateTime.now())
                .build();

        StockEntity stock3 = StockEntity.builder()
                .productId(2L)
                .storeId(1L)
                .quantity(5)
                .productName("Produto 2")
                .storeName("Loja Centro")
                .lastUpdated(LocalDateTime.now())
                .build();

        StockEntity stock4 = StockEntity.builder()
                .productId(2L)
                .storeId(3L)
                .quantity(20)
                .productName("Produto 2")
                .storeName("Loja Online")
                .lastUpdated(LocalDateTime.now())
                .build();

        stockRepository.saveAll(List.of(stock1, stock2, stock3, stock4));
    }

    @Nested
    @DisplayName("GET /api/inventory/stock - Get Stock by Product")
    class GetStockByProductTests {

        @Test
        @Order(1)
        @DisplayName("Should return stock from multiple stores for existing product")
        void shouldReturnStockFromMultipleStoresForExistingProduct() {
            // Given
            Long productId = 1L; // PROD001
            String url = "http://localhost:" + port + "/api/inventory/stock?productId=" + productId;

            // When
            ResponseEntity<List<Stock>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Stock>>() {
                    }
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).hasSize(2); // Two stores have this product

            List<Stock> stocks = response.getBody();
            assertThat(stocks)
                    .extracting(Stock::productId)
                    .containsOnly(productId);
            assertThat(stocks)
                    .extracting(Stock::quantity)
                    .containsExactlyInAnyOrder(10, 15);
        }

        @Test
        @Order(2)
        @DisplayName("Should return empty list when product has no stock")
        void shouldReturnEmptyListWhenProductHasNoStock() {
            // Given
            Long productId = 999L; // Non-existing product
            String url = "http://localhost:" + port + "/api/inventory/stock?productId=" + productId;

            // When
            ResponseEntity<List<Stock>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Stock>>() {
                    }
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).isEmpty();
        }

        @Test
        @Order(3)
        @DisplayName("Should return stock for specific product and store")
        void shouldReturnStockForSpecificProductAndStore() {
            // Given
            Long productId = 1L; // PROD001
            Long storeId = 1L; // LOJA001
            String url = "http://localhost:" + port + "/api/inventory/stock?productId=" + productId + "&storeId=" + storeId;

            // When
            ResponseEntity<List<Stock>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Stock>>() {
                    }
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).hasSize(1);

            Stock stock = response.getBody().get(0);
            assertThat(stock.productId()).isEqualTo(productId);
            assertThat(stock.storeId()).isEqualTo(storeId);
            assertThat(stock.quantity()).isEqualTo(10);
        }
    }

    @Nested
    @DisplayName("GET /api/inventory/stock/store/{storeId} - Get Stock by Store")
    class GetStockByStoreTests {

        @Test
        @Order(4)
        @DisplayName("Should return all products in store")
        void shouldReturnAllProductsInStore() {
            // Given
            Long storeId = 1L; // LOJA001
            String url = "http://localhost:" + port + "/api/inventory/stock/store/" + storeId;

            // When
            ResponseEntity<List<Stock>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Stock>>() {
                    }
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).hasSize(2); // LOJA001 has PROD001 and PROD002

            List<Stock> stocks = response.getBody();
            assertThat(stocks)
                    .extracting(Stock::storeId)
                    .containsOnly(storeId);
            assertThat(stocks)
                    .extracting(Stock::quantity)
                    .containsExactlyInAnyOrder(10, 5);
        }

        @Test
        @Order(5)
        @DisplayName("Should return empty list when store has no stock")
        void shouldReturnEmptyListWhenStoreHasNoStock() {
            // Given
            Long storeId = 999L; // Non-existing store
            String url = "http://localhost:" + port + "/api/inventory/stock/store/" + storeId;

            // When
            ResponseEntity<List<Stock>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Stock>>() {
                    }
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).isEmpty();
        }
    }

    @Nested
    @DisplayName("GET /api/inventory/stock/all - Get All Stock")
    class GetAllStockTests {

        @Test
        @Order(6)
        @DisplayName("Should return all stock from all stores and products")
        void shouldReturnAllStockFromAllStoresAndProducts() {
            // Given
            String url = "http://localhost:" + port + "/api/inventory/stock/all";

            // When
            ResponseEntity<List<Stock>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Stock>>() {
                    }
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).hasSize(4); // Total stock entries from setup

            List<Stock> stocks = response.getBody();
            assertThat(stocks)
                    .extracting(Stock::quantity)
                    .containsExactlyInAnyOrder(10, 15, 5, 20);
        }
    }
}