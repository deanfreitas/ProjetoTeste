package br.com.inventoryservice.adapters.out.persistence.portimpl;

import br.com.inventoryservice.adapters.out.persistence.entity.StockEntity;
import br.com.inventoryservice.adapters.out.persistence.entity.StockId;
import br.com.inventoryservice.adapters.out.persistence.repository.StockRepository;
import br.com.inventoryservice.application.port.out.StockPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockAdapter implements StockPort {

    private final StockRepository stockRepository;

    @Override
    public int getQuantity(String lojaCodigo, String sku) {
        StockId id = StockId.builder().lojaCodigo(lojaCodigo).produtoSku(sku).build();
        Optional<StockEntity> stock = stockRepository.findById(id);
        return stock.map(s -> Optional.ofNullable(s.getQuantidade()).orElse(0)).orElse(0);
    }

    @Override
    public void upsertQuantity(String lojaCodigo, String sku, int quantidade) {
        StockId id = StockId.builder().lojaCodigo(lojaCodigo).produtoSku(sku).build();
        StockEntity entity = stockRepository.findById(id)
                .orElse(StockEntity.builder().id(id).quantidade(0).build());
        entity.setQuantidade(quantidade);
        stockRepository.save(entity);
    }
}
