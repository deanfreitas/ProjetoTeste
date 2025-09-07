package br.com.inventoryservice.infrastructure.adapters.out.persistence.portimpl;

import br.com.inventoryservice.infrastructure.adapters.out.persistence.entity.ProductEntity;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductReadAdapter Tests")
class ProductReadAdapterTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductReadAdapter productReadAdapter;

    @Nested
    @DisplayName("ExistsBySku Method Tests")
    class ExistsBySkuTests {

        @Test
        @DisplayName("Should return true when product exists by SKU")
        void shouldReturnTrueWhenProductExistsBySku() {
            // Given
            String sku = "PROD001";
            ProductEntity productEntity = ProductEntity.builder()
                    .sku(sku)
                    .ativo(true)
                    .build();

            when(productRepository.findById(sku)).thenReturn(Optional.of(productEntity));

            // When
            boolean result = productReadAdapter.existsBySku(sku);

            // Then
            assertTrue(result);
            verify(productRepository).findById(sku);
        }

        @Test
        @DisplayName("Should return false when product does not exist by SKU")
        void shouldReturnFalseWhenProductDoesNotExistBySku() {
            // Given
            String sku = "PROD002";

            when(productRepository.findById(sku)).thenReturn(Optional.empty());

            // When
            boolean result = productReadAdapter.existsBySku(sku);

            // Then
            assertFalse(result);
            verify(productRepository).findById(sku);
        }

        @Test
        @DisplayName("Should handle null SKU parameter")
        void shouldHandleNullSkuParameter() {
            // Given
            String sku = null;

            when(productRepository.findById(sku)).thenReturn(Optional.empty());

            // When
            boolean result = productReadAdapter.existsBySku(sku);

            // Then
            assertFalse(result);
            verify(productRepository).findById(sku);
        }

        @Test
        @DisplayName("Should handle empty SKU parameter")
        void shouldHandleEmptySkuParameter() {
            // Given
            String sku = "";

            when(productRepository.findById(sku)).thenReturn(Optional.empty());

            // When
            boolean result = productReadAdapter.existsBySku(sku);

            // Then
            assertFalse(result);
            verify(productRepository).findById(sku);
        }

        @Test
        @DisplayName("Should handle whitespace SKU parameter")
        void shouldHandleWhitespaceSkuParameter() {
            // Given
            String sku = "   ";

            when(productRepository.findById(sku)).thenReturn(Optional.empty());

            // When
            boolean result = productReadAdapter.existsBySku(sku);

            // Then
            assertFalse(result);
            verify(productRepository).findById(sku);
        }
    }

    @Nested
    @DisplayName("IsAtivo Method Tests")
    class IsAtivoTests {

        @Test
        @DisplayName("Should return Optional with true when product is active")
        void shouldReturnOptionalWithTrueWhenProductIsActive() {
            // Given
            String sku = "PROD001";
            ProductEntity productEntity = ProductEntity.builder()
                    .sku(sku)
                    .ativo(true)
                    .build();

            when(productRepository.findById(sku)).thenReturn(Optional.of(productEntity));

            // When
            Optional<Boolean> result = productReadAdapter.isAtivo(sku);

            // Then
            assertTrue(result.isPresent());
            assertTrue(result.get());
            verify(productRepository).findById(sku);
        }

        @Test
        @DisplayName("Should return Optional with false when product is inactive")
        void shouldReturnOptionalWithFalseWhenProductIsInactive() {
            // Given
            String sku = "PROD002";
            ProductEntity productEntity = ProductEntity.builder()
                    .sku(sku)
                    .ativo(false)
                    .build();

            when(productRepository.findById(sku)).thenReturn(Optional.of(productEntity));

            // When
            Optional<Boolean> result = productReadAdapter.isAtivo(sku);

            // Then
            assertTrue(result.isPresent());
            assertFalse(result.get());
            verify(productRepository).findById(sku);
        }

        @Test
        @DisplayName("Should return empty Optional when product does not exist")
        void shouldReturnEmptyOptionalWhenProductDoesNotExist() {
            // Given
            String sku = "PROD003";

            when(productRepository.findById(sku)).thenReturn(Optional.empty());

            // When
            Optional<Boolean> result = productReadAdapter.isAtivo(sku);

            // Then
            assertFalse(result.isPresent());
            verify(productRepository).findById(sku);
        }

        @Test
        @DisplayName("Should handle null ativo field in product entity")
        void shouldHandleNullAtivoFieldInProductEntity() {
            // Given
            String sku = "PROD004";
            ProductEntity productEntity = ProductEntity.builder()
                    .sku(sku)
                    .ativo(null)
                    .build();

            when(productRepository.findById(sku)).thenReturn(Optional.of(productEntity));

            // When
            Optional<Boolean> result = productReadAdapter.isAtivo(sku);

            // Then
            assertFalse(result.isPresent());
            verify(productRepository).findById(sku);
        }

        @Test
        @DisplayName("Should handle null SKU parameter when checking if active")
        void shouldHandleNullSkuParameterWhenCheckingIfActive() {
            // Given
            String sku = null;

            when(productRepository.findById(sku)).thenReturn(Optional.empty());

            // When
            Optional<Boolean> result = productReadAdapter.isAtivo(sku);

            // Then
            assertFalse(result.isPresent());
            verify(productRepository).findById(sku);
        }

        @Test
        @DisplayName("Should handle empty SKU parameter when checking if active")
        void shouldHandleEmptySkuParameterWhenCheckingIfActive() {
            // Given
            String sku = "";

            when(productRepository.findById(sku)).thenReturn(Optional.empty());

            // When
            Optional<Boolean> result = productReadAdapter.isAtivo(sku);

            // Then
            assertFalse(result.isPresent());
            verify(productRepository).findById(sku);
        }

        @Test
        @DisplayName("Should handle whitespace SKU parameter when checking if active")
        void shouldHandleWhitespaceSkuParameterWhenCheckingIfActive() {
            // Given
            String sku = "   ";

            when(productRepository.findById(sku)).thenReturn(Optional.empty());

            // When
            Optional<Boolean> result = productReadAdapter.isAtivo(sku);

            // Then
            assertFalse(result.isPresent());
            verify(productRepository).findById(sku);
        }
    }
}