package br.com.inventoryservice.infrastructure.adapters.out.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StockEntity - Testes Unitários")
class StockEntityTest {

    @Nested
    @DisplayName("Construtores")
    class Construtores {

        @Test
        @DisplayName("Deve criar StockEntity com construtor sem argumentos")
        void deveCriarStockEntityComConstrutorSemArgumentos() {
            // When
            StockEntity stockEntity = new StockEntity();

            // Then
            assertNotNull(stockEntity);
            assertNull(stockEntity.getId());
            assertNull(stockEntity.getQuantidade());
        }

        @Test
        @DisplayName("Deve criar StockEntity com construtor com todos os argumentos")
        void deveCriarStockEntityComConstrutorComTodosArgumentos() {
            // Given
            StockId stockId = StockId.builder()
                    .lojaCodigo("LOJA001")
                    .produtoSku("SKU001")
                    .build();
            Integer quantidade = 100;

            // When
            StockEntity stockEntity = new StockEntity(stockId, quantidade);

            // Then
            assertNotNull(stockEntity);
            assertEquals(stockId, stockEntity.getId());
            assertEquals(quantidade, stockEntity.getQuantidade());
        }

        @Test
        @DisplayName("Deve criar StockEntity com valores nulos no construtor")
        void deveCriarStockEntityComValoresNulosNoConstitutor() {
            // When
            StockEntity stockEntity = new StockEntity(null, null);

            // Then
            assertNotNull(stockEntity);
            assertNull(stockEntity.getId());
            assertNull(stockEntity.getQuantidade());
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPattern {

        @Test
        @DisplayName("Deve criar StockEntity com builder completo")
        void deveCriarStockEntityComBuilderCompleto() {
            // Given
            StockId stockId = StockId.builder()
                    .lojaCodigo("LOJA002")
                    .produtoSku("SKU002")
                    .build();
            Integer quantidade = 250;

            // When
            StockEntity stockEntity = StockEntity.builder()
                    .id(stockId)
                    .quantidade(quantidade)
                    .build();

            // Then
            assertNotNull(stockEntity);
            assertEquals(stockId, stockEntity.getId());
            assertEquals(quantidade, stockEntity.getQuantidade());
        }

        @Test
        @DisplayName("Deve criar StockEntity com builder vazio")
        void deveCriarStockEntityComBuilderVazio() {
            // When
            StockEntity stockEntity = StockEntity.builder().build();

            // Then
            assertNotNull(stockEntity);
            assertNull(stockEntity.getId());
            assertNull(stockEntity.getQuantidade());
        }

        @Test
        @DisplayName("Deve criar StockEntity com builder parcial")
        void deveCriarStockEntityComBuilderParcial() {
            // Given
            Integer quantidade = 75;

            // When
            StockEntity stockEntity = StockEntity.builder()
                    .quantidade(quantidade)
                    .build();

            // Then
            assertNotNull(stockEntity);
            assertNull(stockEntity.getId());
            assertEquals(quantidade, stockEntity.getQuantidade());
        }
    }

    @Nested
    @DisplayName("Getters e Setters")
    class GettersSetters {

        @Test
        @DisplayName("Deve definir e obter ID corretamente")
        void deveDefinirEObterIdCorretamente() {
            // Given
            StockEntity stockEntity = new StockEntity();
            StockId stockId = StockId.builder()
                    .lojaCodigo("LOJA003")
                    .produtoSku("SKU003")
                    .build();

            // When
            stockEntity.setId(stockId);

            // Then
            assertEquals(stockId, stockEntity.getId());
        }

        @Test
        @DisplayName("Deve definir e obter quantidade corretamente")
        void deveDefinirEObterQuantidadeCorretamente() {
            // Given
            StockEntity stockEntity = new StockEntity();
            Integer quantidade = 150;

            // When
            stockEntity.setQuantidade(quantidade);

            // Then
            assertEquals(quantidade, stockEntity.getQuantidade());
        }

        @Test
        @DisplayName("Deve aceitar valores nulos nos setters")
        void deveAceitarValoresNulosNosSetters() {
            // Given
            StockId stockId = StockId.builder()
                    .lojaCodigo("LOJA004")
                    .produtoSku("SKU004")
                    .build();
            StockEntity stockEntity = new StockEntity(stockId, 200);

            // When
            stockEntity.setId(null);
            stockEntity.setQuantidade(null);

            // Then
            assertNull(stockEntity.getId());
            assertNull(stockEntity.getQuantidade());
        }
    }

    @Nested
    @DisplayName("Validações de Negócio")
    class ValidacoesNegocio {

        @Test
        @DisplayName("Deve aceitar quantidade zero")
        void deveAceitarQuantidadeZero() {
            // Given
            StockId stockId = StockId.builder()
                    .lojaCodigo("LOJA011")
                    .produtoSku("SKU011")
                    .build();
            Integer quantidadeZero = 0;

            // When
            StockEntity stockEntity = StockEntity.builder()
                    .id(stockId)
                    .quantidade(quantidadeZero)
                    .build();

            // Then
            assertNotNull(stockEntity);
            assertEquals(quantidadeZero, stockEntity.getQuantidade());
            assertEquals(0, stockEntity.getQuantidade());
        }

        @Test
        @DisplayName("Deve aceitar quantidade positiva")
        void deveAceitarQuantidadePositiva() {
            // Given
            StockId stockId = StockId.builder()
                    .lojaCodigo("LOJA012")
                    .produtoSku("SKU012")
                    .build();
            Integer quantidadePositiva = 999;

            // When
            StockEntity stockEntity = StockEntity.builder()
                    .id(stockId)
                    .quantidade(quantidadePositiva)
                    .build();

            // Then
            assertNotNull(stockEntity);
            assertEquals(quantidadePositiva, stockEntity.getQuantidade());
            assertTrue(stockEntity.getQuantidade() > 0);
        }

        @Test
        @DisplayName("Deve aceitar quantidade negativa para casos especiais")
        void deveAceitarQuantidadeNegativaParaCasosEspeciais() {
            // Given
            StockId stockId = StockId.builder()
                    .lojaCodigo("LOJA013")
                    .produtoSku("SKU013")
                    .build();
            Integer quantidadeNegativa = -10;

            // When
            StockEntity stockEntity = StockEntity.builder()
                    .id(stockId)
                    .quantidade(quantidadeNegativa)
                    .build();

            // Then
            assertNotNull(stockEntity);
            assertEquals(quantidadeNegativa, stockEntity.getQuantidade());
            assertTrue(stockEntity.getQuantidade() < 0);
        }

        @Test
        @DisplayName("Deve permitir mudança de quantidade")
        void devePermitirMudancaDeQuantidade() {
            // Given
            StockId stockId = StockId.builder()
                    .lojaCodigo("LOJA014")
                    .produtoSku("SKU014")
                    .build();
            StockEntity stockEntity = StockEntity.builder()
                    .id(stockId)
                    .quantidade(50)
                    .build();

            // When
            stockEntity.setQuantidade(75);

            // Then
            assertEquals(75, stockEntity.getQuantidade());
        }
    }

    @Nested
    @DisplayName("Relacionamento com StockId")
    class RelacionamentoComStockId {

        @Test
        @DisplayName("Deve manter referência correta ao StockId")
        void deveManterReferenciaCorretaAoStockId() {
            // Given
            StockId stockId = StockId.builder()
                    .lojaCodigo("LOJA015")
                    .produtoSku("SKU015")
                    .build();

            // When
            StockEntity stockEntity = StockEntity.builder()
                    .id(stockId)
                    .quantidade(125)
                    .build();

            // Then
            assertNotNull(stockEntity.getId());
            assertEquals("LOJA015", stockEntity.getId().getLojaCodigo());
            assertEquals("SKU015", stockEntity.getId().getProdutoSku());
        }

        @Test
        @DisplayName("Deve permitir alteração do StockId")
        void devePermitirAlteracaoDoStockId() {
            // Given
            StockId stockIdOriginal = StockId.builder()
                    .lojaCodigo("LOJA016")
                    .produtoSku("SKU016")
                    .build();

            StockId novoStockId = StockId.builder()
                    .lojaCodigo("LOJA017")
                    .produtoSku("SKU017")
                    .build();

            StockEntity stockEntity = StockEntity.builder()
                    .id(stockIdOriginal)
                    .quantidade(175)
                    .build();

            // When
            stockEntity.setId(novoStockId);

            // Then
            assertEquals(novoStockId, stockEntity.getId());
            assertEquals("LOJA017", stockEntity.getId().getLojaCodigo());
            assertEquals("SKU017", stockEntity.getId().getProdutoSku());
        }

        @Test
        @DisplayName("Deve funcionar corretamente com StockId nulo")
        void deveFuncionarCorretamenteComStockIdNulo() {
            // When
            StockEntity stockEntity = StockEntity.builder()
                    .id(null)
                    .quantidade(225)
                    .build();

            // Then
            assertNull(stockEntity.getId());
            assertEquals(225, stockEntity.getQuantidade());
        }
    }
}