package br.com.stockqueryservice.domain.model;

import java.time.LocalDateTime;

public record Stock(
        Long id,
        Long productId,
        Long storeId,
        Integer quantity,
        LocalDateTime lastUpdated,
        String productName,
        String storeName
) {
}