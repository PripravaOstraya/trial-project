package com.example.demo.repositories;

import com.example.demo.entities.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, String> {
}
