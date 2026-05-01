package com.rodriguez.inventorysales.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas", indexes = {
        @Index(name = "idx_venta_fecha", columnList = "fecha_venta")
})
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleVenta> detalles = new ArrayList<>();

   public Venta() {}

    public Venta(Long id, LocalDateTime fechaVenta, BigDecimal total, List<DetalleVenta> detalles) {
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
        private List<DetalleVenta> detalles = new ArrayList<>();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder fechaVenta(LocalDateTime fechaVenta) { this.fechaVenta = fechaVenta; return this; }
        public Builder total(BigDecimal total) { this.total = total; return this; }
        public Builder detalles(List<DetalleVenta> detalles) { this.detalles = detalles; return this; }

        public Venta build() { return new Venta(id, fechaVenta, total, detalles); }
    }

    public void agregarDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
        detalle.setVenta(this);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDateTime fechaVenta) { this.fechaVenta = fechaVenta; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
}