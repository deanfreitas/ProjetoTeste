package br.com.inventoryservice.adapters.in.messaging.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductData {
    private String sku;
    private String nome;
    private Boolean ativo;
}
