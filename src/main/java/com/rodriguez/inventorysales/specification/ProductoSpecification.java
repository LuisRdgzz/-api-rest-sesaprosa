package com.rodriguez.inventorysales.specification;

import com.rodriguez.inventorysales.entity.Producto;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
public class ProductoSpecification {

    public static Specification<Producto> conNombre(String nombre){
        return (root, query , cb) -> nombre ==  null ? null
                : cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");

    }

    public static Specification<Producto> conPrecioMinimo(BigDecimal min) {
        return (root, query, cb) -> min == null ? null
                : cb.greaterThanOrEqualTo(root.get("precio"), min);
    }

    public static Specification<Producto> conPrecioMaximo(BigDecimal max) {
        return (root, query, cb) -> max == null ? null
                : cb.lessThanOrEqualTo(root.get("precio"), max);
    }

    public static Specification<Producto> conCategoria(Long categoriaId) {
        return (root, query, cb) -> categoriaId == null ? null
                : cb.equal(root.get("categoria").get("id"), categoriaId);
    }

}
