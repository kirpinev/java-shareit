package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    User create(User user);

    List<User> getAll();

    User getEntityById(Long userId);

    User update(User user);

    void delete(Long userId);
}
