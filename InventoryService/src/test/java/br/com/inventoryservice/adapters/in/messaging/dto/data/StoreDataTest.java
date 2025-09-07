package br.com.inventoryservice.adapters.in.messaging.dto.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StoreData - Testes Unitários")
class StoreDataTest {

    @Nested
    @DisplayName("Construtores")
    class Construtores {

        @Test
        @DisplayName("Deve criar StoreData com construtor sem argumentos")
        void deveCriarStoreDataComConstrutorSemArgumentos() {
            // When
            StoreData storeData = new StoreData();

            // Then
            assertNotNull(storeData);
            assertNull(storeData.getCodigo());
            assertNull(storeData.getNome());
        }

        @Test
        @DisplayName("Deve criar StoreData com construtor com todos os argumentos")
        void deveCriarStoreDataComConstrutorComTodosArgumentos() {
            // Given
            String codigo = "STORE001";
            String nome = "Loja Teste";

            // When
            StoreData storeData = new StoreData(codigo, nome);

            // Then
            assertNotNull(storeData);
            assertEquals(codigo, storeData.getCodigo());
            assertEquals(nome, storeData.getNome());
        }

        @Test
        @DisplayName("Deve criar StoreData com valores nulos no construtor")
        void deveCriarStoreDataComValoresNulosNoConstitutor() {
            // When
            StoreData storeData = new StoreData(null, null);

            // Then
            assertNotNull(storeData);
            assertNull(storeData.getCodigo());
            assertNull(storeData.getNome());
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPattern {

        @Test
        @DisplayName("Deve criar StoreData com builder completo")
        void deveCriarStoreDataComBuilderCompleto() {
            // Given
            String codigo = "STORE002";
            String nome = "Loja Builder";

            // When
            StoreData storeData = StoreData.builder()
                    .codigo(codigo)
                    .nome(nome)
                    .build();

            // Then
            assertNotNull(storeData);
            assertEquals(codigo, storeData.getCodigo());
            assertEquals(nome, storeData.getNome());
        }

        @Test
        @DisplayName("Deve criar StoreData com builder vazio")
        void deveCriarStoreDataComBuilderVazio() {
            // When
            StoreData storeData = StoreData.builder().build();

            // Then
            assertNotNull(storeData);
            assertNull(storeData.getCodigo());
            assertNull(storeData.getNome());
        }

        @Test
        @DisplayName("Deve criar StoreData com builder parcial")
        void deveCriarStoreDataComBuilderParcial() {
            // Given
            String codigo = "STORE003";

            // When
            StoreData storeData = StoreData.builder()
                    .codigo(codigo)
                    .build();

            // Then
            assertNotNull(storeData);
            assertEquals(codigo, storeData.getCodigo());
            assertNull(storeData.getNome());
        }

        @Test
        @DisplayName("Deve testar toString do builder")
        void deveTestarToStringDoBuilder() {
            // Given & When
            String builderToString = StoreData.builder()
                    .codigo("STORE001")
                    .nome("Loja Teste")
                    .toString();

            // Then
            assertNotNull(builderToString);
            assertTrue(builderToString.contains("StoreData.StoreDataBuilder"));
        }
    }

    @Nested
    @DisplayName("Getters e Setters")
    class GettersSetters {

        @Test
        @DisplayName("Deve definir e obter código")
        void deveDefinirEObterCodigo() {
            // Given
            StoreData storeData = new StoreData();
            String codigo = "STORE004";

            // When
            storeData.setCodigo(codigo);

            // Then
            assertEquals(codigo, storeData.getCodigo());
        }

        @Test
        @DisplayName("Deve definir e obter nome")
        void deveDefinirEObterNome() {
            // Given
            StoreData storeData = new StoreData();
            String nome = "Loja Getter Setter";

            // When
            storeData.setNome(nome);

            // Then
            assertEquals(nome, storeData.getNome());
        }

        @Test
        @DisplayName("Deve permitir valores nulos nos setters")
        void devePermitirValoresNulosNosSetters() {
            // Given
            StoreData storeData = new StoreData("STORE005", "Loja");

            // When
            storeData.setCodigo(null);
            storeData.setNome(null);

            // Then
            assertNull(storeData.getCodigo());
            assertNull(storeData.getNome());
        }
    }

    @Nested
    @DisplayName("Equals e HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("Deve verificar igualdade entre objetos com mesmos valores")
        void deveVerificarIgualdadeEntreObjetosComMesmosValores() {
            // Given
            StoreData storeData1 = new StoreData("STORE006", "Loja");
            StoreData storeData2 = new StoreData("STORE006", "Loja");

            // Then
            assertEquals(storeData1, storeData2);
            assertEquals(storeData1.hashCode(), storeData2.hashCode());
        }

        @Test
        @DisplayName("Deve verificar desigualdade entre objetos com valores diferentes")
        void deveVerificarDesigualdadeEntreObjetosComValoresDiferentes() {
            // Given
            StoreData storeData1 = new StoreData("STORE007", "Loja 1");
            StoreData storeData2 = new StoreData("STORE008", "Loja 2");

            // Then
            assertNotEquals(storeData1, storeData2);
            assertNotEquals(storeData1.hashCode(), storeData2.hashCode());
        }

        @Test
        @DisplayName("Deve verificar igualdade com valores nulos")
        void deveVerificarIgualdadeComValoresNulos() {
            // Given
            StoreData storeData1 = new StoreData(null, null);
            StoreData storeData2 = new StoreData(null, null);

            // Then
            assertEquals(storeData1, storeData2);
            assertEquals(storeData1.hashCode(), storeData2.hashCode());
        }

        @Test
        @DisplayName("Deve verificar desigualdade com null")
        void deveVerificarDesigualdadeComNull() {
            // Given
            StoreData storeData = new StoreData("STORE009", "Loja");

            // Then
            assertNotEquals(storeData, null);
        }

        @Test
        @DisplayName("Deve verificar igualdade consigo mesmo")
        void deveVerificarIgualdadeConsigoMesmo() {
            // Given
            StoreData storeData = new StoreData("STORE010", "Loja");

            // Then
            assertEquals(storeData, storeData);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToString {

        @Test
        @DisplayName("Deve gerar toString com todos os valores preenchidos")
        void deveGerarToStringComTodosValoresPreenchidos() {
            // Given
            StoreData storeData = new StoreData("STORE011", "Loja ToString");

            // When
            String toString = storeData.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("STORE011"));
            assertTrue(toString.contains("Loja ToString"));
        }

        @Test
        @DisplayName("Deve gerar toString com valores nulos")
        void deveGerarToStringComValoresNulos() {
            // Given
            StoreData storeData = new StoreData(null, null);

            // When
            String toString = storeData.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("null"));
        }

        @Test
        @DisplayName("Deve gerar toString com valores parciais")
        void deveGerarToStringComValoresParciais() {
            // Given
            StoreData storeData = new StoreData("STORE012", null);

            // When
            String toString = storeData.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("STORE012"));
            assertTrue(toString.contains("null"));
        }
    }
}