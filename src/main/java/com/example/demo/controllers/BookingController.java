package com.example.demo.controllers;

import com.example.demo.dto.requests.BookingRequest;
import com.example.demo.dto.requests.VacantRoomsRequest;
import com.example.demo.dto.responses.BookingResponse;
import com.example.demo.dto.responses.RoomResponse;
import com.example.demo.entities.Room;
import com.example.demo.mappers.RoomMapper;
import com.example.demo.services.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookingController {
    private BookingService bookingService;
    private RoomMapper roomMapper;

    public BookingController(BookingService bookingService, RoomMapper roomMapper) {
        this.bookingService = bookingService;
        this.roomMapper = roomMapper;
    }

    @GetMapping("/rooms/vacant")
    public ResponseEntity<List<RoomResponse>> findVacantRooms(@Valid VacantRoomsRequest request) {
        List<Room> rooms = bookingService.findVacantRooms(request.from(), request.to());

        return ResponseEntity.status(HttpStatus.OK).body(roomMapper.toResponseList(rooms));
    }

    @PostMapping("/booking")
    public ResponseEntity<BookingResponse> creatBooking(@Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.creatBooking(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
