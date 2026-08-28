package com.example.demo.services;

import com.example.demo.entities.Guest;
import com.example.demo.repositories.GuestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class GuestService {
    private GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {}

    public List<Guest> listGuests(){
        return guestRepository.findAll();
    }

    public Guest createGuest(Guest newGuest) {
        String generatedId = UUID.randomUUID().toString();

        newGuest.setId(generatedId);

        return guestRepository.save(newGuest);
    }

    public Guest getGuestById(String id) {
        return  guestRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Guest updateGuest(String id, Guest updGuest) {
        guestRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        updGuest.setId(id);

        return guestRepository.save(updGuest);
    }
}
