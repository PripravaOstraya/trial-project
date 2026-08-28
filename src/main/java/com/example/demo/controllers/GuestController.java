package com.example.demo.controllers;

import com.example.demo.dto.requests.GuestRequest;
import com.example.demo.dto.responses.GuestResponse;
import com.example.demo.entities.Guest;
import com.example.demo.mappers.GuestMapper;
import com.example.demo.services.GuestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guests")
public class GuestController {
    private GuestService guestService;
    private GuestMapper guestMapper;

    public GuestController(GuestService guestService, GuestMapper guestMapper) {
        this.guestService = guestService;
        this.guestMapper = guestMapper;
    }

    @GetMapping
    public ResponseEntity<List<GuestResponse>> listGuests(){
        List<Guest> guests = guestService.listGuests();

        return ResponseEntity.status(HttpStatus.OK).body(guestMapper.toResponseList(guests));
    }

    @PostMapping
    public ResponseEntity<GuestResponse> createGuest(@Valid @RequestBody GuestRequest request) {
        Guest newGuest = guestMapper.toEntity(request);

        Guest createdGuest = guestService.createGuest(newGuest);

        return ResponseEntity.status(HttpStatus.CREATED).body(guestMapper.toResponse(createdGuest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> getGuestById(@PathVariable String id) {
        Guest guest = guestService.getGuestById(id);

        return ResponseEntity.status(HttpStatus.OK).body(guestMapper.toResponse(guest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuestResponse> updateGuest(@PathVariable String id, @Valid @RequestBody GuestRequest request) {
        Guest updGuest = guestMapper.toEntity(request);

        Guest updatedGuest = guestService.updateGuest(id, updGuest);

        return ResponseEntity.status(HttpStatus.OK).body(guestMapper.toResponse(updatedGuest));
    }
}
