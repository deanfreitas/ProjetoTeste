package br.com.inventoryservice.application.usecase.helper;

import br.com.inventoryservice.application.port.out.ProcessedEventPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventProcessor Tests")
class EventProcessorTest {

    @Mock
    private ProcessedEventPort processedEventPort;

    @InjectMocks
    private EventProcessor eventProcessor;

    private static final String TOPIC = "test-topic";
    private static final Integer PARTITION = 0;
    private static final Long OFFSET = 100L;

    @Nested
    @DisplayName("Mark Processed Tests with Event ID")
    class MarkProcessedWithEventIdTests {

        @Test
        @DisplayName("Should mark event as processed when event ID is provided and not exists")
        void shouldMarkEventAsProcessedWhenEventIdIsProvidedAndNotExists() {
            // Given
            String eventId = "event-123";
            when(processedEventPort.exists(TOPIC, 0, (long) eventId.hashCode())).thenReturn(false);
            doNothing().when(processedEventPort).save(eq(TOPIC), eq(0), eq((long) eventId.hashCode()), any(Instant.class));

            // When
            boolean result = eventProcessor.markProcessed(eventId, TOPIC, PARTITION, OFFSET);

            // Then
            assertTrue(result);
            verify(processedEventPort).exists(TOPIC, 0, (long) eventId.hashCode());
            verify(processedEventPort).save(eq(TOPIC), eq(0), eq((long) eventId.hashCode()), any(Instant.class));
        }

        @Test
        @DisplayName("Should return false when event ID already exists")
        void shouldReturnFalseWhenEventIdAlreadyExists() {
            // Given
            String eventId = "event-123";
            when(processedEventPort.exists(TOPIC, 0, (long) eventId.hashCode())).thenReturn(true);

            // When
            boolean result = eventProcessor.markProcessed(eventId, TOPIC, PARTITION, OFFSET);

            // Then
            assertFalse(result);
            verify(processedEventPort).exists(TOPIC, 0, (long) eventId.hashCode());
            verify(processedEventPort, never()).save(anyString(), anyInt(), anyLong(), any(Instant.class));
        }

        @Test
        @DisplayName("Should handle null topic with event ID")
        void shouldHandleNullTopicWithEventId() {
            // Given
            String eventId = "event-456";
            when(processedEventPort.exists("unknown", 0, (long) eventId.hashCode())).thenReturn(false);
            doNothing().when(processedEventPort).save(eq("unknown"), eq(0), eq((long) eventId.hashCode()), any(Instant.class));

            // When
            boolean result = eventProcessor.markProcessed(eventId, null, PARTITION, OFFSET);

            // Then
            assertTrue(result);
            verify(processedEventPort).exists("unknown", 0, (long) eventId.hashCode());
            verify(processedEventPort).save(eq("unknown"), eq(0), eq((long) eventId.hashCode()), any(Instant.class));
        }

        @Test
        @DisplayName("Should handle blank event ID")
        void shouldHandleBlankEventId() {
            // Given
            String eventId = "   ";
            when(processedEventPort.exists(TOPIC, PARTITION, OFFSET)).thenReturn(false);
            doNothing().when(processedEventPort).save(eq(TOPIC), eq(PARTITION), eq(OFFSET), any(Instant.class));

            // When
            boolean result = eventProcessor.markProcessed(eventId, TOPIC, PARTITION, OFFSET);

            // Then
            assertTrue(result);
            verify(processedEventPort).exists(TOPIC, PARTITION, OFFSET);
            verify(processedEventPort).save(eq(TOPIC), eq(PARTITION), eq(OFFSET), any(Instant.class));
        }

        @Test
        @DisplayName("Should handle empty event ID")
        void shouldHandleEmptyEventId() {
            // Given
            String eventId = "";
            when(processedEventPort.exists(TOPIC, PARTITION, OFFSET)).thenReturn(false);
            doNothing().when(processedEventPort).save(eq(TOPIC), eq(PARTITION), eq(OFFSET), any(Instant.class));

            // When
            boolean result = eventProcessor.markProcessed(eventId, TOPIC, PARTITION, OFFSET);

            // Then
            assertTrue(result);
            verify(processedEventPort).exists(TOPIC, PARTITION, OFFSET);
            verify(processedEventPort).save(eq(TOPIC), eq(PARTITION), eq(OFFSET), any(Instant.class));
        }

