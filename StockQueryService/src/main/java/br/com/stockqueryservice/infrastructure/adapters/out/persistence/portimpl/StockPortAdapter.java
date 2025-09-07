package br.com.stockqueryservice.infrastructure.adapters.out.persistence.portimpl;

import br.com.stockqueryservice.application.port.out.StockPort;
import br.com.stockqueryservice.domain.model.Stock;
import br.com.stockqueryservice.infrastructure.adapters.out.persistence.entity.StockEntity;
import br.com.stockqueryservice.infrastructure.adapters.out.persistence.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StockPortAdapter implements StockPort {

    private final StockRepository stockRepository;

    @Override
    public List<Stock> findByProductId(Long productId) {
        return stockRepository.findByProductId(productId)
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Stock> findByProductIdAndStoreId(Long productId, Long storeId) {
        return stockRepository.findByProductIdAndStoreId(productId, storeId)
                .map(this::toDomainModel);
    }

    @Override
    public List<Stock> findByStoreId(Long storeId) {
        return stockRepository.findByStoreId(storeId)
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Stock> findAll() {
        return stockRepository.findAll()
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    private Stock toDomainModel(StockEntity entity) {
        return new Stock(
                entity.getId(),
                entity.getProductId(),
                entity.getStoreId(),
                entity.getQuantity(),
                entity.getLastUpdated(),
                entity.getProductName(),
                entity.getStoreName()
        );
    }
}