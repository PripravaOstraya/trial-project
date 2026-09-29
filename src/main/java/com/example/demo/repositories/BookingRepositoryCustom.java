package com.example.demo.repositories;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepositoryCustom {
    List<String> findOccupiedRoomIds(LocalDateTime from, LocalDateTime to);
}
