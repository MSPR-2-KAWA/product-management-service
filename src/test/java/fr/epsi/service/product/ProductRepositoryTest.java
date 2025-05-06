package fr.epsi.service.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndFindAll() {
        Product product = new Product();
        product.setName("Repo Product");
        product.setPrice(25.0f);

        productRepository.save(product);

        List<Product> products = productRepository.findAll();
        assertFalse(products.isEmpty());
        assertEquals("Repo Product", products.getFirst().getName());
    }
}
