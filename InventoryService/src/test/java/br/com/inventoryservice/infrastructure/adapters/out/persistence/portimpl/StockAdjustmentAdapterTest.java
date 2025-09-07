package br.com.inventoryservice.infrastructure.adapters.out.persistence.portimpl;

import br.com.inventoryservice.infrastructure.adapters.out.persistence.entity.StockAdjustmentEntity;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.repository.StockAdjustmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StockAdjustmentAdapter")
class StockAdjustmentAdapterTest {

    @Mock
    private StockAdjustmentRepository stockAdjustmentRepository;

    @InjectMocks
    private StockAdjustmentAdapter stockAdjustmentAdapter;

    @Nested
    @DisplayName("SaveAdjustment Tests")
    class SaveAdjustmentTests {

        @Test
        @DisplayName("Should save stock adjustment successfully with valid parameters")
        void shouldSaveStockAdjustmentSuccessfully() {
            // Given
            String lojaCodigo = "LOJA001";
            String sku = "PROD123";
            int delta = 10;
            String motivo = "Reposição de estoque";
            Instant criadoEm = Instant.now();

            // When
            stockAdjustmentAdapter.saveAdjustment(lojaCodigo, sku, delta, motivo, criadoEm);

            // Then
            ArgumentCaptor<StockAdjustmentEntity> captor = ArgumentCaptor.forClass(StockAdjustmentEntity.class);
            verify(stockAdjustmentRepository, times(1)).save(captor.capture());
            
            StockAdjustmentEntity savedEntity = captor.getValue();
            assertEquals(lojaCodigo, savedEntity.getLojaCodigo());
            assertEquals(sku, savedEntity.getProdutoSku());
            assertEquals(delta, savedEntity.getDelta());
            assertEquals(motivo, savedEntity.getMotivo());
            assertEquals(criadoEm, savedEntity.getCriadoEm());
        }

        @Test
        @DisplayName("Should save stock adjustment with negative delta")
        void shouldSaveStockAdjustmentWithNegativeDelta() {
            // Given
            String lojaCodigo = "LOJA002";
            String sku = "PROD456";
            int delta = -5;
            String motivo = "Perda de estoque";
            Instant criadoEm = Instant.now();

            // When
            stockAdjustmentAdapter.saveAdjustment(lojaCodigo, sku, delta, motivo, criadoEm);

            // Then
            ArgumentCaptor<StockAdjustmentEntity> captor = ArgumentCaptor.forClass(StockAdjustmentEntity.class);
            verify(stockAdjustmentRepository, times(1)).save(captor.capture());
            
            StockAdjustmentEntity savedEntity = captor.getValue();
            assertEquals(delta, savedEntity.getDelta());
            assertTrue(savedEntity.getDelta() < 0);
        }

        @Test
        @DisplayName("Should save stock adjustment with zero delta")
        void shouldSaveStockAdjustmentWithZeroDelta() {
            // Given
            String lojaCodigo = "LOJA003";
            String sku = "PROD789";
            int delta = 0;
            String motivo = "Correção sem alteração";
            Instant criadoEm = Instant.now();

            // When
            stockAdjustmentAdapter.saveAdjustment(lojaCodigo, sku, delta, motivo, criadoEm);

            // Then
            ArgumentCaptor<StockAdjustmentEntity> captor = ArgumentCaptor.forClass(StockAdjustmentEntity.class);
            verify(stockAdjustmentRepository, times(1)).save(captor.capture());
            
            StockAdjustmentEntity savedEntity = captor.getValue();
            assertEquals(0, savedEntity.getDelta());
        }

        @Test
        @DisplayName("Should handle null lojaCodigo parameter")
        void shouldHandleNullLojaCodigoParameter() {
            // Given
            String lojaCodigo = null;
            String sku = "PROD123";
            int delta = 10;
            String motivo = "Teste com loja null";
            Instant criadoEm = Instant.now();

            // When
            stockAdjustmentAdapter.saveAdjustment(lojaCodigo, sku, delta, motivo, criadoEm);

            // Then
            ArgumentCaptor<StockAdjustmentEntity> captor = ArgumentCaptor.forClass(StockAdjustmentEntity.class);
            verify(stockAdjustmentRepository, times(1)).save(captor.capture());
            
            StockAdjustmentEntity savedEntity = captor.getValue();
            assertNull(savedEntity.getLojaCodigo());
        }

        @Test
        @DisplayName("Should handle null sku parameter")
        void shouldHandleNullSkuParameter() {
            // Given
            String lojaCodigo = "LOJA001";
            String sku = null;
            int delta = 10;
            String motivo = "Teste com SKU null";
            Instant criadoEm = Instant.now();

            // When
            stockAdjustmentAdapter.saveAdjustment(lojaCodigo, sku, delta, motivo, criadoEm);

            // Then
            ArgumentCaptor<StockAdjustmentEntity> captor = ArgumentCaptor.forClass(StockAdjustmentEntity.class);
            verify(stockAdjustmentRepository, times(1)).save(captor.capture());
            
            StockAdjustmentEntity savedEntity = captor.getValue();
            assertNull(savedEntity.getProdutoSku());
        }

