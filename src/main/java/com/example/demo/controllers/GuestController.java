package com.example.demo.controllers;

import com.example.demo.entities.Guest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/guests")
public class GuestController {
    private Map<String, Guest> guestMap = new HashMap<>();

    @GetMapping
    public ResponseEntity<List<Guest>> listGuests(){
        return ResponseEntity.status(HttpStatus.OK).body(guestMap.values().stream().toList());
    }

    @PostMapping
    public ResponseEntity<Guest> createGuest(@Valid @RequestBody Guest newGuest) {
        guestMap.put(newGuest.getId(), newGuest);

        return ResponseEntity.status(HttpStatus.CREATED).body(newGuest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuestById(@PathVariable String id) {
        Guest guest = guestMap.get(id);

        if (guest == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found");

        return ResponseEntity.status(HttpStatus.OK).body(guest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(@PathVariable String id, @Valid @RequestBody Guest updGuest) {
        if (!guestMap.containsKey(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found");

        updGuest.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(guestMap.put(id, updGuest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Guest> patchGuest(@PathVariable String id, @RequestBody Guest patchData) {
        Guest guest = guestMap.get(id);

        if (guest == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found");

        //400 оно не кидает, пока PatchGuestRequest нет, так как вот совсем костыли будут
        if (patchData.getName() != null)
            guest.setName(patchData.getName());
        if (patchData.getBirthDate() != null)
            guest.setBirthDate(patchData.getBirthDate());
        if (patchData.getPhoneNumber() != null)
            guest.setPhoneNumber(patchData.getPhoneNumber());

        return ResponseEntity.status(HttpStatus.OK).body(guestMap.put(id, guest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable String id) {
        if (!guestMap.containsKey(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found");

        guestMap.remove(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
