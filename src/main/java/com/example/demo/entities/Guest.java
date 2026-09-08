package com.example.demo.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guest")
public class Guest {
    @Id
    private String id;
    private String name;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "phone_number")
    private String phoneNumber;
    @ManyToMany(mappedBy = "guests")
    private List<Booking> bookings = new ArrayList<>();

    public Guest() {}

    public Guest(String id, String name, LocalDate birthDate, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public List<Booking> getBookings() { return bookings; }

    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.getGuests().add(this);
    }
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        booking.getGuests().remove(this);
    }
}
