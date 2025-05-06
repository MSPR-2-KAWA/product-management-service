package fr.epsi.service.product;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    public ProductServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_shouldReturnListOfProducts() {
        Product product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setPrice(10.0f);

        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> products = productService.getAll();

        assertEquals(1, products.size());
        assertEquals("Test Product", products.getFirst().getName());
        verify(productRepository, times(1)).findAll();
    }
}
