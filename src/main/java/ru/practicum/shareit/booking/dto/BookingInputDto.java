package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Value
@Jacksonized
public class BookingInputDto implements Serializable {
    @NotNull(groups = {Create.class})
    Long itemId;
    @NotNull(groups = {Create.class})
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime start;
    @NotNull(groups = {Create.class})
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime end;
}
