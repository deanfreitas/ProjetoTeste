package br.com.stockqueryservice.application.port.in;

import br.com.stockqueryservice.domain.model.Stock;

import java.util.List;
import java.util.Optional;

public interface StockQueryUseCase {
    List<Stock> getStockByProduct(Long productId);

    Optional<Stock> getStockByProductAndStore(Long productId, Long storeId);

    List<Stock> getStockByStore(Long storeId);

    List<Stock> getAllStock();
}