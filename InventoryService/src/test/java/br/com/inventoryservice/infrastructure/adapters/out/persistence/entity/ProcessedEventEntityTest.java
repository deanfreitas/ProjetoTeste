package br.com.inventoryservice.infrastructure.adapters.out.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProcessedEventEntity - Testes Unitários")
class ProcessedEventEntityTest {

    @Nested
    @DisplayName("Construtores")
    class Construtores {

        @Test
        @DisplayName("Deve criar ProcessedEventEntity com construtor sem argumentos")
        void deveCriarProcessedEventEntityComConstrutorSemArgumentos() {
            // When
            ProcessedEventEntity entity = new ProcessedEventEntity();

            // Then
            assertNotNull(entity);
            assertNull(entity.getId());
            assertNull(entity.getTopico());
            assertNull(entity.getParticao());
            assertNull(entity.getOffset());
            assertNull(entity.getProcessadoEm());
        }

        @Test
        @DisplayName("Deve criar ProcessedEventEntity com construtor com todos os argumentos")
        void deveCriarProcessedEventEntityComConstrutorComTodosArgumentos() {
            // Given
            UUID id = UUID.randomUUID();
            String topico = "inventory-events";
            Integer particao = 0;
            Long offset = 12345L;
            Instant processadoEm = Instant.now();

            // When
            ProcessedEventEntity entity = new ProcessedEventEntity(id, topico, particao, offset, processadoEm);

            // Then
            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertEquals(topico, entity.getTopico());
            assertEquals(particao, entity.getParticao());
            assertEquals(offset, entity.getOffset());
            assertEquals(processadoEm, entity.getProcessadoEm());
        }

