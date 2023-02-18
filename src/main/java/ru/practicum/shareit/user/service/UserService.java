package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    List<User> getAll();

    User getEntityById(Long userId);

    User updateById(User user, Long userId);

    void delete(Long userId);
}
