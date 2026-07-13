package com.example.demo.entities;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDate;

public class Guest {
    @NotNull
    private String id;
    @NotBlank
    private String name;
    @NotNull
    private LocalDate birthDate;
    @NotBlank
    private String phoneNumber;

    public Guest(){
    }

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

    public String toString() {
        return "Guest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
