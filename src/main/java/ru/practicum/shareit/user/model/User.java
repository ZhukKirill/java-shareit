package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
}
