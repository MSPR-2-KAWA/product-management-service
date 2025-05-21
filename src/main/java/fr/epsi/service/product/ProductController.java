package fr.epsi.service.product;

import fr.epsi.service.product.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    @GetMapping("/api/products")
    public List<Product> getAllProducts() { return productService.getAll(); }
    @GetMapping("/api/products/{id}")
    public Product getProductsById(@PathVariable Integer id) {
        return productService.getById(id);
    }
    @PutMapping("/api/products/{id}")
    public Product updateProduct(@PathVariable Integer id, @RequestBody ProductDTO dto) {
        return productService.updateProduct(id, dto);
    }
    @PostMapping("/api/products")
    public Product createProduct(@RequestBody ProductDTO dto) {
        return productService.createProduct(dto);
    }
    @DeleteMapping("/api/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }
}


