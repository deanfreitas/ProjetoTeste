package br.com.inventoryservice.application.usecase;

import br.com.inventoryservice.application.port.in.InventoryEventUseCase;
import br.com.inventoryservice.application.usecase.helper.EventProcessor;
import br.com.inventoryservice.application.usecase.helper.StockManager;
import br.com.inventoryservice.application.usecase.helper.ValidationHelper;
import br.com.inventoryservice.domain.model.ProductModel;
import br.com.inventoryservice.domain.model.SalesModel;
import br.com.inventoryservice.domain.model.StockAdjustmentModel;
import br.com.inventoryservice.domain.model.StoreModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryEventUseCaseImpl implements InventoryEventUseCase {

    private final EventProcessor eventProcessor;
    private final ValidationHelper validationHelper;
    private final StockManager stockManager;

    @Override
    @Transactional
    public void handleProductEvent(ProductModel event, String topic, Integer partition, Long offset) {
        if (!eventProcessor.markProcessed(event != null ? event.eventId() : null, topic, partition, offset)) {
            eventProcessor.handleDuplicate("produto", topic, partition, offset);
            return;
        }
        if (event == null || event.sku() == null) {
            log.warn("[{}] Payload de ProductEvent inválido: {}", topic, event);
            return;
        }
        validationHelper.validateProductEvent(event.sku(), topic);
    }

    @Override
    @Transactional
    public void handleStoreEvent(StoreModel event, String topic, Integer partition, Long offset) {
        if (!eventProcessor.markProcessed(event != null ? event.eventId() : null, topic, partition, offset)) {
            eventProcessor.handleDuplicate("loja", topic, partition, offset);
            return;
        }
        if (event == null || event.codigo() == null) {
            log.warn("[{}] Payload de StoreEvent inválido: {}", topic, event);
            return;
        }
        validationHelper.validateStoreEvent(event.codigo(), topic);
    }

    @Override
    @Transactional
    public void handleSalesEvent(SalesModel event, String topic, Integer partition, Long offset) {
        if (!eventProcessor.markProcessed(event != null ? event.eventId() : null, topic, partition, offset)) {
            eventProcessor.handleDuplicate("venda", topic, partition, offset);
            return;
        }
        if (!validationHelper.isValidSalesEvent(event, topic)) return;

        if (!validationHelper.storeExists(event.loja())) {
            log.warn("[{}] Loja codigo={} não encontrada; ignorando evento de venda para manter integridade.", topic, event.loja());
            return;
        }
        event.itens()
                .stream()
                .filter(item -> validationHelper.isValidSalesItem(item, topic))
                .filter(item -> validationHelper.isActiveProduct(item.sku(), topic))
                .forEach(item -> stockManager.adjustStock(event.loja(), item.sku(), -item.quantidade(), topic, false));
    }

    @Override
    @Transactional
    public void handleStockAdjustmentEvent(StockAdjustmentModel event, String topic, Integer partition, Long offset) {
        if (!eventProcessor.markProcessed(event != null ? event.eventId() : null, topic, partition, offset)) {
            eventProcessor.handleDuplicate("ajuste de estoque", topic, partition, offset);
            return;
        }
        if (event == null || event.loja() == null || event.sku() == null) {
            log.warn("[{}] Payload de StockAdjustmentEvent inválido: {}", topic, event);
            return;
        }
        boolean lojaExiste = validationHelper.storeExists(event.loja());
        boolean produtoExiste = validationHelper.productExists(event.sku());
        if (!lojaExiste || !produtoExiste) {
            log.warn("[{}] Referência inválida (lojaExiste={}, produtoExiste={}); ignorando ajuste para loja={} sku={}", topic, lojaExiste, produtoExiste, event.loja(), event.sku());
            return;
        }
        if (!validationHelper.isActiveProduct(event.sku(), topic)) {
            log.warn("[{}] Produto inválido ou inativo para ajuste; loja={} sku={}", topic, event.loja(), event.sku());
            return;
        }

        int delta = Optional.ofNullable(event.delta()).orElse(0);

        stockManager.saveStockAdjustment(
                event.loja(),
                event.sku(),
                delta,
                event.motivo(),
                event.timestamp() != null ? event.timestamp().toInstant() : Instant.now()
        );

        stockManager.adjustStock(event.loja(), event.sku(), delta, topic, true);
    }

}
