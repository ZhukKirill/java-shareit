package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.exeption.ConflictException;
import ru.practicum.shareit.exeption.NotFoundException;
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
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Transactional
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        checkEmailIsFree(user.getEmail(), null);
        return UserMapper.toUserDto(userStorage.save(user));
    }

    public Collection<UserDto> findAll() {
        return userStorage.findAll().stream().map(UserMapper::toUserDto).toList();
    }

    @Transactional
    public UserDto updateUser(Long userId, PatchUserRequest request) {
        User updatedUser = UserMapper.toUser(findById(userId));
        updatedUser.setId(userId);
        User usersPatch = UserMapper.toUser(request);
        if (StringUtils.hasText(usersPatch.getEmail())) {
            checkEmailIsFree(request.getEmail(), userId);
            updatedUser.setEmail(usersPatch.getEmail());
        }
        if (StringUtils.hasText(usersPatch.getName())) {
            updatedUser.setName(usersPatch.getName());
        }
        return UserMapper.toUserDto(userStorage.save(updatedUser));
    }

    @Transactional
    public void deleteUser(Long userId) {
        itemStorage.deleteByOwner(userId);
        userStorage.deleteById(userId);
    }

    public UserDto findById(Long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь с id " + userId + " не найден"));
        return UserMapper.toUserDto(user);
    }

    private void checkEmailIsFree(String email, Long currentUserId) {
        boolean isFree = userStorage.existsByEmailIgnoreCaseAndIdNot(email, currentUserId);
        if (isFree) throw new ConflictException("Пользователь с email " + email + " уже существует");
    }
}