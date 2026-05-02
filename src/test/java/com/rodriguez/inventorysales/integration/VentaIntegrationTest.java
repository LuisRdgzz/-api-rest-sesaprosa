// VentaIntegrationTest.java
package com.rodriguez.inventorysales.integration;

import com.rodriguez.inventorysales.dto.request.DetalleVentaRequest;
import com.rodriguez.inventorysales.dto.request.VentaRequest;
import com.rodriguez.inventorysales.entity.Categoria;
import com.rodriguez.inventorysales.entity.Producto;
import com.rodriguez.inventorysales.exception.InsufficientStockException;
import com.rodriguez.inventorysales.repository.CategoriaRepository;
import com.rodriguez.inventorysales.repository.ProductoRepository;
import com.rodriguez.inventorysales.service.VentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class VentaIntegrationTest {

    @Autowired private VentaService ventaService;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private CategoriaRepository categoriaRepository;

    @Test
    void registrarVenta_conStockInsuficiente_haceRollbackCompleto() {

        Categoria cat = categoriaRepository.save(Categoria.builder()
                .nombre("Test").descripcion("desc").build());

        Producto p = productoRepository.save(Producto.builder()
                .sku("TEST-1").nombre("Producto Test")
                .precio(BigDecimal.valueOf(100)).stockActual(2)
                .categoria(cat).build());

        DetalleVentaRequest item = new DetalleVentaRequest();
        item.setProductoId(p.getId());
        item.setCantidad(99); // más del stock

        VentaRequest req = new VentaRequest();
        req.setDetalles(List.of(item));

       
        assertThatThrownBy(() -> ventaService.registrarVenta(req))
                .isInstanceOf(InsufficientStockException.class);

        Producto despues = productoRepository.findById(p.getId()).orElseThrow();
        assertThat(despues.getStockActual()).isEqualTo(2); // sin cambio → rollback OK
    }
}