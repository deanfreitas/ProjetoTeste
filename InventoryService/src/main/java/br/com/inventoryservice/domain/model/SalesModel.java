package br.com.inventoryservice.domain.model;

import java.util.List;

public record SalesModel(String eventId, String loja, List<SalesItem> itens) {
}
