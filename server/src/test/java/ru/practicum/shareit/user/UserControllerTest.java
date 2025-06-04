package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mvc;

    private final String urlTemplate = "/users";

    private UserDto makeUserDto(Long id, String email, String name) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setEmail(email);
        dto.setName(name);

        return dto;
    }

    @Test
    void createUserTest() throws Exception {
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe");

        when(userService.create(any())).thenReturn(userDto);

        mvc.perform(post(urlTemplate)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class))
                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class));
    }

    @Test
    void findUserTest() throws Exception {
        UserDto findUser = makeUserDto(1L, "ivan@email", "Ivan Ivanov");

        when(userService.findUser(anyLong())).thenReturn(findUser);

        mvc.perform(get(urlTemplate + "/" + findUser.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(findUser.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(findUser.getEmail()), String.class))
                .andExpect(jsonPath("$.name", is(findUser.getName()), String.class));

    }

    @Test
    void getUsersTest() throws Exception {
        List<UserDto> newUsers = List.of(
                makeUserDto(1L, "ivan@email", "Ivan Ivanov"),
                makeUserDto(2L, "petr@email", "Petr Petrov"));

        when(userService.getUsers()).thenReturn(newUsers);

        mvc.perform(get(urlTemplate)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(is(newUsers.getFirst().getId()), Long.class))
                .andExpect(jsonPath("$[0].email").value(is(newUsers.getFirst().getEmail())))
                .andExpect(jsonPath("$[0].name").value(is(newUsers.getFirst().getName())))
                .andExpect(jsonPath("$[1].id").value(is(newUsers.getLast().getId()), Long.class))
                .andExpect(jsonPath("$[1].email").value(is(newUsers.getLast().getEmail())))
                .andExpect(jsonPath("$[1].name").value(is(newUsers.getLast().getName())));
    }

    @Test
    void updateTest() throws Exception {
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe");

        when(userService.update(anyLong(), any())).thenReturn(userDto);

        mvc.perform(patch(urlTemplate + "/" + userDto.getId())
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class))
                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class));
    }

    @Test
    void deleteTest() throws Exception {
        mvc.perform(delete(urlTemplate + "/" + anyLong()))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(anyLong());
    }
}
