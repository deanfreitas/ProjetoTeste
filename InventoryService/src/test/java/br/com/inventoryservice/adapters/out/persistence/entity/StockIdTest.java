package br.com.inventoryservice.adapters.out.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StockId - Testes Unitários")
class StockIdTest {

    @Nested
    @DisplayName("Construtores")
    class Construtores {

        @Test
        @DisplayName("Deve criar StockId com construtor sem argumentos")
        void deveCriarStockIdComConstrutorSemArgumentos() {
            // When
            StockId stockId = new StockId();

            // Then
            assertNotNull(stockId);
            assertNull(stockId.getLojaCodigo());
            assertNull(stockId.getProdutoSku());
        }

        @Test
        @DisplayName("Deve criar StockId com construtor com todos os argumentos")
        void deveCriarStockIdComConstrutorComTodosArgumentos() {
            // Given
            String lojaCodigo = "LOJA001";
            String produtoSku = "SKU001";

            // When
            StockId stockId = new StockId(lojaCodigo, produtoSku);

            // Then
            assertNotNull(stockId);
            assertEquals(lojaCodigo, stockId.getLojaCodigo());
            assertEquals(produtoSku, stockId.getProdutoSku());
        }

        @Test
        @DisplayName("Deve criar StockId com valores nulos no construtor")
        void deveCriarStockIdComValoresNulosNoConstitutor() {
            // When
            StockId stockId = new StockId(null, null);

            // Then
            assertNotNull(stockId);
            assertNull(stockId.getLojaCodigo());
            assertNull(stockId.getProdutoSku());
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPattern {

        @Test
        @DisplayName("Deve criar StockId com builder completo")
        void deveCriarStockIdComBuilderCompleto() {
            // Given
            String lojaCodigo = "LOJA002";
            String produtoSku = "SKU002";

            // When
            StockId stockId = StockId.builder()
                    .lojaCodigo(lojaCodigo)
                    .produtoSku(produtoSku)
                    .build();

            // Then
            assertNotNull(stockId);
            assertEquals(lojaCodigo, stockId.getLojaCodigo());
            assertEquals(produtoSku, stockId.getProdutoSku());
        }

        @Test
        @DisplayName("Deve criar StockId com builder vazio")
        void deveCriarStockIdComBuilderVazio() {
            // When
            StockId stockId = StockId.builder().build();

            // Then
            assertNotNull(stockId);
            assertNull(stockId.getLojaCodigo());
            assertNull(stockId.getProdutoSku());
        }

        @Test
        @DisplayName("Deve criar StockId com builder parcial")
        void deveCriarStockIdComBuilderParcial() {
            // Given
            String lojaCodigo = "LOJA003";

            // When
            StockId stockId = StockId.builder()
                    .lojaCodigo(lojaCodigo)
                    .build();

            // Then
            assertNotNull(stockId);
            assertEquals(lojaCodigo, stockId.getLojaCodigo());
            assertNull(stockId.getProdutoSku());
        }

        @Test
        @DisplayName("Deve testar toString do builder")
        void deveTestarToStringDoBuilder() {
            // Given & When
            String builderToString = StockId.builder()
                    .lojaCodigo("LOJA001")
                    .produtoSku("SKU001")
                    .toString();

            // Then
            assertNotNull(builderToString);
            assertTrue(builderToString.contains("StockId.StockIdBuilder"));
        }
    }

    @Nested
    @DisplayName("Getters e Setters")
    class GettersSetters {

        @Test
        @DisplayName("Deve definir e obter loja código corretamente")
        void deveDefinirEObterLojaCodigoCorretamente() {
            // Given
            StockId stockId = new StockId();
            String lojaCodigo = "LOJA004";

            // When
            stockId.setLojaCodigo(lojaCodigo);

            // Then
            assertEquals(lojaCodigo, stockId.getLojaCodigo());
        }

        @Test
        @DisplayName("Deve definir e obter produto SKU corretamente")
        void deveDefinirEObterProdutoSkuCorretamente() {
            // Given
            StockId stockId = new StockId();
            String produtoSku = "SKU004";

            // When
            stockId.setProdutoSku(produtoSku);

            // Then
            assertEquals(produtoSku, stockId.getProdutoSku());
        }

        @Test
        @DisplayName("Deve aceitar valores nulos nos setters")
        void deveAceitarValoresNulosNosSetters() {
            // Given
            StockId stockId = new StockId("LOJA005", "SKU005");

            // When
            stockId.setLojaCodigo(null);
            stockId.setProdutoSku(null);

            // Then
            assertNull(stockId.getLojaCodigo());
            assertNull(stockId.getProdutoSku());
        }
    }

    @Nested
    @DisplayName("Validações de Negócio")
    class ValidacoesNegocio {

        @Test
        @DisplayName("Deve aceitar códigos de loja válidos")
        void deveAceitarCodigosDeLojaValidos() {
            // Given
            String[] codigosValidos = {"LOJA001", "L001", "MATRIZ", "FILIAL-001", "CENTRO_SP"};

            for (String codigo : codigosValidos) {
                // When
                StockId stockId = StockId.builder()
                        .lojaCodigo(codigo)
                        .produtoSku("SKU001")
                        .build();

                // Then
                assertNotNull(stockId);
                assertEquals(codigo, stockId.getLojaCodigo());
            }
        }

        @Test
        @DisplayName("Deve aceitar SKUs de produto válidos")
        void deveAceitarSkusDeProdutoValidos() {
            // Given
            String[] skusValidos = {"SKU001", "PROD-123", "ABC123DEF", "12345", "ITEM_ESPECIAL"};

            for (String sku : skusValidos) {
                // When
                StockId stockId = StockId.builder()
                        .lojaCodigo("LOJA001")
                        .produtoSku(sku)
                        .build();

                // Then
                assertNotNull(stockId);
                assertEquals(sku, stockId.getProdutoSku());
            }
        }

        @Test
        @DisplayName("Deve criar StockId para diferentes combinações de loja e produto")
        void deveCriarStockIdParaDiferentesCombinacoes() {
            // Given
            String[] lojas = {"LOJA_A", "LOJA_B", "LOJA_C"};
            String[] produtos = {"PROD_1", "PROD_2", "PROD_3"};

            for (String loja : lojas) {
                for (String produto : produtos) {
                    // When
                    StockId stockId = StockId.builder()
                            .lojaCodigo(loja)
                            .produtoSku(produto)
                            .build();

                    // Then
                    assertNotNull(stockId);
                    assertEquals(loja, stockId.getLojaCodigo());
                    assertEquals(produto, stockId.getProdutoSku());
                }
            }
        }
    }
}