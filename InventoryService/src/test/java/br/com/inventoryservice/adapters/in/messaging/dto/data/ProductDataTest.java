package br.com.inventoryservice.adapters.in.messaging.dto.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductData - Testes Unitários")
class ProductDataTest {

    @Nested
    @DisplayName("Construtores")
    class Construtores {

        @Test
        @DisplayName("Deve criar ProductData com construtor sem argumentos")
        void deveCriarProductDataComConstrutorSemArgumentos() {
            // When
            ProductData productData = new ProductData();

            // Then
            assertNotNull(productData);
            assertNull(productData.getSku());
            assertNull(productData.getNome());
            assertNull(productData.getAtivo());
        }

        @Test
        @DisplayName("Deve criar ProductData com construtor com todos os argumentos")
        void deveCriarProductDataComConstrutorComTodosArgumentos() {
            // Given
            String sku = "SKU001";
            String nome = "Produto Teste";
            Boolean ativo = true;

            // When
            ProductData productData = new ProductData(sku, nome, ativo);

            // Then
            assertNotNull(productData);
            assertEquals(sku, productData.getSku());
            assertEquals(nome, productData.getNome());
            assertEquals(ativo, productData.getAtivo());
        }

        @Test
        @DisplayName("Deve criar ProductData com valores nulos no construtor")
        void deveCriarProductDataComValoresNulosNoConstitutor() {
            // When
            ProductData productData = new ProductData(null, null, null);

            // Then
            assertNotNull(productData);
            assertNull(productData.getSku());
            assertNull(productData.getNome());
            assertNull(productData.getAtivo());
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPattern {

        @Test
        @DisplayName("Deve criar ProductData com builder completo")
        void deveCriarProductDataComBuilderCompleto() {
            // Given
            String sku = "SKU002";
            String nome = "Produto Builder";
            Boolean ativo = false;

            // When
            ProductData productData = ProductData.builder()
                    .sku(sku)
                    .nome(nome)
                    .ativo(ativo)
                    .build();

            // Then
            assertNotNull(productData);
            assertEquals(sku, productData.getSku());
            assertEquals(nome, productData.getNome());
            assertEquals(ativo, productData.getAtivo());
        }

        @Test
        @DisplayName("Deve criar ProductData com builder vazio")
        void deveCriarProductDataComBuilderVazio() {
            // When
            ProductData productData = ProductData.builder().build();

            // Then
            assertNotNull(productData);
            assertNull(productData.getSku());
            assertNull(productData.getNome());
            assertNull(productData.getAtivo());
        }

        @Test
        @DisplayName("Deve criar ProductData com builder parcial")
        void deveCriarProductDataComBuilderParcial() {
            // Given
            String sku = "SKU003";

            // When
            ProductData productData = ProductData.builder()
                    .sku(sku)
                    .build();

            // Then
            assertNotNull(productData);
            assertEquals(sku, productData.getSku());
            assertNull(productData.getNome());
            assertNull(productData.getAtivo());
        }

        @Test
        @DisplayName("Deve testar toString do builder")
        void deveTestarToStringDoBuilder() {
            // Given & When
            String builderToString = ProductData.builder()
                    .sku("SKU001")
                    .nome("Produto Teste")
                    .ativo(true)
                    .toString();

            // Then
            assertNotNull(builderToString);
            assertTrue(builderToString.contains("ProductData.ProductDataBuilder"));
        }
    }

    @Nested
    @DisplayName("Getters e Setters")
    class GettersSetters {

        @Test
        @DisplayName("Deve definir e obter SKU")
        void deveDefinirEObterSku() {
            // Given
            ProductData productData = new ProductData();
            String sku = "SKU004";

            // When
            productData.setSku(sku);

            // Then
            assertEquals(sku, productData.getSku());
        }

        @Test
        @DisplayName("Deve definir e obter nome")
        void deveDefinirEObterNome() {
            // Given
            ProductData productData = new ProductData();
            String nome = "Produto Getter Setter";

            // When
            productData.setNome(nome);

            // Then
            assertEquals(nome, productData.getNome());
        }

        @Test
        @DisplayName("Deve definir e obter ativo")
        void deveDefinirEObterAtivo() {
            // Given
            ProductData productData = new ProductData();
            Boolean ativo = true;

            // When
            productData.setAtivo(ativo);

            // Then
            assertEquals(ativo, productData.getAtivo());
        }

        @Test
        @DisplayName("Deve permitir valores nulos nos setters")
        void devePermitirValoresNulosNosSetters() {
            // Given
            ProductData productData = new ProductData("SKU005", "Produto", true);

            // When
            productData.setSku(null);
            productData.setNome(null);
            productData.setAtivo(null);

            // Then
            assertNull(productData.getSku());
            assertNull(productData.getNome());
            assertNull(productData.getAtivo());
        }
    }

    @Nested
    @DisplayName("Equals e HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("Deve verificar igualdade entre objetos com mesmos valores")
        void deveVerificarIgualdadeEntreObjetosComMesmosValores() {
            // Given
            ProductData productData1 = new ProductData("SKU006", "Produto", true);
            ProductData productData2 = new ProductData("SKU006", "Produto", true);

            // Then
            assertEquals(productData1, productData2);
            assertEquals(productData1.hashCode(), productData2.hashCode());
        }

        @Test
        @DisplayName("Deve verificar desigualdade entre objetos com valores diferentes")
        void deveVerificarDesigualdadeEntreObjetosComValoresDiferentes() {
            // Given
            ProductData productData1 = new ProductData("SKU007", "Produto 1", true);
            ProductData productData2 = new ProductData("SKU008", "Produto 2", false);

            // Then
            assertNotEquals(productData1, productData2);
            assertNotEquals(productData1.hashCode(), productData2.hashCode());
        }

        @Test
        @DisplayName("Deve verificar igualdade com valores nulos")
        void deveVerificarIgualdadeComValoresNulos() {
            // Given
            ProductData productData1 = new ProductData(null, null, null);
            ProductData productData2 = new ProductData(null, null, null);

            // Then
            assertEquals(productData1, productData2);
            assertEquals(productData1.hashCode(), productData2.hashCode());
        }

        @Test
        @DisplayName("Deve verificar desigualdade com null")
        void deveVerificarDesigualdadeComNull() {
            // Given
            ProductData productData = new ProductData("SKU009", "Produto", true);

            // Then
            assertNotEquals(productData, null);
        }

        @Test
        @DisplayName("Deve verificar igualdade consigo mesmo")
        void deveVerificarIgualdadeConsigoMesmo() {
            // Given
            ProductData productData = new ProductData("SKU010", "Produto", false);

            // Then
            assertEquals(productData, productData);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToString {

        @Test
        @DisplayName("Deve gerar toString com todos os valores preenchidos")
        void deveGerarToStringComTodosValoresPreenchidos() {
            // Given
            ProductData productData = new ProductData("SKU011", "Produto ToString", true);

            // When
            String toString = productData.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("SKU011"));
            assertTrue(toString.contains("Produto ToString"));
            assertTrue(toString.contains("true"));
        }

        @Test
        @DisplayName("Deve gerar toString com valores nulos")
        void deveGerarToStringComValoresNulos() {
            // Given
            ProductData productData = new ProductData(null, null, null);

            // When
            String toString = productData.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("null"));
        }

        @Test
        @DisplayName("Deve gerar toString com valores parciais")
        void deveGerarToStringComValoresParciais() {
            // Given
            ProductData productData = new ProductData("SKU012", null, false);

            // When
            String toString = productData.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("SKU012"));
            assertTrue(toString.contains("null"));
            assertTrue(toString.contains("false"));
        }
    }
}