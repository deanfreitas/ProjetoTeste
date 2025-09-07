package br.com.inventoryservice.infrastructure.adapters.out.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StoreEntity - Testes Unitários")
class StoreEntityTest {

    @Nested
    @DisplayName("Construtores")
    class Construtores {

        @Test
        @DisplayName("Deve criar StoreEntity com construtor sem argumentos")
        void deveCriarStoreEntityComConstrutorSemArgumentos() {
            // When
            StoreEntity storeEntity = new StoreEntity();

            // Then
            assertNotNull(storeEntity);
            assertNull(storeEntity.getCodigo());
            assertNull(storeEntity.getNome());
        }

        @Test
        @DisplayName("Deve criar StoreEntity com construtor com todos os argumentos")
        void deveCriarStoreEntityComConstrutorComTodosArgumentos() {
            // Given
            String codigo = "LOJA001";
            String nome = "Loja Centro";

            // When
            StoreEntity storeEntity = new StoreEntity(codigo, nome);

            // Then
            assertNotNull(storeEntity);
            assertEquals(codigo, storeEntity.getCodigo());
            assertEquals(nome, storeEntity.getNome());
        }

        @Test
        @DisplayName("Deve criar StoreEntity com valores nulos no construtor")
        void deveCriarStoreEntityComValoresNulosNoConstitutor() {
            // When
            StoreEntity storeEntity = new StoreEntity(null, null);

            // Then
            assertNotNull(storeEntity);
            assertNull(storeEntity.getCodigo());
            assertNull(storeEntity.getNome());
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPattern {

        @Test
        @DisplayName("Deve criar StoreEntity com builder completo")
        void deveCriarStoreEntityComBuilderCompleto() {
            // Given
            String codigo = "LOJA002";
            String nome = "Loja Shopping";

            // When
            StoreEntity storeEntity = StoreEntity.builder()
                    .codigo(codigo)
                    .nome(nome)
                    .build();

            // Then
            assertNotNull(storeEntity);
            assertEquals(codigo, storeEntity.getCodigo());
            assertEquals(nome, storeEntity.getNome());
        }

        @Test
        @DisplayName("Deve criar StoreEntity com builder vazio")
        void deveCriarStoreEntityComBuilderVazio() {
            // When
            StoreEntity storeEntity = StoreEntity.builder().build();

            // Then
            assertNotNull(storeEntity);
            assertNull(storeEntity.getCodigo());
            assertNull(storeEntity.getNome());
        }

        @Test
        @DisplayName("Deve criar StoreEntity com builder parcial")
        void deveCriarStoreEntityComBuilderParcial() {
            // Given
            String codigo = "LOJA003";

            // When
            StoreEntity storeEntity = StoreEntity.builder()
                    .codigo(codigo)
                    .build();

            // Then
            assertNotNull(storeEntity);
            assertEquals(codigo, storeEntity.getCodigo());
            assertNull(storeEntity.getNome());
        }
    }

    @Nested
    @DisplayName("Getters e Setters")
    class GettersSetters {

        @Test
        @DisplayName("Deve definir e obter código corretamente")
        void deveDefinirEObterCodigoCorretamente() {
            // Given
            StoreEntity storeEntity = new StoreEntity();
            String codigo = "LOJA004";

            // When
            storeEntity.setCodigo(codigo);

            // Then
            assertEquals(codigo, storeEntity.getCodigo());
        }

        @Test
        @DisplayName("Deve definir e obter nome corretamente")
        void deveDefinirEObterNomeCorretamente() {
            // Given
            StoreEntity storeEntity = new StoreEntity();
            String nome = "Loja Matriz";

            // When
            storeEntity.setNome(nome);

            // Then
            assertEquals(nome, storeEntity.getNome());
        }

        @Test
        @DisplayName("Deve aceitar valores nulos nos setters")
        void deveAceitarValoresNulosNosSetters() {
            // Given
            StoreEntity storeEntity = new StoreEntity("LOJA005", "Loja Filial");

            // When
            storeEntity.setCodigo(null);
            storeEntity.setNome(null);

            // Then
            assertNull(storeEntity.getCodigo());
            assertNull(storeEntity.getNome());
        }
    }

    @Nested
    @DisplayName("Validações de Negócio")
    class ValidacoesNegocio {

        @Test
        @DisplayName("Deve aceitar códigos de loja válidos")
        void deveAceitarCodigosDeLojaValidos() {
            // Given
            String[] codigosValidos = {
                "LOJA001", "L001", "MATRIZ", "FILIAL-001", "CENTRO_SP", 
                "SHOPPING_ABC", "OUTLET", "MEGA_STORE", "BR001", "FRANQUIA_01"
            };

            for (String codigo : codigosValidos) {
                // When
                StoreEntity storeEntity = StoreEntity.builder()
                        .codigo(codigo)
                        .nome("Loja Teste")
                        .build();

                // Then
                assertNotNull(storeEntity);
                assertEquals(codigo, storeEntity.getCodigo());
            }
        }

        @Test
        @DisplayName("Deve aceitar nomes de loja válidos")
        void deveAceitarNomesDeLojaValidos() {
            // Given
            String[] nomesValidos = {
                "Loja Centro", "Shopping ABC", "Matriz São Paulo", "Filial Rio de Janeiro",
                "Outlet Premium", "Mega Store", "Loja Express", "Centro de Distribuição",
                "Franquia Norte", "Ponto de Venda Sul"
            };

            for (String nome : nomesValidos) {
                // When
                StoreEntity storeEntity = StoreEntity.builder()
                        .codigo("TESTE")
                        .nome(nome)
                        .build();

                // Then
                assertNotNull(storeEntity);
                assertEquals(nome, storeEntity.getNome());
            }
        }

        @Test
        @DisplayName("Deve permitir alteração de dados da loja")
        void devePermitirAlteracaoDeDadosDaLoja() {
            // Given
            StoreEntity storeEntity = StoreEntity.builder()
                    .codigo("LOJA_ORIGINAL")
                    .nome("Nome Original")
                    .build();

            // When
            storeEntity.setCodigo("LOJA_ALTERADA");
            storeEntity.setNome("Nome Alterado");

            // Then
            assertEquals("LOJA_ALTERADA", storeEntity.getCodigo());
            assertEquals("Nome Alterado", storeEntity.getNome());
        }

        @Test
        @DisplayName("Deve aceitar apenas código sem nome")
        void deveAceitarApenasCodigoSemNome() {
            // When
            StoreEntity storeEntity = StoreEntity.builder()
                    .codigo("LOJA_SEM_NOME")
                    .build();

            // Then
            assertNotNull(storeEntity);
            assertEquals("LOJA_SEM_NOME", storeEntity.getCodigo());
            assertNull(storeEntity.getNome());
        }

        @Test
        @DisplayName("Deve aceitar apenas nome sem código")
        void deveAceitarApenasNomeSemCodigo() {
            // When
            StoreEntity storeEntity = StoreEntity.builder()
                    .nome("Loja Sem Código")
                    .build();

            // Then
            assertNotNull(storeEntity);
            assertNull(storeEntity.getCodigo());
            assertEquals("Loja Sem Código", storeEntity.getNome());
        }
    }

    @Nested
    @DisplayName("Cenários de Uso")
    class CenariosDeUso {

        @Test
        @DisplayName("Deve representar loja matriz corretamente")
        void deveRepresentarLojaMatrizCorretamente() {
            // When
            StoreEntity matriz = StoreEntity.builder()
                    .codigo("MATRIZ")
                    .nome("Loja Matriz - São Paulo")
                    .build();

            // Then
            assertNotNull(matriz);
            assertEquals("MATRIZ", matriz.getCodigo());
            assertEquals("Loja Matriz - São Paulo", matriz.getNome());
        }

        @Test
        @DisplayName("Deve representar filial corretamente")
        void deveRepresentarFilialCorretamente() {
            // When
            StoreEntity filial = StoreEntity.builder()
                    .codigo("FILIAL_RJ")
                    .nome("Filial Rio de Janeiro")
                    .build();

            // Then
            assertNotNull(filial);
            assertEquals("FILIAL_RJ", filial.getCodigo());
            assertEquals("Filial Rio de Janeiro", filial.getNome());
        }

        @Test
        @DisplayName("Deve representar loja de shopping corretamente")
        void deveRepresentarLojaDeSoppingCorretamente() {
            // When
            StoreEntity lojaShopping = StoreEntity.builder()
                    .codigo("SHOPPING_001")
                    .nome("Loja Shopping Iguatemi")
                    .build();

            // Then
            assertNotNull(lojaShopping);
            assertEquals("SHOPPING_001", lojaShopping.getCodigo());
            assertEquals("Loja Shopping Iguatemi", lojaShopping.getNome());
        }

        @Test
        @DisplayName("Deve representar outlet corretamente")
        void deveRepresentarOutletCorretamente() {
            // When
            StoreEntity outlet = StoreEntity.builder()
                    .codigo("OUTLET_SP")
                    .nome("Outlet São Paulo Premium")
                    .build();

            // Then
            assertNotNull(outlet);
            assertEquals("OUTLET_SP", outlet.getCodigo());
            assertEquals("Outlet São Paulo Premium", outlet.getNome());
        }

        @Test
        @DisplayName("Deve representar franquia corretamente")
        void deveRepresentarFranquiaCorretamente() {
            // When
            StoreEntity franquia = StoreEntity.builder()
                    .codigo("FRANQ_001")
                    .nome("Franquia Zona Norte")
                    .build();

            // Then
            assertNotNull(franquia);
            assertEquals("FRANQ_001", franquia.getCodigo());
            assertEquals("Franquia Zona Norte", franquia.getNome());
        }

        @Test
        @DisplayName("Deve representar centro de distribuição corretamente")
        void deveRepresentarCentroDeDistribuicaoCorretamente() {
            // When
            StoreEntity cd = StoreEntity.builder()
                    .codigo("CD_SP")
                    .nome("Centro de Distribuição São Paulo")
                    .build();

            // Then
            assertNotNull(cd);
            assertEquals("CD_SP", cd.getCodigo());
            assertEquals("Centro de Distribuição São Paulo", cd.getNome());
        }
    }

    @Nested
    @DisplayName("Integração com Outros Componentes")
    class IntegracaoComOutrosComponentes {

        @Test
        @DisplayName("Deve funcionar como chave primária em relacionamentos")
        void deveFuncionarComoChavePrimariaEmRelacionamentos() {
            // Given
            StoreEntity loja1 = StoreEntity.builder()
                    .codigo("LOJA_PK_1")
                    .nome("Loja Primary Key 1")
                    .build();

            StoreEntity loja2 = StoreEntity.builder()
                    .codigo("LOJA_PK_2")
                    .nome("Loja Primary Key 2")
                    .build();

            // Then
            assertNotNull(loja1.getCodigo());
            assertNotNull(loja2.getCodigo());
            assertNotEquals(loja1.getCodigo(), loja2.getCodigo());
        }

        @Test
        @DisplayName("Deve ser compatível com StockId para relacionamento de estoque")
        void deveSerCompativelComStockIdParaRelacionamentoDeEstoque() {
            // Given
            StoreEntity loja = StoreEntity.builder()
                    .codigo("LOJA_ESTOQUE")
                    .nome("Loja com Estoque")
                    .build();

            StockId stockId = StockId.builder()
                    .lojaCodigo(loja.getCodigo())
                    .produtoSku("SKU_TESTE")
                    .build();

            // Then
            assertEquals(loja.getCodigo(), stockId.getLojaCodigo());
            assertNotNull(stockId.getProdutoSku());
        }

        @Test
        @DisplayName("Deve manter integridade referencial com ajustes de estoque")
        void deveManterIntegridadeReferencialComAjustesDeEstoque() {
            // Given
            StoreEntity loja = StoreEntity.builder()
                    .codigo("LOJA_AJUSTE")
                    .nome("Loja para Ajustes")
                    .build();

            // Simulando o que seria usado em StockAdjustmentEntity
            String lojaCodigoParaAjuste = loja.getCodigo();

            // Then
            assertEquals("LOJA_AJUSTE", lojaCodigoParaAjuste);
            assertNotNull(loja.getNome());
        }
    }
}