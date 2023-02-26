package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = userMapper.toUser(userDto);

        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userMapper.toUserDto(userRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Пользователя с id %s нет", userId));
        });

        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateById(UserDto userDto, Long userId) {
        User userFromMap = userMapper.toUser(findById(userId));
        User userFromDto = userMapper.toUser(userDto);

        userFromMap.setName(Objects.requireNonNullElse(userFromDto.getName(), userFromMap.getName()));
        userFromMap.setEmail(Objects.requireNonNullElse(userFromDto.getEmail(), userFromMap.getEmail()));

        return userMapper.toUserDto(userRepository.save(userFromMap));
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}
