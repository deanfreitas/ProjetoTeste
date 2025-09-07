package br.com.inventoryservice.domain.model;

import java.time.OffsetDateTime;

public record StockAdjustmentModel(String eventId, String loja, String sku, Integer delta, String motivo, OffsetDateTime timestamp) {
}
