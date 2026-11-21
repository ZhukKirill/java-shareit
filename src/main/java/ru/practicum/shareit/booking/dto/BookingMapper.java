package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

public class BookingMapper {
    public static Booking toBooking(NewBookingRequestDto newBookingRequestDto) {
        Booking booking = new Booking();
        booking.setStart(newBookingRequestDto.getStart());
        booking.setEnd(newBookingRequestDto.getEnd());
        return booking;
    }

    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setStart(booking.getStart());
        bookingResponseDto.setEnd(booking.getEnd());
        bookingResponseDto.setStatus(booking.getStatus());
        bookingResponseDto.setItem(ItemMapper.toItemDto(booking.getItem()));
        bookingResponseDto.setBooker(UserMapper.toUserDto(booking.getBooker()));
        return bookingResponseDto;
    }

}
