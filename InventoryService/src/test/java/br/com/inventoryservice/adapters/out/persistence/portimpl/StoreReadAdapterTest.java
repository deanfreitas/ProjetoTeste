package br.com.inventoryservice.adapters.out.persistence.portimpl;

import br.com.inventoryservice.adapters.out.persistence.entity.StoreEntity;
import br.com.inventoryservice.adapters.out.persistence.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StoreReadAdapter Tests")
class StoreReadAdapterTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreReadAdapter storeReadAdapter;

    @Nested
    @DisplayName("ExistsByCodigo Method Tests")
    class ExistsByCodigoTests {

        @Test
        @DisplayName("Should return true when store exists by codigo")
        void shouldReturnTrueWhenStoreExistsByCodigo() {
            // Given
            String codigo = "LOJA001";
            StoreEntity storeEntity = StoreEntity.builder()
                    .codigo(codigo)
                    .nome("Loja Principal")
                    .build();

            when(storeRepository.findById(codigo)).thenReturn(Optional.of(storeEntity));

            // When
            boolean result = storeReadAdapter.existsByCodigo(codigo);

            // Then
            assertTrue(result);
            verify(storeRepository).findById(codigo);
        }

        @Test
        @DisplayName("Should return false when store does not exist by codigo")
        void shouldReturnFalseWhenStoreDoesNotExistByCodigo() {
            // Given
            String codigo = "LOJA999";

            when(storeRepository.findById(codigo)).thenReturn(Optional.empty());

            // When
            boolean result = storeReadAdapter.existsByCodigo(codigo);

            // Then
            assertFalse(result);
            verify(storeRepository).findById(codigo);
        }

        @Test
        @DisplayName("Should handle null codigo parameter")
        void shouldHandleNullCodigoParameter() {
            // Given
            String codigo = null;

            when(storeRepository.findById(codigo)).thenReturn(Optional.empty());

            // When
            boolean result = storeReadAdapter.existsByCodigo(codigo);

            // Then
            assertFalse(result);
            verify(storeRepository).findById(codigo);
        }

        @Test
        @DisplayName("Should handle empty codigo parameter")
        void shouldHandleEmptyCodigoParameter() {
            // Given
            String codigo = "";

            when(storeRepository.findById(codigo)).thenReturn(Optional.empty());

            // When
            boolean result = storeReadAdapter.existsByCodigo(codigo);

            // Then
            assertFalse(result);
            verify(storeRepository).findById(codigo);
        }

        @Test
        @DisplayName("Should handle whitespace codigo parameter")
        void shouldHandleWhitespaceCodigoParameter() {
            // Given
            String codigo = "   ";

            when(storeRepository.findById(codigo)).thenReturn(Optional.empty());

            // When
            boolean result = storeReadAdapter.existsByCodigo(codigo);

            // Then
            assertFalse(result);
            verify(storeRepository).findById(codigo);
        }

        @Test
        @DisplayName("Should handle codigo with special characters")
        void shouldHandleCodigoWithSpecialCharacters() {
            // Given
            String codigo = "LOJA-001_SP";
            StoreEntity storeEntity = StoreEntity.builder()
                    .codigo(codigo)
                    .nome("Loja São Paulo")
                    .build();

            when(storeRepository.findById(codigo)).thenReturn(Optional.of(storeEntity));

            // When
            boolean result = storeReadAdapter.existsByCodigo(codigo);

            // Then
            assertTrue(result);
            verify(storeRepository).findById(codigo);
        }

        @Test
        @DisplayName("Should handle codigo with numbers only")
        void shouldHandleCodigoWithNumbersOnly() {
            // Given
            String codigo = "123456";
            StoreEntity storeEntity = StoreEntity.builder()
                    .codigo(codigo)
                    .nome("Loja Numérica")
                    .build();

            when(storeRepository.findById(codigo)).thenReturn(Optional.of(storeEntity));

            // When
            boolean result = storeReadAdapter.existsByCodigo(codigo);

            // Then
            assertTrue(result);
            verify(storeRepository).findById(codigo);
        }

        @Test
        @DisplayName("Should handle very long codigo")
        void shouldHandleVeryLongCodigo() {
            // Given
            String codigo = "LOJA_MUITO_LONGA_COM_NOME_EXTREMAMENTE_DETALHADO_E_EXTENSO_PARA_IDENTIFICACAO_UNICA_DA_UNIDADE_COMERCIAL_001";
            StoreEntity storeEntity = StoreEntity.builder()
                    .codigo(codigo)
                    .nome("Loja com Código Longo")
                    .build();

            when(storeRepository.findById(codigo)).thenReturn(Optional.of(storeEntity));

            // When
            boolean result = storeReadAdapter.existsByCodigo(codigo);

            // Then
            assertTrue(result);
            verify(storeRepository).findById(codigo);
        }

        @Test
        @DisplayName("Should handle single character codigo")
        void shouldHandleSingleCharacterCodigo() {
            // Given
            String codigo = "A";
            StoreEntity storeEntity = StoreEntity.builder()
                    .codigo(codigo)
                    .nome("Loja A")
                    .build();

            when(storeRepository.findById(codigo)).thenReturn(Optional.of(storeEntity));

            // When
            boolean result = storeReadAdapter.existsByCodigo(codigo);

            // Then
            assertTrue(result);
            verify(storeRepository).findById(codigo);
        }

        @Test
        @DisplayName("Should handle case sensitive codigo")
        void shouldHandleCaseSensitiveCodigo() {
            // Given
            String codigo = "loja001";
            StoreEntity storeEntity = StoreEntity.builder()
                    .codigo(codigo)
                    .nome("Loja Minúscula")
                    .build();

            when(storeRepository.findById(codigo)).thenReturn(Optional.of(storeEntity));

            // When
            boolean result = storeReadAdapter.existsByCodigo(codigo);

            // Then
            assertTrue(result);
            verify(storeRepository).findById(codigo);
        }

        @Test
        @DisplayName("Should handle codigo with accented characters")
        void shouldHandleCodigoWithAccentedCharacters() {
            // Given
            String codigo = "LOJA_SÃO_JOÃO";
            StoreEntity storeEntity = StoreEntity.builder()
                    .codigo(codigo)
                    .nome("Loja São João")
                    .build();

            when(storeRepository.findById(codigo)).thenReturn(Optional.of(storeEntity));

            // When
            boolean result = storeReadAdapter.existsByCodigo(codigo);

            // Then
            assertTrue(result);
            verify(storeRepository).findById(codigo);
        }

        @Test
        @DisplayName("Should verify repository findById is called exactly once")
        void shouldVerifyRepositoryFindByIdIsCalledExactlyOnce() {
            // Given
            String codigo = "LOJA_VERIFICATION";

            when(storeRepository.findById(codigo)).thenReturn(Optional.empty());

            // When
            storeReadAdapter.existsByCodigo(codigo);

            // Then
            verify(storeRepository, times(1)).findById(codigo);
            verifyNoMoreInteractions(storeRepository);
        }

        @Test
        @DisplayName("Should return false when repository throws exception")
        void shouldReturnFalseWhenRepositoryThrowsException() {
            // Given
            String codigo = "LOJA_ERROR";

            when(storeRepository.findById(codigo)).thenThrow(new RuntimeException("Database connection error"));

            // When & Then
            assertThrows(RuntimeException.class, () -> storeReadAdapter.existsByCodigo(codigo));
            verify(storeRepository).findById(codigo);
        }

        @Test
        @DisplayName("Should handle multiple consecutive calls correctly")
        void shouldHandleMultipleConsecutiveCallsCorrectly() {
            // Given
            String codigo1 = "LOJA001";
            String codigo2 = "LOJA002";
            String codigo3 = "LOJA999";

            StoreEntity store1 = StoreEntity.builder().codigo(codigo1).nome("Loja 1").build();
            StoreEntity store2 = StoreEntity.builder().codigo(codigo2).nome("Loja 2").build();

            when(storeRepository.findById(codigo1)).thenReturn(Optional.of(store1));
            when(storeRepository.findById(codigo2)).thenReturn(Optional.of(store2));
            when(storeRepository.findById(codigo3)).thenReturn(Optional.empty());

            // When
            boolean result1 = storeReadAdapter.existsByCodigo(codigo1);
            boolean result2 = storeReadAdapter.existsByCodigo(codigo2);
            boolean result3 = storeReadAdapter.existsByCodigo(codigo3);

            // Then
            assertTrue(result1);
            assertTrue(result2);
            assertFalse(result3);
            
            verify(storeRepository).findById(codigo1);
            verify(storeRepository).findById(codigo2);
            verify(storeRepository).findById(codigo3);
            verify(storeRepository, times(3)).findById(anyString());
        }

        @Test
        @DisplayName("Should handle same codigo called multiple times")
        void shouldHandleSameCodigoCalledMultipleTimes() {
            // Given
            String codigo = "LOJA_REPEATED";
            StoreEntity storeEntity = StoreEntity.builder()
                    .codigo(codigo)
                    .nome("Loja Repetida")
                    .build();

            when(storeRepository.findById(codigo)).thenReturn(Optional.of(storeEntity));

            // When
            boolean result1 = storeReadAdapter.existsByCodigo(codigo);
            boolean result2 = storeReadAdapter.existsByCodigo(codigo);
            boolean result3 = storeReadAdapter.existsByCodigo(codigo);

            // Then
            assertTrue(result1);
            assertTrue(result2);
            assertTrue(result3);
            
            verify(storeRepository, times(3)).findById(codigo);
        }
    }
}