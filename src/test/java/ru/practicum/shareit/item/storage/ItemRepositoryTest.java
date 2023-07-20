package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.Storage.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @BeforeEach
    public void addItem() {
        User user = new User(null, "name", "mail@mail.com");
        User user2 = userRepository.save(user);
        ItemRequest request = new ItemRequest(null, "description", user2, LocalDateTime.now());
        ItemRequest request2 = itemRequestRepository.save(request);
        Item item = new Item(null, "name", "description", true, user2, request2);
        itemRepository.save(item);
        Item item2 = new Item(null, "nick2", "about", true, user, request);
        itemRepository.save(item);
    }

    @AfterEach
    void deleteAll() {
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllByOwnerIdIs() {
        User user = new User(1L, "name", "mail@mail.com");
        User user2 = new User(2L, "name2", "mail2@mail.com");
        ItemRequest request = new ItemRequest(1L, "description", user2, LocalDateTime.now());
        Item item = new Item(1L, "name", "description", true, user, request);
        long userId = 1L;
        Item returnItem = itemRepository.findAllByOwnerIdIs(userRepository.findAll().get(0).getId()).get(0);
        assertEquals(item.getAvailable(), returnItem.getAvailable());
        assertEquals(item.getName(), returnItem.getName());
        assertEquals(item.getDescription(), returnItem.getDescription());
    }

    @Test
    void findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable() {
        User user = new User(1L, "name", "mail@mail.com");
        User user2 = new User(2L, "name2", "mail2@mail.com");
        ItemRequest request = new ItemRequest(1L, "description", user2, LocalDateTime.now());
        Item item = new Item(1L, "name", "description", true, user, request);
        String searchText = "descr";
        Item returnItem = itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(searchText,
                        searchText, true).get(0);
        assertEquals(item.getAvailable(), returnItem.getAvailable());
        assertEquals(item.getName(), returnItem.getName());
        assertEquals(item.getDescription(), returnItem.getDescription());
    }

    @Test
    void findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable2() {
        User user = new User(1L, "name", "mail@mail.com");
        User user2 = new User(2L, "name2", "mail2@mail.com");
        ItemRequest request = new ItemRequest(1L, "description", user2, LocalDateTime.now());
        Item item = new Item(1L, "name", "description", true, user, request);
        String searchText = "nam";
        Item returnItem = itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(searchText,
                        searchText, true).get(0);
        assertEquals(item.getAvailable(), returnItem.getAvailable());
        assertEquals(item.getName(), returnItem.getName());
        assertEquals(item.getDescription(), returnItem.getDescription());
    }

    @Test
    void findByRequestId() {
        User user = new User(1L, "name", "mail@mail.com");
        User user2 = new User(2L, "name2", "mail2@mail.com");
        ItemRequest request = new ItemRequest(1L, "description", user2, LocalDateTime.now());
        Item item = new Item(1L, "name", "description", true, user, request);
        Item returnItem = itemRepository.findByRequestId(itemRequestRepository.findAll().get(0).getId()).get(0);
        assertEquals(item.getAvailable(), returnItem.getAvailable());
        assertEquals(item.getName(), returnItem.getName());
        assertEquals(item.getDescription(), returnItem.getDescription());
    }
}