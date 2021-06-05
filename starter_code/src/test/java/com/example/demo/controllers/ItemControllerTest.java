package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.repositories.ItemRepository;

import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @BeforeEach
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

}
