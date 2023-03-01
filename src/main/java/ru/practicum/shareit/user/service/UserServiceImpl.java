package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);

        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return UserMapper.toUserDto(userRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Пользователя с id %s нет", userId));
        });

        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateById(UserDto userDto, Long userId) {
        User userFromMap = UserMapper.toUser(findById(userId));
        User userFromDto = UserMapper.toUser(userDto);

        userFromMap.setName(Objects.requireNonNullElse(userFromDto.getName(), userFromMap.getName()));
        userFromMap.setEmail(Objects.requireNonNullElse(userFromDto.getEmail(), userFromMap.getEmail()));

        return UserMapper.toUserDto(userRepository.save(userFromMap));
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}
