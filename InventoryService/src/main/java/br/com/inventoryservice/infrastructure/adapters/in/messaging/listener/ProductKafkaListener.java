package br.com.inventoryservice.infrastructure.adapters.in.messaging.listener;

import br.com.inventoryservice.application.port.in.InventoryEventUseCase;
import br.com.inventoryservice.domain.model.ProductModel;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.ProductEvent;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.mapper.ProductEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Listener for product catalog events (produtos).
 * Now receives a typed ProductEvent DTO and forwards to service with idempotency handling.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ProductKafkaListener {

    private final InventoryEventUseCase service;

    @KafkaListener(topics = "${inventory.kafka.topics.produtos:produtos}", groupId = "${inventory.kafka.group-id:inventory-service}")
    public void onProdutoEvento(
            @Payload ProductEvent event,
            @Header(name = KafkaHeaders.RECEIVED_TOPIC, required = false) String topic,
            @Header(name = KafkaHeaders.RECEIVED_PARTITION, required = false) Integer partition,
            @Header(name = KafkaHeaders.OFFSET, required = false) Long offset,
            ConsumerRecord<String, String> record
    ) {
        log.info("[produtos] Evento recebido eventId={} topic='{}' partition={} offset={} tipo={} sku={} nome={} ativo={}",
                event.getEventId(), topic, partition, offset,
                event.getTipo(),
                event.getDados() != null ? event.getDados().getSku() : null,
                event.getDados() != null ? event.getDados().getNome() : null,
                event.getDados() != null ? event.getDados().getAtivo() : null);
        ProductModel domainEvent = ProductEventMapper.toDomain(event);
        service.handleProductEvent(domainEvent, topic, partition, offset);
    }
}
