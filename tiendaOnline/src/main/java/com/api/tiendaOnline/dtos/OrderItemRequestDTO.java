package com.api.tiendaOnline.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequestDTO {

    @NotNull(message = "El ID del producto no puede ser nulo")
    private Long productId;

    @NotNull(message = "La cantidad del producto no puede ser nula")
    private Integer quantity;
}
