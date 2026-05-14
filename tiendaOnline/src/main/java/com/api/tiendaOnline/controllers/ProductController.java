package com.api.tiendaOnline.controllers;

import com.api.tiendaOnline.models.ProductModel;
import com.api.tiendaOnline.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public List<ProductModel> getProducts (
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String search) {
        return productService.getProducts(category, minPrice, maxPrice, search);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> getProductById (@PathVariable("id") Long id){
        return productService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductModel> saveProduct (@RequestBody ProductModel product){
        ProductModel saved = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ProductModel> updateProductById (@Valid @PathVariable("id") Long id, @RequestBody ProductModel request){
        ProductModel updated = productService.updateById(request, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable("id") Long id){
        boolean ok = productService.deleteById(id);
        if(ok) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
