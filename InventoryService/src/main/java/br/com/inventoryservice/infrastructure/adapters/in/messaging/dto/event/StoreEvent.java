package br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.event;

import br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data.StoreData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for topic 'lojas' aligning with README examples.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreEvent {
    private String eventId;
    private String tipo; // loja_criada, loja_atualizada
    private StoreData dados;
}
