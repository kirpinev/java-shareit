package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemServiceIntegrationTest {

    @Autowired
    ItemService itemService;

    @Test
    void getItemByWrongItemId() {
        Long id = 2L;

        final NotFoundException exception = Assertions
                .assertThrows(NotFoundException.class, () -> itemService.getByItemIdAndUserId(id, id));

        Assertions.assertEquals(String.format("Вещи с id %s нет", id), exception.getMessage());
    }
}
