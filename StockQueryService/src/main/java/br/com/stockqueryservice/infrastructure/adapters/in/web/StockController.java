package br.com.stockqueryservice.infrastructure.adapters.in.web;

import br.com.stockqueryservice.application.port.in.StockQueryUseCase;
import br.com.stockqueryservice.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class StockController {

    private final StockQueryUseCase stockQueryUseCase;

    @GetMapping("/stock")
    public ResponseEntity<List<Stock>> getStock(
            @RequestParam Long productId,
            @RequestParam(required = false) Long storeId) {

        if (storeId != null) {
            Optional<Stock> stock = stockQueryUseCase.getStockByProductAndStore(productId, storeId);
            return stock.map(s -> ResponseEntity.ok(List.of(s)))
                    .orElse(ResponseEntity.ok(List.of()));
        }

        List<Stock> stocks = stockQueryUseCase.getStockByProduct(productId);
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/stock/store/{storeId}")
    public ResponseEntity<List<Stock>> getStockByStore(@PathVariable Long storeId) {
        List<Stock> stocks = stockQueryUseCase.getStockByStore(storeId);
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/stock/all")
    public ResponseEntity<List<Stock>> getAllStock() {
        List<Stock> stocks = stockQueryUseCase.getAllStock();
        return ResponseEntity.ok(stocks);
    }
}