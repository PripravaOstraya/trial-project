package com.example.demo.dto.responses;

import java.time.LocalDateTime;
import java.util.List;

public record BookingResponse(String id,
                              LocalDateTime checkInDate,
                              LocalDateTime checkOutDate,
                              List<GuestResponse> guests,
                              RoomResponse room) {}
