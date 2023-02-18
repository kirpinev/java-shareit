package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;


    @Override
    public UserDto create(UserDto userDto) {
        User user = userMapper.toUser(userDto);

        checkUserEmailIsNotUnique(user, user.getId());

        return userMapper.toUserDto(userDao.create(user));
    }

    @Override
    public List<UserDto> findAll() {
        return userDao.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long userId) {
        User user = userDao.findById(userId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Пользователя с id %s нет", userId));
        });

        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateById(UserDto userDto, Long userId) {
        User userFromMap = userMapper.toUser(findById(userId));
        User userFromDto = userMapper.toUser(userDto);
        String emailRegExp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        checkUserIsNotExists(userFromMap);
        checkUserEmailIsNotUnique(userFromDto, userId);

        if (userFromDto.getEmail() != null
                && userFromDto.getEmail().matches(emailRegExp)) {
            userFromMap.setEmail(userFromDto.getEmail());
        }

        if (userFromDto.getName() != null
                && !userFromDto.getName().isEmpty()
                && !userFromDto.getName().isBlank()) {
            userFromMap.setName(userFromDto.getName());
        }

        return userMapper.toUserDto(userDao.update(userFromMap, userId));
    }

    @Override
    public void delete(Long userId) {
        User userFromMap = userMapper.toUser(findById(userId));

        checkUserIsNotExists(userFromMap);

        userDao.delete(userId);
    }

    private void checkUserIsNotExists(User user) {
        if (user == null) {
            throw new NotFoundException("Такого пользователя нет");
        }
    }

    private void checkUserEmailIsNotUnique(User user, Long userId) {
        List<UserDto> userWithSameEmail = findAll()
                .stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .filter(u -> !Objects.equals(u.getId(), userId))
                .collect(Collectors.toList());

        if (!userWithSameEmail.isEmpty()) {
            throw new ConflictException("Пользователь с такой почтой уже есть");
        }
    }
}
