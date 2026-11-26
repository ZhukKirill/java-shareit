package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class NewBookingRequestDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "начало срока бронирования должно быть указано")
    @Future(message = "начало срока бронирования должно быть в будущем")
    private LocalDateTime start;

    @NotNull(message = "конец срока бронирования должен быть указан")
    @Future(message = "конец срока бронирования должен быть в будущем")
    private LocalDateTime end;

    @NotNull
    private Long itemId;

    @JsonIgnore
    @AssertTrue(message = "Дата окончания должна быть позже даты начала")
    private boolean isValideBookingTime() {
        return start == null || end == null || end.isAfter(start);
    }

}
