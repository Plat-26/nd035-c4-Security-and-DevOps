package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);

    private OrderRepository orderRepo = mock(OrderRepository.class);

    private User user;


    @BeforeEach
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        user = new User();
        user.setId(0);
        user.setUsername("test");
        Cart cart = new Cart();
        cart.setId(0L);
        cart.setItems(Arrays.asList(new Item(1L, "milk"), new Item(2L, "sugar")));
        cart.setTotal(BigDecimal.valueOf(50.0));
        cart.setUser(user);

    }

    @Test
    public void submit_happy_path() {
        when(userRepo.findByUsername("test")).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        UserOrder actualOrder = response.getBody();
        assertNotNull(actualOrder);
        assertEquals(BigDecimal.valueOf(50.0), actualOrder.getTotal());
        assertEquals(0, actualOrder.getId());
    }

    @Test
    public void get_orders_for_users_happy_path() {
        UserOrder order1 = new UserOrder(0L, Arrays.asList(new Item(1L, "milk"), new Item(2L, "sugar")));
        UserOrder order2 = new UserOrder(1L, Arrays.asList(new Item(4L, "sun-glasses"), new Item(5L, "carrots")));

        when(userRepo.findByUsername("test")).thenReturn(user);
        when(orderRepo.findByUser(user)).thenReturn(Arrays.asList(order1, order2));
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        List<UserOrder> orderList = response.getBody();
        assertNotNull(orderList);
        assert(orderList.contains(order1));
    }



}
