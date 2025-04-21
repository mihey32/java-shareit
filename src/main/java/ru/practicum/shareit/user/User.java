package ru.practicum.shareit.user;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class User {
    private Long id;
    @NotBlank(message = "E-mail должен быть указан")
    @Email(message = "Email должен быть в формате user@yandex.ru")
    private String email;
    @NotBlank(message = "Имя пользователя должно быть указано")
    private String name;
}