        @Test
        @DisplayName("Should use event ID hash code as offset")
        void shouldUseEventIdHashCodeAsOffset() {
            // Given
            String eventId = "unique-event-id";
            long expectedOffset = eventId.hashCode();
            when(processedEventPort.exists(TOPIC, 0, expectedOffset)).thenReturn(false);
            
            ArgumentCaptor<Long> offsetCaptor = ArgumentCaptor.forClass(Long.class);
            doNothing().when(processedEventPort).save(eq(TOPIC), eq(0), offsetCaptor.capture(), any(Instant.class));

            // When
            boolean result = eventProcessor.markProcessed(eventId, TOPIC, PARTITION, OFFSET);

            // Then
            assertTrue(result);
            verify(processedEventPort).exists(TOPIC, 0, expectedOffset);
            assertEquals(expectedOffset, offsetCaptor.getValue());
        }
    }

    @Nested
    @DisplayName("Mark Processed Tests with Topic/Partition/Offset")
    class MarkProcessedWithTopicPartitionOffsetTests {

        @Test
        @DisplayName("Should mark event as processed when topic/partition/offset provided and not exists")
        void shouldMarkEventAsProcessedWhenTopicPartitionOffsetProvidedAndNotExists() {
            // Given
            when(processedEventPort.exists(TOPIC, PARTITION, OFFSET)).thenReturn(false);
            doNothing().when(processedEventPort).save(eq(TOPIC), eq(PARTITION), eq(OFFSET), any(Instant.class));

            // When
            boolean result = eventProcessor.markProcessed(null, TOPIC, PARTITION, OFFSET);

            // Then
            assertTrue(result);
            verify(processedEventPort).exists(TOPIC, PARTITION, OFFSET);
            verify(processedEventPort).save(eq(TOPIC), eq(PARTITION), eq(OFFSET), any(Instant.class));
        }

        @Test
        @DisplayName("Should return false when topic/partition/offset already exists")
        void shouldReturnFalseWhenTopicPartitionOffsetAlreadyExists() {
            // Given
            when(processedEventPort.exists(TOPIC, PARTITION, OFFSET)).thenReturn(true);

            // When
            boolean result = eventProcessor.markProcessed(null, TOPIC, PARTITION, OFFSET);

            // Then
            assertFalse(result);
            verify(processedEventPort).exists(TOPIC, PARTITION, OFFSET);
            verify(processedEventPort, never()).save(anyString(), anyInt(), anyLong(), any(Instant.class));
        }

        @Test
        @DisplayName("Should return true when topic is null")
        void shouldReturnTrueWhenTopicIsNull() {
            // When
            boolean result = eventProcessor.markProcessed(null, null, PARTITION, OFFSET);

            // Then
            assertTrue(result);
            verify(processedEventPort, never()).exists(anyString(), anyInt(), anyLong());
            verify(processedEventPort, never()).save(anyString(), anyInt(), anyLong(), any(Instant.class));
        }

        @Test
        @DisplayName("Should return true when partition is null")
        void shouldReturnTrueWhenPartitionIsNull() {
            // When
            boolean result = eventProcessor.markProcessed(null, TOPIC, null, OFFSET);

            // Then
            assertTrue(result);
            verify(processedEventPort, never()).exists(anyString(), anyInt(), anyLong());
            verify(processedEventPort, never()).save(anyString(), anyInt(), anyLong(), any(Instant.class));
        }

        @Test
        @DisplayName("Should return true when offset is null")
        void shouldReturnTrueWhenOffsetIsNull() {
            // When
            boolean result = eventProcessor.markProcessed(null, TOPIC, PARTITION, null);

            // Then
            assertTrue(result);
            verify(processedEventPort, never()).exists(anyString(), anyInt(), anyLong());
            verify(processedEventPort, never()).save(anyString(), anyInt(), anyLong(), any(Instant.class));
        }

        @Test
        @DisplayName("Should return true when all parameters are null")
        void shouldReturnTrueWhenAllParametersAreNull() {
            // When
            boolean result = eventProcessor.markProcessed(null, null, null, null);

            // Then
            assertTrue(result);
            verify(processedEventPort, never()).exists(anyString(), anyInt(), anyLong());
            verify(processedEventPort, never()).save(anyString(), anyInt(), anyLong(), any(Instant.class));
        }

