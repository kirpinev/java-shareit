package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public User create(User user) {
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
    public User update(User user) {
        return userDao.update(user);
    }

    @Override
    public void delete(Long userId) {
        userDao.delete(userId);
    }
}
