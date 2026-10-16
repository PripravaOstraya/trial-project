package com.example.demo.dto.requests;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record BookingRequest(@NotNull LocalDateTime checkInDate,
                             @NotNull LocalDateTime checkOutDate,
                             @NotEmpty List<String> guestIds,
                             @NotBlank String roomId) {
    @AssertTrue
    public boolean isСheckInBeforeСheckOutDate() {
        boolean result = true;
        if (checkInDate != null && checkOutDate != null) {
            result = !checkInDate.isAfter(checkOutDate);
        }
        return result;
    }
}
