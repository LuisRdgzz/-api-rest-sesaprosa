package com.rodriguez.inventorysales.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_ventas", indexes = {
        @Index(name = "idx_detalle_venta", columnList = "venta_id"),
        @Index(name = "idx_detalle_producto", columnList = "producto_id")
})
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venta_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_detalle_venta"))
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_detalle_producto"))
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    public DetalleVenta() {}

    public DetalleVenta(Long id, Venta venta, Producto producto,
                        Integer cantidad, BigDecimal precioUnitario) {
        this.id = id;
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Venta venta;
        private Producto producto;
        private Integer cantidad;
        private BigDecimal precioUnitario;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder venta(Venta venta) { this.venta = venta; return this; }
        public Builder producto(Producto producto) { this.producto = producto; return this; }
        public Builder cantidad(Integer cantidad) { this.cantidad = cantidad; return this; }
        public Builder precioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; return this; }

        public DetalleVenta build() {
            return new DetalleVenta(id, venta, producto, cantidad, precioUnitario);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
}