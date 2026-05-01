package com.rodriguez.inventorysales.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos", indexes = {
        @Index(name = "idx_producto_sku", columnList = "sku", unique = true),
        @Index(name = "idx_producto_nombre", columnList = "nombre"),
        @Index(name = "idx_producto_categoria", columnList = "categoria_id")
})
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_producto_categoria"))
    private Categoria categoria;

    @Version
    @Column(nullable = false)
    private Long version;

    public Producto() {}

    public Producto(Long id, String sku, String nombre, BigDecimal precio,
                    Integer stockActual, Categoria categoria, Long version) {
        this.id = id;
        this.sku = sku;
        this.nombre = nombre;
        this.precio = precio;
        this.stockActual = stockActual;
        this.categoria = categoria;
        this.version = version;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String sku;
        private String nombre;
        private BigDecimal precio;
        private Integer stockActual;
        private Categoria categoria;
        private Long version;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder sku(String sku) { this.sku = sku; return this; }
        public Builder nombre(String nombre) { this.nombre = nombre; return this; }
        public Builder precio(BigDecimal precio) { this.precio = precio; return this; }
        public Builder stockActual(Integer stockActual) { this.stockActual = stockActual; return this; }
        public Builder categoria(Categoria categoria) { this.categoria = categoria; return this; }
        public Builder version(Long version) { this.version = version; return this; }

        public Producto build() {
            return new Producto(id, sku, nombre, precio, stockActual, categoria, version);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}