package com.Project3.E_commerce.services;


import com.Project3.E_commerce.exceptions.InformationNotFoundException;
import com.Project3.E_commerce.exceptions.InformationExistException;
import com.Project3.E_commerce.models.*;
import com.Project3.E_commerce.repositorys.CartRepository;
import com.Project3.E_commerce.repositorys.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public List<Order> getAllOrders() { return orderRepository.findAll(); }
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new InformationNotFoundException("Order not found"));
    }


    @Transactional
    public Order checkout(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new InformationNotFoundException("Cart not found"));
        if (cart.getItems().isEmpty()) throw new InformationNotFoundException("Cart is empty");

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalPrice(0.0);

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (CartItem cartItem : cart.getItems()) {
            Long productId = cartItem.getProduct().getId();
            int qty = cartItem.getQuantity();


            boolean success = productService.controlStock(productId, qty);
            if (!success) {
                throw new InformationExistException("Insufficient stock for: " + cartItem.getProduct().getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setOrder(order);
            orderItem.setQuantity(qty);
            orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());
            orderItem.setSubtotal(qty * cartItem.getProduct().getPrice());
            orderItems.add(orderItem);
            total += orderItem.getSubtotal();
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(total);
        Order savedOrder = orderRepository.save(order);


        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) throw new InformationNotFoundException("Order not found");
        orderRepository.deleteById(id);
    }
}