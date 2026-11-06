package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;
import java.util.Collection;

public interface UserStorage {

    User addUser(User user);

    User findById(Long id);

    User updateUser(User usersPatch, Long userId);

    void deleteUser(Long id);

    Collection<User> findAll();
}