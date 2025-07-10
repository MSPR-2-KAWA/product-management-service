package fr.epsi.service.product;

import fr.epsi.service.product.dto.ProductDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;


import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = {ProductController.class, ProductControllerTest.TestConfig.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Test                  //test get all endpoint
    void getAllProducts_shouldReturnJsonArray() throws Exception {
        Product product = new Product();
        product.setId(1);
        product.setName("Product Test");
        product.setPrice(12.5f);

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product Test 2");
        product2.setPrice(25.5f);

        when(productService.getAll()).thenReturn(Arrays.asList(product, product2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product Test"))
                .andExpect(jsonPath("$[0].price").value(12.5))
                .andExpect(jsonPath("$[1].name").value("Product Test 2"))
                .andExpect(jsonPath("$[1].price").value(25.5))
                .andExpect(jsonPath("$.length()").value(2));
    }
    @Test
    void getProductById_shouldReturnSingleProduct() throws Exception {
        Product product = new Product();
        product.setId(1);
        product.setName("Product Test");
        product.setPrice(12.5f);

        when(productService.getById(1)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product Test"))
                .andExpect(jsonPath("$.price").value(12.5));
    }

    @Test
    void getProductById_shouldReturn404IfNotFound() throws Exception {
        when(productService.getById(999)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product 999 not found"));

        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product 999 not found"));
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct() throws Exception {
        Product updatedProduct = getUpdatedProduct();

        when(productService.updateProduct(eq(1), any(ProductDto.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/1")
                        .contentType("application/json")
                        .content("""
                {
                    "name": "Updated Product",
                    "price": 99.99,
                    "description": "Updated description",
                    "color": "Black",
                    "stock": 20
                }
            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(99.99))
                .andExpect(jsonPath("$.stock").value(20));
    }

    @Test
    void updateProduct_shouldReturn404IfProductNotFound() throws Exception {
        ProductDto dto = new ProductDto();
        dto.setName("New Name");
        dto.setPrice(99.99f);
        dto.setDescription("Desc");
        dto.setColor("Red");
        dto.setStock(15);

        // Simule une exception comme dans le service
        when(productService.updateProduct(eq(999), any(ProductDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product 999 not found"));

        mockMvc.perform(put("/api/products/999")
                        .contentType("application/json")
                        .content("""
                {
                    "name": "New Name",
                    "price": 99.99,
                    "description": "Desc",
                    "color": "Red",
                    "stock": 15
                }
            """))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product 999 not found"));
    }

    @NotNull
    private static Product getUpdatedProduct() {
        ProductDto updateDto = new ProductDto();
        updateDto.setName("Updated Product");
        updateDto.setPrice(99.99f);
        updateDto.setDescription("Updated description");
        updateDto.setColor("Black");
        updateDto.setStock(20);

        Product updatedProduct = new Product();
        updatedProduct.setId(1);
        updatedProduct.setName(updateDto.getName());
        updatedProduct.setPrice(updateDto.getPrice());
        updatedProduct.setDescription(updateDto.getDescription());
        updatedProduct.setColor(updateDto.getColor());
        updatedProduct.setStock(updateDto.getStock());
        return updatedProduct;
    }


    @Test
    void createProduct_shouldReturnCreatedProduct() throws Exception {
        ProductDto dto = new ProductDto();
        dto.setName("New Product");
        dto.setPrice(49.99f);
        dto.setDescription("Brand new");
        dto.setColor("Red");
        dto.setStock(10);

        Product savedProduct = new Product();
        savedProduct.setId(1);
        savedProduct.setName(dto.getName());
        savedProduct.setPrice(dto.getPrice());
        savedProduct.setDescription(dto.getDescription());
        savedProduct.setColor(dto.getColor());
        savedProduct.setStock(dto.getStock());

        when(productService.createProduct(any(ProductDto.class))).thenReturn(savedProduct);

        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content("""
                        {
                            "name": "New Product",
                            "price": 49.99,
                            "description": "Brand new",
                            "color": "Red",
                            "stock": 10
                        }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.price").value(49.99))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void deleteProduct_shouldReturnNoContent() throws Exception {
        doNothing().when(productService).deleteProduct(1);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_shouldReturn404IfProductNotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product 99 not found"))
                .when(productService).deleteProduct(99);

        mockMvc.perform(delete("/api/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product 99 not found"));
    }

    @Configuration
    static class TestConfig {
        @Bean
        public ProductService productService() {
            return Mockito.mock(ProductService.class);
        }
    }
}
