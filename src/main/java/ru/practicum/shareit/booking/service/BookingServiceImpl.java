package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
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
    private final BookingMapper bookingMapper;
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

        Booking booking = bookingMapper.toBooking(bookingInputDto, Status.WAITING, itemDto, userDto);

        return bookingMapper.toBookingCreatedDto(bookingRepository.save(booking));
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

        return bookingMapper.toBookingCreatedDto(bookingRepository.save(booking));
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

        return bookingMapper.toBookingCreatedDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingOutputDto> findAllByBooker(Long bookerId, State state) {
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                return bookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllBookingsByBookerId(bookerId));
            case CURRENT:
                return bookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllCurrentBookingsByBookerId(bookerId, now));
            case WAITING:
                return bookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllWaitingBookingsByBookerId(bookerId, now));
            case PAST:
                return bookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllPastBookingsByBookerId(bookerId, now));
            case FUTURE:
                return bookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllFutureBookingsByBookerId(bookerId, now));
            case REJECTED:
                return bookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllRejectedBookingsByBookerId(bookerId));
            default:
                throw new IncorrectStateException("Unknown state");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingOutputDto> findAllByOwner(Long userId, State state) {
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                return bookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllBookingsByOwnerId(userId));
            case CURRENT:
                return bookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllCurrentBookingsByOwnerId(userId, now));
            case WAITING:
                return bookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllWaitingBookingsByOwnerId(userId, now));
            case PAST:
                return bookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllPastBookingsByOwnerId(userId, now));
            case FUTURE:
                return bookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllFutureBookingsByOwnerId(userId, now));
            case REJECTED:
                return bookingMapper
                        .toBookingCreatedDto(bookingRepository
                                .getAllRejectedBookingsByOwnerId(userId));
            default:
                throw new IncorrectStateException("Unknown state");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BookingOutputDto findByUserIdAndBookingId(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findByBookerIdAndId(userId, bookingId);

        return bookingMapper.toBookingCreatedDto(booking);
    }
}
