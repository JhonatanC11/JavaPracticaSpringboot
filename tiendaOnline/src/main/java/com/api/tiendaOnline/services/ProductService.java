package com.api.tiendaOnline.services;

import com.api.tiendaOnline.exceptions.ProductNotFoundException;
import com.api.tiendaOnline.models.ProductModel;
import com.api.tiendaOnline.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<ProductModel> getProducts(Long category, Double minPrice, Double maxPrice, String search) {
        return productRepository.findProducts(category, minPrice, maxPrice, search);
    }

    public Optional<ProductModel> getById(Long id) {
        return productRepository.findById(id);
    }

    public ProductModel saveProduct(ProductModel product) {
        return productRepository.save(product);
    }

    public ProductModel updateById(ProductModel product, Long id){
        ProductModel updated = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto con id " + id + " no encontrado"));

        updated.setName(product.getName());
        updated.setPrice(product.getPrice());
        updated.setStock(product.getStock());
        updated.setCategories(product.getCategories());

        return productRepository.save(updated);
    }
    public boolean deleteById(Long id){
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return true;
                })
                .orElse(false);
    }
}
