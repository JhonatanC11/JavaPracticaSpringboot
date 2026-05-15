package com.api.tiendaOnline.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long userId;

    @NotNull(message = "La lista de productos no puede estar vacía")
    private List<OrderItemRequestDTO> items;
}
