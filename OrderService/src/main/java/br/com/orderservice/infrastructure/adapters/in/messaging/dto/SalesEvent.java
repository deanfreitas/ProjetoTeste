package br.com.orderservice.infrastructure.adapters.in.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesEvent {
    private String eventId;
    private String numero;
    private String customerId;
    private String storeCode;
    private Long totalCentavos;
    private LocalDateTime timestamp;
    private List<SalesEventItem> items;
}