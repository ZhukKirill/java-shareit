package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDatesDto;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemDetailsDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDatesDto lastBooking;
    private BookingDatesDto nextBooking;
    private List<CommentResponseDto> comments;

    public ItemDetailsDto(BookingDatesDto lastBooking, BookingDatesDto nextBooking) {
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
    }
}
