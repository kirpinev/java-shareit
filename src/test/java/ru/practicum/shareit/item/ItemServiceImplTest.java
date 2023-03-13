package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    private final UserDto userDto = new UserDto(
            1L,
            "Igor",
            "igor@gmail.dom");

    private final User user = new User(
            1L,
            "Igor",
            "igor@gmail.dom");

    private final Item item = new Item(
            1L,
            "Какая-то вещь",
            "Какое-то описание",
            true,
            1L,
            user);

    private final Comment comment = new Comment(
            1L,
            "Какой-то текст",
            item,
            user,
            LocalDateTime.now());

    private final CommentDto commentDto = new CommentDto(
            null,
            "Какой-то текст",
            "Igor",
            LocalDateTime.now());

    private final ItemDto itemDto = new ItemDto(
            1L,
            "Какая-то вещь",
            "Какое-то описание",
            true,
            1L,
            null,
            null,
            new ArrayList<>(),
            1L);


    private final Booking booking = new Booking(
            1L,
            LocalDateTime.now(),
            LocalDateTime.now(),
            item,
            user,
            Status.WAITING);

    private final BookingOutputDto bookingOutputDto = new BookingOutputDto(
            1L,
            LocalDateTime.now(),
            LocalDateTime.now(),
            Status.WAITING,
            userDto,
            itemDto);


    @Test
    void createItem() {
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto createdItem = itemService.create(itemDto, userDto);

        Assertions.assertNotNull(createdItem);
        Assertions.assertEquals(createdItem.getId(), 1);
        Assertions.assertEquals(createdItem.getName(), itemDto.getName());
        Assertions.assertEquals(createdItem.getDescription(), itemDto.getDescription());
        Assertions.assertTrue(createdItem.getAvailable());
        Assertions.assertEquals(createdItem.getOwnerId(), itemDto.getOwnerId());
        Assertions.assertNull(createdItem.getLastBooking());
        Assertions.assertNull(createdItem.getNextBooking());
        Assertions.assertEquals(createdItem.getComments().size(), itemDto.getComments().size());
        Assertions.assertEquals(createdItem.getRequestId(), itemDto.getRequestId());

        verify(itemRepository, times(1)).save(any(Item.class));
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getByItemIdAndUserId() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.getLastBooking(anyLong(), any())).thenReturn(Optional.of(booking));
        when(bookingRepository.getNextBooking(anyLong(), any())).thenReturn(Optional.of(booking));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment));

        ItemDto itemById = itemService.getByItemIdAndUserId(1L, 1L);

        Assertions.assertNotNull(itemById);
        Assertions.assertEquals(itemById.getId(), 1);
        Assertions.assertEquals(itemById.getName(), itemDto.getName());
        Assertions.assertEquals(itemById.getDescription(), itemDto.getDescription());
        Assertions.assertTrue(itemById.getAvailable());
        Assertions.assertEquals(itemById.getOwnerId(), itemDto.getOwnerId());
        Assertions.assertEquals(itemById.getLastBooking().getId(), bookingOutputDto.getId());
        Assertions.assertEquals(itemById.getNextBooking().getId(), bookingOutputDto.getId());
        Assertions.assertEquals(itemById.getComments().size(), 1);
        Assertions.assertEquals(itemById.getRequestId(), itemDto.getRequestId());

        verify(itemRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(itemRepository);
        verify(commentRepository, times(1)).findAllByItemId(anyLong());
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void getByItemIdAndUserIdWithoutBookings() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment));

        ItemDto itemById = itemService.getByItemIdAndUserId(1L, 2L);

        Assertions.assertNotNull(itemById);
        Assertions.assertEquals(itemById.getId(), 1);
        Assertions.assertEquals(itemById.getName(), itemDto.getName());
        Assertions.assertEquals(itemById.getDescription(), itemDto.getDescription());
        Assertions.assertTrue(itemById.getAvailable());
        Assertions.assertEquals(itemById.getOwnerId(), itemDto.getOwnerId());
        Assertions.assertNull(itemById.getLastBooking());
        Assertions.assertNull(itemById.getNextBooking());
        Assertions.assertEquals(itemById.getComments().size(), 1);
        Assertions.assertEquals(itemById.getRequestId(), itemDto.getRequestId());

        verify(itemRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(itemRepository);
        verify(commentRepository, times(1)).findAllByItemId(anyLong());
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void getAllItemsByUserId() {
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyLong())).thenReturn(List.of(item));
        when(bookingRepository.getLastBooking(anyLong(), any())).thenReturn(Optional.of(booking));
        when(bookingRepository.getNextBooking(anyLong(), any())).thenReturn(Optional.of(booking));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment));

        List<ItemDto> items = itemService.getAllByUserId(1L);

        Assertions.assertEquals(items.size(), 1);
        Assertions.assertEquals(items.get(0).getId(), 1);
        Assertions.assertEquals(items.get(0).getName(), itemDto.getName());
        Assertions.assertEquals(items.get(0).getDescription(), itemDto.getDescription());
        Assertions.assertTrue(items.get(0).getAvailable());
        Assertions.assertEquals(items.get(0).getOwnerId(), itemDto.getOwnerId());
        Assertions.assertEquals(items.get(0).getLastBooking().getId(), bookingOutputDto.getId());
        Assertions.assertEquals(items.get(0).getNextBooking().getId(), bookingOutputDto.getId());
        Assertions.assertEquals(items.get(0).getComments().size(), 1);
        Assertions.assertEquals(items.get(0).getRequestId(), itemDto.getRequestId());

        verify(itemRepository, times(1)).findAllByOwnerIdOrderByIdAsc(anyLong());
        verifyNoMoreInteractions(itemRepository);
        verify(bookingRepository, times(1)).getLastBooking(anyLong(), any(LocalDateTime.class));
        verify(bookingRepository, times(1)).getNextBooking(anyLong(), any(LocalDateTime.class));
        verifyNoMoreInteractions(bookingRepository);
        verify(commentRepository, times(1)).findAllByItemId(anyLong());
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void getAllItemsByRequestId() {
        when(itemRepository.findAllByRequestIdOrderByIdAsc(anyLong())).thenReturn(List.of(item));
        when(bookingRepository.getLastBooking(anyLong(), any())).thenReturn(Optional.of(booking));
        when(bookingRepository.getNextBooking(anyLong(), any())).thenReturn(Optional.of(booking));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment));

        List<ItemDto> items = itemService.getAllByRequestId(1L);

        Assertions.assertEquals(items.size(), 1);
        Assertions.assertEquals(items.get(0).getId(), 1);
        Assertions.assertEquals(items.get(0).getName(), itemDto.getName());
        Assertions.assertEquals(items.get(0).getDescription(), itemDto.getDescription());
        Assertions.assertTrue(items.get(0).getAvailable());
        Assertions.assertEquals(items.get(0).getOwnerId(), itemDto.getOwnerId());
        Assertions.assertEquals(items.get(0).getLastBooking().getId(), bookingOutputDto.getId());
        Assertions.assertEquals(items.get(0).getNextBooking().getId(), bookingOutputDto.getId());
        Assertions.assertEquals(items.get(0).getComments().size(), 1);
        Assertions.assertEquals(items.get(0).getRequestId(), itemDto.getRequestId());

        verify(itemRepository, times(1)).findAllByRequestIdOrderByIdAsc(anyLong());
        verifyNoMoreInteractions(itemRepository);
        verify(bookingRepository, times(1)).getLastBooking(anyLong(), any(LocalDateTime.class));
        verify(bookingRepository, times(1)).getNextBooking(anyLong(), any(LocalDateTime.class));
        verifyNoMoreInteractions(bookingRepository);
        verify(commentRepository, times(1)).findAllByItemId(anyLong());
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void getAllItemsByText() {
        when(itemRepository.search(anyString())).thenReturn(List.of(item));
        when(bookingRepository.getLastBooking(anyLong(), any())).thenReturn(Optional.of(booking));
        when(bookingRepository.getNextBooking(anyLong(), any())).thenReturn(Optional.of(booking));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment));

        List<ItemDto> items = itemService.getAllByText("Hello");

        Assertions.assertEquals(items.size(), 1);
        Assertions.assertEquals(items.get(0).getId(), 1);
        Assertions.assertEquals(items.get(0).getName(), itemDto.getName());
        Assertions.assertEquals(items.get(0).getDescription(), itemDto.getDescription());
        Assertions.assertTrue(items.get(0).getAvailable());
        Assertions.assertEquals(items.get(0).getOwnerId(), itemDto.getOwnerId());
        Assertions.assertEquals(items.get(0).getLastBooking().getId(), bookingOutputDto.getId());
        Assertions.assertEquals(items.get(0).getNextBooking().getId(), bookingOutputDto.getId());
        Assertions.assertEquals(items.get(0).getComments().size(), 1);
        Assertions.assertEquals(items.get(0).getRequestId(), itemDto.getRequestId());

        verify(itemRepository, times(1)).search(anyString());
        verifyNoMoreInteractions(itemRepository);
        verify(bookingRepository, times(1)).getLastBooking(anyLong(), any(LocalDateTime.class));
        verify(bookingRepository, times(1)).getNextBooking(anyLong(), any(LocalDateTime.class));
        verifyNoMoreInteractions(bookingRepository);
        verify(commentRepository, times(1)).findAllByItemId(anyLong());
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void getAllItemsByEmptyText() {
        List<ItemDto> items = itemService.getAllByText("");

        Assertions.assertEquals(items.size(), 0);
    }

    @Test
    void updateItem() {
        final Item newItem = new Item(
                1L,
                "Какая-то обновленная вещь",
                "Какое-то описание",
                true,
                1L,
                user);

        when(itemRepository.findByOwnerIdAndId(anyLong(), anyLong())).thenReturn(item);
        when(itemRepository.save(any())).thenReturn(newItem);
        when(bookingRepository.getLastBooking(anyLong(), any())).thenReturn(Optional.of(booking));
        when(bookingRepository.getNextBooking(anyLong(), any())).thenReturn(Optional.of(booking));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment));

        ItemDto updatedItem = itemService.update(userDto, 1L, itemDto);

        Assertions.assertNotNull(updatedItem);
        Assertions.assertEquals(updatedItem.getId(), 1);
        Assertions.assertEquals(updatedItem.getName(), newItem.getName());
        Assertions.assertEquals(updatedItem.getDescription(), itemDto.getDescription());
        Assertions.assertTrue(updatedItem.getAvailable());
        Assertions.assertEquals(updatedItem.getOwnerId(), itemDto.getOwnerId());
        Assertions.assertEquals(updatedItem.getLastBooking().getId(), bookingOutputDto.getId());
        Assertions.assertEquals(updatedItem.getNextBooking().getId(), bookingOutputDto.getId());
        Assertions.assertEquals(updatedItem.getComments().size(), 1);
        Assertions.assertEquals(updatedItem.getRequestId(), itemDto.getRequestId());

        verify(itemRepository, times(1)).findByOwnerIdAndId(anyLong(), anyLong());
        verify(itemRepository, times(1)).save(any());
        verifyNoMoreInteractions(itemRepository);
        verify(bookingRepository, times(1)).getLastBooking(anyLong(), any(LocalDateTime.class));
        verify(bookingRepository, times(1)).getNextBooking(anyLong(), any(LocalDateTime.class));
        verifyNoMoreInteractions(bookingRepository);
        verify(commentRepository, times(1)).findAllByItemId(anyLong());
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void updateWithNotFoundItem() {
        Long itemId = 1L;

        when(itemRepository.findByOwnerIdAndId(anyLong(), anyLong())).thenReturn(null);

        final NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> itemService.update(userDto, itemId, itemDto));

        Assertions.assertEquals(String.format("Вещи с id %s нет", itemId), exception.getMessage());
    }

    @Test
    void createComment() {
        when(bookingRepository.getAllUserBookings(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto createdComment = itemService.createComment(commentDto, userDto, itemDto);

        Assertions.assertNotNull(createdComment);
        Assertions.assertEquals(createdComment.getText(), comment.getText());
        Assertions.assertEquals(createdComment.getId(), comment.getId());
        Assertions.assertEquals(createdComment.getCreated().toString(), comment.getCreated().toString());
        Assertions.assertEquals(createdComment.getAuthorName(), comment.getAuthor().getName());

        verify(bookingRepository, times(1))
                .getAllUserBookings(anyLong(), anyLong(), any(LocalDateTime.class));
        verifyNoMoreInteractions(bookingRepository);
        verify(commentRepository, times(1)).save(any(Comment.class));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void createCommentWithEmptyBookings() {
        when(bookingRepository.getAllUserBookings(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        final BadRequestException exception = Assertions.assertThrows(BadRequestException.class,
                () -> itemService.createComment(commentDto, userDto, itemDto));

        Assertions.assertEquals("Чтобы оставить комментарий нужно сначала оформить бронирование",
                exception.getMessage());
    }
}
