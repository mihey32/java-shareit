package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserMapperTest {
    private final NewUserRequest newUser = new NewUserRequest("john.doe@mail.com", "John Doe");
    private final UpdateUserRequest updUser = new UpdateUserRequest(1L, "john.doe@mail.com", "John Doe");
    private final User user = new User(1L, "john.doe@mail.com", "John Doe");
    private final UserDto dto = new UserDto(1L, "john.doe@mail.com", "John Doe");

    private final UpdateUserRequest emptyUpdUser = new UpdateUserRequest(1L, "", "");

    @Test
    public void toUserDtoTest() {
        UserDto userDto = UserMapper.mapToUserDto(user);
        assertThat(userDto, equalTo(dto));
    }

    @Test
    public void toUserTest() {
        User us = UserMapper.mapToUser(newUser);
        assertThat(us.getName(), equalTo(user.getName()));
        assertThat(us.getEmail(), equalTo(user.getEmail()));
        assertThat(us.getName(), equalTo(user.getName()));
    }

    @Test
    public void updateUserFieldsTest() {
        User us = UserMapper.updateUserFields(user, updUser);
        assertThat(us.getId(), equalTo(user.getId()));
        assertThat(us.getName(), equalTo(user.getName()));
        assertThat(us.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    public void updateUserEmptyFieldsTest() {
        User us = UserMapper.updateUserFields(user, emptyUpdUser);
        assertThat(us.getId(), equalTo(user.getId()));
        assertThat(us.getName(), equalTo(user.getName()));
        assertThat(us.getEmail(), equalTo(user.getEmail()));
    }
}
