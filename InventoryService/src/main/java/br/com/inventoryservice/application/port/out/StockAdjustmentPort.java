package br.com.inventoryservice.application.port.out;

import java.time.Instant;

public interface StockAdjustmentPort {
    void saveAdjustment(String lojaCodigo, String sku, int delta, String motivo, Instant criadoEm);
}
