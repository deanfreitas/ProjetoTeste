package br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SalesItem - Testes Unitários")
class SalesItemTest {

    @Nested
    @DisplayName("Construtores")
    class Construtores {

        @Test
        @DisplayName("Deve criar SalesItem com construtor sem argumentos")
        void deveCriarSalesItemComConstrutorSemArgumentos() {
            // When
            SalesItem salesItem = new SalesItem();

            // Then
            assertNotNull(salesItem);
            assertNull(salesItem.getSku());
            assertNull(salesItem.getQuantidade());
        }

        @Test
        @DisplayName("Deve criar SalesItem com construtor com todos os argumentos")
        void deveCriarSalesItemComConstrutorComTodosArgumentos() {
            // Given
            String sku = "SKU001";
            Integer quantidade = 10;

            // When
            SalesItem salesItem = new SalesItem(sku, quantidade);

            // Then
            assertNotNull(salesItem);
            assertEquals(sku, salesItem.getSku());
            assertEquals(quantidade, salesItem.getQuantidade());
        }

        @Test
        @DisplayName("Deve criar SalesItem com valores nulos no construtor")
        void deveCriarSalesItemComValoresNulosNoConstitutor() {
            // When
            SalesItem salesItem = new SalesItem(null, null);

            // Then
            assertNotNull(salesItem);
            assertNull(salesItem.getSku());
            assertNull(salesItem.getQuantidade());
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPattern {

        @Test
        @DisplayName("Deve criar SalesItem com builder completo")
        void deveCriarSalesItemComBuilderCompleto() {
            // Given
            String sku = "SKU002";
            Integer quantidade = 25;

            // When
            SalesItem salesItem = SalesItem.builder()
                    .sku(sku)
                    .quantidade(quantidade)
                    .build();

            // Then
            assertNotNull(salesItem);
            assertEquals(sku, salesItem.getSku());
            assertEquals(quantidade, salesItem.getQuantidade());
        }

        @Test
        @DisplayName("Deve criar SalesItem com builder vazio")
        void deveCriarSalesItemComBuilderVazio() {
            // When
            SalesItem salesItem = SalesItem.builder().build();

            // Then
            assertNotNull(salesItem);
            assertNull(salesItem.getSku());
            assertNull(salesItem.getQuantidade());
        }

        @Test
        @DisplayName("Deve criar SalesItem com builder parcial")
        void deveCriarSalesItemComBuilderParcial() {
            // Given
            String sku = "SKU003";

            // When
            SalesItem salesItem = SalesItem.builder()
                    .sku(sku)
                    .build();

            // Then
            assertNotNull(salesItem);
            assertEquals(sku, salesItem.getSku());
            assertNull(salesItem.getQuantidade());
        }

        @Test
        @DisplayName("Deve testar toString do builder")
        void deveTestarToStringDoBuilder() {
            // Given & When
            String builderToString = SalesItem.builder()
                    .sku("SKU001")
                    .quantidade(10)
                    .toString();

            // Then
            assertNotNull(builderToString);
            assertTrue(builderToString.contains("SalesItem.SalesItemBuilder"));
        }
    }

    @Nested
    @DisplayName("Getters e Setters")
    class GettersSetters {

        @Test
        @DisplayName("Deve definir e obter SKU")
        void deveDefinirEObterSku() {
            // Given
            SalesItem salesItem = new SalesItem();
            String sku = "SKU004";

            // When
            salesItem.setSku(sku);

            // Then
            assertEquals(sku, salesItem.getSku());
        }

        @Test
        @DisplayName("Deve definir e obter quantidade")
        void deveDefinirEObterQuantidade() {
            // Given
            SalesItem salesItem = new SalesItem();
            Integer quantidade = 15;

            // When
            salesItem.setQuantidade(quantidade);

            // Then
            assertEquals(quantidade, salesItem.getQuantidade());
        }

        @Test
        @DisplayName("Deve permitir valores nulos nos setters")
        void devePermitirValoresNulosNosSetters() {
            // Given
            SalesItem salesItem = new SalesItem("SKU005", 20);

            // When
            salesItem.setSku(null);
            salesItem.setQuantidade(null);

            // Then
            assertNull(salesItem.getSku());
            assertNull(salesItem.getQuantidade());
        }

        @Test
        @DisplayName("Deve permitir quantidade zero")
        void devePermitirQuantidadeZero() {
            // Given
            SalesItem salesItem = new SalesItem();
            Integer quantidadeZero = 0;

            // When
            salesItem.setQuantidade(quantidadeZero);

            // Then
            assertEquals(quantidadeZero, salesItem.getQuantidade());
        }

        @Test
        @DisplayName("Deve permitir quantidade negativa")
        void devePermitirQuantidadeNegativa() {
            // Given
            SalesItem salesItem = new SalesItem();
            Integer quantidadeNegativa = -5;

            // When
            salesItem.setQuantidade(quantidadeNegativa);

            // Then
            assertEquals(quantidadeNegativa, salesItem.getQuantidade());
        }
    }

    @Nested
    @DisplayName("Equals e HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("Deve verificar igualdade entre objetos com mesmos valores")
        void deveVerificarIgualdadeEntreObjetosComMesmosValores() {
            // Given
            SalesItem salesItem1 = new SalesItem("SKU006", 30);
            SalesItem salesItem2 = new SalesItem("SKU006", 30);

            // Then
            assertEquals(salesItem1, salesItem2);
            assertEquals(salesItem1.hashCode(), salesItem2.hashCode());
        }

        @Test
        @DisplayName("Deve verificar desigualdade entre objetos com valores diferentes")
        void deveVerificarDesigualdadeEntreObjetosComValoresDiferentes() {
            // Given
            SalesItem salesItem1 = new SalesItem("SKU007", 10);
            SalesItem salesItem2 = new SalesItem("SKU008", 20);

            // Then
            assertNotEquals(salesItem1, salesItem2);
            assertNotEquals(salesItem1.hashCode(), salesItem2.hashCode());
        }

        @Test
        @DisplayName("Deve verificar igualdade com valores nulos")
        void deveVerificarIgualdadeComValoresNulos() {
            // Given
            SalesItem salesItem1 = new SalesItem(null, null);
            SalesItem salesItem2 = new SalesItem(null, null);

            // Then
            assertEquals(salesItem1, salesItem2);
            assertEquals(salesItem1.hashCode(), salesItem2.hashCode());
        }

        @Test
        @DisplayName("Deve verificar desigualdade com null")
        void deveVerificarDesigualdadeComNull() {
            // Given
            SalesItem salesItem = new SalesItem("SKU009", 35);

            // Then
            assertNotEquals(salesItem, null);
        }

        @Test
        @DisplayName("Deve verificar igualdade consigo mesmo")
        void deveVerificarIgualdadeConsigoMesmo() {
            // Given
            SalesItem salesItem = new SalesItem("SKU010", 40);

            // Then
            assertEquals(salesItem, salesItem);
        }

        @Test
        @DisplayName("Deve verificar desigualdade com SKU diferente")
        void deveVerificarDesigualdadeComSkuDiferente() {
            // Given
            SalesItem salesItem1 = new SalesItem("SKU011", 15);
            SalesItem salesItem2 = new SalesItem("SKU012", 15);

            // Then
            assertNotEquals(salesItem1, salesItem2);
        }

        @Test
        @DisplayName("Deve verificar desigualdade com quantidade diferente")
        void deveVerificarDesigualdadeComQuantidadeDiferente() {
            // Given
            SalesItem salesItem1 = new SalesItem("SKU013", 10);
            SalesItem salesItem2 = new SalesItem("SKU013", 20);

            // Then
            assertNotEquals(salesItem1, salesItem2);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToString {

        @Test
        @DisplayName("Deve gerar toString com todos os valores preenchidos")
        void deveGerarToStringComTodosValoresPreenchidos() {
            // Given
            SalesItem salesItem = new SalesItem("SKU014", 50);

            // When
            String toString = salesItem.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("SKU014"));
            assertTrue(toString.contains("50"));
        }

        @Test
        @DisplayName("Deve gerar toString com valores nulos")
        void deveGerarToStringComValoresNulos() {
            // Given
            SalesItem salesItem = new SalesItem(null, null);

            // When
            String toString = salesItem.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("null"));
        }

        @Test
        @DisplayName("Deve gerar toString com valores parciais")
        void deveGerarToStringComValoresParciais() {
            // Given
            SalesItem salesItem = new SalesItem("SKU015", null);

            // When
            String toString = salesItem.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("SKU015"));
            assertTrue(toString.contains("null"));
        }

        @Test
        @DisplayName("Deve gerar toString com quantidade zero")
        void deveGerarToStringComQuantidadeZero() {
            // Given
            SalesItem salesItem = new SalesItem("SKU016", 0);

            // When
            String toString = salesItem.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("SKU016"));
            assertTrue(toString.contains("0"));
        }

        @Test
        @DisplayName("Deve gerar toString com quantidade negativa")
        void deveGerarToStringComQuantidadeNegativa() {
            // Given
            SalesItem salesItem = new SalesItem("SKU017", -10);

            // When
            String toString = salesItem.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("SKU017"));
            assertTrue(toString.contains("-10"));
        }
    }
}