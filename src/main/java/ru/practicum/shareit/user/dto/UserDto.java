package ru.practicum.shareit.user.dto;

import lombok.Value;
import ru.practicum.shareit.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
public class UserDto {
    Long id;
    @NotBlank(groups = {Create.class})
    String name;
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            groups = {Create.class})
    @NotBlank(groups = {Create.class})
    String email;
}
