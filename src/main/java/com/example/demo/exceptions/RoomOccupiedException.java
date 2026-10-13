package com.example.demo.exceptions;

public class RoomOccupiedException extends RuntimeException {
    public RoomOccupiedException() {
        super("Room occupied");
    }
}
