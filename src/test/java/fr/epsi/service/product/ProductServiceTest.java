package fr.epsi.service.product;

import fr.epsi.service.product.dto.UpdateProductDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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

    @Test
    void getById_shouldReturnProductIfExists() {
        Product product = new Product();
        product.setId(1);
        product.setName("Test Product");

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Product result = productService.getById(1);
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            productService.getById(99);
        });

        assertEquals(404, exception.getStatusCode().value());
        assertEquals("Product 99 not found", exception.getReason());
    }

    @Test
    void updateProduct_shouldUpdateAndReturnProduct() {
        // Produit existant
        Product existingProduct = new Product();
        existingProduct.setId(1);
        existingProduct.setName("Old Name");

        // DTO avec nouvelles valeurs
        UpdateProductDTO dto = new UpdateProductDTO();
        dto.setName("New Name");
        dto.setPrice(99.99f);
        dto.setDescription("Updated description");
        dto.setColor("Black");
        dto.setStock(30);

        when(productRepository.findById(1)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product result = productService.updateProduct(1, dto);

        assertEquals("New Name", result.getName());
        assertEquals(99.99f, result.getPrice());
        assertEquals("Black", result.getColor());
        verify(productRepository).findById(1);
        verify(productRepository).save(existingProduct);
    }
}
