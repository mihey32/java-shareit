package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public UserDto create(NewUserRequest request) {
        Optional<User> findUser = repository.findByEmail(request.getEmail());
        if (findUser.isPresent()) {
            throw new DuplicatedDataException("Этот E-mail " + request.getEmail() + " уже используется");
        }

        User user = UserMapper.mapToUser(request);
        user = repository.save(user);

        return UserMapper.mapToUserDto(user);
    }

    private User findById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c ID " + userId + " не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findUser(Long userId) {
        return UserMapper.mapToUserDto(findById(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<UserDto> getUsers() {
        return repository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto update(Long userId, UpdateUserRequest request) {

        if (userId == null) {
            throw new ValidationException("ID пользователя должен быть указан");
        }

        Optional<User> findUser = repository.findByEmail(request.getEmail());

        if (findUser.isPresent()) {
            throw new DuplicatedDataException("Этот E-mail " + request.getEmail() + " уже используется");
        }

        User updatedUser = UserMapper.updateUserFields(findById(userId), request);
        updatedUser = repository.save(updatedUser);

        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        User user = findById(userId);
        repository.delete(user);
    }
}
