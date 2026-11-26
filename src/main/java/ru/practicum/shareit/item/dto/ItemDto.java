package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "название предмета не должно быть пустым")
    @Size(max = 255, message = "название не может быть больше 255 символов")
    private String name;

    @NotBlank(message = "описание не должно быть пустым")
    @Size(max = 1000, message = "описание не может быть больше 1000 символов")
    private String description;

    @NotNull
    private Boolean available;
}
