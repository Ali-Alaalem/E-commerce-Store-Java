package com.Project3.E_commerce.repositorys;

import com.Project3.E_commerce.models.Cart;
import com.Project3.E_commerce.models.CartItem;
import com.Project3.E_commerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}