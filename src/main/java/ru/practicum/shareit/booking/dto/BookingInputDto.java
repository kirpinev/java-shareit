package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.validation.group.Create;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class BookingInputDto implements Serializable {
    @NotNull(groups = {Create.class})
    Long itemId;
    @NotNull(groups = {Create.class})
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    LocalDateTime start;
    @NotNull(groups = {Create.class})
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    LocalDateTime end;
}
