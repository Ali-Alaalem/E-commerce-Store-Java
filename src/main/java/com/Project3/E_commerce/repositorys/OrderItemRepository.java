package com.Project3.E_commerce.repositorys;

import com.Project3.E_commerce.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}
