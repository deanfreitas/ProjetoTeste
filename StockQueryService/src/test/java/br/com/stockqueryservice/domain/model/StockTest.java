package br.com.stockqueryservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Stock Domain Model Tests")
class StockTest {

    private final LocalDateTime testDateTime = LocalDateTime.of(2025, 9, 7, 14, 47, 0);

    @Nested
    @DisplayName("Stock Record Creation")
    class StockCreation {

        @Test
        @DisplayName("Should create Stock with all valid fields")
        void shouldCreateStockWithAllValidFields() {
            // Given
            Long id = 1L;
            Long productId = 100L;
            Long storeId = 10L;
            Integer quantity = 50;
            LocalDateTime lastUpdated = testDateTime;
            String productName = "Test Product";
            String storeName = "Test Store";

            // When
            Stock stock = new Stock(id, productId, storeId, quantity, lastUpdated, productName, storeName);

            // Then
            assertAll("Stock should be created with all fields correctly assigned",
                    () -> assertThat(stock.id()).isEqualTo(id),
                    () -> assertThat(stock.productId()).isEqualTo(productId),
                    () -> assertThat(stock.storeId()).isEqualTo(storeId),
                    () -> assertThat(stock.quantity()).isEqualTo(quantity),
                    () -> assertThat(stock.lastUpdated()).isEqualTo(lastUpdated),
                    () -> assertThat(stock.productName()).isEqualTo(productName),
                    () -> assertThat(stock.storeName()).isEqualTo(storeName)
            );
        }

        @Test
        @DisplayName("Should create Stock with null values")
        void shouldCreateStockWithNullValues() {
            // When
            Stock stock = new Stock(null, null, null, null, null, null, null);

            // Then
            assertAll("Stock should accept null values for all fields",
                    () -> assertThat(stock.id()).isNull(),
                    () -> assertThat(stock.productId()).isNull(),
                    () -> assertThat(stock.storeId()).isNull(),
                    () -> assertThat(stock.quantity()).isNull(),
                    () -> assertThat(stock.lastUpdated()).isNull(),
                    () -> assertThat(stock.productName()).isNull(),
                    () -> assertThat(stock.storeName()).isNull()
            );
        }

        @Test
        @DisplayName("Should create Stock with zero quantity")
        void shouldCreateStockWithZeroQuantity() {
            // When
            Stock stock = new Stock(1L, 100L, 10L, 0, testDateTime, "Product", "Store");

            // Then
            assertThat(stock.quantity()).isZero();
        }

        @Test
        @DisplayName("Should create Stock with negative quantity")
        void shouldCreateStockWithNegativeQuantity() {
            // When
            Stock stock = new Stock(1L, 100L, 10L, -5, testDateTime, "Product", "Store");

            // Then
            assertThat(stock.quantity()).isEqualTo(-5);
        }
    }

    @Nested
    @DisplayName("Stock Record Equality")
    class StockEquality {

