package br.com.inventoryservice.infrastructure.adapters.out.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StockAdjustmentEntity - Testes Unitários")
class StockAdjustmentEntityTest {

    @Nested
    @DisplayName("Construtores")
    class Construtores {

        @Test
        @DisplayName("Deve criar StockAdjustmentEntity com construtor sem argumentos")
        void deveCriarStockAdjustmentEntityComConstrutorSemArgumentos() {
            // When
            StockAdjustmentEntity entity = new StockAdjustmentEntity();

            // Then
            assertNotNull(entity);
            assertNull(entity.getId());
            assertNull(entity.getLojaCodigo());
            assertNull(entity.getProdutoSku());
            assertNull(entity.getDelta());
            assertNull(entity.getMotivo());
            assertNull(entity.getCriadoEm());
        }

        @Test
        @DisplayName("Deve criar StockAdjustmentEntity com construtor com todos os argumentos")
        void deveCriarStockAdjustmentEntityComConstrutorComTodosArgumentos() {
            // Given
            UUID id = UUID.randomUUID();
            String lojaCodigo = "LOJA001";
            String produtoSku = "SKU001";
            Integer delta = 10;
            String motivo = "Ajuste de inventário";
            Instant criadoEm = Instant.now();

            // When
            StockAdjustmentEntity entity = new StockAdjustmentEntity(id, lojaCodigo, produtoSku, delta, motivo, criadoEm);

            // Then
            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertEquals(lojaCodigo, entity.getLojaCodigo());
            assertEquals(produtoSku, entity.getProdutoSku());
            assertEquals(delta, entity.getDelta());
            assertEquals(motivo, entity.getMotivo());
            assertEquals(criadoEm, entity.getCriadoEm());
        }

