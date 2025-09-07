package br.com.stockqueryservice.infrastructure.adapters.out.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StockEntity Tests")
class StockEntityTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create StockEntity with NoArgsConstructor")
        void shouldCreateStockEntityWithNoArgsConstructor() {
            // When
            StockEntity entity = new StockEntity();

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isNull();
            assertThat(entity.getProductId()).isNull();
            assertThat(entity.getStoreId()).isNull();
            assertThat(entity.getQuantity()).isNull();
            assertThat(entity.getLastUpdated()).isNull();
            assertThat(entity.getProductName()).isNull();
            assertThat(entity.getStoreName()).isNull();
        }

        @Test
        @DisplayName("Should create StockEntity with AllArgsConstructor")
        void shouldCreateStockEntityWithAllArgsConstructor() {
            // Given
            Long id = 1L;
            Long productId = 100L;
            Long storeId = 10L;
            Integer quantity = 50;
            LocalDateTime lastUpdated = LocalDateTime.now();
            String productName = "Test Product";
            String storeName = "Test Store";

            // When
            StockEntity entity = new StockEntity(id, productId, storeId, quantity, lastUpdated, productName, storeName);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(id);
            assertThat(entity.getProductId()).isEqualTo(productId);
            assertThat(entity.getStoreId()).isEqualTo(storeId);
            assertThat(entity.getQuantity()).isEqualTo(quantity);
            assertThat(entity.getLastUpdated()).isEqualTo(lastUpdated);
            assertThat(entity.getProductName()).isEqualTo(productName);
            assertThat(entity.getStoreName()).isEqualTo(storeName);
        }

        @Test
        @DisplayName("Should create StockEntity with Builder pattern")
        void shouldCreateStockEntityWithBuilder() {
            // Given
            Long id = 1L;
            Long productId = 100L;
            Long storeId = 10L;
            Integer quantity = 50;
            LocalDateTime lastUpdated = LocalDateTime.now();
            String productName = "Test Product";
            String storeName = "Test Store";

            // When
            StockEntity entity = StockEntity.builder()
                    .id(id)
                    .productId(productId)
                    .storeId(storeId)
                    .quantity(quantity)
                    .lastUpdated(lastUpdated)
                    .productName(productName)
                    .storeName(storeName)
                    .build();

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(id);
            assertThat(entity.getProductId()).isEqualTo(productId);
            assertThat(entity.getStoreId()).isEqualTo(storeId);
            assertThat(entity.getQuantity()).isEqualTo(quantity);
            assertThat(entity.getLastUpdated()).isEqualTo(lastUpdated);
            assertThat(entity.getProductName()).isEqualTo(productName);
            assertThat(entity.getStoreName()).isEqualTo(storeName);
        }

        @Test
        @DisplayName("Should create StockEntity with Builder pattern and partial data")
        void shouldCreateStockEntityWithBuilderPartialData() {
            // Given
            Long productId = 100L;
            Long storeId = 10L;
            Integer quantity = 25;

            // When
            StockEntity entity = StockEntity.builder()
                    .productId(productId)
                    .storeId(storeId)
                    .quantity(quantity)
                    .build();

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isNull();
            assertThat(entity.getProductId()).isEqualTo(productId);
            assertThat(entity.getStoreId()).isEqualTo(storeId);
            assertThat(entity.getQuantity()).isEqualTo(quantity);
            assertThat(entity.getLastUpdated()).isNull();
            assertThat(entity.getProductName()).isNull();
            assertThat(entity.getStoreName()).isNull();
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should get and set id correctly")
        void shouldGetAndSetId() {
            // Given
            StockEntity entity = new StockEntity();
            Long id = 123L;

            // When
            entity.setId(id);

            // Then
            assertThat(entity.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should get and set productId correctly")
        void shouldGetAndSetProductId() {
            // Given
            StockEntity entity = new StockEntity();
            Long productId = 456L;

            // When
            entity.setProductId(productId);

            // Then
            assertThat(entity.getProductId()).isEqualTo(productId);
        }

        @Test
        @DisplayName("Should get and set storeId correctly")
        void shouldGetAndSetStoreId() {
            // Given
            StockEntity entity = new StockEntity();
            Long storeId = 789L;

            // When
            entity.setStoreId(storeId);

            // Then
            assertThat(entity.getStoreId()).isEqualTo(storeId);
        }

        @Test
        @DisplayName("Should get and set quantity correctly")
        void shouldGetAndSetQuantity() {
            // Given
            StockEntity entity = new StockEntity();
            Integer quantity = 100;

            // When
            entity.setQuantity(quantity);

            // Then
            assertThat(entity.getQuantity()).isEqualTo(quantity);
        }

        @Test
        @DisplayName("Should get and set lastUpdated correctly")
        void shouldGetAndSetLastUpdated() {
            // Given
            StockEntity entity = new StockEntity();
            LocalDateTime lastUpdated = LocalDateTime.now();

            // When
            entity.setLastUpdated(lastUpdated);

            // Then
            assertThat(entity.getLastUpdated()).isEqualTo(lastUpdated);
        }

        @Test
        @DisplayName("Should get and set productName correctly")
        void shouldGetAndSetProductName() {
            // Given
            StockEntity entity = new StockEntity();
            String productName = "Amazing Product";

            // When
            entity.setProductName(productName);

            // Then
            assertThat(entity.getProductName()).isEqualTo(productName);
        }

        @Test
        @DisplayName("Should get and set storeName correctly")
        void shouldGetAndSetStoreName() {
            // Given
            StockEntity entity = new StockEntity();
            String storeName = "Super Store";

            // When
            entity.setStoreName(storeName);

            // Then
            assertThat(entity.getStoreName()).isEqualTo(storeName);
        }
    }

    @Nested
    @DisplayName("Field Validation Tests")
    class FieldValidationTests {

        @Test
        @DisplayName("Should handle null values for optional fields")
        void shouldHandleNullValuesForOptionalFields() {
            // When
            StockEntity entity = StockEntity.builder()
                    .productId(100L)
                    .storeId(10L)
                    .quantity(50)
                    .lastUpdated(null)
                    .productName(null)
                    .storeName(null)
                    .build();

            // Then
            assertThat(entity.getProductId()).isEqualTo(100L);
            assertThat(entity.getStoreId()).isEqualTo(10L);
            assertThat(entity.getQuantity()).isEqualTo(50);
            assertThat(entity.getLastUpdated()).isNull();
            assertThat(entity.getProductName()).isNull();
            assertThat(entity.getStoreName()).isNull();
        }

        @Test
        @DisplayName("Should handle zero quantity")
        void shouldHandleZeroQuantity() {
            // When
            StockEntity entity = StockEntity.builder()
                    .productId(100L)
                    .storeId(10L)
                    .quantity(0)
                    .build();

            // Then
            assertThat(entity.getQuantity()).isZero();
        }

        @Test
        @DisplayName("Should handle negative quantity")
        void shouldHandleNegativeQuantity() {
            // When
            StockEntity entity = StockEntity.builder()
                    .productId(100L)
                    .storeId(10L)
                    .quantity(-10)
                    .build();

            // Then
            assertThat(entity.getQuantity()).isEqualTo(-10);
        }

        @Test
        @DisplayName("Should handle empty strings for name fields")
        void shouldHandleEmptyStringsForNameFields() {
            // When
            StockEntity entity = StockEntity.builder()
                    .productId(100L)
                    .storeId(10L)
                    .quantity(50)
                    .productName("")
                    .storeName("")
                    .build();

            // Then
            assertThat(entity.getProductName()).isEmpty();
            assertThat(entity.getStoreName()).isEmpty();
        }

        @Test
        @DisplayName("Should handle very long strings for name fields")
        void shouldHandleVeryLongStringsForNameFields() {
            // Given
            String veryLongProductName = "A".repeat(1000);
            String veryLongStoreName = "B".repeat(1000);

            // When
            StockEntity entity = StockEntity.builder()
                    .productId(100L)
                    .storeId(10L)
                    .quantity(50)
                    .productName(veryLongProductName)
                    .storeName(veryLongStoreName)
                    .build();

            // Then
            assertThat(entity.getProductName()).hasSize(1000);
            assertThat(entity.getStoreName()).hasSize(1000);
            assertThat(entity.getProductName()).isEqualTo(veryLongProductName);
            assertThat(entity.getStoreName()).isEqualTo(veryLongStoreName);
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should represent a complete stock record")
        void shouldRepresentCompleteStockRecord() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When
            StockEntity entity = StockEntity.builder()
                    .id(1L)
                    .productId(100L)
                    .storeId(10L)
                    .quantity(75)
                    .lastUpdated(now)
                    .productName("Premium Widget")
                    .storeName("Main Store")
                    .build();

            // Then
            assertThat(entity.getId()).isPositive();
            assertThat(entity.getProductId()).isPositive();
            assertThat(entity.getStoreId()).isPositive();
            assertThat(entity.getQuantity()).isPositive();
            assertThat(entity.getLastUpdated()).isBeforeOrEqualTo(LocalDateTime.now());
            assertThat(entity.getProductName()).isNotBlank();
            assertThat(entity.getStoreName()).isNotBlank();
        }

        @Test
        @DisplayName("Should represent minimum required stock record")
        void shouldRepresentMinimumRequiredStockRecord() {
            // When
            StockEntity entity = StockEntity.builder()
                    .productId(100L)
                    .storeId(10L)
                    .quantity(0)
                    .build();

            // Then
            assertThat(entity.getProductId()).isNotNull();
            assertThat(entity.getStoreId()).isNotNull();
            assertThat(entity.getQuantity()).isNotNull();
        }

        @Test
        @DisplayName("Should handle stock update scenarios")
        void shouldHandleStockUpdateScenarios() {
            // Given
            LocalDateTime initialTime = LocalDateTime.now().minusHours(1);
            LocalDateTime updateTime = LocalDateTime.now();

            StockEntity entity = StockEntity.builder()
                    .id(1L)
                    .productId(100L)
                    .storeId(10L)
                    .quantity(100)
                    .lastUpdated(initialTime)
                    .productName("Widget")
                    .storeName("Store A")
                    .build();

            // When - Simulate stock update
            entity.setQuantity(75);
            entity.setLastUpdated(updateTime);

            // Then
            assertThat(entity.getQuantity()).isEqualTo(75);
            assertThat(entity.getLastUpdated()).isEqualTo(updateTime);
            assertThat(entity.getLastUpdated()).isAfter(initialTime);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle maximum Long values for IDs")
        void shouldHandleMaximumLongValuesForIds() {
            // When
            StockEntity entity = StockEntity.builder()
                    .id(Long.MAX_VALUE)
                    .productId(Long.MAX_VALUE)
                    .storeId(Long.MAX_VALUE)
                    .quantity(Integer.MAX_VALUE)
                    .build();

            // Then
            assertThat(entity.getId()).isEqualTo(Long.MAX_VALUE);
            assertThat(entity.getProductId()).isEqualTo(Long.MAX_VALUE);
            assertThat(entity.getStoreId()).isEqualTo(Long.MAX_VALUE);
            assertThat(entity.getQuantity()).isEqualTo(Integer.MAX_VALUE);
        }

        @Test
        @DisplayName("Should handle minimum Long values for IDs")
        void shouldHandleMinimumLongValuesForIds() {
            // When
            StockEntity entity = StockEntity.builder()
                    .id(1L)
                    .productId(1L)
                    .storeId(1L)
                    .quantity(Integer.MIN_VALUE)
                    .build();

            // Then
            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getProductId()).isEqualTo(1L);
            assertThat(entity.getStoreId()).isEqualTo(1L);
            assertThat(entity.getQuantity()).isEqualTo(Integer.MIN_VALUE);
        }

        @Test
        @DisplayName("Should handle past and future timestamps")
        void shouldHandlePastAndFutureTimestamps() {
            // Given
            LocalDateTime pastTime = LocalDateTime.of(2020, 1, 1, 0, 0);
            LocalDateTime futureTime = LocalDateTime.of(2030, 12, 31, 23, 59);

            // When
            StockEntity pastEntity = StockEntity.builder()
                    .productId(100L)
                    .storeId(10L)
                    .quantity(50)
                    .lastUpdated(pastTime)
                    .build();

            StockEntity futureEntity = StockEntity.builder()
                    .productId(200L)
                    .storeId(20L)
                    .quantity(75)
                    .lastUpdated(futureTime)
                    .build();

            // Then
            assertThat(pastEntity.getLastUpdated()).isEqualTo(pastTime);
            assertThat(futureEntity.getLastUpdated()).isEqualTo(futureTime);
            assertThat(pastEntity.getLastUpdated()).isBefore(LocalDateTime.now());
            assertThat(futureEntity.getLastUpdated()).isAfter(LocalDateTime.now());
        }

        @Test
        @DisplayName("Should handle special characters in name fields")
        void shouldHandleSpecialCharactersInNameFields() {
            // Given
            String productNameWithSpecialChars = "Produto Açaí & Café 100% ñ";
            String storeNameWithSpecialChars = "Loja São João - R$ & €";

            // When
            StockEntity entity = StockEntity.builder()
                    .productId(100L)
                    .storeId(10L)
                    .quantity(50)
                    .productName(productNameWithSpecialChars)
                    .storeName(storeNameWithSpecialChars)
                    .build();

            // Then
            assertThat(entity.getProductName()).isEqualTo(productNameWithSpecialChars);
            assertThat(entity.getStoreName()).isEqualTo(storeNameWithSpecialChars);
        }
    }

    @Nested
    @DisplayName("Object Methods Tests")
    class ObjectMethodsTests {

        @Test
        @DisplayName("Should handle object identity and references")
        void shouldHandleObjectIdentityAndReferences() {
            // Given
            LocalDateTime time = LocalDateTime.now();

            StockEntity entity1 = StockEntity.builder()
                    .id(1L)
                    .productId(100L)
                    .storeId(10L)
                    .quantity(50)
                    .lastUpdated(time)
                    .productName("Product A")
                    .storeName("Store A")
                    .build();

            StockEntity entity2 = entity1; // Same reference
            StockEntity entity3 = StockEntity.builder()
                    .id(2L)
                    .productId(200L)
                    .storeId(20L)
                    .quantity(100)
                    .build();

            // Then - Test object identity and basic behavior
            assertThat(entity1).isSameAs(entity2);
            assertThat(entity1).isNotSameAs(entity3);
            assertThat(entity1.hashCode()).isNotNull();
            assertThat(entity3.hashCode()).isNotNull();
            assertThat(entity1).isNotEqualTo(null);
            assertThat(entity1).isNotEqualTo("not a StockEntity");
        }

        @Test
        @DisplayName("Should provide meaningful toString representation")
        void shouldProvideMeaningfulToStringRepresentation() {
            // Given
            StockEntity entity = StockEntity.builder()
                    .id(1L)
                    .productId(100L)
                    .storeId(10L)
                    .quantity(50)
                    .productName("Test Product")
                    .storeName("Test Store")
                    .build();

            // When
            String toString = entity.toString();

            // Then
            assertThat(toString).isNotNull();
            assertThat(toString).isNotEmpty();
            assertThat(toString).contains("StockEntity");
        }
    }
}