package br.com.stockqueryservice.application.usecase;

import br.com.stockqueryservice.application.port.in.StockQueryUseCase;
import br.com.stockqueryservice.application.port.out.StockPort;
import br.com.stockqueryservice.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockQueryUseCaseImpl implements StockQueryUseCase {

    private final StockPort stockPort;

    @Override
    public List<Stock> getStockByProduct(Long productId) {
        return stockPort.findByProductId(productId);
    }

    @Override
    public Optional<Stock> getStockByProductAndStore(Long productId, Long storeId) {
        return stockPort.findByProductIdAndStoreId(productId, storeId);
    }

    @Override
    public List<Stock> getStockByStore(Long storeId) {
        return stockPort.findByStoreId(storeId);
    }

    @Override
    public List<Stock> getAllStock() {
        return stockPort.findAll();
    }
}