package fr.epsi.service.product;

import fr.epsi.service.product.dto.UpdateProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    public List<Product> getAll() { return productRepository.findAll(); }
    public Product getById(Integer id){
        return productRepository.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product " + id + " not found") );
    }
    public Product updateProduct(Integer id, UpdateProductDTO dto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product " + id + " not found"));

        existingProduct.setName(dto.getName());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setColor(dto.getColor());
        existingProduct.setStock(dto.getStock());

        return productRepository.save(existingProduct);
    }


}


