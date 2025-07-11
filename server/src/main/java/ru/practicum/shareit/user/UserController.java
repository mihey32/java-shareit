package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody NewUserRequest user) {
        return userService.create(user);
    }

    @GetMapping("/{user-id}")
    public UserDto findUser(@PathVariable("user-id") Long userId) {
        return userService.findUser(userId);
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PatchMapping("/{user-id}")
    public UserDto update(@PathVariable("user-id") Long userId,
                          @RequestBody UpdateUserRequest newUser) {
        return userService.update(userId, newUser);
    }

    @DeleteMapping("/{user-id}")
    public void delete(@PathVariable("user-id") Long userId) {
        userService.delete(userId);
    }
}
