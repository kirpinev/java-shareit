package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private final User user = new User(
            null,
            "Igor",
            "igor@gmail.dom");

    @Test
    void createUser() {
        User found = userRepository.save(user);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(1L, found.getId());
        Assertions.assertEquals(user.getName(), found.getName());
        Assertions.assertEquals(user.getEmail(), found.getEmail());
    }

    @Test
    void findUserById() {
        entityManager.persist(user);
        entityManager.flush();

        User found = userRepository.findById(1L).orElse(null);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(1L, found.getId());
        Assertions.assertEquals(user.getName(), found.getName());
        Assertions.assertEquals(user.getEmail(), found.getEmail());
    }

    @Test
    void findUserByWrongId() {
        User found = userRepository.findById(2L).orElse(null);

        Assertions.assertNull(found);
    }

    @Test
    void updateUserById() {
        entityManager.persist(user);
        entityManager.flush();

        User userFound = userRepository.findById(1L).orElse(null);

        Assertions.assertNotNull(userFound);
        Assertions.assertEquals(1L, userFound.getId());
        Assertions.assertEquals(user.getName(), userFound.getName());
        Assertions.assertEquals(user.getEmail(), userFound.getEmail());

        userFound.setEmail("stas@nail.com");

        User updatedUserFound = userRepository.findById(1L).orElse(null);

        Assertions.assertNotNull(updatedUserFound);
        Assertions.assertEquals(1L, updatedUserFound.getId());
        Assertions.assertEquals(user.getName(), updatedUserFound.getName());
        Assertions.assertEquals("stas@nail.com", updatedUserFound.getEmail());
    }

    @Test
    void deleteUserById() {
        entityManager.persist(user);
        entityManager.flush();

        userRepository.deleteById(1L);

        User userFound = userRepository.findById(1L).orElse(null);

        Assertions.assertNull(userFound);
    }
}
