package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    @NotBlank
    private Long id;
    @NotNull
    private LocalDate start;
    @NotNull
    private LocalDate end;
    @NotNull
    private Item item;
    @NotNull
    private User booker;
    @NotBlank
    private StatusEnum status;
}
