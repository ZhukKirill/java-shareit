package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO Sprint add-controllers.
 */
@EqualsAndHashCode(of = "id")
@Data
public class Item {
    private Long id;
    private Long owner;
    private String name;
    private String description;
    private Boolean isAvailable;
}