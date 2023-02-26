package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.BookerInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingMapper {

    public BookingOutputDto toBookingCreatedDto(Booking booking) {
        UserDto userDto = new UserDto(booking.getBooker().getId(),
                booking.getBooker().getName(),
                booking.getBooker().getEmail());
        ItemDto itemDto = new ItemDto(booking.getItem().getId(),
                booking.getItem().getName(), booking.getItem().getDescription(),
                booking.getItem().getAvailable(), booking.getItem().getOwner().getId(),
               null, null, new ArrayList<>());

        return new BookingOutputDto(booking.getId(),
                booking.getStart(), booking.getEnd(),
                booking.getStatus(), userDto, itemDto);
    }

    public List<BookingOutputDto> toBookingCreatedDto(List<Booking> booking) {
        return booking
                .stream()
                .map(this::toBookingCreatedDto)
                .collect(Collectors.toList());
    }

    public BookerInfoDto toBookingInfoDto(Booking booking) {
        return new BookerInfoDto(booking.getId(), booking.getBooker().getId(),
                booking.getStart(), booking.getEnd());
    }

    public Booking toBooking(BookingInputDto bookingInputDto,
                             Status status, ItemDto itemDto, UserDto userDto) {
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();
        User owner = new User();

        owner.setId(itemDto.getOwnerId());

        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);

        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        booking.setStart(bookingInputDto.getStart());
        booking.setEnd(bookingInputDto.getEnd());
        booking.setStatus(status);
        booking.setBooker(user);
        booking.setItem(item);

        return booking;
    }
}
