package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    @NotBlank
    private Long id;
    @NotBlank
    private String description;
    @NotNull
    private User requestor;
    @NotNull
    private LocalDate created;
}
