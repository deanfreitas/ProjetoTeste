package br.com.inventoryservice.application.port.out;

public interface StoreReadPort {
    boolean existsByCodigo(String codigo);
}
