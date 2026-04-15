package com.Project3.E_commerce.controllers;

import com.Project3.E_commerce.models.Cart;
import com.Project3.E_commerce.services.CartService;
import com.Project3.E_commerce.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasAuthority('CUSTOMER')")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }


    @GetMapping
    public Cart getCart(Authentication auth) {
        Long userId= userService.findUserByEmail(auth.getName()).getId();
        return cartService.getCart(userId);
    }

    @PostMapping("/addToCart")
    public Cart addToCart(@RequestParam Long productId, @RequestParam int quantity, Authentication auth) {
        Long userId= userService.findUserByEmail(auth.getName()).getId();
        return cartService.addToCart(userId, productId, quantity);
    }

    @DeleteMapping("/removeFromCart/{productId}")
    public Cart removeFromCart(@PathVariable Long productId, Authentication auth) {
        Long userId= userService.findUserByEmail(auth.getName()).getId();
        return cartService.removeFromCart(userId, productId);
    }

    @DeleteMapping("/clearCart")
    public void clearCart(Authentication auth) {
        Long userId= userService.findUserByEmail(auth.getName()).getId();
        cartService.clearCart(userId);
    }
}
