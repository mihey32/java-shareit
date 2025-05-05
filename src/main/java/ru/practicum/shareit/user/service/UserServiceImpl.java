package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    @Override
    public UserDto create(NewUserRequest request) {
        if (userStorage.isUserWithEmailExist(request.getEmail())) {
            throw new DuplicatedDataException("Этот E-mail " + request.getEmail() + " уже используется");
        }

        User user = UserMapper.mapToUser(request);
        user = userStorage.createUser(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto findUser(Long userId) {
        return UserMapper.mapToUserDto(userStorage.findUser(userId));
    }

    @Override
    public Collection<UserDto> getUsers() {
        return userStorage.getAllUsers().stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto update(Long userId, UpdateUserRequest request) {
        /*if (userId == null) {
            throw new ValidationException("ID пользователя должен быть указан");
        }*/

        if (userStorage.isUserWithEmailExist(request.getEmail())) {
            throw new DuplicatedDataException("Этот E-mail "+ request.getEmail() + " уже используется");
        }

        User updatedUser = UserMapper.updateUserFields(userStorage.findUser(userId), request);
        updatedUser = userStorage.updateUser(updatedUser);

        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public boolean delete(Long userId) {
        User user = userStorage.findUser(userId);
        return userStorage.delete(userId);
    }
}
