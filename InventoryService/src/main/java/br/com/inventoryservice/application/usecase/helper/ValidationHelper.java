package br.com.inventoryservice.application.usecase.helper;

import br.com.inventoryservice.application.port.out.ProductReadPort;
import br.com.inventoryservice.application.port.out.StoreReadPort;
import br.com.inventoryservice.domain.model.SalesItem;
import br.com.inventoryservice.domain.model.SalesModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidationHelper {

    private final ProductReadPort productReadPort;
    private final StoreReadPort storeReadPort;

    public boolean isValidSalesEvent(SalesModel event, String topic) {
        if (event == null || event.loja() == null || event.itens() == null || event.itens().isEmpty()) {
            log.warn("[{}] Payload de SalesEvent inválido: {}", topic, event);
            return false;
        }
        return true;
    }

    public boolean isValidSalesItem(SalesItem item, String topic) {
        if (item == null || item.sku() == null || item.quantidade() == null || item.quantidade() <= 0) {
            log.warn("[{}] Ignorando SalesItem inválido: {}", topic, item);
            return false;
        }
        return true;
    }

    public boolean productExists(String sku) {
        return productReadPort.existsBySku(sku);
    }

    public boolean storeExists(String lojaCodigo) {
        return storeReadPort.existsByCodigo(lojaCodigo);
    }

    public boolean isActiveProduct(String sku, String topic) {
        Optional<Boolean> ativoOpt = productReadPort.isAtivo(sku);
        if (ativoOpt.isEmpty()) {
            log.warn("[{}] Produto sku={} não encontrado", topic, sku);
            return false;
        }
        if (!ativoOpt.get()) {
            log.warn("[{}] Produto sku={} inativo", topic, sku);
            return false;
        }
        return true;
    }

    public boolean validateProductEvent(String sku, String topic) {
        if (sku == null) {
            return false;
        }
        boolean exists = productExists(sku);
        if (!exists) {
            log.warn("[{}] Produto sku={} não encontrado na fonte externa/replicada; evento aceito para idempotência, mas nenhuma escrita será realizada.", topic, sku);
        } else {
            log.debug("[{}] Produto sku={} validado (somente leitura)", topic, sku);
        }
        return true;
    }

    public boolean validateStoreEvent(String codigo, String topic) {
        if (codigo == null) {
            return false;
        }
        boolean exists = storeExists(codigo);
        if (!exists) {
            log.warn("[{}] Loja codigo={} não encontrada na fonte externa/replicada; evento aceito para idempotência, mas nenhuma escrita será realizada.", topic, codigo);
        } else {
            log.debug("[{}] Loja codigo={} validada (somente leitura)", topic, codigo);
        }
        return true;
    }
}