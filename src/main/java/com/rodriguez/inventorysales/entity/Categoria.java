package com.rodriguez.inventorysales.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias", indexes = {
        @Index(name = "idx_categoria_nombre", columnList = "nombre")
})
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private Set<Producto> productos = new HashSet<>();


    public Categoria() {}

    public Categoria(Long id, String nombre, String descripcion, Set<Producto> productos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.productos = productos;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String nombre;
        private String descripcion;
        private Set<Producto> productos = new HashSet<>();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder nombre(String nombre) { this.nombre = nombre; return this; }
        public Builder descripcion(String descripcion) { this.descripcion = descripcion; return this; }
        public Builder productos(Set<Producto> productos) { this.productos = productos; return this; }

        public Categoria build() {
            return new Categoria(id, nombre, descripcion, productos);
        }
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Set<Producto> getProductos() { return productos; }
    public void setProductos(Set<Producto> productos) { this.productos = productos; }
}