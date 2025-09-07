package br.com.inventoryservice.adapters.out.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductEntity - Testes Unitários")
class ProductEntityTest {

    @Nested
    @DisplayName("Construtores")
    class Construtores {

        @Test
        @DisplayName("Deve criar ProductEntity com construtor sem argumentos")
        void deveCriarProductEntityComConstrutorSemArgumentos() {
            // When
            ProductEntity productEntity = new ProductEntity();

            // Then
            assertNotNull(productEntity);
            assertNull(productEntity.getSku());
            assertNull(productEntity.getNome());
            assertNull(productEntity.getAtivo());
        }

        @Test
        @DisplayName("Deve criar ProductEntity com construtor com todos os argumentos")
        void deveCriarProductEntityComConstrutorComTodosArgumentos() {
            // Given
            String sku = "SKU001";
            String nome = "Produto Teste";
            Boolean ativo = true;

            // When
            ProductEntity productEntity = new ProductEntity(sku, nome, ativo);

            // Then
            assertNotNull(productEntity);
            assertEquals(sku, productEntity.getSku());
            assertEquals(nome, productEntity.getNome());
            assertEquals(ativo, productEntity.getAtivo());
        }

        @Test
        @DisplayName("Deve criar ProductEntity com valores nulos no construtor")
        void deveCriarProductEntityComValoresNulosNoConstitutor() {
            // When
            ProductEntity productEntity = new ProductEntity(null, null, null);

            // Then
            assertNotNull(productEntity);
            assertNull(productEntity.getSku());
            assertNull(productEntity.getNome());
            assertNull(productEntity.getAtivo());
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPattern {

        @Test
        @DisplayName("Deve criar ProductEntity com builder completo")
        void deveCriarProductEntityComBuilderCompleto() {
            // Given
            String sku = "SKU002";
            String nome = "Produto Builder";
            Boolean ativo = false;

            // When
            ProductEntity productEntity = ProductEntity.builder()
                    .sku(sku)
                    .nome(nome)
                    .ativo(ativo)
                    .build();

            // Then
            assertNotNull(productEntity);
            assertEquals(sku, productEntity.getSku());
            assertEquals(nome, productEntity.getNome());
            assertEquals(ativo, productEntity.getAtivo());
        }

        @Test
        @DisplayName("Deve criar ProductEntity com builder vazio")
        void deveCriarProductEntityComBuilderVazio() {
            // When
            ProductEntity productEntity = ProductEntity.builder().build();

            // Then
            assertNotNull(productEntity);
            assertNull(productEntity.getSku());
            assertNull(productEntity.getNome());
            assertNull(productEntity.getAtivo());
        }

        @Test
        @DisplayName("Deve criar ProductEntity com builder parcial")
        void deveCriarProductEntityComBuilderParcial() {
            // Given
            String sku = "SKU003";

            // When
            ProductEntity productEntity = ProductEntity.builder()
                    .sku(sku)
                    .build();

            // Then
            assertNotNull(productEntity);
            assertEquals(sku, productEntity.getSku());
            assertNull(productEntity.getNome());
            assertNull(productEntity.getAtivo());
        }
    }

    @Nested
    @DisplayName("Getters e Setters")
    class GettersSetters {

        @Test
        @DisplayName("Deve definir e obter SKU corretamente")
        void deveDefinirEObterSkuCorretamente() {
            // Given
            ProductEntity productEntity = new ProductEntity();
            String sku = "SKU004";

            // When
            productEntity.setSku(sku);

            // Then
            assertEquals(sku, productEntity.getSku());
        }

        @Test
        @DisplayName("Deve definir e obter nome corretamente")
        void deveDefinirEObterNomeCorretamente() {
            // Given
            ProductEntity productEntity = new ProductEntity();
            String nome = "Produto Setter";

            // When
            productEntity.setNome(nome);

            // Then
            assertEquals(nome, productEntity.getNome());
        }

        @Test
        @DisplayName("Deve definir e obter ativo corretamente")
        void deveDefinirEObterAtivoCorretamente() {
            // Given
            ProductEntity productEntity = new ProductEntity();
            Boolean ativo = true;

            // When
            productEntity.setAtivo(ativo);

            // Then
            assertEquals(ativo, productEntity.getAtivo());
        }

        @Test
        @DisplayName("Deve aceitar valores nulos nos setters")
        void deveAceitarValoresNulosNosSetters() {
            // Given
            ProductEntity productEntity = new ProductEntity("SKU005", "Produto", true);

            // When
            productEntity.setSku(null);
            productEntity.setNome(null);
            productEntity.setAtivo(null);

            // Then
            assertNull(productEntity.getSku());
            assertNull(productEntity.getNome());
            assertNull(productEntity.getAtivo());
        }
    }
}