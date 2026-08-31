package com.example.demo.exceptions;

public class GuestNotFoundException extends RuntimeException {
    public GuestNotFoundException() {
        super("Guest not found");
    }
}
