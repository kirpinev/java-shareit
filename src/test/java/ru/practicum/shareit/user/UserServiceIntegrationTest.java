package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Test
    void getUserByWrongId() {
        Long userId = 2L;

        final NotFoundException exception = Assertions
                .assertThrows(NotFoundException.class, () -> userService.findById(userId));

        Assertions.assertEquals(String.format("Пользователя с id %s нет", userId), exception.getMessage());
    }
}
