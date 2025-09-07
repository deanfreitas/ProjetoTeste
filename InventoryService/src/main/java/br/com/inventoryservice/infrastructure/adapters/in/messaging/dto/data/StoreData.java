package br.com.inventoryservice.infrastructure.adapters.in.messaging.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreData {
    private String codigo;
    private String nome;
}
