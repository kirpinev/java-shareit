package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        User userFromDto = userMapper.toUser(userDto);
        User createdUser = userService.create(userFromDto);

        return userMapper.toUserDto(createdUser);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable("userId") Long userId) {
        return userMapper.toUserDto(userService.getEntityById(userId));
    }

    @GetMapping
    public List<UserDto> getAll() {
        List<User> userDtos = userService.getAll();

        return userDtos
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto,
                          @PathVariable("userId") Long userId) {
        userDto.setId(userId);
        User userFromDto = userMapper.toUser(userDto);
        User updatedUser = userService.update(userFromDto);

        return userMapper.toUserDto(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") Long userId) {
        userService.delete(userId);
    }
}
