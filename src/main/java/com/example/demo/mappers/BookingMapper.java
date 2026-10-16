package com.example.demo.mappers;

import com.example.demo.dto.requests.BookingRequest;
import com.example.demo.dto.responses.BookingResponse;
import com.example.demo.entities.Booking;
import com.example.demo.entities.Guest;
import com.example.demo.entities.Room;
import com.example.demo.exceptions.GuestNotFoundException;
import com.example.demo.exceptions.RoomNotFoundException;
import com.example.demo.repositories.GuestRepository;
import com.example.demo.repositories.RoomRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapper {
    private RoomRepository roomRepository;
    private GuestRepository guestRepository;
    private GuestMapper guestMapper;
    private RoomMapper roomMapper;

    public BookingMapper(RoomRepository roomRepository,
                         GuestRepository guestRepository,
                         GuestMapper guestMapper,
                         RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.guestMapper = guestMapper;
        this.roomMapper = roomMapper;
    }

    public Booking toEntity(BookingRequest request) {
        Room room = roomRepository.findById(request.roomId()).orElseThrow(() -> new RoomNotFoundException());
        List<Guest> guests = new ArrayList<>();
        for (String guestId : request.guestIds()) {
            Guest guest = guestRepository.findById(guestId).orElseThrow(() -> new GuestNotFoundException());
            guests.add(guest);
        }

        return new Booking(null,
                request.checkInDate(),
                request.checkOutDate(),
                guests,
                room);
    }

    public BookingResponse toResponse(Booking booking) {
        return new BookingResponse(booking.getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                guestMapper.toResponseList(booking.getGuests()),
                roomMapper.toResponse(booking.getRoom()));
    }
}
