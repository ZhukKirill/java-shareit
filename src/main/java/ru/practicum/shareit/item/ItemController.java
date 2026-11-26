package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.addItem(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDetailsDto findById(@PathVariable Long itemId) {
        return itemService.findById(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody @Valid PatchItemRequest patchItem,
                              @PathVariable Long itemId,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.updateItem(patchItem, itemId, userId);
    }

    @GetMapping
    public Collection<ItemDetailsDto> findItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAllByUser(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemService.deleteItem(userId, itemId);
    }

    @DeleteMapping()
    public void deleteItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        itemService.deleteItemsByUser(userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComments(@RequestBody @Valid CommentRequestDto commentRequest,
                            @RequestHeader("X-Sharer-User-Id") Long userId,
                            @PathVariable Long itemId) {
        return itemService.addComment(commentRequest, userId, itemId);
    }
}
