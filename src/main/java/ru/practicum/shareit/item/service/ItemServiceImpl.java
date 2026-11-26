package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingDatesDto;
import ru.practicum.shareit.exeption.ForbiddenException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;
    private final BookingStorage bookingStorage;
    private final CommentStorage commentStorage;

    @Transactional
    public ItemDto addItem(ItemDto itemDto, Long userId) {
        checkUser(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userId);
        return ItemMapper.toItemDto(itemStorage.save(item));
    }

    public ItemDetailsDto findById(Long itemId) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена"));
        List<Comment> comments = commentStorage.findByItemId(item.getId());
        return ItemMapper.toItemDetailsDto(new ItemDetailsDto(), item, comments);
    }

    @Transactional
    public ItemDto updateItem(PatchItemRequest patch, Long itemId, Long owner) {
        checkUser(owner);
        Item updatedItem = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена"));

        if (!updatedItem.getOwner().equals(owner)) throw new ForbiddenException("Нет прав на изменение вещи");

        if (StringUtils.hasText(patch.getName())) {
            updatedItem.setName(patch.getName());
        }
        if (StringUtils.hasText(patch.getDescription())) {
            updatedItem.setDescription(patch.getDescription());
        }
        if (patch.getAvailable() != null)
            updatedItem.setIsAvailable(patch.getAvailable());

        return ItemMapper.toItemDto(updatedItem);
    }

    public Collection<ItemDetailsDto> findAllByUser(Long userId) {
        checkUser(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Item> itemList = itemStorage.findByOwnerOrderByIdAsc(userId);
        return itemList.stream().map(item -> {
                    BookingDatesDto lastBookingDates = bookingStorage.findLastApproved(item.getId(), now)
                            .map(b -> new BookingDatesDto(b.getStart(), b.getEnd())).orElse(null);
                    BookingDatesDto nextBookingDates = bookingStorage.findNextApproved(item.getId(), now)
                            .map(b -> new BookingDatesDto(b.getStart(), b.getEnd())).orElse(null);
                    ItemDetailsDto itemDetailsDto = new ItemDetailsDto(lastBookingDates, nextBookingDates);
                    List<Comment> comments = commentStorage.findByItemId(item.getId());
                    return ItemMapper.toItemDetailsDto(itemDetailsDto, item, comments);
                }
        )
                .toList();
    }

    public Collection<ItemDto> search(String text) {
        if (!StringUtils.hasText(text)) return List.of();
        return itemStorage.search(text).stream().map(ItemMapper::toItemDto).toList();
    }

    @Transactional
    public void deleteItem(Long userId, Long itemId) {
        checkUser(userId);
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена"));
        if (!item.getOwner().equals(userId)) throw new ForbiddenException("Нет прав на удаление вещи");
        itemStorage.deleteById(itemId);
    }

    @Transactional
    public void deleteItemsByUser(Long userId) {
        itemStorage.deleteByOwner(userId);
    }

    @Transactional
    public CommentResponseDto addComment(CommentRequestDto commentRequest, Long userId, Long itemId) {
        User author = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь с id " + userId + " не найден"));
        LocalDateTime now = LocalDateTime.now();
        boolean hasFinished = bookingStorage.hasFinishedBooking(itemId, userId, now);
        if (!hasFinished) {
            throw new ValidationException("Пользователь не брал эту вещь в аренду или аренда ещё не завершена");
        }

        Comment comment = CommentMapper.toComment(commentRequest, author, itemId, now);
        return CommentMapper.toCommentResponseDto(commentStorage.save(comment));
    }

    private void checkUser(Long userId) {
        boolean isExists = userStorage.existsById(userId);
        if (!isExists) throw new NotFoundException("пользователь с id " + userId + " не найден");
    }
}
