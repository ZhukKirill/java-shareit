package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {

    Item addItem(Item item, Long userId);

    Item findById(Long id);

    Item updateItem(Item patchedItem, Long itemId, Long userId);

    Collection<Item> findAllItemsByUser(Long userId);

    Collection<Item> search(String text);

    void deleteItem(Long userId, Long itemId);

    void deleteItemsByUser(Long userId);
}