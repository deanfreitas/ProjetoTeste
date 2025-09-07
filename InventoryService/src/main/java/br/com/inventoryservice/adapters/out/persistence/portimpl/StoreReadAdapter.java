package br.com.inventoryservice.adapters.out.persistence.portimpl;

import br.com.inventoryservice.adapters.out.persistence.repository.StoreRepository;
import br.com.inventoryservice.application.port.out.StoreReadPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreReadAdapter implements StoreReadPort {

    private final StoreRepository storeRepository;

    @Override
    public boolean existsByCodigo(String codigo) {
        return storeRepository.findById(codigo).isPresent();
    }
}