        @Test
        @DisplayName("Should be equal when all fields are the same")
        void shouldBeEqualWhenAllFieldsAreTheSame() {
            // Given
            Stock stock1 = new Stock(1L, 100L, 10L, 50, testDateTime, "Product", "Store");
            Stock stock2 = new Stock(1L, 100L, 10L, 50, testDateTime, "Product", "Store");

            // Then
            assertThat(stock1).isEqualTo(stock2);
            assertThat(stock1.hashCode()).isEqualTo(stock2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when any field differs")
        void shouldNotBeEqualWhenAnyFieldDiffers() {
            // Given
            Stock originalStock = new Stock(1L, 100L, 10L, 50, testDateTime, "Product", "Store");

            // When & Then
            assertAll("Stocks should not be equal when any field differs",
                    () -> {
                        Stock differentId = new Stock(2L, 100L, 10L, 50, testDateTime, "Product", "Store");
                        assertThat(originalStock).isNotEqualTo(differentId);
                    },
                    () -> {
                        Stock differentProductId = new Stock(1L, 200L, 10L, 50, testDateTime, "Product", "Store");
                        assertThat(originalStock).isNotEqualTo(differentProductId);
                    },
                    () -> {
                        Stock differentStoreId = new Stock(1L, 100L, 20L, 50, testDateTime, "Product", "Store");
                        assertThat(originalStock).isNotEqualTo(differentStoreId);
                    },
                    () -> {
                        Stock differentQuantity = new Stock(1L, 100L, 10L, 25, testDateTime, "Product", "Store");
                        assertThat(originalStock).isNotEqualTo(differentQuantity);
                    },
                    () -> {
                        Stock differentDateTime = new Stock(1L, 100L, 10L, 50, testDateTime.plusHours(1), "Product", "Store");
                        assertThat(originalStock).isNotEqualTo(differentDateTime);
                    },
                    () -> {
                        Stock differentProductName = new Stock(1L, 100L, 10L, 50, testDateTime, "Different Product", "Store");
                        assertThat(originalStock).isNotEqualTo(differentProductName);
                    },
                    () -> {
                        Stock differentStoreName = new Stock(1L, 100L, 10L, 50, testDateTime, "Product", "Different Store");
                        assertThat(originalStock).isNotEqualTo(differentStoreName);
                    }
            );
        }

        @Test
        @DisplayName("Should handle equality with null values")
        void shouldHandleEqualityWithNullValues() {
            // Given
            Stock stock1 = new Stock(null, null, null, null, null, null, null);
            Stock stock2 = new Stock(null, null, null, null, null, null, null);

            // Then
            assertThat(stock1).isEqualTo(stock2);
            assertThat(stock1.hashCode()).isEqualTo(stock2.hashCode());
        }
    }

    @Nested
    @DisplayName("Stock Record String Representation")
    class StockStringRepresentation {

        @Test
        @DisplayName("Should have meaningful toString representation")
        void shouldHaveMeaningfulToStringRepresentation() {
            // Given
            Stock stock = new Stock(1L, 100L, 10L, 50, testDateTime, "Test Product", "Test Store");

            // When
            String toString = stock.toString();

            // Then
            assertAll("toString should contain all field values",
                    () -> assertThat(toString).contains("1"),
                    () -> assertThat(toString).contains("100"),
                    () -> assertThat(toString).contains("10"),
                    () -> assertThat(toString).contains("50"),
                    () -> assertThat(toString).contains("Test Product"),
                    () -> assertThat(toString).contains("Test Store"),
                    () -> assertThat(toString).contains(testDateTime.toString())
            );
        }

        @Test
        @DisplayName("Should handle toString with null values")
        void shouldHandleToStringWithNullValues() {
            // Given
            Stock stock = new Stock(null, null, null, null, null, null, null);

            // When
            String toString = stock.toString();

            // Then
            assertThat(toString).isNotNull();
            assertThat(toString).contains("null");
        }
    }

    @Nested
    @DisplayName("Stock Record Field Access")
    class StockFieldAccess {

        @Test
        @DisplayName("Should provide access to all fields through accessor methods")
        void shouldProvideAccessToAllFieldsThroughAccessorMethods() {
            // Given
            Long expectedId = 1L;
            Long expectedProductId = 100L;
            Long expectedStoreId = 10L;
            Integer expectedQuantity = 50;
            LocalDateTime expectedLastUpdated = testDateTime;
            String expectedProductName = "Test Product";
            String expectedStoreName = "Test Store";

            Stock stock = new Stock(expectedId, expectedProductId, expectedStoreId, expectedQuantity,
                    expectedLastUpdated, expectedProductName, expectedStoreName);

            // Then
            assertAll("All accessor methods should return correct values",
                    () -> assertThat(stock.id()).isEqualTo(expectedId),
                    () -> assertThat(stock.productId()).isEqualTo(expectedProductId),
                    () -> assertThat(stock.storeId()).isEqualTo(expectedStoreId),
                    () -> assertThat(stock.quantity()).isEqualTo(expectedQuantity),
                    () -> assertThat(stock.lastUpdated()).isEqualTo(expectedLastUpdated),
                    () -> assertThat(stock.productName()).isEqualTo(expectedProductName),
                    () -> assertThat(stock.storeName()).isEqualTo(expectedStoreName)
            );
        }
    }

    @Nested
    @DisplayName("Stock Record Business Scenarios")
    class StockBusinessScenarios {

        @Test
        @DisplayName("Should represent out-of-stock scenario")
        void shouldRepresentOutOfStockScenario() {
            // Given
            Stock outOfStock = new Stock(1L, 100L, 10L, 0, testDateTime, "Out of Stock Product", "Store A");

            // Then
            assertThat(outOfStock.quantity()).isZero();
            assertThat(outOfStock.productName()).isEqualTo("Out of Stock Product");
        }

        @Test
        @DisplayName("Should represent high-volume stock scenario")
        void shouldRepresentHighVolumeStockScenario() {
            // Given
            Integer highVolume = 1000000;
            Stock highVolumeStock = new Stock(1L, 100L, 10L, highVolume, testDateTime, "Popular Product", "Warehouse");

            // Then
            assertThat(highVolumeStock.quantity()).isEqualTo(highVolume);
            assertThat(highVolumeStock.storeName()).isEqualTo("Warehouse");
        }

        @Test
        @DisplayName("Should handle recent stock update timestamp")
        void shouldHandleRecentStockUpdateTimestamp() {
            // Given
            LocalDateTime recentUpdate = LocalDateTime.now().minusMinutes(5);
            Stock recentStock = new Stock(1L, 100L, 10L, 25, recentUpdate, "Recently Updated", "Store B");

            // Then
            assertThat(recentStock.lastUpdated()).isEqualTo(recentUpdate);
            assertThat(recentStock.lastUpdated()).isBefore(LocalDateTime.now());
        }

        @Test
        @DisplayName("Should handle stock with empty product name")
        void shouldHandleStockWithEmptyProductName() {
            // Given
            Stock stockWithEmptyName = new Stock(1L, 100L, 10L, 10, testDateTime, "", "Store C");

            // Then
            assertThat(stockWithEmptyName.productName()).isEmpty();
            assertThat(stockWithEmptyName.productName()).isNotNull();
        }

        @Test
        @DisplayName("Should handle stock with empty store name")
        void shouldHandleStockWithEmptyStoreName() {
            // Given
            Stock stockWithEmptyStoreName = new Stock(1L, 100L, 10L, 15, testDateTime, "Product X", "");

            // Then
            assertThat(stockWithEmptyStoreName.storeName()).isEmpty();
            assertThat(stockWithEmptyStoreName.storeName()).isNotNull();
        }
    }
}