package com.example.demo;

import com.example.demo.entities.Booking;
import com.example.demo.entities.Room;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.GuestRepository;
import com.example.demo.repositories.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class BookingControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        Room room1 = new Room("1", 1, "101", 2);
        Room room2 = new Room("2", 1, "102", 1);
        Room room3 = new Room("3", 4, "401", 3);

        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);

        entityManager.flush();
    }

    @Test
    void allVacant() throws Exception {
        LocalDateTime from = LocalDateTime.of(2026, 9, 15, 14, 0);
        LocalDateTime to = LocalDateTime.of(2026, 9, 20, 12, 0);

        mockMvc.perform(get("/rooms/vacant")
                        .param("from", from.toString())
                        .param("to", to.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[2].id").value("3"));
    }

    @Test
    void someOccupied() throws Exception {
        Booking booking = new Booking("1",
                LocalDateTime.of(2026, 9, 15, 14, 0),
                LocalDateTime.of(2026, 9, 20, 12, 0),
                null,
                roomRepository.findById("1").get());
        bookingRepository.save(booking);
        entityManager.flush();

        LocalDateTime from = LocalDateTime.of(2026, 9, 15, 14, 0);
        LocalDateTime to = LocalDateTime.of(2026, 9, 20, 12, 0);

        mockMvc.perform(get("/rooms/vacant")
                        .param("from", from.toString())
                        .param("to", to.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("2"))
                .andExpect(jsonPath("$[1].id").value("3"));
    }

    @Test
    void allOccupied() throws Exception {
        Booking booking1 = new Booking("1",
                LocalDateTime.of(2026, 9, 15, 14, 0),
                LocalDateTime.of(2026, 9, 20, 12, 0),
                null,
                roomRepository.findById("1").get());
        Booking booking2 = new Booking("2",
                LocalDateTime.of(2026, 9, 15, 14, 0),
                LocalDateTime.of(2026, 9, 20, 12, 0),
                null,
                roomRepository.findById("2").get());
        Booking booking3 = new Booking("3",
                LocalDateTime.of(2026, 9, 15, 14, 0),
                LocalDateTime.of(2026, 9, 20, 12, 0),
                null,
                roomRepository.findById("3").get());
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        entityManager.flush();

        LocalDateTime from = LocalDateTime.of(2026, 9, 15, 14, 0);
        LocalDateTime to = LocalDateTime.of(2026, 9, 20, 12, 0);

        mockMvc.perform(get("/rooms/vacant")
                        .param("from", from.toString())
                        .param("to", to.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void bookingsEndAtFromAndStartAtTo() throws Exception {
        Booking booking1 = new Booking("1",
                LocalDateTime.of(2026, 9, 14, 14, 0),
                LocalDateTime.of(2026, 9, 15, 14, 0),
                null,
                roomRepository.findById("1").get());
        Booking booking2 = new Booking("2",
                LocalDateTime.of(2026, 9, 20, 12, 0),
                LocalDateTime.of(2026, 9, 21, 12, 0),
                null,
                roomRepository.findById("1").get());
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        entityManager.flush();

        LocalDateTime from = LocalDateTime.of(2026, 9, 15, 14, 0);
        LocalDateTime to = LocalDateTime.of(2026, 9, 20, 12, 0);

        mockMvc.perform(get("/rooms/vacant")
                        .param("from", from.toString())
                        .param("to", to.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    void missingParam () throws Exception {
        LocalDateTime from = LocalDateTime.of(2026, 9, 15, 14, 0);

        mockMvc.perform(get("/rooms/vacant")
                        .param("from", from.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void fromAfterTo() throws Exception {
        LocalDateTime from = LocalDateTime.of(2026, 9, 20, 12, 0);
        LocalDateTime to = LocalDateTime.of(2026, 9, 15, 14, 0);

        mockMvc.perform(get("/rooms/vacant")
                        .param("from", from.toString())
                        .param("to", to.toString()))
                .andExpect(status().isBadRequest());
    }
}
