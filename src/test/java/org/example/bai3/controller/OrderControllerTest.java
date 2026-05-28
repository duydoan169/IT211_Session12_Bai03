package org.example.bai3.controller;

import org.example.bai3.model.Order;
import org.example.bai3.service.OrderService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllOrders_ReturnHttp200AndJsonArray() throws Exception {
        Order order1 = new Order(1L, "Nguyễn Văn A", "Laptop", 1, 15000000.0);
        Order order2 = new Order(2L, "Trần Thị B", "Mouse", 2, 500000.0);

        Mockito.when(orderService.getAllOrders()).thenReturn(Arrays.asList(order1, order2));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Nguyễn Văn A"))
                .andExpect(jsonPath("$[0].product").value("Laptop"));
    }

    @Test
    void getOrderById_Found_ReturnHttp200() throws Exception {
        Order order = new Order(1L, "Nguyễn Văn A", "Laptop", 1, 15000000.0);

        Mockito.when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("Nguyễn Văn A"))
                .andExpect(jsonPath("$.product").value("Laptop"));
    }

    @Test
    void getOrderById_NotFound_ReturnHttp404() throws Exception {
        Mockito.when(orderService.getOrderById(999L))
                .thenThrow(new RuntimeException("Order not found with id: 999"));

        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found with id: 999"));
    }

    @Test
    void addOrder_ReturnHttp201AndBodyContainsId() throws Exception {
        Order requestOrder = new Order(null, "Lê Văn C", "Keyboard", 1, 800000.0);
        Order savedOrder = new Order(3L, "Lê Văn C", "Keyboard", 1, 800000.0);

        Mockito.when(orderService.addOrder(any(Order.class))).thenReturn(savedOrder);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestOrder)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.customerName").value("Lê Văn C"))
                .andExpect(jsonPath("$.product").value("Keyboard"));
    }

    @Test
    void updateOrder_ReturnHttp200() throws Exception {
        Order requestOrder = new Order(null, "Nguyễn Văn A Updated", "Gaming Laptop", 1, 25000000.0);
        Order updatedOrder = new Order(1L, "Nguyễn Văn A Updated", "Gaming Laptop", 1, 25000000.0);

        Mockito.when(orderService.updateOrder(eq(1L), any(Order.class))).thenReturn(updatedOrder);

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("Nguyễn Văn A Updated"))
                .andExpect(jsonPath("$.product").value("Gaming Laptop"));
    }

    @Test
    void deleteOrder_ReturnHttp204() throws Exception {
        Mockito.doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());
    }
}
