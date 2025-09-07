package br.com.orderservice.infrastructure.adapters.in.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class StoreResolver {
    private final Map<String, Long> storeCodeToIdMap = new ConcurrentHashMap<>();

    public Long resolveStoreId(String storeCode) {
        if (storeCode == null) {
            throw new IllegalArgumentException("Store code cannot be null");
        }

        Long storeId = storeCodeToIdMap.get(storeCode);
        if (storeId == null) {
            storeId = Math.abs(storeCode.hashCode()) % 1000L + 1L;
            storeCodeToIdMap.put(storeCode, storeId);
            log.warn("Unknown store code '{}', created mapping to ID: {}", storeCode, storeId);
        }

        return storeId;
    }
}