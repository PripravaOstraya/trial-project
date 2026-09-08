package com.example.demo.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    private String id;
    @Column(name = "check_in_date")
    private LocalDateTime checkInDate;
    @Column(name = "check_out_date")
    private LocalDateTime checkOutDate;
    @ManyToMany
    @JoinTable(name = "booking_guest",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "guest_id"))
    private List<Guest> guests = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public Booking() {}

    public Booking(String id, LocalDateTime checkInDate, LocalDateTime checkOutDate, List<Guest> guests, Room room) {
        this.id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guests = guests;
        this.room = room;
    }

    public String getId() { return id; }
    public LocalDateTime getCheckInDate() { return checkInDate; }
    public LocalDateTime getCheckOutDate() { return checkOutDate; }
    public List<Guest> getGuests() { return guests; }
    public Room getRoom() { return room; }

    public void setId(String id) { this.id = id; }
    public void setCheckInDate(LocalDateTime checkInDate) { this.checkInDate = checkInDate; }
    public void setCheckOutDate(LocalDateTime checkOutDate) { this.checkOutDate = checkOutDate; }
    public void setRoom(Room room) { this.room = room; }

    public void addGuest(Guest guest) {
        guests.add(guest);
        guest.getBookings().add(this); // Синхронизация
    }

    public void removeGuest(Guest guest) {
        guests.remove(guest);
        guest.getBookings().remove(this);
    }
}
