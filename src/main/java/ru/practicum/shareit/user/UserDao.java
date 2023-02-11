package ru.practicum.shareit.user;


import java.util.List;
import java.util.Optional;

public interface UserDao {
    User create(User user);

    List<User> getAll();

    Optional<User> getEntityById(Long id);

    User update(User user);

    void delete(Long id);
}
