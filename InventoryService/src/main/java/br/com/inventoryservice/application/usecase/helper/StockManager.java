package br.com.inventoryservice.application.usecase.helper;

import br.com.inventoryservice.application.port.out.StockAdjustmentPort;
import br.com.inventoryservice.application.port.out.StockPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockManager {

    private final StockPort stockPort;
    private final StockAdjustmentPort stockAdjustmentPort;

    @Value("${inventory.allowNegative:false}")
    private boolean allowNegative;

    public void adjustStock(String lojaCodigo, String sku, int delta, String topic, boolean allowZeroDelta) {
        if (delta == 0 && !allowZeroDelta) return;
        
        int base = stockPort.getQuantity(lojaCodigo, sku);
        int newQty = base + delta;
        
        if (newQty < 0 && !allowNegative) {
            log.warn("[{}] Operação resultaria em estoque negativo (base={}, delta={}) para loja={} sku={}; operação bloqueada.",
                    topic, base, delta, lojaCodigo, sku);
            return;
        }
        
        stockPort.upsertQuantity(lojaCodigo, sku, newQty);
    }

    public void saveStockAdjustment(String loja, String sku, int delta, String motivo, Instant timestamp) {
        stockAdjustmentPort.saveAdjustment(loja, sku, delta, motivo, timestamp);
    }
}