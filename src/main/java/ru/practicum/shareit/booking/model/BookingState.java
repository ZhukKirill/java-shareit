package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exeption.ValidationException;

import java.util.Arrays;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState from(String state) {
        return Arrays.stream(values()).filter(s -> s.name().equalsIgnoreCase(state))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Неизвестное состояние бронирования: " + state));
    }

}