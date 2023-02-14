package ru.practicum.shareit.user.dao;


import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User create(User user);

    List<User> getAll();

    Optional<User> getEntityById(Long id);

    User updateById(User user, Long userId);

    void delete(Long id);
}
