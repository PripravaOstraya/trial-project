package com.example.demo.dto.requests;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record VacantRoomsRequest(@NotNull LocalDateTime from,
                                 @NotNull LocalDateTime to) {
    @AssertTrue
    public boolean isFromBeforeTo() {
        return !from.isAfter(to);
    }
}
