package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setIsAvailable(itemDto.getAvailable());
        return item;
    }

    public static ItemDetailsDto toItemDetailsDto(ItemDetailsDto itemDetailsDto, Item item, List<Comment> comments) {
        itemDetailsDto.setId(item.getId());
        itemDetailsDto.setName(item.getName());
        itemDetailsDto.setDescription(item.getDescription());
        itemDetailsDto.setAvailable(item.getIsAvailable());
        itemDetailsDto.setComments(comments.stream().map(CommentMapper::toCommentResponseDto).toList());
        return itemDetailsDto;
    }

}