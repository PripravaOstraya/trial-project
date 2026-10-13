package com.example.demo.services;

import com.example.demo.dto.requests.BookingRequest;
import com.example.demo.dto.responses.BookingResponse;
import com.example.demo.entities.Booking;
import com.example.demo.entities.Room;
import com.example.demo.exceptions.RoomOccupiedException;
import com.example.demo.mappers.BookingMapper;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    private BookingRepository bookingRepository;
    private RoomRepository roomRepository;
    private BookingMapper bookingMapper;

    public BookingService(BookingRepository bookingRepository,
                          RoomRepository roomRepository,
                          BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.bookingMapper = bookingMapper;
    }

    public List<Room> findVacantRooms(LocalDateTime from, LocalDateTime to) {
        List<String> occupiedRoomIds = bookingRepository.findOccupiedRoomIds(from, to);
        return roomRepository.findByIdNotIn(occupiedRoomIds);
    }

    @Transactional
    public BookingResponse creatBooking(BookingRequest request) {
        Booking newBooking = bookingMapper.toEntity(request);

        List<String> occupiedRoomIds = bookingRepository.findOccupiedRoomIds(newBooking.getCheckInDate(),
                newBooking.getCheckOutDate());
        if (occupiedRoomIds.contains(newBooking.getRoom().getId())) {
            throw new RoomOccupiedException();
        }

        newBooking.setId(UUID.randomUUID().toString());

        Booking createdBooking = bookingRepository.save(newBooking);

        return bookingMapper.toResponse(createdBooking);
    }
}
