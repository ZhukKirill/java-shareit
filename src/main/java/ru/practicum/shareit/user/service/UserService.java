package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.PatchUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    UserDto addUser(UserDto userDto);

    Collection<UserDto> findAll();

    UserDto updateUser(Long userId, PatchUserRequest request);

    void deleteUser(Long userId);

    UserDto findById(Long userId);
}