package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.exeption.ConflictException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        checkEmailIsFree(user.getEmail(), null);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(Long id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NotFoundException("пользователь с id " + id + " не найден"));
    }

    @Override
    public User updateUser(User usersPatch, Long userId) {
        User updatedUser = findById(userId);
        if (StringUtils.hasText(usersPatch.getEmail())) {
            checkEmailIsFree(usersPatch.getEmail(), userId);
            updatedUser.setEmail(usersPatch.getEmail());
        }
        if (StringUtils.hasText(usersPatch.getName())) {
            updatedUser.setName(usersPatch.getName());
        }
        return updatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    private void checkEmailIsFree(String email, Long currentUserId) {
        boolean taken = users.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email) && !u.getId().equals(currentUserId));
        if (taken) {
            throw new ConflictException("Пользователь с email " + email + " уже существует");
        }
    }

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}