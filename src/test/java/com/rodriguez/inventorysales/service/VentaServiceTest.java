
package com.rodriguez.inventorysales.service;

import com.rodriguez.inventorysales.dto.request.DetalleVentaRequest;
import com.rodriguez.inventorysales.dto.request.VentaRequest;
import com.rodriguez.inventorysales.dto.response.VentaResponse;
import com.rodriguez.inventorysales.entity.Categoria;
import com.rodriguez.inventorysales.entity.Producto;
import com.rodriguez.inventorysales.entity.Venta;
import com.rodriguez.inventorysales.exception.InsufficientStockException;
import com.rodriguez.inventorysales.mapper.VentaMapper;
import com.rodriguez.inventorysales.repository.ProductoRepository;
import com.rodriguez.inventorysales.repository.VentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock private ProductoRepository productoRepository;
    @Mock private VentaRepository ventaRepository;
    @Mock private VentaMapper ventaMapper;

    @InjectMocks private VentaService ventaService;

    @Test
    void registrarVenta_exitosa_descuentaStockYRetornaResponse() {

        Categoria cat = Categoria.builder().id(1L).nombre("Electrónica").build();
        Producto p = Producto.builder()
                .id(1L).sku("SKU-1").nombre("Laptop")
                .precio(BigDecimal.valueOf(1000))
                .stockActual(10).version(0L).categoria(cat).build();

        DetalleVentaRequest itemReq = new DetalleVentaRequest();
        itemReq.setProductoId(1L);
        itemReq.setCantidad(3);

        VentaRequest req = new VentaRequest();
        req.setDetalles(List.of(itemReq));

        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> {
            Venta v = i.getArgument(0);
            v.setId(99L);
            return v;
        });
        when(ventaMapper.toResponse(any(Venta.class))).thenAnswer(i -> {
            Venta v = i.getArgument(0);
            return VentaResponse.builder().id(v.getId()).total(v.getTotal()).build();
        });


        VentaResponse resp = ventaService.registrarVenta(req);


        assertThat(resp.getId()).isEqualTo(99L);
        assertThat(resp.getTotal()).isEqualByComparingTo("3000");
        assertThat(p.getStockActual()).isEqualTo(7);  // 10 - 3
    }

    @Test
    void registrarVenta_stockInsuficiente_lanzaExcepcion() {
        Categoria cat = Categoria.builder().id(1L).nombre("Electrónica").build();
        Producto p = Producto.builder()
                .id(1L).sku("SKU-1").nombre("Laptop")
                .precio(BigDecimal.valueOf(1000))
                .stockActual(2).version(0L).categoria(cat).build();

        DetalleVentaRequest itemReq = new DetalleVentaRequest();
        itemReq.setProductoId(1L);
        itemReq.setCantidad(5);

        VentaRequest req = new VentaRequest();
        req.setDetalles(List.of(itemReq));

        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));

        assertThatThrownBy(() -> ventaService.registrarVenta(req))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Stock insuficiente");
    }
}