        @Test
        @DisplayName("Should save with current timestamp")
        void shouldSaveWithCurrentTimestamp() {
            // Given
            Instant beforeCall = Instant.now();
            when(processedEventPort.exists(TOPIC, PARTITION, OFFSET)).thenReturn(false);
            
            ArgumentCaptor<Instant> timestampCaptor = ArgumentCaptor.forClass(Instant.class);
            doNothing().when(processedEventPort).save(eq(TOPIC), eq(PARTITION), eq(OFFSET), timestampCaptor.capture());

            // When
            boolean result = eventProcessor.markProcessed(null, TOPIC, PARTITION, OFFSET);
            Instant afterCall = Instant.now();

            // Then
            assertTrue(result);
            Instant capturedTimestamp = timestampCaptor.getValue();
            assertTrue(capturedTimestamp.isAfter(beforeCall.minusSeconds(1)));
            assertTrue(capturedTimestamp.isBefore(afterCall.plusSeconds(1)));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long event ID")
        void shouldHandleVeryLongEventId() {
            // Given
            String longEventId = "a".repeat(1000);
            long expectedOffset = longEventId.hashCode();
            when(processedEventPort.exists(TOPIC, 0, expectedOffset)).thenReturn(false);
            doNothing().when(processedEventPort).save(eq(TOPIC), eq(0), eq(expectedOffset), any(Instant.class));

            // When
            boolean result = eventProcessor.markProcessed(longEventId, TOPIC, PARTITION, OFFSET);

            // Then
            assertTrue(result);
            verify(processedEventPort).exists(TOPIC, 0, expectedOffset);
            verify(processedEventPort).save(eq(TOPIC), eq(0), eq(expectedOffset), any(Instant.class));
        }

        @Test
        @DisplayName("Should handle special characters in event ID")
        void shouldHandleSpecialCharactersInEventId() {
            // Given
            String eventIdWithSpecialChars = "event-123!@#$%^&*()_+{}[]";
            long expectedOffset = eventIdWithSpecialChars.hashCode();
            when(processedEventPort.exists(TOPIC, 0, expectedOffset)).thenReturn(false);
            doNothing().when(processedEventPort).save(eq(TOPIC), eq(0), eq(expectedOffset), any(Instant.class));

            // When
            boolean result = eventProcessor.markProcessed(eventIdWithSpecialChars, TOPIC, PARTITION, OFFSET);

            // Then
            assertTrue(result);
            verify(processedEventPort).exists(TOPIC, 0, expectedOffset);
            verify(processedEventPort).save(eq(TOPIC), eq(0), eq(expectedOffset), any(Instant.class));
        }

        @Test
        @DisplayName("Should handle unicode characters in event ID")
        void shouldHandleUnicodeCharactersInEventId() {
            // Given
            String unicodeEventId = "event-测试-事件-123";
            long expectedOffset = unicodeEventId.hashCode();
            when(processedEventPort.exists(TOPIC, 0, expectedOffset)).thenReturn(false);
            doNothing().when(processedEventPort).save(eq(TOPIC), eq(0), eq(expectedOffset), any(Instant.class));

            // When
            boolean result = eventProcessor.markProcessed(unicodeEventId, TOPIC, PARTITION, OFFSET);

            // Then
            assertTrue(result);
            verify(processedEventPort).exists(TOPIC, 0, expectedOffset);
            verify(processedEventPort).save(eq(TOPIC), eq(0), eq(expectedOffset), any(Instant.class));
        }

        @Test
        @DisplayName("Should handle negative partition and offset values")
        void shouldHandleNegativePartitionAndOffsetValues() {
            // Given
            Integer negativePartition = -1;
            Long negativeOffset = -100L;
            when(processedEventPort.exists(TOPIC, negativePartition, negativeOffset)).thenReturn(false);
            doNothing().when(processedEventPort).save(eq(TOPIC), eq(negativePartition), eq(negativeOffset), any(Instant.class));

            // When
            boolean result = eventProcessor.markProcessed(null, TOPIC, negativePartition, negativeOffset);

            // Then
            assertTrue(result);
            verify(processedEventPort).exists(TOPIC, negativePartition, negativeOffset);
            verify(processedEventPort).save(eq(TOPIC), eq(negativePartition), eq(negativeOffset), any(Instant.class));
        }

        @Test
        @DisplayName("Should handle zero partition and offset values")
        void shouldHandleZeroPartitionAndOffsetValues() {
            // Given
            Integer zeroPartition = 0;
            Long zeroOffset = 0L;
            when(processedEventPort.exists(TOPIC, zeroPartition, zeroOffset)).thenReturn(false);
            doNothing().when(processedEventPort).save(eq(TOPIC), eq(zeroPartition), eq(zeroOffset), any(Instant.class));

            // When
            boolean result = eventProcessor.markProcessed(null, TOPIC, zeroPartition, zeroOffset);

            // Then
            assertTrue(result);
            verify(processedEventPort).exists(TOPIC, zeroPartition, zeroOffset);
            verify(processedEventPort).save(eq(TOPIC), eq(zeroPartition), eq(zeroOffset), any(Instant.class));
        }

        @Test
        @DisplayName("Should handle maximum values for partition and offset")
        void shouldHandleMaximumValuesForPartitionAndOffset() {
            // Given
            Integer maxPartition = Integer.MAX_VALUE;
            Long maxOffset = Long.MAX_VALUE;
            when(processedEventPort.exists(TOPIC, maxPartition, maxOffset)).thenReturn(false);
            doNothing().when(processedEventPort).save(eq(TOPIC), eq(maxPartition), eq(maxOffset), any(Instant.class));

            // When
            boolean result = eventProcessor.markProcessed(null, TOPIC, maxPartition, maxOffset);

            // Then
            assertTrue(result);
            verify(processedEventPort).exists(TOPIC, maxPartition, maxOffset);
            verify(processedEventPort).save(eq(TOPIC), eq(maxPartition), eq(maxOffset), any(Instant.class));
        }
    }

