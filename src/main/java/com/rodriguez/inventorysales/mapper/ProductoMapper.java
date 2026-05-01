package com.rodriguez.inventorysales.mapper;

import com.rodriguez.inventorysales.dto.response.ProductoResponse;
import com.rodriguez.inventorysales.entity.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    public ProductoResponse toResponse(Producto p) {
        return ProductoResponse.builder()
                .id(p.getId())
                .sku(p.getSku())
                .nombre(p.getNombre())
                .precio(p.getPrecio())
                .stockActual(p.getStockActual())
                .categoriaId(p.getCategoria().getId())
                .categoriaNombre(p.getCategoria().getNombre())
                .build();
    }
}