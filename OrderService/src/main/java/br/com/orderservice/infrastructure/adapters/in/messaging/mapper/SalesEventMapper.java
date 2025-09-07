package br.com.orderservice.infrastructure.adapters.in.messaging.mapper;

import br.com.orderservice.domain.enuns.OrderStatus;
import br.com.orderservice.domain.model.Order;
import br.com.orderservice.domain.model.OrderItem;
import br.com.orderservice.infrastructure.adapters.in.messaging.dto.SalesEvent;
import br.com.orderservice.infrastructure.adapters.in.messaging.dto.SalesEventItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting SalesEvent (adapter) to domain model.
 */
public final class SalesEventMapper {

    private SalesEventMapper() {
    }

    public static Order toDomain(SalesEvent salesEvent, Long storeId) {
        if (salesEvent == null) return null;

        List<OrderItem> orderItems = salesEvent.getItems() == null ? null :
                salesEvent.getItems().stream()
                        .map(SalesEventMapper::toOrderItem)
                        .collect(Collectors.toList());

        return Order.builder()
                .numero(salesEvent.getNumero())
                .customerId(salesEvent.getCustomerId())
                .storeId(storeId)
                .status(OrderStatus.COMPLETED)
                .totalCentavos(salesEvent.getTotalCentavos())
                .createdAt(salesEvent.getTimestamp() != null ? salesEvent.getTimestamp() : LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(orderItems)
                .build();
    }

    private static OrderItem toOrderItem(SalesEventItem eventItem) {
        if (eventItem == null) return null;

        return OrderItem.builder()
                .sku(eventItem.getSku())
                .quantity(eventItem.getQuantidade())
                .priceCentavos(eventItem.getPrecoCentavos())
                .build();
    }
}