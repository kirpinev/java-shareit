package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.service.RequestService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RequestServiceIntegrationTest {

    @Autowired
    RequestService requestService;

    @Test
    void getRequestByWrongRequestId() {
        Long id = 2L;

        final NotFoundException exception = Assertions
                .assertThrows(NotFoundException.class, () -> requestService.getByRequestId(id));

        Assertions.assertEquals(String.format("Запроса с id %s нет", id), exception.getMessage());
    }
}
