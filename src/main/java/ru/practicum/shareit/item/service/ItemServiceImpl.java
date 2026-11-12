package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.PatchItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    public ItemDto addItem(ItemDto itemDto, Long userId) {
        userStorage.findById(userId);
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemStorage.addItem(item, userId));
    }

    public ItemDto findById(Long itemId) {
        return ItemMapper.toItemDto(itemStorage.findById(itemId));
    }

    public ItemDto updateItem(PatchItemRequest patchItem, Long itemId, Long userId) {
        userStorage.findById(userId);
        Item item = ItemMapper.toItem(patchItem);
        return ItemMapper.toItemDto(itemStorage.updateItem(item, itemId, userId));
    }

    public Collection<ItemDto> findAllByUser(Long userId) {
        userStorage.findById(userId);
        return itemStorage.findAllItemsByUser(userId).stream().map(ItemMapper::toItemDto).toList();
    }

    public Collection<ItemDto> search(String text) {
        return itemStorage.search(text).stream().map(ItemMapper::toItemDto).toList();
    }

    public void deleteItem(Long userId, Long itemId) {
        userStorage.findById(userId);
        itemStorage.deleteItem(userId, itemId);
    }

    public void deleteItemsByUser(Long userId) {
        userStorage.findById(userId);
        itemStorage.deleteItemsByUser(userId);
    }
}
