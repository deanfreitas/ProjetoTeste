package br.com.orderservice.infrastructure.adapters.in.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesEventItem {
    private String sku;
    private Long quantidade;
    private Long precoCentavos;
}