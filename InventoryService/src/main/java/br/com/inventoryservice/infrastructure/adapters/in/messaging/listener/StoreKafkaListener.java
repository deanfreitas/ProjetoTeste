package br.com.inventoryservice.infrastructure.adapters.in.messaging.listener;

import br.com.inventoryservice.application.port.in.InventoryEventUseCase;
import br.com.inventoryservice.domain.model.StoreModel;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.StoreEvent;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.mapper.StoreEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Listener for store catalog events (lojas).
 * Now receives a typed StoreEvent DTO and forwards to service with idempotency handling.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class StoreKafkaListener {

    private final InventoryEventUseCase service;

    @KafkaListener(topics = "${inventory.kafka.topics.lojas:lojas}", groupId = "${inventory.kafka.group-id:inventory-service}")
    public void onLojaEvento(
            @Payload StoreEvent event,
            @Header(name = KafkaHeaders.RECEIVED_TOPIC, required = false) String topic,
            @Header(name = KafkaHeaders.RECEIVED_PARTITION, required = false) Integer partition,
            @Header(name = KafkaHeaders.OFFSET, required = false) Long offset,
            ConsumerRecord<String, String> record
    ) {
        log.info("[lojas] Evento recebido eventId={} topic='{}' partition={} offset={} tipo={} codigo={} nome={}",
                event.getEventId(), topic, partition, offset,
                event.getTipo(),
                event.getDados() != null ? event.getDados().getCodigo() : null,
                event.getDados() != null ? event.getDados().getNome() : null);
        StoreModel domainEvent = StoreEventMapper.toDomain(event);
        service.handleStoreEvent(domainEvent, topic, partition, offset);
    }
}
