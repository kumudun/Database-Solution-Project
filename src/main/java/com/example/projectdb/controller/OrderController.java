package com.example.projectdb.controller;

import com.example.projectdb.dto.OrderRequest;
import com.example.projectdb.entity.Order;
import com.example.projectdb.entity.OrderItem;
import com.example.projectdb.repository.OrderItemRepository;
import com.example.projectdb.repository.OrderRepository;
import com.example.projectdb.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderService orderService;

    public OrderController(OrderRepository orderRepository,
                           OrderItemRepository orderItemRepository,
                           OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderService = orderService;
    }

    // READ - Get all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // READ - Get one order
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - Get items belonging to an order
    @GetMapping("/{id}/items")
    public List<OrderItem> getOrderItems(@PathVariable Integer id) {
        return orderItemRepository.findByOrderId(id);
    }

    // READ - Get order together with its items
    @GetMapping("/{id}/details")
    public ResponseEntity<?> getOrderDetails(@PathVariable Integer id) {

        return orderRepository.findById(id)
                .map(order -> {

                    List<OrderItem> items =
                            orderItemRepository.findByOrderId(id);

                    return ResponseEntity.ok(
                            java.util.Map.of(
                                    "order", order,
                                    "items", items
                            )
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE - Basic order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {

        order.setId(null);

        if (order.getOrderDate() == null) {
            order.setOrderDate(java.time.LocalDateTime.now());
        }

        if (order.getStatus() == null || order.getStatus().isBlank()) {
            order.setStatus("NEW");
        }

        Order savedOrder = orderRepository.save(order);

        return ResponseEntity
                .status(201)
                .body(savedOrder);
    }

    // CREATE - Order together with items using a transaction
    @PostMapping("/with-items")
    public ResponseEntity<Order> createOrderWithItems(
            @RequestBody OrderRequest request) {

        Order savedOrder = orderService.createOrderWithItems(
                request.getOrder(),
                request.getItems()
        );

        return ResponseEntity
                .status(201)
                .body(savedOrder);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Integer id,
            @RequestBody Order updatedOrder) {

        return orderRepository.findById(id)
                .map(order -> {

                    order.setCustomerId(updatedOrder.getCustomerId());
                    order.setShippingAddressId(
                            updatedOrder.getShippingAddressId()
                    );
                    order.setDeliveryDate(
                            updatedOrder.getDeliveryDate()
                    );
                    order.setStatus(updatedOrder.getStatus());

                    Order savedOrder =
                            orderRepository.save(order);

                    return ResponseEntity.ok(savedOrder);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable Integer id) {

        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        orderRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}