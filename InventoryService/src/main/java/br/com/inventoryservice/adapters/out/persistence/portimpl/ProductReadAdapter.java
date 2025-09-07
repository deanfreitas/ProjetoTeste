package br.com.inventoryservice.adapters.out.persistence.portimpl;

import br.com.inventoryservice.adapters.out.persistence.entity.ProductEntity;
import br.com.inventoryservice.adapters.out.persistence.repository.ProductRepository;
import br.com.inventoryservice.application.port.out.ProductReadPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductReadAdapter implements ProductReadPort {

    private final ProductRepository productRepository;

    @Override
    public boolean existsBySku(String sku) {
        return productRepository.findById(sku).isPresent();
    }

    @Override
    public Optional<Boolean> isAtivo(String sku) {
        return productRepository.findById(sku).map(ProductEntity::getAtivo);
    }
}
