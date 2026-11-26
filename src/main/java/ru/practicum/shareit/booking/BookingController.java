package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.NewBookingRequestDto;
import ru.practicum.shareit.booking.service.BookingServiceImp;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImp bookingServiceImp;

    @PostMapping
    BookingResponseDto addBooking(@RequestBody @Valid NewBookingRequestDto newBookingRequestDto,
                                  @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        return bookingServiceImp.addBooking(newBookingRequestDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBookingStatus(@RequestParam boolean approved, @PathVariable Long bookingId,
                                    @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return bookingServiceImp.updateBookingStatus(approved,bookingId, ownerId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto findById(@PathVariable Long bookingId,
                                       @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return bookingServiceImp.findById(bookingId, requesterId);
    }

    @GetMapping
    public List<BookingResponseDto> findByBooker(@RequestParam(required = false, defaultValue = "ALL") String state,
                                               @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return bookingServiceImp.findByBooker(state, requesterId);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> findByOwner(@RequestParam(required = false, defaultValue = "ALL") String state,
                                                @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return bookingServiceImp.findByOwner(state, requesterId);
    }
}
