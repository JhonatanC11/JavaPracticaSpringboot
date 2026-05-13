package com.api.tiendaOnline.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
public class CategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    private String name;

    @NotBlank(message = "La descripción de la categoría no puede estar vacía")
    private String description;

    @ManyToMany(mappedBy = "categories")
    private Set<ProductModel> products = new HashSet<>();

}
