package br.com.inventoryservice.application.usecase.helper;

import br.com.inventoryservice.application.port.out.ProcessedEventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventProcessor {

    private final ProcessedEventPort processedEventPort;

    public boolean markProcessed(String eventId, String topic, Integer partition, Long offset) {
        log.debug("Iniciando marcação de evento como processado: eventId={}, topic={}, partition={}, offset={}", 
                 eventId, topic, partition, offset);
        
        if (eventId != null && !eventId.isBlank()) {
            String effectiveTopic = topic != null ? topic : "unknown";
            Integer effectivePartition = 0;
            Long effectiveOffset = (long) eventId.hashCode();
            
            log.debug("Processando evento com eventId. Topic efetivo: {}, partition: {}, offset: {}", 
                     effectiveTopic, effectivePartition, effectiveOffset);
            
            boolean exists = processedEventPort.exists(effectiveTopic, effectivePartition, effectiveOffset);
            if (exists) {
                log.debug("Evento já foi processado anteriormente: eventId={}", eventId);
                return false;
            }
            processedEventPort.save(effectiveTopic, effectivePartition, effectiveOffset, Instant.now());
            log.info("Evento marcado como processado com sucesso: eventId={}, topic={}", eventId, effectiveTopic);
            return true;
        }

        if (topic == null || partition == null || offset == null) {
            log.warn("Parâmetros insuficientes para marcar evento como processado. Ignorando processamento.");
            return true;
        }

        boolean exists = processedEventPort.exists(topic, partition, offset);
        if (exists) {
            log.debug("Evento já foi processado anteriormente: topic={}, partition={}, offset={}", 
                     topic, partition, offset);
            return false;
        }
        processedEventPort.save(topic, partition, offset, Instant.now());
        log.info("Evento marcado como processado com sucesso: topic={}, partition={}, offset={}", 
                topic, partition, offset);
        return true;
    }

    public void handleDuplicate(String eventType, String topic, Integer partition, Long offset) {
        log.info("[{}] Evento de {} duplicado ignorado na partition={} offset={}", topic, eventType, partition, offset);
    }
}