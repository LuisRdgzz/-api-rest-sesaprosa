package com.rodriguez.inventorysales.mapper;

import com.rodriguez.inventorysales.dto.response.CategoriaResponse;
import com.rodriguez.inventorysales.entity.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {
    public CategoriaResponse toResponse(Categoria c) {
        return CategoriaResponse.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .descripcion(c.getDescripcion())
                .build();
    }
}