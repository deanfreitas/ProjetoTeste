package br.com.inventoryservice.infrastructure.adapters.in.messaging.mapper;

import br.com.inventoryservice.domain.model.StoreModel;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.StoreEvent;

/**
 * Mapper for converting StoreEvent (adapter) to domain model.
 */
public final class StoreEventMapper {

    private StoreEventMapper() {}

    public static StoreModel toDomain(StoreEvent event) {
        if (event == null) return null;
        return new StoreModel(
                event.getEventId(),
                event.getDados() != null ? event.getDados().getCodigo() : null
        );
    }
}
