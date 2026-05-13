package com.api.tiendaOnline.repositories;

import com.api.tiendaOnline.models.OrderItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemModel, Long> {
}
