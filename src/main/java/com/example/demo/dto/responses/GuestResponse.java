package com.example.demo.dto.responses;

import java.time.LocalDate;

public record GuestResponse (String id,
                             String name,
                             LocalDate birthDate,
                             String phoneNumber) {}
