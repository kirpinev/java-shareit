package ru.practicum.shareit.booking.dto;

import lombok.Value;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class BookingInputDto {
    @NotNull(groups = {Create.class})
    Long itemId;
    @NotNull(groups = {Create.class})
    LocalDateTime start;
    @NotNull(groups = {Create.class})
    LocalDateTime end;
}
