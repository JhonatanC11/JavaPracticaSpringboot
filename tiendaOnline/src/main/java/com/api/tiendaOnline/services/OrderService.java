package com.api.tiendaOnline.services;

import com.api.tiendaOnline.dtos.OrderItemRequestDTO;
import com.api.tiendaOnline.dtos.OrderRequestDTO;
import com.api.tiendaOnline.exceptions.ProductNotFoundException;
import com.api.tiendaOnline.exceptions.UserNotFoundException;
import com.api.tiendaOnline.models.OrderItemModel;
import com.api.tiendaOnline.models.OrderModel;
import com.api.tiendaOnline.models.ProductModel;
import com.api.tiendaOnline.models.UserModel;
import com.api.tiendaOnline.repositories.OrderItemRepository;
import com.api.tiendaOnline.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Transactional
    public OrderModel createOrder(OrderRequestDTO request) {
        UserModel user = userService.getById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        OrderModel order = new OrderModel();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus("PENDING");
        order.setTotal(0.0);

        OrderModel savedOrder = orderRepository.save(order);

        double totalOrder = 0.0;

        for (OrderItemRequestDTO itemRequest : request.getItems()) {
            Long productId = itemRequest.getProductId();
            Integer quantity = itemRequest.getQuantity();

            ProductModel product = productService.getById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Producto con ID " + productId + " no encontrado"));

            if (product.getStock() < quantity) {
                throw new RuntimeException("Stock insuficiente para el producto: " + product.getName() + " Disponible: " + product.getStock() + " Requerida: " + quantity);
            }

            product.setStock(product.getStock() - quantity);
            productService.saveProduct(product);

            double unitPrice = product.getPrice();
            double subTotal = unitPrice * quantity;
            totalOrder += subTotal;

            OrderItemModel orderItem = new OrderItemModel();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setUnitPrice(unitPrice);
            orderItem.setSubtotal(subTotal);
            orderItemRepository.save(orderItem);
        }
        savedOrder.setTotal(totalOrder);
        return orderRepository.save(savedOrder);
    }
}