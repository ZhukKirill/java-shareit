package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private Long id;

    @NotBlank(message = "название предмета не должно быть пустым")
    @Size(max = 100, message = "название не может быть больше 100 символов")
    private String name;

    @NotBlank(message = "описание не должно быть пустым")
    @Size(max = 255, message = "описание не может быть больше 255 символов")
    private String description;

    @NotNull
    private Boolean available;
}