        @Test
        @DisplayName("Deve criar StockAdjustmentEntity com valores nulos no construtor")
        void deveCriarStockAdjustmentEntityComValoresNulosNoConstitutor() {
            // When
            StockAdjustmentEntity entity = new StockAdjustmentEntity(null, null, null, null, null, null);

            // Then
            assertNotNull(entity);
            assertNull(entity.getId());
            assertNull(entity.getLojaCodigo());
            assertNull(entity.getProdutoSku());
            assertNull(entity.getDelta());
            assertNull(entity.getMotivo());
            assertNull(entity.getCriadoEm());
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPattern {

        @Test
        @DisplayName("Deve criar StockAdjustmentEntity com builder completo")
        void deveCriarStockAdjustmentEntityComBuilderCompleto() {
            // Given
            UUID id = UUID.randomUUID();
            String lojaCodigo = "LOJA002";
            String produtoSku = "SKU002";
            Integer delta = -5;
            String motivo = "Correção de estoque";
            Instant criadoEm = Instant.now();

            // When
            StockAdjustmentEntity entity = StockAdjustmentEntity.builder()
                    .id(id)
                    .lojaCodigo(lojaCodigo)
                    .produtoSku(produtoSku)
                    .delta(delta)
                    .motivo(motivo)
                    .criadoEm(criadoEm)
                    .build();

            // Then
            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertEquals(lojaCodigo, entity.getLojaCodigo());
            assertEquals(produtoSku, entity.getProdutoSku());
            assertEquals(delta, entity.getDelta());
            assertEquals(motivo, entity.getMotivo());
            assertEquals(criadoEm, entity.getCriadoEm());
        }

        @Test
        @DisplayName("Deve criar StockAdjustmentEntity com builder vazio")
        void deveCriarStockAdjustmentEntityComBuilderVazio() {
            // When
            StockAdjustmentEntity entity = StockAdjustmentEntity.builder().build();

            // Then
            assertNotNull(entity);
            assertNull(entity.getId());
            assertNull(entity.getLojaCodigo());
            assertNull(entity.getProdutoSku());
            assertNull(entity.getDelta());
            assertNull(entity.getMotivo());
            assertNull(entity.getCriadoEm());
        }

        @Test
        @DisplayName("Deve criar StockAdjustmentEntity com builder parcial")
        void deveCriarStockAdjustmentEntityComBuilderParcial() {
            // Given
            String lojaCodigo = "LOJA003";
            Integer delta = 15;

            // When
            StockAdjustmentEntity entity = StockAdjustmentEntity.builder()
                    .lojaCodigo(lojaCodigo)
                    .delta(delta)
                    .build();

            // Then
            assertNotNull(entity);
            assertNull(entity.getId());
            assertEquals(lojaCodigo, entity.getLojaCodigo());
            assertNull(entity.getProdutoSku());
            assertEquals(delta, entity.getDelta());
            assertNull(entity.getMotivo());
            assertNull(entity.getCriadoEm());
        }
    }

    @Nested
    @DisplayName("Getters e Setters")
    class GettersSetters {

        @Test
        @DisplayName("Deve definir e obter ID corretamente")
        void deveDefinirEObterIdCorretamente() {
            // Given
            StockAdjustmentEntity entity = new StockAdjustmentEntity();
            UUID id = UUID.randomUUID();

            // When
            entity.setId(id);

            // Then
            assertEquals(id, entity.getId());
        }

        @Test
        @DisplayName("Deve definir e obter loja código corretamente")
        void deveDefinirEObterLojaCodigoCorretamente() {
            // Given
            StockAdjustmentEntity entity = new StockAdjustmentEntity();
            String lojaCodigo = "LOJA004";

            // When
            entity.setLojaCodigo(lojaCodigo);

            // Then
            assertEquals(lojaCodigo, entity.getLojaCodigo());
        }

        @Test
        @DisplayName("Deve definir e obter produto SKU corretamente")
        void deveDefinirEObterProdutoSkuCorretamente() {
            // Given
            StockAdjustmentEntity entity = new StockAdjustmentEntity();
            String produtoSku = "SKU004";

            // When
            entity.setProdutoSku(produtoSku);

            // Then
            assertEquals(produtoSku, entity.getProdutoSku());
        }

        @Test
        @DisplayName("Deve definir e obter delta corretamente")
        void deveDefinirEObterDeltaCorretamente() {
            // Given
            StockAdjustmentEntity entity = new StockAdjustmentEntity();
            Integer delta = -10;

            // When
            entity.setDelta(delta);

            // Then
            assertEquals(delta, entity.getDelta());
        }

        @Test
        @DisplayName("Deve definir e obter motivo corretamente")
        void deveDefinirEObterMotivoCorretamente() {
            // Given
            StockAdjustmentEntity entity = new StockAdjustmentEntity();
            String motivo = "Quebra de produto";

            // When
            entity.setMotivo(motivo);

            // Then
            assertEquals(motivo, entity.getMotivo());
        }

        @Test
        @DisplayName("Deve definir e obter criado em corretamente")
        void deveDefinirEObterCriadoEmCorretamente() {
            // Given
            StockAdjustmentEntity entity = new StockAdjustmentEntity();
            Instant criadoEm = Instant.now();

            // When
            entity.setCriadoEm(criadoEm);

            // Then
            assertEquals(criadoEm, entity.getCriadoEm());
        }

        @Test
        @DisplayName("Deve aceitar valores nulos nos setters")
        void deveAceitarValoresNulosNosSetters() {
            // Given
            StockAdjustmentEntity entity = StockAdjustmentEntity.builder()
                    .id(UUID.randomUUID())
                    .lojaCodigo("LOJA005")
                    .produtoSku("SKU005")
                    .delta(5)
                    .motivo("Teste")
                    .criadoEm(Instant.now())
                    .build();

            // When
            entity.setId(null);
            entity.setLojaCodigo(null);
            entity.setProdutoSku(null);
            entity.setDelta(null);
            entity.setMotivo(null);
            entity.setCriadoEm(null);

            // Then
            assertNull(entity.getId());
            assertNull(entity.getLojaCodigo());
            assertNull(entity.getProdutoSku());
            assertNull(entity.getDelta());
            assertNull(entity.getMotivo());
            assertNull(entity.getCriadoEm());
        }
    }

    @Nested
    @DisplayName("Validações de Negócio")
    class ValidacoesNegocio {

        @Test
        @DisplayName("Deve aceitar delta positivo para entrada de estoque")
        void deveAceitarDeltaPositivoParaEntradaDeEstoque() {
            // Given
            Integer deltaPositivo = 100;

            // When
            StockAdjustmentEntity entity = StockAdjustmentEntity.builder()
                    .lojaCodigo("LOJA012")
                    .produtoSku("SKU012")
                    .delta(deltaPositivo)
                    .motivo("Entrada de mercadoria")
                    .build();

            // Then
            assertNotNull(entity);
            assertEquals(deltaPositivo, entity.getDelta());
            assertTrue(entity.getDelta() > 0);
        }

        @Test
        @DisplayName("Deve aceitar delta negativo para saída de estoque")
        void deveAceitarDeltaNegativoParaSaidaDeEstoque() {
            // Given
            Integer deltaNegativo = -50;

            // When
            StockAdjustmentEntity entity = StockAdjustmentEntity.builder()
                    .lojaCodigo("LOJA013")
                    .produtoSku("SKU013")
                    .delta(deltaNegativo)
                    .motivo("Quebra de produto")
                    .build();

            // Then
            assertNotNull(entity);
            assertEquals(deltaNegativo, entity.getDelta());
            assertTrue(entity.getDelta() < 0);
        }

        @Test
        @DisplayName("Deve aceitar delta zero para ajuste sem alteração")
        void deveAceitarDeltaZeroParaAjusteSemAlteracao() {
            // Given
            Integer deltaZero = 0;

            // When
            StockAdjustmentEntity entity = StockAdjustmentEntity.builder()
                    .lojaCodigo("LOJA014")
                    .produtoSku("SKU014")
                    .delta(deltaZero)
                    .motivo("Ajuste sem alteração")
                    .build();

            // Then
            assertNotNull(entity);
            assertEquals(deltaZero, entity.getDelta());
            assertEquals(0, entity.getDelta());
        }
    }
}