        @Test
        @DisplayName("Deve criar ProcessedEventEntity com valores nulos no construtor")
        void deveCriarProcessedEventEntityComValoresNulosNoConstitutor() {
            // When
            ProcessedEventEntity entity = new ProcessedEventEntity(null, null, null, null, null);

            // Then
            assertNotNull(entity);
            assertNull(entity.getId());
            assertNull(entity.getTopico());
            assertNull(entity.getParticao());
            assertNull(entity.getOffset());
            assertNull(entity.getProcessadoEm());
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPattern {

        @Test
        @DisplayName("Deve criar ProcessedEventEntity com builder completo")
        void deveCriarProcessedEventEntityComBuilderCompleto() {
            // Given
            UUID id = UUID.randomUUID();
            String topico = "product-events";
            Integer particao = 1;
            Long offset = 67890L;
            Instant processadoEm = Instant.now();

            // When
            ProcessedEventEntity entity = ProcessedEventEntity.builder()
                    .id(id)
                    .topico(topico)
                    .particao(particao)
                    .offset(offset)
                    .processadoEm(processadoEm)
                    .build();

            // Then
            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertEquals(topico, entity.getTopico());
            assertEquals(particao, entity.getParticao());
            assertEquals(offset, entity.getOffset());
            assertEquals(processadoEm, entity.getProcessadoEm());
        }

        @Test
        @DisplayName("Deve criar ProcessedEventEntity com builder vazio")
        void deveCriarProcessedEventEntityComBuilderVazio() {
            // When
            ProcessedEventEntity entity = ProcessedEventEntity.builder().build();

            // Then
            assertNotNull(entity);
            assertNull(entity.getId());
            assertNull(entity.getTopico());
            assertNull(entity.getParticao());
            assertNull(entity.getOffset());
            assertNull(entity.getProcessadoEm());
        }

        @Test
        @DisplayName("Deve criar ProcessedEventEntity com builder parcial")
        void deveCriarProcessedEventEntityComBuilderParcial() {
            // Given
            String topico = "sales-events";
            Integer particao = 2;

            // When
            ProcessedEventEntity entity = ProcessedEventEntity.builder()
                    .topico(topico)
                    .particao(particao)
                    .build();

            // Then
            assertNotNull(entity);
            assertNull(entity.getId());
            assertEquals(topico, entity.getTopico());
            assertEquals(particao, entity.getParticao());
            assertNull(entity.getOffset());
            assertNull(entity.getProcessadoEm());
        }
    }

    @Nested
    @DisplayName("Getters e Setters")
    class GettersSetters {

        @Test
        @DisplayName("Deve definir e obter ID corretamente")
        void deveDefinirEObterIdCorretamente() {
            // Given
            ProcessedEventEntity entity = new ProcessedEventEntity();
            UUID id = UUID.randomUUID();

            // When
            entity.setId(id);

            // Then
            assertEquals(id, entity.getId());
        }

        @Test
        @DisplayName("Deve definir e obter tópico corretamente")
        void deveDefinirEObterTopicoCorretamente() {
            // Given
            ProcessedEventEntity entity = new ProcessedEventEntity();
            String topico = "stock-events";

            // When
            entity.setTopico(topico);

            // Then
            assertEquals(topico, entity.getTopico());
        }

        @Test
        @DisplayName("Deve definir e obter partição corretamente")
        void deveDefinirEObterParticaoCorretamente() {
            // Given
            ProcessedEventEntity entity = new ProcessedEventEntity();
            Integer particao = 3;

            // When
            entity.setParticao(particao);

            // Then
            assertEquals(particao, entity.getParticao());
        }

        @Test
        @DisplayName("Deve definir e obter offset corretamente")
        void deveDefinirEObterOffsetCorretamente() {
            // Given
            ProcessedEventEntity entity = new ProcessedEventEntity();
            Long offset = 999999L;

            // When
            entity.setOffset(offset);

            // Then
            assertEquals(offset, entity.getOffset());
        }

        @Test
        @DisplayName("Deve definir e obter processado em corretamente")
        void deveDefinirEObterProcessadoEmCorretamente() {
            // Given
            ProcessedEventEntity entity = new ProcessedEventEntity();
            Instant processadoEm = Instant.now();

            // When
            entity.setProcessadoEm(processadoEm);

            // Then
            assertEquals(processadoEm, entity.getProcessadoEm());
        }

        @Test
        @DisplayName("Deve aceitar valores nulos nos setters")
        void deveAceitarValoresNulosNosSetters() {
            // Given
            ProcessedEventEntity entity = ProcessedEventEntity.builder()
                    .id(UUID.randomUUID())
                    .topico("test-topic")
                    .particao(0)
                    .offset(123L)
                    .processadoEm(Instant.now())
                    .build();

            // When
            entity.setId(null);
            entity.setTopico(null);
            entity.setParticao(null);
            entity.setOffset(null);
            entity.setProcessadoEm(null);

            // Then
            assertNull(entity.getId());
            assertNull(entity.getTopico());
            assertNull(entity.getParticao());
            assertNull(entity.getOffset());
            assertNull(entity.getProcessadoEm());
        }
    }

    @Nested
    @DisplayName("Validações de Negócio")
    class ValidacoesNegocio {

        @Test
        @DisplayName("Deve aceitar tópicos Kafka válidos")
        void deveAceitarTopicosKafkaValidos() {
            // Given
            String[] topicosValidos = {
                "inventory-events", "product-events", "sales-events", "stock-adjustment-events",
                "store-events", "user-events", "order-events", "payment-events"
            };

            for (String topico : topicosValidos) {
                // When
                ProcessedEventEntity entity = ProcessedEventEntity.builder()
                        .topico(topico)
                        .particao(0)
                        .offset(1L)
                        .processadoEm(Instant.now())
                        .build();

                // Then
                assertNotNull(entity);
                assertEquals(topico, entity.getTopico());
            }
        }

        @Test
        @DisplayName("Deve aceitar partições válidas")
        void deveAceitarParticoesValidas() {
            // Given
            Integer[] particoesValidas = {0, 1, 2, 3, 5, 10, 15, 31};

            for (Integer particao : particoesValidas) {
                // When
                ProcessedEventEntity entity = ProcessedEventEntity.builder()
                        .topico("test-topic")
                        .particao(particao)
                        .offset(1L)
                        .processadoEm(Instant.now())
                        .build();

                // Then
                assertNotNull(entity);
                assertEquals(particao, entity.getParticao());
                assertTrue(entity.getParticao() >= 0);
            }
        }

        @Test
        @DisplayName("Deve aceitar offsets válidos")
        void deveAceitarOffsetsValidos() {
            // Given
            Long[] offsetsValidos = {0L, 1L, 100L, 1000L, 10000L, 999999L, Long.MAX_VALUE};

            for (Long offset : offsetsValidos) {
                // When
                ProcessedEventEntity entity = ProcessedEventEntity.builder()
                        .topico("test-topic")
                        .particao(0)
                        .offset(offset)
                        .processadoEm(Instant.now())
                        .build();

                // Then
                assertNotNull(entity);
                assertEquals(offset, entity.getOffset());
                assertTrue(entity.getOffset() >= 0);
            }
        }

        @Test
        @DisplayName("Deve registrar timestamp de processamento")
        void deveRegistrarTimestampDeProcessamento() {
            // Given
            Instant agora = Instant.now();
            
            // When
            ProcessedEventEntity entity = ProcessedEventEntity.builder()
                    .topico("timestamp-topic")
                    .particao(0)
                    .offset(1L)
                    .processadoEm(agora)
                    .build();

            // Then
            assertNotNull(entity.getProcessadoEm());
            assertEquals(agora, entity.getProcessadoEm());
        }
    }

    @Nested
    @DisplayName("Idempotência e Controle de Duplicação")
    class IdempotenciaEControleDeDuplicacao {

        @Test
        @DisplayName("Deve garantir unicidade por combinação tópico-partição-offset")
        void deveGarantirUnicidadePorCombinacaoTopicoParticaoOffset() {
            // Given
            String topico = "unique-topic";
            Integer particao = 0;
            Long offset = 12345L;

            ProcessedEventEntity entity1 = ProcessedEventEntity.builder()
                    .topico(topico)
                    .particao(particao)
                    .offset(offset)
                    .processadoEm(Instant.now())
                    .build();

            ProcessedEventEntity entity2 = ProcessedEventEntity.builder()
                    .topico(topico)
                    .particao(particao)
                    .offset(offset)
                    .processadoEm(Instant.now().plusSeconds(10))
                    .build();

            // Then - Mesma combinação tópico-partição-offset
            assertEquals(entity1.getTopico(), entity2.getTopico());
            assertEquals(entity1.getParticao(), entity2.getParticao());
            assertEquals(entity1.getOffset(), entity2.getOffset());
        }

        @Test
        @DisplayName("Deve distinguir eventos com diferentes tópicos")
        void deveDistinguirEventosComDiferentesTopicos() {
            // Given
            ProcessedEventEntity entity1 = ProcessedEventEntity.builder()
                    .topico("topic-a")
                    .particao(0)
                    .offset(100L)
                    .build();

            ProcessedEventEntity entity2 = ProcessedEventEntity.builder()
                    .topico("topic-b")
                    .particao(0)
                    .offset(100L)
                    .build();

            // Then
            assertNotEquals(entity1.getTopico(), entity2.getTopico());
        }

        @Test
        @DisplayName("Deve distinguir eventos com diferentes partições")
        void deveDistinguirEventosComDiferentesParticoes() {
            // Given
            ProcessedEventEntity entity1 = ProcessedEventEntity.builder()
                    .topico("same-topic")
                    .particao(0)
                    .offset(200L)
                    .build();

            ProcessedEventEntity entity2 = ProcessedEventEntity.builder()
                    .topico("same-topic")
                    .particao(1)
                    .offset(200L)
                    .build();

            // Then
            assertEquals(entity1.getTopico(), entity2.getTopico());
            assertNotEquals(entity1.getParticao(), entity2.getParticao());
        }

        @Test
        @DisplayName("Deve distinguir eventos com diferentes offsets")
        void deveDistinguirEventosComDiferentesOffsets() {
            // Given
            ProcessedEventEntity entity1 = ProcessedEventEntity.builder()
                    .topico("same-topic")
                    .particao(0)
                    .offset(300L)
                    .build();

            ProcessedEventEntity entity2 = ProcessedEventEntity.builder()
                    .topico("same-topic")
                    .particao(0)
                    .offset(301L)
                    .build();

            // Then
            assertEquals(entity1.getTopico(), entity2.getTopico());
            assertEquals(entity1.getParticao(), entity2.getParticao());
            assertNotEquals(entity1.getOffset(), entity2.getOffset());
        }

        @Test
        @DisplayName("Deve permitir múltiplos eventos do mesmo tópico em partições diferentes")
        void devePermitirMultiplosEventosDoMesmoTopicoEmParticoesDiferentes() {
            // Given
            String topico = "multi-partition-topic";

            ProcessedEventEntity evento0 = ProcessedEventEntity.builder()
                    .topico(topico)
                    .particao(0)
                    .offset(1L)
                    .build();

            ProcessedEventEntity evento1 = ProcessedEventEntity.builder()
                    .topico(topico)
                    .particao(1)
                    .offset(1L)
                    .build();

            ProcessedEventEntity evento2 = ProcessedEventEntity.builder()
                    .topico(topico)
                    .particao(2)
                    .offset(1L)
                    .build();

            // Then
            assertEquals(evento0.getTopico(), evento1.getTopico());
            assertEquals(evento1.getTopico(), evento2.getTopico());
            assertNotEquals(evento0.getParticao(), evento1.getParticao());
            assertNotEquals(evento1.getParticao(), evento2.getParticao());
        }
    }
}