package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage extends JpaRepository<Item, Long> {
    List<Item> findByOwnerOrderByIdAsc(Long owner);

    void deleteByOwner(Long owner);

    @Query("SELECT item from Item AS item WHERE (LOWER(item.name) LIKE LOWER(concat('%', ?1, '%'))" +
            " or LOWER(item.description) LIKE LOWER(concat('%', ?1, '%'))) AND item.isAvailable = true")
    List<Item> search(String text);
}