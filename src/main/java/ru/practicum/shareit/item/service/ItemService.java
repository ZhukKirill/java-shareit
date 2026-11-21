package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;
import java.util.Collection;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, Long userId);

    ItemDetailsDto findById(Long itemId);

    ItemDto updateItem(PatchItemRequest patchItem, Long itemId, Long userId);

    Collection<ItemDetailsDto> findAllByUser(Long userId);

    Collection<ItemDto> search(String text);

    void deleteItem(Long userId, Long itemId);

    void deleteItemsByUser(Long userId);

    CommentResponseDto addComment(CommentRequestDto commentRequest, Long userId, Long itemId);
}