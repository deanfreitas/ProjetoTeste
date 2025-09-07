package br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event;

import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.SalesItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesEvent {
    private String eventId;
    private String tipo;
    private OffsetDateTime timestamp;
    private String loja;
    private String pedidoId;
    private List<SalesItem> itens;
}
