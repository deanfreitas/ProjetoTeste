package br.com.orderservice.infrastructure.adapters.out.persistence.mapper;

import br.com.orderservice.domain.model.Order;
import br.com.orderservice.domain.model.OrderItem;
import br.com.orderservice.infrastructure.adapters.out.persistence.entity.OrderEntity;
import br.com.orderservice.infrastructure.adapters.out.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderEntity toEntity(Order order) {
        if (order == null) {
            return null;
        }

        OrderEntity entity = OrderEntity.builder()
                .id(order.getId())
                .numero(order.getNumero())
                .customerId(order.getCustomerId())
                .storeId(order.getStoreId())
                .status(order.getStatus())
                .totalCentavos(order.getTotalCentavos())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();

        if (order.getItems() != null) {
            List<OrderItemEntity> itemEntities = order.getItems().stream()
                    .map(item -> toItemEntity(item, entity))
                    .collect(Collectors.toList());
            entity.setItems(itemEntities);
        }

        return entity;
    }

    public Order toDomain(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        Order.OrderBuilder builder = Order.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .customerId(entity.getCustomerId())
                .storeId(entity.getStoreId())
                .status(entity.getStatus())
                .totalCentavos(entity.getTotalCentavos())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt());

        if (entity.getItems() != null) {
            List<OrderItem> items = entity.getItems().stream()
                    .map(this::toItemDomain)
                    .collect(Collectors.toList());
            builder.items(items);
        }

        return builder.build();
    }

    private OrderItemEntity toItemEntity(OrderItem item, OrderEntity orderEntity) {
        return OrderItemEntity.builder()
                .id(item.getId())
                .order(orderEntity)
                .sku(item.getSku())
                .quantity(item.getQuantity())
                .priceCentavos(item.getPriceCentavos())
                .build();
    }

    private OrderItem toItemDomain(OrderItemEntity entity) {
        return OrderItem.builder()
                .id(entity.getId())
                .orderId(entity.getOrder() != null ? entity.getOrder().getId() : null)
                .sku(entity.getSku())
                .quantity(entity.getQuantity())
                .priceCentavos(entity.getPriceCentavos())
                .build();
    }
}