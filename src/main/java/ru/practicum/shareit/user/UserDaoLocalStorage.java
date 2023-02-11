package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserDaoLocalStorage implements UserDao {
    private final Map<Long, User> userMap = new HashMap<>();
    private Long userId = 1L;

    @Override
    public User create(User user) {
        User userFromMap = userMap.get(user.getId());

        checkUserEmailIsNotUnique(user);

        if (Objects.isNull(userFromMap)) {
            user.setId(userId);
            userId += 1;
            userMap.put(user.getId(), user);

            return user;
        }

        return null;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> getEntityById(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public User update(User user) {
        User userFromMap = userMap.get(user.getId());
        String emailRegExp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        checkUserIsNotExists(userFromMap);
        checkUserEmailIsNotUnique(user);

        if (Objects.nonNull(user.getEmail())
                && user.getEmail().matches(emailRegExp)) {
            userFromMap.setEmail(user.getEmail());
        }

        if (Objects.nonNull(user.getName())
                && !user.getName().isEmpty() && !user.getName().isBlank()) {
            userFromMap.setName(user.getName());
        }

        userMap.put(user.getId(), userFromMap);

        return userFromMap;
    }

    @Override
    public void delete(Long id) {
        User userFromMap = userMap.get(id);
        checkUserIsNotExists(userFromMap);
        userMap.remove(id);
    }

    private void checkUserEmailIsNotUnique(User user) {
        List<User> userWithSameEmail = userMap
                .values()
                .stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .filter(u -> !Objects.equals(u.getId(), user.getId()))
                .collect(Collectors.toList());

        if (!userWithSameEmail.isEmpty()) {
            throw new ConflictException("Пользователь с такой почтой уже есть");
        }
    }

    private void checkUserIsNotExists(User user) {
        if (Objects.isNull(user)) {
            throw new NotFoundException("Такого пользователя нет");
        }
    }
}
