package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private BookingService bookingService;


    private final UserDto userDto = new UserDto(
            null,
            "Igor",
            "igor@gmail.dom");

    private final ItemDto itemDto = new ItemDto(
            null,
            "Какая-то вещь",
            "Какое-то описание",
            true,
            1L,
            null,
            null,
            new ArrayList<>(),
            null);

    private final ItemDto itemDto2 = new ItemDto(
            null,
            "Какая-то вещь",
            "Какое-то описание",
            true,
            2L,
            null,
            null,
            new ArrayList<>(),
            null);

    private final UserDto userDto2 = new UserDto(
            null,
            "Stas",
            "stas@gmail.dom");

    private final ItemDto itemDtoToRequest = new ItemDto(
            null,
            "Какая-то вещь",
            "Какое-то описание",
            true,
            1L,
            null,
            null,
            new ArrayList<>(),
            1L);

    private final RequestDto requestDto = new RequestDto(
            1L,
            "Какой-то запрос",
            null,
            null);

    private final BookingInputDto bookingInputDto1 = new BookingInputDto(
            1L,
            LocalDateTime.now().plusMinutes(3),
            LocalDateTime.now().plusMinutes(5));

    private final CommentDto commentDto = new CommentDto(
            null,
            "Коммент",
            null,
            null);

    @Test
    void createItem() {
        UserDto createdUser = userService.create(userDto);
        ItemDto createdItem = itemService.create(itemDto, createdUser);

        Assertions.assertEquals(1L, createdItem.getId());
        Assertions.assertEquals(itemDto.getName(), createdItem.getName());
        Assertions.assertEquals(itemDto.getDescription(), createdItem.getDescription());
    }

    @Test
    void addItemToRequest() {
        UserDto createdUser = userService.create(userDto);
        requestService.create(requestDto, createdUser);

        ItemDto createdItemToRequest = itemService.create(itemDtoToRequest, createdUser);

        Assertions.assertEquals(1L, createdItemToRequest.getRequestId());
        Assertions.assertEquals(itemDtoToRequest.getName(), createdItemToRequest.getName());
        Assertions.assertEquals(itemDtoToRequest.getDescription(), createdItemToRequest.getDescription());
        Assertions.assertEquals(itemDtoToRequest.getOwnerId(), createdItemToRequest.getOwnerId());
    }

    @Test
    void addComment() {
        UserDto createdUser1 = userService.create(userDto);
        UserDto createdUser2 = userService.create(userDto2);
        ItemDto createdItem = itemService.create(itemDto2, createdUser2);
        BookingOutputDto bookingOutputDto1 = bookingService.create(createdUser1, createdItem, bookingInputDto1);

        bookingService
                .approveByOwner(createdUser2.getId(), bookingOutputDto1.getId(), true);

        CommentDto createdComment = itemService.createComment(commentDto, createdUser1,
                createdItem, LocalDateTime.now().plusMinutes(10));

        Assertions.assertEquals(1L, createdComment.getId());
        Assertions.assertEquals(commentDto.getText(), createdComment.getText());
    }

    @Test
    void getItemByWrongItemId() {
        Long id = 2L;

        Assertions
                .assertThrows(NotFoundException.class, () -> itemService.getByItemIdAndUserId(id, id));
    }
}
