package br.com.inventoryservice.application.port.out;

import java.util.Optional;

public interface ProductReadPort {
    boolean existsBySku(String sku);
    Optional<Boolean> isAtivo(String sku);
}