        @Test
        @DisplayName("Should handle null motivo parameter")
        void shouldHandleNullMotivoParameter() {
            // Given
            String lojaCodigo = "LOJA001";
            String sku = "PROD123";
            int delta = 10;
            String motivo = null;
            Instant criadoEm = Instant.now();

            // When
            stockAdjustmentAdapter.saveAdjustment(lojaCodigo, sku, delta, motivo, criadoEm);

            // Then
            ArgumentCaptor<StockAdjustmentEntity> captor = ArgumentCaptor.forClass(StockAdjustmentEntity.class);
            verify(stockAdjustmentRepository, times(1)).save(captor.capture());
            
            StockAdjustmentEntity savedEntity = captor.getValue();
            assertNull(savedEntity.getMotivo());
        }

        @Test
        @DisplayName("Should handle null criadoEm parameter")
        void shouldHandleNullCriadoEmParameter() {
            // Given
            String lojaCodigo = "LOJA001";
            String sku = "PROD123";
            int delta = 10;
            String motivo = "Teste com data null";
            Instant criadoEm = null;

            // When
            stockAdjustmentAdapter.saveAdjustment(lojaCodigo, sku, delta, motivo, criadoEm);

            // Then
            ArgumentCaptor<StockAdjustmentEntity> captor = ArgumentCaptor.forClass(StockAdjustmentEntity.class);
            verify(stockAdjustmentRepository, times(1)).save(captor.capture());
            
            StockAdjustmentEntity savedEntity = captor.getValue();
            assertNull(savedEntity.getCriadoEm());
        }

        @Test
        @DisplayName("Should handle empty string parameters")
        void shouldHandleEmptyStringParameters() {
            // Given
            String lojaCodigo = "";
            String sku = "";
            int delta = 15;
            String motivo = "";
            Instant criadoEm = Instant.now();

            // When
            stockAdjustmentAdapter.saveAdjustment(lojaCodigo, sku, delta, motivo, criadoEm);

            // Then
            ArgumentCaptor<StockAdjustmentEntity> captor = ArgumentCaptor.forClass(StockAdjustmentEntity.class);
            verify(stockAdjustmentRepository, times(1)).save(captor.capture());
            
            StockAdjustmentEntity savedEntity = captor.getValue();
            assertEquals("", savedEntity.getLojaCodigo());
            assertEquals("", savedEntity.getProdutoSku());
            assertEquals("", savedEntity.getMotivo());
        }

        @Test
        @DisplayName("Should handle whitespace string parameters")
        void shouldHandleWhitespaceStringParameters() {
            // Given
            String lojaCodigo = "   ";
            String sku = "   ";
            int delta = 20;
            String motivo = "   ";
            Instant criadoEm = Instant.now();

            // When
            stockAdjustmentAdapter.saveAdjustment(lojaCodigo, sku, delta, motivo, criadoEm);

            // Then
            ArgumentCaptor<StockAdjustmentEntity> captor = ArgumentCaptor.forClass(StockAdjustmentEntity.class);
            verify(stockAdjustmentRepository, times(1)).save(captor.capture());
            
            StockAdjustmentEntity savedEntity = captor.getValue();
            assertEquals("   ", savedEntity.getLojaCodigo());
            assertEquals("   ", savedEntity.getProdutoSku());
            assertEquals("   ", savedEntity.getMotivo());
        }

        @Test
        @DisplayName("Should verify repository save is called exactly once")
        void shouldVerifyRepositorySaveIsCalledExactlyOnce() {
            // Given
            String lojaCodigo = "LOJA001";
            String sku = "PROD123";
            int delta = 10;
            String motivo = "Verificação de chamada única";
            Instant criadoEm = Instant.now();

            // When
            stockAdjustmentAdapter.saveAdjustment(lojaCodigo, sku, delta, motivo, criadoEm);

            // Then
            verify(stockAdjustmentRepository, times(1)).save(any(StockAdjustmentEntity.class));
            verifyNoMoreInteractions(stockAdjustmentRepository);
        }

        @Test
        @DisplayName("Should handle large positive delta values")
        void shouldHandleLargePositiveDeltaValues() {
            // Given
            String lojaCodigo = "LOJA001";
            String sku = "PROD999";
            int delta = Integer.MAX_VALUE;
            String motivo = "Teste com valor máximo";
            Instant criadoEm = Instant.now();

            // When
            stockAdjustmentAdapter.saveAdjustment(lojaCodigo, sku, delta, motivo, criadoEm);

            // Then
            ArgumentCaptor<StockAdjustmentEntity> captor = ArgumentCaptor.forClass(StockAdjustmentEntity.class);
            verify(stockAdjustmentRepository, times(1)).save(captor.capture());
            
            StockAdjustmentEntity savedEntity = captor.getValue();
            assertEquals(Integer.MAX_VALUE, savedEntity.getDelta());
        }

        @Test
        @DisplayName("Should handle large negative delta values")
        void shouldHandleLargeNegativeDeltaValues() {
            // Given
            String lojaCodigo = "LOJA001";
            String sku = "PROD999";
            int delta = Integer.MIN_VALUE;
            String motivo = "Teste com valor mínimo";
            Instant criadoEm = Instant.now();

            // When
            stockAdjustmentAdapter.saveAdjustment(lojaCodigo, sku, delta, motivo, criadoEm);

            // Then
            ArgumentCaptor<StockAdjustmentEntity> captor = ArgumentCaptor.forClass(StockAdjustmentEntity.class);
            verify(stockAdjustmentRepository, times(1)).save(captor.capture());
            
            StockAdjustmentEntity savedEntity = captor.getValue();
            assertEquals(Integer.MIN_VALUE, savedEntity.getDelta());
        }
    }
}