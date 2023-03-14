package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private final UserDto userDto = new UserDto(
            null,
            "Igor",
            "igor@gmail.dom");

    private final User user = new User(
            1L,
            "Igor",
            "igor@gmail.dom");

    @Test
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto createdUser = userService.create(userDto);

        Assertions.assertNotNull(createdUser);
        Assertions.assertEquals(1, createdUser.getId());
        Assertions.assertEquals(userDto.getName(), createdUser.getName());
        Assertions.assertEquals(userDto.getEmail(), createdUser.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> users = userService.findAll();

        Assertions.assertNotNull(users);
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(1, users.get(0).getId());
        Assertions.assertEquals(userDto.getName(), users.get(0).getName());
        Assertions.assertEquals(userDto.getEmail(), users.get(0).getEmail());

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findUserById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto user = userService.findById(1L);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals(userDto.getName(), user.getName());
        Assertions.assertEquals(userDto.getEmail(), user.getEmail());

        verify(userRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUserById() {
        UserDto newUpdatedUserDto = new UserDto(null, "Stas", "stas@nail.com");
        User newUpdatedUser = new User(1L, "Stas", "stas@nail.com");

        when(userRepository.save(any(User.class))).thenReturn(newUpdatedUser);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto updatedUser = userService.updateById(newUpdatedUserDto, 1L);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(1, updatedUser.getId());
        Assertions.assertEquals(newUpdatedUserDto.getName(), updatedUser.getName());
        Assertions.assertEquals(newUpdatedUserDto.getEmail(), updatedUser.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUserById() {
        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }
}
