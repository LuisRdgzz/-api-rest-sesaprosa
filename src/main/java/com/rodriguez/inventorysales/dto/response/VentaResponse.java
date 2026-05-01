package com.rodriguez.inventorysales.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VentaResponse {

    private Long id;
    private LocalDateTime fechaVenta;
    private BigDecimal total;
    private List<DetalleResponse> detalles;

    public VentaResponse() {}

    public VentaResponse(Long id, LocalDateTime fechaVenta, BigDecimal total, List<DetalleResponse> detalles) {
        this.id = id;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.detalles = detalles;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private LocalDateTime fechaVenta;
        private BigDecimal total;
        private List<DetalleResponse> detalles;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder fechaVenta(LocalDateTime fechaVenta) { this.fechaVenta = fechaVenta; return this; }
        public Builder total(BigDecimal total) { this.total = total; return this; }
        public Builder detalles(List<DetalleResponse> detalles) { this.detalles = detalles; return this; }

        public VentaResponse build() { return new VentaResponse(id, fechaVenta, total, detalles); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDateTime fechaVenta) { this.fechaVenta = fechaVenta; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<DetalleResponse> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleResponse> detalles) { this.detalles = detalles; }

    // ===== Clase anidada =====
    public static class DetalleResponse {

        private Long productoId;
        private String productoNombre;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;

        public DetalleResponse() {}

        public DetalleResponse(Long productoId, String productoNombre, Integer cantidad,
                               BigDecimal precioUnitario, BigDecimal subtotal) {
            this.productoId = productoId;
            this.productoNombre = productoNombre;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = subtotal;
        }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long productoId;
            private String productoNombre;
            private Integer cantidad;
            private BigDecimal precioUnitario;
            private BigDecimal subtotal;

            public Builder productoId(Long productoId) { this.productoId = productoId; return this; }
            public Builder productoNombre(String productoNombre) { this.productoNombre = productoNombre; return this; }
            public Builder cantidad(Integer cantidad) { this.cantidad = cantidad; return this; }
            public Builder precioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; return this; }
            public Builder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }

            public DetalleResponse build() {
                return new DetalleResponse(productoId, productoNombre, cantidad, precioUnitario, subtotal);
            }
        }

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }

        public String getProductoNombre() { return productoNombre; }
        public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    }
}