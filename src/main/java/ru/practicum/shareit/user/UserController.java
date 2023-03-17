package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.group.Create;
import ru.practicum.shareit.validation.group.Update;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@Validated({Create.class}) @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable("userId") Long userId) {
        return userService.findById(userId);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.findAll();
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Validated({Update.class}) @RequestBody UserDto userDto,
                          @PathVariable("userId") Long userId) {
        return userService.updateById(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") Long userId) {
        userService.delete(userId);
    }
}
