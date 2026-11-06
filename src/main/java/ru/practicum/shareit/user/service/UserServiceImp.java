package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.PatchUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.addUser(user));
    }

    public Collection<UserDto> findAll() {
        return userStorage.findAll().stream().map(UserMapper::toUserDto).toList();
    }

    public UserDto updateUser(Long userId, PatchUserRequest request) {
        User usersPatch = UserMapper.toUser(request);
        return UserMapper.toUserDto(userStorage.updateUser(usersPatch, userId));
    }

    public void deleteUser(Long userId) {
        findById(userId);
        itemStorage.deleteItemsByUser(userId);
        userStorage.deleteUser(userId);
    }

    public UserDto findById(Long userId) {
        return UserMapper.toUserDto(userStorage.findById(userId));
    }
}