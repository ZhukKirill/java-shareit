package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class PatchUserRequest {
    @Email(message = "Некорректный email")
    private String email;
    private String name;
}