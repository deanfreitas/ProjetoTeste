package br.com.inventoryservice.infrastructure.adapters.in.messaging.mapper;

import br.com.inventoryservice.domain.model.StockAdjustmentModel;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.StockAdjustmentEvent;

/**
 * Mapper for converting StockAdjustmentEvent (adapter) to domain model.
 */
public final class StockAdjustmentEventMapper {

    private StockAdjustmentEventMapper() {}

    public static StockAdjustmentModel toDomain(StockAdjustmentEvent event) {
        if (event == null) return null;
        return new StockAdjustmentModel(
                event.getEventId(),
                event.getLoja(),
                event.getSku(),
                event.getDelta(),
                event.getMotivo(),
                event.getTimestamp()
        );
    }
}
