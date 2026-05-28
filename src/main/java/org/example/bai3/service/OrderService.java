package org.example.bai3.service;

import org.example.bai3.model.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private Long currentId = 1L;

    public OrderService() {
        orders.add(new Order(currentId++, "Nguyễn Văn A", "Laptop", 1, 15000000.0));
        orders.add(new Order(currentId++, "Trần Thị B", "Mouse", 2, 500000.0));
    }

    public List<Order> getAllOrders() {
        return orders;
    }

    public Order getOrderById(Long id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public Order addOrder(Order order) {
        order.setId(currentId++);
        orders.add(order);
        return order;
    }

    public Order updateOrder(Long id, Order newOrder) {
        Order existingOrder = getOrderById(id);

        existingOrder.setCustomerName(newOrder.getCustomerName());
        existingOrder.setProduct(newOrder.getProduct());
        existingOrder.setQuantity(newOrder.getQuantity());
        existingOrder.setTotalAmount(newOrder.getTotalAmount());

        return existingOrder;
    }

    public void deleteOrder(Long id) {
        Order existingOrder = getOrderById(id);
        orders.remove(existingOrder);
    }
}
