package com.Project3.E_commerce.repositorys;

import com.Project3.E_commerce.models.Order;
import com.Project3.E_commerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}