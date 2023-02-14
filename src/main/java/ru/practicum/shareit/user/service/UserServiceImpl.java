package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public User create(User user) {
        checkUserEmailIsNotUnique(user, user.getId());

        return userDao.create(user);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public User getEntityById(Long userId) {
        return userDao.getEntityById(userId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Пользователя с id %s нет", userId));
        });
    }

    @Override
    public User updateById(User user, Long userId) {
        User userFromMap = getEntityById(userId);
        String emailRegExp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        checkUserIsNotExists(userFromMap);
        checkUserEmailIsNotUnique(user, userId);

        if (user.getEmail() != null
                && user.getEmail().matches(emailRegExp)) {
            userFromMap.setEmail(user.getEmail());
        }

        if (user.getName() != null
                && !user.getName().isEmpty() && !user.getName().isBlank()) {
            userFromMap.setName(user.getName());
        }

        return userDao.updateById(userFromMap, userId);
    }

    @Override
    public void delete(Long userId) {
        User user = getEntityById(userId);

        checkUserIsNotExists(user);

        userDao.delete(userId);
    }

    private void checkUserIsNotExists(User user) {
        if (user == null) {
            throw new NotFoundException("Такого пользователя нет");
        }
    }

    private void checkUserEmailIsNotUnique(User user, Long userId) {
        List<User> userWithSameEmail = getAll()
                .stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .filter(u -> !Objects.equals(u.getId(), userId))
                .collect(Collectors.toList());

        if (!userWithSameEmail.isEmpty()) {
            throw new ConflictException("Пользователь с такой почтой уже есть");
        }
    }
}
