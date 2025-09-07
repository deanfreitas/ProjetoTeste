package br.com.inventoryservice.application.service.mapper;

import br.com.inventoryservice.domain.model.SalesItem;
import br.com.inventoryservice.domain.model.SalesModel;
import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event.SalesEvent;

import java.util.stream.Collectors;

/**
 * Mapper for converting SalesEvent (adapter) to domain model.
 */
public final class SalesEventMapper {

    private SalesEventMapper() {}

    public static SalesModel toDomain(SalesEvent event) {
        if (event == null) return null;
        return new SalesModel(
                event.getEventId(),
                event.getLoja(),
                event.getItens() == null ? null : event.getItens().stream()
                        .map(i -> new SalesItem(i.getSku(), i.getQuantidade()))
                        .collect(Collectors.toList())
        );
    }
}
