package fr.epsi.service.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

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

    @Test
    void testSaveAndFindById() {
        Product product = new Product();
        product.setName("H2 Product");
        product.setPrice(99.9f);

        Product saved = productRepository.save(product);
        Optional<Product> found = productRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("H2 Product", found.get().getName());
    }

    @Test
    void updateProduct_shouldPersistChanges() {
        // Création et sauvegarde
        Product product = new Product();
        product.setName("Initial Name");
        product.setPrice(10.0f);
        product.setDescription("Initial desc");
        product.setColor("White");
        product.setStock(10);

        Product saved = productRepository.save(product);

        // Mise à jour des champs
        saved.setName("Updated Name");
        saved.setPrice(15.5f);
        saved.setStock(20);
        productRepository.save(saved);

        Optional<Product> updated = productRepository.findById(saved.getId());
        assertTrue(updated.isPresent());
        assertEquals("Updated Name", updated.get().getName());
        assertEquals(15.5f, updated.get().getPrice());
        assertEquals(20, updated.get().getStock());
    }
}
