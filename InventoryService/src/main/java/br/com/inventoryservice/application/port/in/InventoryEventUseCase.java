package br.com.inventoryservice.application.port.in;

import br.com.inventoryservice.domain.model.ProductModel;
import br.com.inventoryservice.domain.model.SalesModel;
import br.com.inventoryservice.domain.model.StockAdjustmentModel;
import br.com.inventoryservice.domain.model.StoreModel;

public interface InventoryEventUseCase {

    void handleProductEvent(ProductModel event, String topic, Integer partition, Long offset);

    void handleStoreEvent(StoreModel event, String topic, Integer partition, Long offset);

    void handleSalesEvent(SalesModel event, String topic, Integer partition, Long offset);

    void handleStockAdjustmentEvent(StockAdjustmentModel event, String topic, Integer partition, Long offset);
}
