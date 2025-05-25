package ru.practicum.shareit.user.storage;


import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserStorage {
    User updateUser(User updateUser);

    User createUser(User newUser);

    Collection<User> getAllUsers();

    User findUser(Long userId);

    boolean delete(Long userId);

    boolean isUserWithEmailExist(String eMail);
}