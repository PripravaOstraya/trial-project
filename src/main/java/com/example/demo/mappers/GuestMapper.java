package com.example.demo.mappers;

import com.example.demo.dto.requests.GuestRequest;
import com.example.demo.dto.responses.GuestResponse;
import com.example.demo.entities.Guest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GuestMapper {
    public Guest toEntity(GuestRequest guestRequest) {
        return new Guest(null,
                guestRequest.name(),
                guestRequest.birthDate(),
                guestRequest.phoneNumber());
    }

    public GuestResponse toResponse(Guest guest) {
        return new GuestResponse(guest.getId(),
                guest.getName(),
                guest.getBirthDate(),
                guest.getPhoneNumber());
    }

    public List<GuestResponse>  toResponseList(List<Guest> guests) {
        return guests.stream().map(this::toResponse).toList();
    }
}
