package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addUser(@Valid @RequestBody NewUserRequest newUser) {
        return userClient.addUser(newUser);
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<Object> findUser(@PathVariable("user-id") @Positive Long userId) {
        return userClient.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        return userClient.getUsers();
    }

    @PatchMapping("/{user-id}")
    public ResponseEntity<Object> updateUser(@PathVariable("user-id") @Positive Long userId,
                                             @Valid @RequestBody UpdateUserRequest newUser) {
        return userClient.updateUser(userId, newUser);
    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("user-id") @Positive Long userId) {
        return userClient.deleteUser(userId);
    }
}
