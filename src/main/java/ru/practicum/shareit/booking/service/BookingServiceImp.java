package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.NewBookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exeption.ForbiddenException;
import ru.practicum.shareit.exeption.ItemNotAvailableException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImp implements BookingService {
    private final BookingStorage bookingStorage;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Transactional
    public BookingResponseDto addBooking(NewBookingRequestDto newBookingRequestDto, Long bookerId) {
        Item item = itemStorage.findById(newBookingRequestDto.getItemId())
                .orElseThrow(()
                        -> new NotFoundException("Вещь с id = " + newBookingRequestDto.getItemId() + " не найдена"));

        if (item.getOwner().equals(bookerId)) {
            throw new ValidationException("Владелец не может забронировать свою вещь");
        }

        User booker = userStorage.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + bookerId + " не найден"));

        if (!item.getIsAvailable()) {
            throw new ItemNotAvailableException("Вещь не доступна для бронирования");
        }

        Booking booking = BookingMapper.toBooking(newBookingRequestDto);
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);
        booking.setBooker(booker);

        return BookingMapper.toBookingResponseDto(bookingStorage.save(booking));
    }

    @Transactional
    public BookingResponseDto updateBookingStatus(boolean approved, Long bookingId, Long ownerId) {
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));
        boolean isOwner = booking.getItem().getOwner().equals(ownerId);
        if (!isOwner) {
            throw new ForbiddenException("Нет прав на изменение статуса бронирования");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingResponseDto(booking);
    }

    public BookingResponseDto findById(Long bookingId, Long requesterId) {
        checkUser(requesterId);
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));
        boolean isOwnerOrRequester = booking.getItem().getOwner().equals(requesterId)
                || booking.getBooker().getId().equals(requesterId);
        if (!isOwnerOrRequester) {
            throw new ForbiddenException("Нет прав на получение информации о бронировании");
        }
        return BookingMapper.toBookingResponseDto(booking);
    }

    public List<BookingResponseDto> findByBooker(String state, Long requesterId) {
        checkUser(requesterId);
        BookingState bookingState = BookingState.from(state);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookingList = switch (bookingState) {
            case ALL -> bookingStorage.findByBookerIdOrderByStartDesc(requesterId);
            case CURRENT -> bookingStorage.findByBookerIdAndStartBeforeAndEndAfterAndStatusOrderByStartDesc(requesterId,
                    now, now, BookingStatus.APPROVED);
            case PAST -> bookingStorage.findByBookerIdAndEndBeforeAndStatusOrderByStartDesc(requesterId,
                    now, BookingStatus.APPROVED);
            case FUTURE -> bookingStorage.findByBookerIdAndStartAfterAndStatusOrderByStartDesc(requesterId, now,
                    BookingStatus.APPROVED);
            case WAITING -> bookingStorage.findByBookerIdAndStatusAndEndAfterOrderByStartDesc(requesterId,
                    BookingStatus.WAITING, now);
            case REJECTED -> bookingStorage.findByBookerIdAndEndAfterAndStatusOrderByStartDesc(requesterId, now,
                    BookingStatus.REJECTED);
        };
        return bookingList.stream().map(BookingMapper::toBookingResponseDto).toList();
    }

    public List<BookingResponseDto> findByOwner(String state, Long requesterId) {
        checkUser(requesterId);
        BookingState bookingState = BookingState.from(state);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookingList = switch (bookingState) {
            case ALL -> bookingStorage.findByItemOwnerOrderByStartDesc(requesterId);
            case CURRENT -> bookingStorage.findByItemOwnerAndStartBeforeAndEndAfterAndStatusOrderByStartDesc(
                    requesterId, now, now, BookingStatus.APPROVED);
            case PAST -> bookingStorage.findByItemOwnerAndEndBeforeAndStatusOrderByStartDesc(requesterId,
                    now, BookingStatus.APPROVED);
            case FUTURE -> bookingStorage.findByItemOwnerAndStartAfterAndStatusOrderByStartDesc(requesterId, now,
                    BookingStatus.APPROVED);
            case WAITING -> bookingStorage.findByItemOwnerAndStatusAndEndAfterOrderByStartDesc(requesterId,
                    BookingStatus.WAITING, now);
            case REJECTED -> bookingStorage.findByItemOwnerAndEndAfterAndStatusOrderByStartDesc(requesterId, now,
                    BookingStatus.REJECTED);
        };
        return bookingList.stream().map(BookingMapper::toBookingResponseDto).toList();
    }

    private void checkUser(Long userId) {
        boolean isExists = userStorage.existsById(userId);
        if (!isExists) throw new NotFoundException("пользователь с id " + userId + " не найден");
    }

}
