package br.com.inventoryservice.infrastructure.adapters.out.persistence.portimpl;

import br.com.inventoryservice.application.port.out.StockAdjustmentPort;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.entity.StockAdjustmentEntity;
import br.com.inventoryservice.infrastructure.adapters.out.persistence.repository.StockAdjustmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

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
