package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatchItemRequest {

    @Size(max = 100, message = "имя не может быть больше 100 символов")
    private String name;

    @Size(max = 255, message = "описание не может быть больше 255 символов")
    private String description;

    private Boolean available;
}
