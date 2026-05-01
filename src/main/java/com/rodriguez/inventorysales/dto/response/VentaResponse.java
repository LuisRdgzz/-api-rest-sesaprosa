package com.rodriguez.inventorysales.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VentaResponse {
    private Long id;
    private LocalDateTime fechaVenta;
    private BigDecimal total;
    private List<DetalleResponse> detalles;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetalleResponse {
        private Long productoId;
        private String productoNombre;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}