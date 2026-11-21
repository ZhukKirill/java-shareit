package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.NewBookingRequestDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto addBooking(NewBookingRequestDto newBookingRequestDto, Long bookerId);

    BookingResponseDto updateBookingStatus(boolean approved, Long bookingId, Long ownerId);

    BookingResponseDto findById(Long bookingId, Long requesterId);

    List<BookingResponseDto> findByBooker(String state, Long requesterId);

    List<BookingResponseDto> findByOwner(String state, Long requesterId);
}