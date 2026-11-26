package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequestDto {
    @NotBlank(message = "отзыв не должен быть пустым")
    @Size(max = 1000, message = "текст отзыва не должен превышать 1000 символов")
    private String text;
}
