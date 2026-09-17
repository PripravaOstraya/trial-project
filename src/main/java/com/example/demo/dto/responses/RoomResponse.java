package com.example.demo.dto.responses;

public record RoomResponse(String id,
                           int floor,
                           String number,
                           int capacity) {}
