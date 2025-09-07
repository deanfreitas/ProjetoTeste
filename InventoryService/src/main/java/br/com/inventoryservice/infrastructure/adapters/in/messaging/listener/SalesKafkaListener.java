package br.com.inventoryservice.infrastructure.adapters.in.messaging.listener;

import br.com.inventoryservice.application.port.in.InventoryEventUseCase;
import br.com.inventoryservice.domain.model.SalesModel;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.SalesEvent;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.mapper.SalesEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Listener for vendas (sales) events that should decrement inventory.
 * Now receives a typed SalesEvent DTO.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SalesKafkaListener {

    private final InventoryEventUseCase service;

    @KafkaListener(topics = "${inventory.kafka.topics.vendas:vendas}", groupId = "${inventory.kafka.group-id:inventory-service}")
    public void onVenda(
            @Payload SalesEvent event,
            @Header(name = KafkaHeaders.RECEIVED_TOPIC, required = false) String topic,
            @Header(name = KafkaHeaders.RECEIVED_PARTITION, required = false) Integer partition,
            @Header(name = KafkaHeaders.OFFSET, required = false) Long offset,
            ConsumerRecord<String, String> record
    ) {
        log.info("[vendas] Evento recebido eventId={} topic='{}' partition={} offset={} loja={} itens={}",
                event.getEventId(), topic, partition, offset, event.getLoja(), event.getItens());
        SalesModel domainEvent = SalesEventMapper.toDomain(event);
        service.handleSalesEvent(domainEvent, topic, partition, offset);
    }
}
