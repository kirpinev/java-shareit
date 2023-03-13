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
import ru.practicum.shareit.exception.IncorrectStateException;
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
            Status.APPROVED,
            userDto,
            itemDto);

    private final BookingInputDto bookingInputDto = new BookingInputDto(
            1L,
            start,
            end);
    private final BookingInputDto bookingInputWrongEndDateDto = new BookingInputDto(
            1L,
            start,
            end.minusDays(2));

    private final BookingInputDto bookingInputWringStartDateDto = new BookingInputDto(
            1L,
            start.minusDays(2),
            end);

    private final BookingInputDto bookingInputEqualDatesDto = new BookingInputDto(
            1L,
            start,
            start);

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
        Assertions.assertEquals(createdBooking.getId(), bookingOutputDto.getId());
        Assertions.assertEquals(createdBooking.getStart(), bookingOutputDto.getStart());
        Assertions.assertEquals(createdBooking.getEnd(), bookingOutputDto.getEnd());
        Assertions.assertEquals(createdBooking.getItem().getId(), bookingOutputDto.getItem().getId());
        Assertions.assertEquals(createdBooking.getBooker().getId(), bookingOutputDto.getBooker().getId());
        Assertions.assertEquals(createdBooking.getStatus().toString(), bookingOutputDto.getStatus().toString());
    }

    @Test
    void createBookingUnavailable() {
        final BadRequestException exception = Assertions.assertThrows(BadRequestException.class,
                () -> bookingService.create(userDto, itemDtoUnavailable, bookingInputDto));

        Assertions.assertEquals("Вещь недоступна для бронирования", exception.getMessage());
    }

    @Test
    void createBookingWrongEndDate() {
        final BadRequestException exception = Assertions.assertThrows(BadRequestException.class,
                () -> bookingService.create(userDto, itemDto, bookingInputWrongEndDateDto));

        Assertions.assertEquals("Дата окончания не может быть раньше даты начала", exception.getMessage());
    }

    @Test
    void createBookingWrongStartDate() {
        final BadRequestException exception = Assertions.assertThrows(BadRequestException.class,
                () -> bookingService.create(userDto, itemDto, bookingInputWringStartDateDto));

        Assertions.assertEquals("Дата начала не может быть раньше текущей даты", exception.getMessage());
    }

    @Test
    void createBookingEqualDates() {
        final BadRequestException exception = Assertions.assertThrows(BadRequestException.class,
                () -> bookingService.create(userDto, itemDto, bookingInputEqualDatesDto));

        Assertions.assertEquals("Дата начала не может быть равна дате окончания", exception.getMessage());
    }

    @Test
    void createBookingEqualOwnerIds() {
        final NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.create(userDto, itemEqualOwnerIdDto, bookingInputDto));

        Assertions.assertEquals("Такой вещи нет", exception.getMessage());
    }

    @Test
    void approvedByOwner() {
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingWaiting);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingWaiting));

        BookingOutputDto approvedBooking = bookingService.approveByOwner(1L, 1L, true);

        Assertions.assertNotNull(approvedBooking);
        Assertions.assertEquals(approvedBooking.getId(), bookingOutputDto.getId());
        Assertions.assertEquals(approvedBooking.getStart(), bookingOutputDto.getStart());
        Assertions.assertEquals(approvedBooking.getEnd(), bookingOutputDto.getEnd());
        Assertions.assertEquals(approvedBooking.getItem().getId(), bookingOutputDto.getItem().getId());
        Assertions.assertEquals(approvedBooking.getBooker().getId(), bookingOutputDto.getBooker().getId());
        Assertions.assertEquals(approvedBooking.getStatus().toString(), bookingOutputDto.getStatus().toString());
    }

    @Test
    void approvedByOwnerStatusApproved() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        final BadRequestException exception = Assertions.assertThrows(BadRequestException.class,
                () -> bookingService.approveByOwner(1L, 1L, true));

        Assertions.assertEquals("Статус уже поставлен", exception.getMessage());
    }

    @Test
    void approvedByOwnerNoItem() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        final NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.approveByOwner(2L, 1L, true));

        Assertions.assertEquals("У пользователя нет такой вещи", exception.getMessage());
    }

    @Test
    void getBookingByBookingIdAndUserId() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingOutputDto bookingOutput = bookingService.getBookingByIdAndUser(1L, 1L);

        Assertions.assertNotNull(bookingOutput);
        Assertions.assertEquals(bookingOutput.getId(), bookingOutputDto.getId());
        Assertions.assertEquals(bookingOutput.getStart(), bookingOutputDto.getStart());
        Assertions.assertEquals(bookingOutput.getEnd(), bookingOutputDto.getEnd());
        Assertions.assertEquals(bookingOutput.getItem().getId(), bookingOutputDto.getItem().getId());
        Assertions.assertEquals(bookingOutput.getBooker().getId(), bookingOutputDto.getBooker().getId());
        Assertions.assertEquals(bookingOutput.getStatus().toString(), bookingOutputDto.getStatus().toString());
    }

    @Test
    void getBookingByBookingIdAndUserIdWrongUserId() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        final NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.getBookingByIdAndUser(1L, 2L));

        Assertions.assertEquals("Такого бронирования нет", exception.getMessage());
    }

    @Test
    void findAllByBooker() {
        when(bookingRepository.getAllBookingsByBookerId(1L, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookings = bookingService.findAllByBooker(1L, State.ALL, 0, 1);

        Assertions.assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllCurrentByBooker() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllCurrentBookingsByBookerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByBooker(1L, State.CURRENT, 0, 1);

        Assertions.assertEquals(bookingsCurrent.size(), 1);

    }

    @Test
    void findAllWaitingByBooker() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllWaitingBookingsByBookerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByBooker(1L, State.WAITING, 0, 1);

        Assertions.assertEquals(bookingsCurrent.size(), 1);

    }

    @Test
    void findAllPastByBooker() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllPastBookingsByBookerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByBooker(1L, State.PAST, 0, 1);

        Assertions.assertEquals(bookingsCurrent.size(), 1);

    }

    @Test
    void findAllFutureByBooker() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllFutureBookingsByBookerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByBooker(1L, State.FUTURE, 0, 1);

        Assertions.assertEquals(bookingsCurrent.size(), 1);

    }

    @Test
    void findAllRejectedByBooker() {
        when(bookingRepository.getAllRejectedBookingsByBookerId(1L, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByBooker(1L, State.REJECTED, 0, 1);

        Assertions.assertEquals(bookingsCurrent.size(), 1);

    }

    @Test
    void findAllByBookerWrongState() {
        final IncorrectStateException exception = Assertions.assertThrows(IncorrectStateException.class,
                () -> bookingService.findAllByBooker(1L, State.UNKNOWN, 0, 1));

        Assertions.assertEquals("Unknown state: " + State.UNKNOWN, exception.getMessage());
    }

    @Test
    void findAllByOwner() {
        when(bookingRepository.getAllBookingsByOwnerId(1L, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookings = bookingService.findAllByOwner(1L, State.ALL, 0, 1);

        Assertions.assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllCurrentByOwner() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllCurrentBookingsByOwnerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByOwner(1L, State.CURRENT, 0, 1);

        Assertions.assertEquals(bookingsCurrent.size(), 1);

    }

    @Test
    void findAllWaitingByOwner() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllWaitingBookingsByOwnerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByOwner(1L, State.WAITING, 0, 1);

        Assertions.assertEquals(bookingsCurrent.size(), 1);

    }

    @Test
    void findAllPastByOwner() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllPastBookingsByOwnerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByOwner(1L, State.PAST, 0, 1);

        Assertions.assertEquals(bookingsCurrent.size(), 1);

    }

    @Test
    void findAllFutureByOwner() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        when(bookingRepository.getAllFutureBookingsByOwnerId(1L, currentTime, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByOwner(1L, State.FUTURE, 0, 1);

        Assertions.assertEquals(bookingsCurrent.size(), 1);

    }

    @Test
    void findAllRejectedByOwner() {
        when(bookingRepository.getAllRejectedBookingsByOwnerId(1L, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));

        List<BookingOutputDto> bookingsCurrent = bookingService.findAllByOwner(1L, State.REJECTED, 0, 1);

        Assertions.assertEquals(bookingsCurrent.size(), 1);

    }

    @Test
    void findAllByOwnerWrongState() {
        final IncorrectStateException exception = Assertions.assertThrows(IncorrectStateException.class,
                () -> bookingService.findAllByOwner(1L, State.UNKNOWN, 0, 1));

        Assertions.assertEquals("Unknown state: " + State.UNKNOWN, exception.getMessage());
    }
}
