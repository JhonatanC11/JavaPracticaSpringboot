package com.api.tiendaOnline.controllers;

import com.api.tiendaOnline.models.CategoryModel;
import com.api.tiendaOnline.models.ProductModel;
import com.api.tiendaOnline.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public List<CategoryModel> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CategoryModel> getCategoryById(@PathVariable("id") Long id){
        return categoryService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoryModel> saveCategory(@Valid @RequestBody CategoryModel category) {
        CategoryModel saved = categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CategoryModel> updateCategoryById(@Valid @RequestBody CategoryModel request, @PathVariable("id") Long id){
        CategoryModel updated = categoryService.updateById(request, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable("id") Long id) {
        boolean ok = categoryService.deleteById(id);
        if (ok) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Categoria no encontrada");
        }
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<Set<ProductModel>> getProductsByCategory(@PathVariable("categoryId") Long categoryId) {
        Set<ProductModel> products = categoryService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<Void> addProductToCategory(@PathVariable("categoryId") Long categoryId, @PathVariable("productId") Long productId) {
        categoryService.addProductToCategory(categoryId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<Void> removeProductFromCategory(@PathVariable("categoryId") Long categoryId, @PathVariable("productId") Long productId) {
        categoryService.removeProductFromCategory(categoryId, productId);
        return ResponseEntity.noContent().build();
    }
}
