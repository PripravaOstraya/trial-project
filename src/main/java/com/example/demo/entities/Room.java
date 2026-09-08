package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "room")
public class Room {
    @Id
    private String id;
    private int floor;
    private String number;
    private int capacity;

    public Room() {}

    public Room(String id, int floor, String number, int capacity) {
        this.id = id;
        this.floor = floor;
        this.number = number;
        this.capacity = capacity;
    }

    public String getId() { return id; }
    public int getFloor() { return floor; }
    public String getNumber() { return number; }
    public int getCapacity() { return capacity; }

    public void setId(String id) { this.id = id; }
    public void setFloor(int floor) { this.floor = floor; }
    public void setNumber(String number) { this.number = number; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}
