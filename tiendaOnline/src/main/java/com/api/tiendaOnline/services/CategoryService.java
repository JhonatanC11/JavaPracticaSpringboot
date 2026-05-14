package com.api.tiendaOnline.services;

import com.api.tiendaOnline.exceptions.CategoryNotFoundException;
import com.api.tiendaOnline.exceptions.ProductNotFoundException;
import com.api.tiendaOnline.models.CategoryModel;
import com.api.tiendaOnline.models.ProductModel;
import com.api.tiendaOnline.repositories.CategoryRepository;
import com.api.tiendaOnline.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    public List<CategoryModel> getCategories() {
        return categoryRepository.findAll();
    }

    public Optional<CategoryModel> getById(Long id){
        return categoryRepository.findById(id);
    }

    public CategoryModel saveCategory(CategoryModel category){
        return categoryRepository.save(category);
    }

    public CategoryModel updateById(CategoryModel request, Long id){
        CategoryModel category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("La categoria con id " + id + " no existe"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return categoryRepository.save(category);
    }

    public boolean deleteById(Long id){
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.delete(category);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public void addProductToCategory(Long categoryId, Long productoId){
        CategoryModel category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("La categoria con id " + categoryId + " no existe"));

        ProductModel product = productRepository.findById(productoId)
                .orElseThrow(() -> new ProductNotFoundException("El producto con id " + productoId + " no existe"));

        category.getProducts().add(product);
        categoryRepository.save(category);
    }

    @Transactional
    public void removeProductFromCategory(Long categoryId, Long productoId){
        CategoryModel category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("La categoria con id " + categoryId + " no existe"));

        ProductModel product = productRepository.findById(productoId)
                .orElseThrow(() -> new ProductNotFoundException("El producto con id " + productoId + " no existe"));

        category.getProducts().remove(product);
    }

    @Transactional
    public Set<ProductModel> getProductsByCategory(Long categoryId){
        CategoryModel category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("La categoria con id " + categoryId + " no existe"));

        return category.getProducts();
    }

}
