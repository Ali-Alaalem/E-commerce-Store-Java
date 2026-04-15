package com.Project3.E_commerce.services;

import com.Project3.E_commerce.exceptions.InformationNotFoundException;
import com.Project3.E_commerce.models.Cart;
import com.Project3.E_commerce.models.CartItem;
import com.Project3.E_commerce.models.Product;
import com.Project3.E_commerce.repositorys.CartRepository;
import com.Project3.E_commerce.repositorys.ProductRepository;
import com.Project3.E_commerce.repositorys.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;


    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseThrow(() -> new InformationNotFoundException("You don't have any Cart until now"));
    }

    @Transactional
    public Cart addToCart(Long userId, Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        Cart cart = getCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new InformationNotFoundException("Product not found"));

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId)).findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
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