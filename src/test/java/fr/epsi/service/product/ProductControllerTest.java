package fr.epsi.service.product;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Configuration
    static class TestConfig {
        @Bean
        public ProductService productService() {
            return Mockito.mock(ProductService.class);
        }
    }
}
