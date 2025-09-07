package br.com.inventoryservice.application.service.mapper;

import br.com.inventoryservice.adapters.in.messaging.dto.event.ProductEvent;
import br.com.inventoryservice.domain.model.ProductModel;

/**
 * Simple mapper for converting incoming adapter events to application service DTOs.
 */
public final class ProductEventMapper {

    private ProductEventMapper() {}

    public static ProductModel toDomain(ProductEvent event) {
        if (event == null) return null;
        String sku = null;
        if (event.getDados() != null) {
            sku = event.getDados().getSku();
        }
        return new ProductModel(
                event.getEventId(),
                sku
        );
    }
}
