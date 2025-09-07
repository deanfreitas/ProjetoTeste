package br.com.inventoryservice.application.port.out;

import java.time.Instant;

public interface ProcessedEventPort {
    boolean exists(String topic, Integer partition, Long offset);
    void save(String topic, Integer partition, Long offset, Instant processadoEm);
}
