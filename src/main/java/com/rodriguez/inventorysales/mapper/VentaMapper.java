package com.rodriguez.inventorysales.mapper;
import com.rodriguez.inventorysales.dto.response.VentaResponse;
import com.rodriguez.inventorysales.entity.Venta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
public class VentaMapper {
    public VentaResponse toResponse(Venta v) {
        return VentaResponse.builder()
                .id(v.getId())
                .fechaVenta(v.getFechaVenta())
                .total(v.getTotal())
                .detalles(v.getDetalles().stream()
                        .map(d -> VentaResponse.DetalleResponse.builder()
                                .productoId(d.getProducto().getId())
                                .productoNombre(d.getProducto().getNombre())
                                .cantidad(d.getCantidad())
                                .precioUnitario(d.getPrecioUnitario())
                                .subtotal(d.getPrecioUnitario()
                                        .multiply(BigDecimal.valueOf(d.getCantidad())))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}