package com.example.demo.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


import java.time.LocalDate;

public record GuestRequest(@NotBlank String name,
                           @NotNull LocalDate birthDate,
                           @NotBlank @Pattern(regexp = "^\\+[0-9]{10,15}$") String phoneNumber) {}


