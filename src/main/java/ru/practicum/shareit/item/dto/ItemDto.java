package ru.practicum.shareit.item.dto;

import lombok.Value;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Value
public class ItemDto {
    Long id;
    @NotBlank(groups = {Create.class})
    @Size(groups = {Create.class, Update.class}, min = 1)
    String name;
    @NotBlank(groups = {Create.class})
    @Size(groups = {Create.class, Update.class}, min = 1)
    String description;
    @NotNull(groups = {Create.class})
    Boolean available;
    Long ownerId;
    BookerInfoDto lastBooking;
    BookerInfoDto nextBooking;
    List<CommentDto> comments;
}
