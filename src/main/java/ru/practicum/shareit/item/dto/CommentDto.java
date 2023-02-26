package ru.practicum.shareit.item.dto;

import lombok.Value;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Value
public class CommentDto {
    Long id;

    @NotBlank(groups = {Create.class})
    String text;

    String authorName;

    LocalDateTime created;
}
