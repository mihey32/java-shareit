package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewUserRequest {
    @NotBlank(message = "E-mail должен быть указан")
    @Email(message = "Email должен быть в формате user@yandex.ru")
    private String email;
    @NotBlank(message = "Имя пользователя должно быть указано")
    private String name;
}
