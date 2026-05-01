package com.rodriguez.inventorysales.dto.response;

public class CategoriaResponse {

    private Long id;
    private String nombre;
    private String descripcion;

    public CategoriaResponse() {}

    public CategoriaResponse(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String nombre;
        private String descripcion;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder nombre(String nombre) { this.nombre = nombre; return this; }
        public Builder descripcion(String descripcion) { this.descripcion = descripcion; return this; }

        public CategoriaResponse build() { return new CategoriaResponse(id, nombre, descripcion); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}