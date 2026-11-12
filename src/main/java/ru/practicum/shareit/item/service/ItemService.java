package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.PatchItemRequest;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, Long userId);

    ItemDto findById(Long itemId);

    ItemDto updateItem(PatchItemRequest patchItem, Long itemId, Long userId);

    Collection<ItemDto> findAllByUser(Long userId);

    Collection<ItemDto> search(String text);

    void deleteItem(Long userId, Long itemId);

    void deleteItemsByUser(Long userId);
}