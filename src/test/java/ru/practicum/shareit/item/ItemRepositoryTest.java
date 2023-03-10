package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    private final User user = new User(
            null,
            "Igor",
            "igor@gmail.dom");

    private final Item item = new Item(
            null,
            "Какая-то вещь",
            "Какое-то описание",
            true,
            1L,
            user);

    private final Request request = new Request(
            null,
            "Запрос",
            1L,
            LocalDateTime.now());

    @BeforeEach
    void setup() {
        entityManager.persist(user);
        entityManager.persist(request);
        entityManager.flush();
    }

    @Test
    void createItem() {
        Item found = itemRepository.save(item);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(found.getId(), 1L);
        Assertions.assertEquals(found.getName(), item.getName());
        Assertions.assertEquals(found.getDescription(), item.getDescription());
        Assertions.assertTrue(found.getAvailable());
        Assertions.assertNotNull(found.getRequestId());
        Assertions.assertEquals(found.getOwner().getName(), user.getName());
        Assertions.assertEquals(found.getOwner().getEmail(), user.getEmail());
    }

    @Test
    void findItemById() {
        entityManager.persist(item);
        entityManager.flush();

        Item found = itemRepository.findById(1L).orElse(null);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(found.getId(), 1L);
        Assertions.assertEquals(found.getName(), item.getName());
        Assertions.assertEquals(found.getDescription(), item.getDescription());
        Assertions.assertTrue(found.getAvailable());
        Assertions.assertNotNull(found.getRequestId());
        Assertions.assertEquals(found.getOwner().getName(), user.getName());
        Assertions.assertEquals(found.getOwner().getEmail(), user.getEmail());
    }

    @Test
    void findAllByOwnerIdOrderByIdAsc() {
        entityManager.persist(item);
        entityManager.flush();

        List<Item> found = itemRepository.findAllByOwnerIdOrderByIdAsc(1L);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(found.size(), 1);
        Assertions.assertEquals(found.get(0).getId(), 1L);
        Assertions.assertEquals(found.get(0).getName(), item.getName());
        Assertions.assertEquals(found.get(0).getDescription(), item.getDescription());
        Assertions.assertTrue(found.get(0).getAvailable());
        Assertions.assertNotNull(found.get(0).getRequestId());
        Assertions.assertEquals(found.get(0).getOwner().getName(), user.getName());
        Assertions.assertEquals(found.get(0).getOwner().getEmail(), user.getEmail());
    }

    @Test
    void findAllByRequestIdOrderByIdAsc() {
        entityManager.persist(item);
        entityManager.flush();

        List<Item> found = itemRepository.findAllByRequestIdOrderByIdAsc(1L);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(found.size(), 1);
        Assertions.assertEquals(found.get(0).getId(), 1L);
        Assertions.assertEquals(found.get(0).getName(), item.getName());
        Assertions.assertEquals(found.get(0).getDescription(), item.getDescription());
        Assertions.assertTrue(found.get(0).getAvailable());
        Assertions.assertNotNull(found.get(0).getRequestId());
        Assertions.assertEquals(found.get(0).getOwner().getName(), user.getName());
        Assertions.assertEquals(found.get(0).getOwner().getEmail(), user.getEmail());
    }

    @Test
    void search() {
        entityManager.persist(item);
        entityManager.flush();

        List<Item> found = itemRepository.search("Какая-то вещь");

        Assertions.assertNotNull(found);
        Assertions.assertEquals(found.size(), 1);
        Assertions.assertEquals(found.get(0).getId(), 1L);
        Assertions.assertEquals(found.get(0).getName(), item.getName());
        Assertions.assertEquals(found.get(0).getDescription(), item.getDescription());
        Assertions.assertTrue(found.get(0).getAvailable());
        Assertions.assertNotNull(found.get(0).getRequestId());
        Assertions.assertEquals(found.get(0).getOwner().getName(), user.getName());
        Assertions.assertEquals(found.get(0).getOwner().getEmail(), user.getEmail());
    }

    @Test
    void findByOwnerIdAndId() {
        entityManager.persist(item);
        entityManager.flush();

        Item found = itemRepository.findByOwnerIdAndId(1L, 1L);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(found.getId(), 1L);
        Assertions.assertEquals(found.getName(), item.getName());
        Assertions.assertEquals(found.getDescription(), item.getDescription());
        Assertions.assertTrue(found.getAvailable());
        Assertions.assertNotNull(found.getRequestId());
        Assertions.assertEquals(found.getOwner().getName(), user.getName());
        Assertions.assertEquals(found.getOwner().getEmail(), user.getEmail());
    }
}
