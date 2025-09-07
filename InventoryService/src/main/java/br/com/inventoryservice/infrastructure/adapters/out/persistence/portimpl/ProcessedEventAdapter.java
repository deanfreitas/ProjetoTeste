package br.com.inventoryservice.infrastructure.adapters.out.persistence.portimpl;

import br.com.inventoryservice.application.port.out.ProcessedEventPort;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.entity.ProcessedEventEntity;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ProcessedEventAdapter implements ProcessedEventPort {

    private final ProcessedEventRepository processedEventRepository;

    @Override
    public boolean exists(String topic, Integer partition, Long offset) {
        return processedEventRepository.findByTopicoAndParticaoAndOffset(topic, partition, offset).isPresent();
    }

    @Override
    public void save(String topic, Integer partition, Long offset, Instant processadoEm) {
        try {
            processedEventRepository.save(ProcessedEventEntity.builder()
                    .topico(topic)
                    .particao(partition)
                    .offset(offset)
                    .processadoEm(processadoEm)
                    .build());
        } catch (DataIntegrityViolationException e) {
            // ignore duplicates due to unique constraint
        }
    }
}
