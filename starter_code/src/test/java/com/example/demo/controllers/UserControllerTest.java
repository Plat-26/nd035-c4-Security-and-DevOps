package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @BeforeEach
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", "encoder");
    }

    @Test
    public void create_user_happy_path() {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest createUser = new CreateUserRequest();
        createUser.setUsername("user");
        createUser.setPassword("testPassword");
        createUser.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(createUser);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
        assertNotNull(u.getCart());

    }

    @Test
    public void find_by_username_happy_path() {
        String username = "test";
        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername(username);
        user.setPassword("testPassword");
        user.setCart(cart);


        when(userRepo.findByUsername(username)).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName(username);
        User actualUser = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals(0, actualUser.getId());
        assertEquals("test", actualUser.getUsername());
        assertEquals("testPassword", actualUser.getPassword());
        assertNotNull(actualUser.getCart());
    }

    @Test
    public void find_by_id_happy_path() {
        final long id = 0;
        User user = new User();
        Cart cart = new Cart();
        user.setId(id);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(cart);


//        when(userRepo.findById(0L)).thenReturn(user);

        ResponseEntity<User> response = userController.findById(id);
        User actualUser = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals(id, actualUser.getId());
        assertEquals("test", actualUser.getUsername());
        assertEquals("testPassword", actualUser.getPassword());
        assertNotNull(actualUser.getCart());
    }

}