    @Nested
    @DisplayName("Handle Duplicate Tests")
    class HandleDuplicateTests {

        @Test
        @DisplayName("Should log duplicate event information")
        void shouldLogDuplicateEventInformation() {
            // Given
            String eventType = "sales";
            String topic = "sales-topic";
            Integer partition = 1;
            Long offset = 100L;

            // When
            eventProcessor.handleDuplicate(eventType, topic, partition, offset);

            // Then - This test verifies the method executes without throwing exceptions
            // The actual logging is verified through the log output (Slf4j)
            // No assertions needed as the method only logs
        }

        @Test
        @DisplayName("Should handle null parameters in duplicate logging")
        void shouldHandleNullParametersInDuplicateLogging() {
            // Given
            String eventType = null;
            String topic = null;
            Integer partition = null;
            Long offset = null;

            // When & Then - Should not throw exception
            assertDoesNotThrow(() -> eventProcessor.handleDuplicate(eventType, topic, partition, offset));
        }
    }

    @Nested
    @DisplayName("Additional Branch Coverage Tests")
    class AdditionalBranchCoverageTests {

        @Test
        @DisplayName("Should return true when eventId is null and topic is null")
        void shouldReturnTrueWhenEventIdIsNullAndTopicIsNull() {
            // When
            boolean result = eventProcessor.markProcessed(null, null, PARTITION, OFFSET);

            // Then
            assertTrue(result);
            verify(processedEventPort, never()).exists(anyString(), anyInt(), anyLong());
            verify(processedEventPort, never()).save(anyString(), anyInt(), anyLong(), any(Instant.class));
        }

        @Test
        @DisplayName("Should return true when eventId is null and partition is null")
        void shouldReturnTrueWhenEventIdIsNullAndPartitionIsNull() {
            // When
            boolean result = eventProcessor.markProcessed(null, TOPIC, null, OFFSET);

            // Then
            assertTrue(result);
            verify(processedEventPort, never()).exists(anyString(), anyInt(), anyLong());
            verify(processedEventPort, never()).save(anyString(), anyInt(), anyLong(), any(Instant.class));
        }

        @Test
        @DisplayName("Should return true when eventId is null and offset is null")
        void shouldReturnTrueWhenEventIdIsNullAndOffsetIsNull() {
            // When
            boolean result = eventProcessor.markProcessed(null, TOPIC, PARTITION, null);

            // Then
            assertTrue(result);
            verify(processedEventPort, never()).exists(anyString(), anyInt(), anyLong());
            verify(processedEventPort, never()).save(anyString(), anyInt(), anyLong(), any(Instant.class));
        }

        @Test
        @DisplayName("Should return true when eventId is null and topic and partition are null")
        void shouldReturnTrueWhenEventIdIsNullAndTopicAndPartitionAreNull() {
            // When
            boolean result = eventProcessor.markProcessed(null, null, null, OFFSET);

            // Then
            assertTrue(result);
            verify(processedEventPort, never()).exists(anyString(), anyInt(), anyLong());
            verify(processedEventPort, never()).save(anyString(), anyInt(), anyLong(), any(Instant.class));
        }

        @Test
        @DisplayName("Should return true when eventId is null and topic and offset are null")
        void shouldReturnTrueWhenEventIdIsNullAndTopicAndOffsetAreNull() {
            // When
            boolean result = eventProcessor.markProcessed(null, null, PARTITION, null);

            // Then
            assertTrue(result);
            verify(processedEventPort, never()).exists(anyString(), anyInt(), anyLong());
            verify(processedEventPort, never()).save(anyString(), anyInt(), anyLong(), any(Instant.class));
        }

        @Test
        @DisplayName("Should return true when eventId is null and partition and offset are null")
        void shouldReturnTrueWhenEventIdIsNullAndPartitionAndOffsetAreNull() {
            // When
            boolean result = eventProcessor.markProcessed(null, TOPIC, null, null);

            // Then
            assertTrue(result);
            verify(processedEventPort, never()).exists(anyString(), anyInt(), anyLong());
            verify(processedEventPort, never()).save(anyString(), anyInt(), anyLong(), any(Instant.class));
        }
    }
}