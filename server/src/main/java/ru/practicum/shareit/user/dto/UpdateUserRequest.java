package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class UpdateUserRequest {
    private Long id;
    private String email;
    private String name;

    public boolean hasEmail() {
        return email != null && !email.trim().isEmpty();
    }

    public boolean hasName() {
        return name != null && !name.trim().isEmpty();
    }
}
