package br.com.inventoryservice.adapters.in.messaging.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAdjustmentEvent {
    private String eventId;
    private String tipo; // e.g., ajuste_aplicado
    private OffsetDateTime timestamp; // optional
    private String loja; // store code
    private String sku;
    private Integer delta; // can be positive or negative
    private String motivo; // optional reason
}
