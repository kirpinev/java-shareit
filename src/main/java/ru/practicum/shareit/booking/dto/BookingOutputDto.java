package ru.practicum.shareit.booking.dto;

import lombok.Value;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Value
public class BookingOutputDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Status status;
    UserDto booker;
    ItemDto item;
}
