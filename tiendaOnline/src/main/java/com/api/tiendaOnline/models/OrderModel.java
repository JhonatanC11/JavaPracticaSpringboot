package com.api.tiendaOnline.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Data
public class OrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @NotNull(message = "El total de la orden no puede ser nulo")
    @Positive(message = "El total de la orden debe ser un valor positivo")
    private Double total;

    @NotBlank(message = "La fecha de la orden no puede estar vacía")
    private LocalDate orderDate;

    @NotBlank(message = "El estado de la orden no puede estar vacío")
    private String status;


}
