package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RequestServiceIntegrationTest {

    @Autowired
    private RequestService requestService;
    @Autowired
    private UserService userService;

    private final UserDto userDto = new UserDto(
            null,
            "Igor",
            "igor@gmail.dom");

    private final RequestDto requestDto = new RequestDto(
            1L,
            "Какой-то запрос",
            null,
            null);

    @Test
    void createRequest() {
        UserDto createdUser = userService.create(userDto);
        requestService.create(requestDto, createdUser);

        List<RequestDto> requestDtos = requestService.getAllOwnRequestsById(createdUser.getId());

        Assertions.assertEquals(1, requestDtos.size());
        Assertions.assertEquals(1L, requestDtos.get(0).getId());
        Assertions.assertEquals(requestDto.getDescription(), requestDtos.get(0).getDescription());
        Assertions.assertEquals(0, requestDtos.get(0).getItems().size());
    }

    @Test
    void getRequestByWrongRequestId() {
        Long id = 2L;

        Assertions
                .assertThrows(NotFoundException.class, () -> requestService.getByRequestId(id));
    }
}
