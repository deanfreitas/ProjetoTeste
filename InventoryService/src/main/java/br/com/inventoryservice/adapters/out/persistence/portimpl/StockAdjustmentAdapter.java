package br.com.inventoryservice.adapters.out.persistence.portimpl;

import br.com.inventoryservice.adapters.out.persistence.entity.StockAdjustmentEntity;
import br.com.inventoryservice.adapters.out.persistence.repository.StockAdjustmentRepository;
import br.com.inventoryservice.application.port.out.StockAdjustmentPort;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockAdjustmentAdapter implements StockAdjustmentPort {

    private final StockAdjustmentRepository stockAdjustmentRepository;

    @Override
    public void saveAdjustment(String lojaCodigo, String sku, int delta, String motivo, Instant criadoEm) {
        StockAdjustmentEntity entity = StockAdjustmentEntity.builder()
                .lojaCodigo(lojaCodigo)
                .produtoSku(sku)
                .delta(delta)
                .motivo(motivo)
                .criadoEm(criadoEm)
                .build();
        stockAdjustmentRepository.save(entity);
    }
}
