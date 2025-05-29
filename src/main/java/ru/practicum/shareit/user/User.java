package ru.practicum.shareit.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "E-mail должен быть указан")
    @Email(message = "Email должен быть в формате user@yandex.ru")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @NotBlank(message = "Имя пользователя должно быть указано")
    @Column(name = "name", nullable = false)
    private String name;
}
