package com.Project3.E_commerce.controllers;

import com.Project3.E_commerce.models.Order;
import com.Project3.E_commerce.services.OrderService;
import com.Project3.E_commerce.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    private Long getCurrentUserId(Authentication auth) {
        return userService.findUserByEmail(auth.getName()).getId();
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Order checkout(Authentication auth) {
        return orderService.checkout(getCurrentUserId(auth));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Order> getAll() {
        return orderService.getAllOrders();
    }

    @GetMapping("/order/{orderId}")
    public Order getById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @PutMapping("/updateOrderStatus/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Order updateStatus(@PathVariable Long orderId, @RequestParam String status) {
        return orderService.updateOrderStatus(orderId, status);
    }

    @DeleteMapping("/deleteOrder/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }
}

