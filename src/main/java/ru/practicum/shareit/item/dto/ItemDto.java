package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.*;

/**
 * TODO Sprint add-controllers.
 */
@Value
public class ItemDto {
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotNull
    Boolean available;
}
