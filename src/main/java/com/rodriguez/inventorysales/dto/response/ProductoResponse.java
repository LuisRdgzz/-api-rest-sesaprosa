package com.rodriguez.inventorysales.dto.response;

import java.math.BigDecimal;

public class ProductoResponse {

    private Long id;
    private String sku;
    private String nombre;
    private BigDecimal precio;
    private Integer stockActual;
    private Long categoriaId;
    private String categoriaNombre;

    public ProductoResponse() {}

    public ProductoResponse(Long id, String sku, String nombre, BigDecimal precio,
                            Integer stockActual, Long categoriaId, String categoriaNombre) {
        this.id = id;
        this.sku = sku;
        this.nombre = nombre;
        this.precio = precio;
        this.stockActual = stockActual;
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String sku;
        private String nombre;
        private BigDecimal precio;
        private Integer stockActual;
        private Long categoriaId;
        private String categoriaNombre;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder sku(String sku) { this.sku = sku; return this; }
        public Builder nombre(String nombre) { this.nombre = nombre; return this; }
        public Builder precio(BigDecimal precio) { this.precio = precio; return this; }
        public Builder stockActual(Integer stockActual) { this.stockActual = stockActual; return this; }
        public Builder categoriaId(Long categoriaId) { this.categoriaId = categoriaId; return this; }
        public Builder categoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; return this; }

        public ProductoResponse build() {
            return new ProductoResponse(id, sku, nombre, precio, stockActual, categoriaId, categoriaNombre);
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

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }

    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
}