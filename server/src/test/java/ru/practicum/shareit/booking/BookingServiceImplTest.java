package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.service.State;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    private final UserDto userDto = new UserDto(
            1L,
            "Igor",
            "igor@gmail.dom");

    private final ItemDto itemDto = new ItemDto(
            1L,
            "Какая-то вещь",
            "Какое-то описание",
            true,
            2L,
            null,
            null,
            new ArrayList<>(),
            1L);

    private final ItemDto itemEqualOwnerIdDto = new ItemDto(
            1L,
            "Какая-то вещь",
            "Какое-то описание",
            true,
            1L,
            null,
            null,
            new ArrayList<>(),
            1L);

    private final ItemDto itemDtoUnavailable = new ItemDto(
            1L,
            "Какая-то вещь",
            "Какое-то описание",
            false,
            2L,
            null,
            null,
            new ArrayList<>(),
            1L);

    private final LocalDateTime start = LocalDateTime.now().plusMinutes(1);
    private final LocalDateTime end = LocalDateTime.now().plusDays(1);

    private final BookingOutputDto bookingOutputDto = new BookingOutputDto(
            1L,
            start,
            end,
            Status.APPROVED.name(),
            userDto,
            itemDto);

    private final BookingInputDto bookingInputDto = new BookingInputDto(
            1L,
            start,
            end);

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
            user,
            new ArrayList<>());

    private final Booking booking = new Booking(1L,
            start,
            end,
            item,
            user,
            Status.APPROVED);

    private final Booking bookingWaiting = new Booking(1L,
            start,
            end,
            item,
            user,
            Status.WAITING);

    @Test
    void createBooking() {
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingOutputDto createdBooking = bookingService.create(userDto, itemDto, bookingInputDto);

        Assertions.assertNotNull(createdBooking);
        Assertions.assertEquals(bookingOutputDto.getId(), createdBooking.getId());
        Assertions.assertEquals(bookingOutputDto.getStart(), createdBooking.getStart());
        Assertions.assertEquals(bookingOutputDto.getEnd(), createdBooking.getEnd());
        Assertions.assertEquals(bookingOutputDto.getItem().getId(), createdBooking.getItem().getId());
        Assertions.assertEquals(bookingOutputDto.getBooker().getId(), createdBooking.getBooker().getId());
        Assertions.assertEquals(bookingOutputDto.getStatus(), createdBooking.getStatus());
    }

    @Test
    void createBookingUnavailable() {
        Assertions.assertThrows(BadRequestException.class,
                () -> bookingService.create(userDto, itemDtoUnavailable, bookingInputDto));
    }

    @Test
    void createBookingEqualOwnerIds() {
        Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.create(userDto, itemEqualOwnerIdDto, bookingInputDto));
    }

    @Test
    void approvedByOwner() {
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingWaiting);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingWaiting));

        BookingOutputDto approvedBooking = bookingService.approveByOwner(1L, 1L, true);

        Assertions.assertNotNull(approvedBooking);
        Assertions.assertEquals(bookingOutputDto.getId(), approvedBooking.getId());
        Assertions.assertEquals(bookingOutputDto.getStart(), approvedBooking.getStart());
        Assertions.assertEquals(bookingOutputDto.getEnd(), approvedBooking.getEnd());
        Assertions.assertEquals(bookingOutputDto.getItem().getId(), approvedBooking.getItem().getId());
        Assertions.assertEquals(bookingOutputDto.getBooker().getId(), approvedBooking.getBooker().getId());
        Assertions.assertEquals(bookingOutputDto.getStatus(), approvedBooking.getStatus());
    }

    @Test
    void approvedByOwnerStatusApproved() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Assertions.assertThrows(BadRequestException.class,
                () -> bookingService.approveByOwner(1L, 1L, true));
    }

    @Test
    void approvedByOwnerNoItem() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.approveByOwner(2L, 1L, true));
    }

    @Test
    void getBookingByBookingIdAndUserId() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingOutputDto bookingOutput = bookingService.getBookingByIdAndUser(1L, 1L);

        Assertions.assertNotNull(bookingOutput);
        Assertions.assertEquals(bookingOutputDto.getId(), bookingOutput.getId());
        Assertions.assertEquals(bookingOutputDto.getStart(), bookingOutput.getStart());
        Assertions.assertEquals(bookingOutputDto.getEnd(), bookingOutput.getEnd());
        Assertions.assertEquals(bookingOutputDto.getItem().getId(), bookingOutput.getItem().getId());
        Assertions.assertEquals(bookingOutputDto.getBooker().getId(), bookingOutput.getBooker().getId());
        Assertions.assertEquals(bookingOutputDto.getStatus(), bookingOutput.getStatus());
    }

    @Test
    void getBookingByBookingIdAndUserIdWrongUserId() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.getBookingByIdAndUser(1L, 2L));
    }

    @Test
    void findAllByBooker() {
        when(bookingRepository.getAllBookingsByBookerId(1L, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookings = bookingService.findAllByBooker(1L, State.ALL, 0, 1);

        Assertions.assertEquals(1, bookings.size());
    }

    @Test
    void findAllCurrentByBooker() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllCurrentBookingsByBookerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByBooker(1L, State.CURRENT, 0, 1);

        Assertions.assertEquals(1, bookingsCurrent.size());

    }

    @Test
    void findAllWaitingByBooker() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllWaitingBookingsByBookerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByBooker(1L, State.WAITING, 0, 1);

        Assertions.assertEquals(1, bookingsCurrent.size());

    }

    @Test
    void findAllPastByBooker() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllPastBookingsByBookerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByBooker(1L, State.PAST, 0, 1);

        Assertions.assertEquals(1, bookingsCurrent.size());

    }

    @Test
    void findAllFutureByBooker() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllFutureBookingsByBookerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByBooker(1L, State.FUTURE, 0, 1);

        Assertions.assertEquals(1, bookingsCurrent.size());

    }

    @Test
    void findAllRejectedByBooker() {
        when(bookingRepository.getAllRejectedBookingsByBookerId(1L, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByBooker(1L, State.REJECTED, 0, 1);

        Assertions.assertEquals(1, bookingsCurrent.size());

    }

    @Test
    void findAllByOwner() {
        when(bookingRepository.getAllBookingsByOwnerId(1L, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookings = bookingService.findAllByOwner(1L, State.ALL, 0, 1);

        Assertions.assertEquals(1, bookings.size());
    }

    @Test
    void findAllCurrentByOwner() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllCurrentBookingsByOwnerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByOwner(1L, State.CURRENT, 0, 1);

        Assertions.assertEquals(1, bookingsCurrent.size());

    }

    @Test
    void findAllWaitingByOwner() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllWaitingBookingsByOwnerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByOwner(1L, State.WAITING, 0, 1);

        Assertions.assertEquals(1, bookingsCurrent.size());

    }

    @Test
    void findAllPastByOwner() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllPastBookingsByOwnerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByOwner(1L, State.PAST, 0, 1);

        Assertions.assertEquals(1, bookingsCurrent.size());

    }

    @Test
    void findAllFutureByOwner() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllFutureBookingsByOwnerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByOwner(1L, State.FUTURE, 0, 1);

        Assertions.assertEquals(1, bookingsCurrent.size());

    }

    @Test
    void findAllRejectedByOwner() {
        when(bookingRepository.getAllRejectedBookingsByOwnerId(1L, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByOwner(1L, State.REJECTED, 0, 1);

        Assertions.assertEquals(1, bookingsCurrent.size());

    }
}
