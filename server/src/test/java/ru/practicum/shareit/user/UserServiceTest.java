package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testUserByIdWhenUserIdIsNull() {
        when((userRepository).findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findUser(2L));
    }

    @Test
    void testCreateUserWhenEMailExist() {
        NewUserRequest newUser = new NewUserRequest("john.doe@mail.com", "John Doe");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(new User(1L, "john.doe@mail.com", "Some User")));

        DuplicatedDataException thrown = assertThrows(DuplicatedDataException.class, () -> {
            userService.create(newUser);
        });

        assertEquals(String.format("Этот E-mail " + newUser.getEmail() + " уже используется"), thrown.getMessage());
    }

    @Test
    void testUpdateUserWhenUserWithSameEmail() {
        UpdateUserRequest newUser = new UpdateUserRequest(1L, "john.doe@mail.com", "John Doe");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(new User(1L, "john.doe@mail.com", "Some User")));

        DuplicatedDataException thrown = assertThrows(DuplicatedDataException.class, () -> {
            userService.update(1L, newUser);
        });

        assertEquals(String.format("Этот E-mail " + newUser.getEmail() + " уже используется"), thrown.getMessage());
    }

    @Test
    void testUpdateUserWhenUserIdIsNull() {
        UpdateUserRequest newUser = new UpdateUserRequest(1L, "john.doe@mail.com", "John Doe");

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userService.update(null, newUser);
        });

        assertEquals("ID пользователя должен быть указан", thrown.getMessage());
    }
}
