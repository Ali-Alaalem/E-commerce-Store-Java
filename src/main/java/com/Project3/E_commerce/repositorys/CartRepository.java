package com.Project3.E_commerce.repositorys;

import com.Project3.E_commerce.models.Cart;
import com.Project3.E_commerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
}