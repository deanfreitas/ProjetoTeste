package br.com.inventoryservice.infrastructure.adapters.in.messaging;

import br.com.inventoryservice.application.port.in.InventoryEventUseCase;
import br.com.inventoryservice.application.service.mapper.StockAdjustmentEventMapper;
import br.com.inventoryservice.domain.model.StockAdjustmentModel;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.StockAdjustmentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Listener for administrative stock adjustments (ajustes_estoque).
 * Now receives a typed StockAdjustmentEvent DTO.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class StockAdjustmentKafkaListener {

    private final InventoryEventUseCase service;

    @KafkaListener(topics = "${inventory.kafka.topics.ajustes:ajustes_estoque}", groupId = "${inventory.kafka.group-id:inventory-service}")
    public void onAjusteEstoque(
            @Payload StockAdjustmentEvent event,
            @Header(name = KafkaHeaders.RECEIVED_TOPIC, required = false) String topic,
            @Header(name = KafkaHeaders.RECEIVED_PARTITION, required = false) Integer partition,
            @Header(name = KafkaHeaders.OFFSET, required = false) Long offset,
            ConsumerRecord<String, String> record
    ) {
        log.info("[ajustes_estoque] Evento recebido eventId={} topic='{}' partition={} offset={} loja={} sku={} delta={} motivo={}",
                event.getEventId(), topic, partition, offset, event.getLoja(), event.getSku(), event.getDelta(), event.getMotivo());
        StockAdjustmentModel domainEvent = StockAdjustmentEventMapper.toDomain(event);
        service.handleStockAdjustmentEvent(domainEvent, topic, partition, offset);
    }
}
