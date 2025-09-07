package br.com.orderservice.infrastructure.adapters.in.messaging;

import br.com.orderservice.application.port.in.OrderUseCase;
import br.com.orderservice.domain.model.Order;
import br.com.orderservice.infrastructure.adapters.in.messaging.dto.SalesEvent;
import br.com.orderservice.infrastructure.adapters.in.messaging.mapper.SalesEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class SalesKafkaListener {

    private final OrderUseCase orderUseCase;
    private final StoreResolver storeResolver;

    @KafkaListener(topics = "${order.kafka.topics.vendas}", groupId = "${order.kafka.group-id}")
    public void handleSalesEvent(SalesEvent salesEvent) {
        if (salesEvent == null) {
            log.warn("Received null sales event, ignoring");
            return;
        }

        log.info("Received sales event with eventId: {}, numero: {}",
                salesEvent.getEventId(), salesEvent.getNumero());

        try {
            Long storeId = storeResolver.resolveStoreId(salesEvent.getStoreCode());

            Order order = SalesEventMapper.toDomain(salesEvent, storeId);

            Order createdOrder = orderUseCase.createOrder(order);

            log.info("Successfully created order with ID: {} for sales event: {}",
                    createdOrder.getId(), salesEvent.getEventId());

        } catch (Exception e) {
            log.error("Error processing sales event with eventId: {}, error: {}",
                    salesEvent != null ? salesEvent.getEventId() : "null", e.getMessage(), e);
        }
    }

}