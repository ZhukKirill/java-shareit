package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    private Long id;

    @NotBlank(message = "email не должен быть пустым")
    @Email(message = "Некорректный email")
    @Size(max = 255, message = "длина email не должна превышать 255 символов")
    private String email;
    @NotBlank(message = "имя не должно быть пустым")
    @Size(max = 255, message = "имя не может быть больше 255 символов")
    private String name;
}