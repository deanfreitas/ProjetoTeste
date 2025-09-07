package br.com.stockqueryservice.application.port.out;

import br.com.stockqueryservice.domain.model.Stock;

import java.util.List;
import java.util.Optional;

public interface StockPort {

    List<Stock> findByProductId(Long productId);

    Optional<Stock> findByProductIdAndStoreId(Long productId, Long storeId);

    List<Stock> findByStoreId(Long storeId);

    List<Stock> findAll();
}