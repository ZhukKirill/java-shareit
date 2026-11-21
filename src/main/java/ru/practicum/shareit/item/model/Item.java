package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * TODO Sprint add-controllers.
 */
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long owner;
    private String name;
    private String description;
    private Boolean isAvailable;
}