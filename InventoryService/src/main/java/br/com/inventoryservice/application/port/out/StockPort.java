package br.com.inventoryservice.application.port.out;

public interface StockPort {
    int getQuantity(String lojaCodigo, String sku);
    void upsertQuantity(String lojaCodigo, String sku, int quantidade);
}
