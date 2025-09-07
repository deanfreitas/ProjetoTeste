package br.com.inventoryservice.adapters.out.persistence.portimpl;

import br.com.inventoryservice.adapters.out.persistence.entity.ProcessedEventEntity;
import br.com.inventoryservice.adapters.out.persistence.repository.ProcessedEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProcessedEventAdapter Tests")
class ProcessedEventAdapterTest {

    @Mock
    private ProcessedEventRepository processedEventRepository;

    @InjectMocks
    private ProcessedEventAdapter processedEventAdapter;

    @Nested
    @DisplayName("Exists Method Tests")
    class ExistsTests {

        @Test
        @DisplayName("Should return true when processed event exists")
        void shouldReturnTrueWhenProcessedEventExists() {
            // Given
            String topic = "test-topic";
            Integer partition = 1;
            Long offset = 100L;
            ProcessedEventEntity entity = ProcessedEventEntity.builder()
                    .topico(topic)
                    .particao(partition)
                    .offset(offset)
                    .processadoEm(Instant.now())
                    .build();

            when(processedEventRepository.findByTopicoAndParticaoAndOffset(topic, partition, offset))
                    .thenReturn(Optional.of(entity));

            // When
            boolean result = processedEventAdapter.exists(topic, partition, offset);

            // Then
            assertTrue(result);
            verify(processedEventRepository).findByTopicoAndParticaoAndOffset(topic, partition, offset);
        }

        @Test
        @DisplayName("Should return false when processed event does not exist")
        void shouldReturnFalseWhenProcessedEventDoesNotExist() {
            // Given
            String topic = "test-topic";
            Integer partition = 1;
            Long offset = 100L;

            when(processedEventRepository.findByTopicoAndParticaoAndOffset(topic, partition, offset))
                    .thenReturn(Optional.empty());

            // When
            boolean result = processedEventAdapter.exists(topic, partition, offset);

            // Then
            assertFalse(result);
            verify(processedEventRepository).findByTopicoAndParticaoAndOffset(topic, partition, offset);
        }

        @Test
        @DisplayName("Should handle null topic parameter")
        void shouldHandleNullTopicParameter() {
            // Given
            String topic = null;
            Integer partition = 1;
            Long offset = 100L;

            when(processedEventRepository.findByTopicoAndParticaoAndOffset(topic, partition, offset))
                    .thenReturn(Optional.empty());

            // When
            boolean result = processedEventAdapter.exists(topic, partition, offset);

            // Then
            assertFalse(result);
            verify(processedEventRepository).findByTopicoAndParticaoAndOffset(topic, partition, offset);
        }

        @Test
        @DisplayName("Should handle null partition parameter")
        void shouldHandleNullPartitionParameter() {
            // Given
            String topic = "test-topic";
            Integer partition = null;
            Long offset = 100L;

            when(processedEventRepository.findByTopicoAndParticaoAndOffset(topic, partition, offset))
                    .thenReturn(Optional.empty());

            // When
            boolean result = processedEventAdapter.exists(topic, partition, offset);

            // Then
            assertFalse(result);
            verify(processedEventRepository).findByTopicoAndParticaoAndOffset(topic, partition, offset);
        }

        @Test
        @DisplayName("Should handle null offset parameter")
        void shouldHandleNullOffsetParameter() {
            // Given
            String topic = "test-topic";
            Integer partition = 1;
            Long offset = null;

            when(processedEventRepository.findByTopicoAndParticaoAndOffset(topic, partition, offset))
                    .thenReturn(Optional.empty());

            // When
            boolean result = processedEventAdapter.exists(topic, partition, offset);

            // Then
            assertFalse(result);
            verify(processedEventRepository).findByTopicoAndParticaoAndOffset(topic, partition, offset);
        }
    }

    @Nested
    @DisplayName("Save Method Tests")
    class SaveTests {

        @Test
        @DisplayName("Should save processed event successfully")
        void shouldSaveProcessedEventSuccessfully() {
            // Given
            String topic = "test-topic";
            Integer partition = 1;
            Long offset = 100L;
            Instant processadoEm = Instant.now();

            ProcessedEventEntity expectedEntity = ProcessedEventEntity.builder()
                    .topico(topic)
                    .particao(partition)
                    .offset(offset)
                    .processadoEm(processadoEm)
                    .build();

            when(processedEventRepository.save(any(ProcessedEventEntity.class))).thenReturn(expectedEntity);

            // When
            assertDoesNotThrow(() -> processedEventAdapter.save(topic, partition, offset, processadoEm));

            // Then
            verify(processedEventRepository).save(any(ProcessedEventEntity.class));
        }

        @Test
        @DisplayName("Should ignore DataIntegrityViolationException when saving duplicate")
        void shouldIgnoreDataIntegrityViolationExceptionWhenSavingDuplicate() {
            // Given
            String topic = "test-topic";
            Integer partition = 1;
            Long offset = 100L;
            Instant processadoEm = Instant.now();

            when(processedEventRepository.save(any(ProcessedEventEntity.class)))
                    .thenThrow(new DataIntegrityViolationException("Duplicate key"));

            // When & Then
            assertDoesNotThrow(() -> processedEventAdapter.save(topic, partition, offset, processadoEm));
            verify(processedEventRepository).save(any(ProcessedEventEntity.class));
        }

        @Test
        @DisplayName("Should handle null topic when saving")
        void shouldHandleNullTopicWhenSaving() {
            // Given
            String topic = null;
            Integer partition = 1;
            Long offset = 100L;
            Instant processadoEm = Instant.now();

            // When & Then
            assertDoesNotThrow(() -> processedEventAdapter.save(topic, partition, offset, processadoEm));
            verify(processedEventRepository).save(any(ProcessedEventEntity.class));
        }

        @Test
        @DisplayName("Should handle null partition when saving")
        void shouldHandleNullPartitionWhenSaving() {
            // Given
            String topic = "test-topic";
            Integer partition = null;
            Long offset = 100L;
            Instant processadoEm = Instant.now();

            // When & Then
            assertDoesNotThrow(() -> processedEventAdapter.save(topic, partition, offset, processadoEm));
            verify(processedEventRepository).save(any(ProcessedEventEntity.class));
        }

        @Test
        @DisplayName("Should handle null offset when saving")
        void shouldHandleNullOffsetWhenSaving() {
            // Given
            String topic = "test-topic";
            Integer partition = 1;
            Long offset = null;
            Instant processadoEm = Instant.now();

            // When & Then
            assertDoesNotThrow(() -> processedEventAdapter.save(topic, partition, offset, processadoEm));
            verify(processedEventRepository).save(any(ProcessedEventEntity.class));
        }

        @Test
        @DisplayName("Should handle null processadoEm when saving")
        void shouldHandleNullProcessadoEmWhenSaving() {
            // Given
            String topic = "test-topic";
            Integer partition = 1;
            Long offset = 100L;
            Instant processadoEm = null;

            // When & Then
            assertDoesNotThrow(() -> processedEventAdapter.save(topic, partition, offset, processadoEm));
            verify(processedEventRepository).save(any(ProcessedEventEntity.class));
        }
    }
}