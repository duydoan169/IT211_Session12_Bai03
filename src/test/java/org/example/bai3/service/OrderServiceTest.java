package org.example.bai3.service;

import org.example.bai3.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
    }

    @Test
    void getAllOrders_ReturnNonEmptyList() {
        List<Order> orders = orderService.getAllOrders();

        assertNotNull(orders);
        assertFalse(orders.isEmpty());
    }

    @Test
    void getOrderById_Found() {
        Order order = orderService.getOrderById(1L);

        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals("Nguyễn Văn A", order.getCustomerName());
        assertEquals("Laptop", order.getProduct());
    }

    @Test
    void getOrderById_NotFound_ThrowException() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderService.getOrderById(999L)
        );

        assertEquals("Order not found with id: 999", exception.getMessage());
    }

    @Test
    void addOrder_Success() {
        Order newOrder = new Order(null, "Lê Văn C", "Keyboard", 1, 800000.0);

        Order savedOrder = orderService.addOrder(newOrder);

        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getId());
        assertEquals(3L, savedOrder.getId());
        assertEquals("Lê Văn C", savedOrder.getCustomerName());
        assertEquals(3, orderService.getAllOrders().size());
    }

    @Test
    void updateOrder_Success() {
        Order updateOrder = new Order(null, "Nguyễn Văn A Updated", "Gaming Laptop", 1, 25000000.0);

        Order updatedOrder = orderService.updateOrder(1L, updateOrder);

        assertNotNull(updatedOrder);
        assertEquals(1L, updatedOrder.getId());
        assertEquals("Nguyễn Văn A Updated", updatedOrder.getCustomerName());
        assertEquals("Gaming Laptop", updatedOrder.getProduct());
        assertEquals(1, updatedOrder.getQuantity());
        assertEquals(25000000.0, updatedOrder.getTotalAmount());
    }

    @Test
    void deleteOrder_RemovesElement() {
        int beforeSize = orderService.getAllOrders().size();

        orderService.deleteOrder(1L);

        int afterSize = orderService.getAllOrders().size();

        assertEquals(beforeSize - 1, afterSize);
        assertThrows(RuntimeException.class, () -> orderService.getOrderById(1L));
    }
}
