package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody NewUserRequest user) {
        return userService.create(user);
    }

    @GetMapping("/{user-id}")
    public UserDto findUser(@PathVariable("user-id") @Positive Long userId) {
        return userService.findUser(userId);
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PatchMapping("/{user-id}")
    public UserDto update(@PathVariable("user-id") @Positive Long userId,
                          @Valid @RequestBody UpdateUserRequest newUser) {
        return userService.update(userId, newUser);
    }

    @DeleteMapping("/{user-id}")
    public void delete(@PathVariable("user-id") @Positive Long userId) {
        userService.delete(userId);
    }
}
