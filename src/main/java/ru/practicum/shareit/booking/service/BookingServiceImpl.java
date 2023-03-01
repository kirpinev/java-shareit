package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.IncorrectStateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private static final String BOOKING_NOT_FOUND_MESSAGE = "Бронирования с id %s нет";

    @Override
    @Transactional
    public BookingOutputDto create(UserDto userDto, ItemDto itemDto, BookingInputDto bookingInputDto) {
        if (!itemDto.getAvailable()) {
            throw new BadRequestException("Вещь недоступна для бронирования");
        }

        if (bookingInputDto.getEnd().isBefore(bookingInputDto.getStart())) {
            throw new BadRequestException("Дата окончания не может быть раньше даты начала");
        }

        if (bookingInputDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Дата начала не может быть раньше текущей даты");
        }

        if (Objects.equals(itemDto.getOwnerId(), userDto.getId())) {
            throw new NotFoundException("Такой вещи нет");
        }

        Booking booking = BookingMapper.toBooking(bookingInputDto, Status.WAITING, itemDto, userDto);

        return BookingMapper.toBookingCreatedDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingOutputDto approveByOwner(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            throw new NotFoundException(String.format(BOOKING_NOT_FOUND_MESSAGE, bookingId));
        });


        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new NotFoundException("У пользователя нет такой вещи");
        }

        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new BadRequestException("Статус уже поставлен");
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);

        return BookingMapper.toBookingCreatedDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingOutputDto getBookingByIdAndUser(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            throw new NotFoundException(String.format(BOOKING_NOT_FOUND_MESSAGE, bookingId));
        });

        if (!Objects.equals(booking.getBooker().getId(), userId)
                && !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new NotFoundException("Такого бронирования нет");
        }

        return BookingMapper.toBookingCreatedDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingOutputDto> findAllByBooker(Long bookerId, State state) {
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                return BookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllBookingsByBookerId(bookerId));
            case CURRENT:
                return BookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllCurrentBookingsByBookerId(bookerId, now));
            case WAITING:
                return BookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllWaitingBookingsByBookerId(bookerId, now));
            case PAST:
                return BookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllPastBookingsByBookerId(bookerId, now));
            case FUTURE:
                return BookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllFutureBookingsByBookerId(bookerId, now));
            case REJECTED:
                return BookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllRejectedBookingsByBookerId(bookerId));
            default:
                throw new IncorrectStateException("Unknown state: " + state);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingOutputDto> findAllByOwner(Long userId, State state) {
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                return BookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllBookingsByOwnerId(userId));
            case CURRENT:
                return BookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllCurrentBookingsByOwnerId(userId, now));
            case WAITING:
                return BookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllWaitingBookingsByOwnerId(userId, now));
            case PAST:
                return BookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllPastBookingsByOwnerId(userId, now));
            case FUTURE:
                return BookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllFutureBookingsByOwnerId(userId, now));
            case REJECTED:
                return BookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllRejectedBookingsByOwnerId(userId));
            default:
                throw new IncorrectStateException("Unknown state: " + state);
        }
    }
}
