package br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event;

import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.ProductData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEvent {
    private String eventId;
    private String tipo;
    private ProductData dados;
}
