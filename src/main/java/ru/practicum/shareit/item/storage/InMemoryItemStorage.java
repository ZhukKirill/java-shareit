package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

import ru.practicum.shareit.exeption.*;

@Slf4j
@Repository
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Set<Item>> itemsByUser = new HashMap<>();
    private final Map<Long, Item> items = new HashMap<>();

    public Item addItem(Item item, Long userId) {
        if (itemsByUser.containsKey(userId)) {
            item.setOwner(userId);
            item.setId(getNextId());
            items.put(item.getId(), item);
            itemsByUser.get(userId).add(item);
            return item;
        } else {
            item.setOwner(userId);
            item.setId(getNextId());
            items.put(item.getId(), item);
            itemsByUser.put(userId, new HashSet<>(Set.of(item)));
            return item;
        }
    }

    public Item findById(Long id) {
        return Optional.ofNullable(items.get(id))
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + id + " не найдена"));
    }

    public Item updateItem(Item patchedItem, Long itemId, Long userId) {
        Item oldItem = findById(itemId);
        if (!oldItem.getOwner().equals(userId))
            throw new ForbiddenException("Нет прав на изменение вещи");
        if (patchedItem.getName() != null && !patchedItem.getName().isBlank())
            oldItem.setName(patchedItem.getName());
        if (patchedItem.getDescription() != null && !patchedItem.getDescription().isBlank())
            oldItem.setDescription(patchedItem.getDescription());
        if (patchedItem.getIsAvailable() != null)
            oldItem.setIsAvailable(patchedItem.getIsAvailable());
        return oldItem;
    }

    public Collection<Item> findAllItemsByUser(Long userId) {
        Set<Item> usersItems = itemsByUser.get(userId);
        if (usersItems == null) return List.of();
        return usersItems.stream()
                .sorted(Comparator.comparingLong(Item::getId)).toList();
    }

    public Collection<Item> search(String text) {
        if (text == null || text.isBlank()) return List.of();
        String lowerText = text.toLowerCase();
        return items.values().stream().filter(Item::getIsAvailable)
                .filter(item -> item.getName().toLowerCase().contains(lowerText)
                        || item.getDescription().toLowerCase().contains(lowerText))
                .toList();
    }

    public void deleteItem(Long userId, Long itemId) {
        Item item = findById(itemId);
        if (!item.getOwner().equals(userId)) {
            throw new ForbiddenException("Нет прав на удаление вещи");
        }
        items.remove(itemId);
        itemsByUser.get(userId).remove(item);
    }

    public void deleteItemsByUser(Long userId) {
        Set<Item> removed = itemsByUser.remove(userId);
        if (removed != null) {
            removed.forEach(item -> items.remove(item.getId()));
        }
    }

    private Long getNextId() {
        Long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("вычислен максимальный id");
        return ++currentMaxId;
    }
}
