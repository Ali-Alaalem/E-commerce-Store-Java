package com.Project3.E_commerce.services;

import com.Project3.E_commerce.exceptions.InformationNotFoundException;
import com.Project3.E_commerce.exceptions.OutOfStockException;
import com.Project3.E_commerce.models.Cart;
import com.Project3.E_commerce.models.CartItem;
import com.Project3.E_commerce.models.Product;
import com.Project3.E_commerce.models.User;
import com.Project3.E_commerce.repositorys.CartRepository;
import com.Project3.E_commerce.repositorys.ProductRepository;
import com.Project3.E_commerce.repositorys.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository, UserRepository userRepository1) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository1;
    }

    public Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId).orElseThrow(() -> new InformationNotFoundException("User not found"));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public Cart addToCart(Long userId, Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        Cart cart = getCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new InformationNotFoundException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new OutOfStockException("Sorry we don't have stock for " + product.getName() +
            " more than " + product.getStockQuantity()
            );
        }


        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId)).findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            int newTotal = item.getQuantity() + quantity;
            if (newTotal > product.getStockQuantity()) {
                throw new OutOfStockException("Sorry we don't have stock for " + product.getName() +
                        " more than " + product.getStockQuantity()
                );
            }
            item.setQuantity(item.getQuantity() + quantity);
            item.setSubtotal(item.getQuantity() * product.getPrice());
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setCart(cart);
            newItem.setQuantity(quantity);
            newItem.setSubtotal(quantity * product.getPrice());
            cart.getItems().add(newItem);
        }
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeFromCart(Long userId, Long productId) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}