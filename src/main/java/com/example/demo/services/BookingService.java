package com.example.demo.services;

import com.example.demo.entities.Room;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {
    private BookingRepository bookingRepository;
    private RoomRepository roomRepository;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    public List<Room> findVacantRooms(LocalDateTime from, LocalDateTime to) {
        List<String> occupiedRoomIds = bookingRepository.findOccupiedRoomIds(from, to);
        return roomRepository.findByIdNotIn(occupiedRoomIds);
    }
}
