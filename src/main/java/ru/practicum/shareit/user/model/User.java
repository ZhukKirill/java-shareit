package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO Sprint add-controllers.
 */
@EqualsAndHashCode(of = "id")
@Data
public class User {
    private Long id;
    private String email;
    private String name;
}
