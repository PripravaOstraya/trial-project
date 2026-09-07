package com.example.demo;

import com.example.demo.dto.requests.GuestRequest;
import com.example.demo.entities.Guest;
import com.example.demo.repositories.GuestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class GuestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GuestRepository guestRepository;

    @Test
    void shouldReturnEmptyListWhenNoGuests() throws Exception {
        mockMvc.perform(get("/guests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldReturnAllGuestsWithCorrectData() throws Exception {
        Guest guest1 = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");
        Guest guest2 = new Guest("2", "Maria",
                LocalDate.of(1999, 12, 2), "+22222222222");
        guestRepository.save(guest1);
        guestRepository.save(guest2);

        mockMvc.perform(get("/guests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Ivan"))
                .andExpect(jsonPath("$[0].birthDate").value("1999-12-01"))
                .andExpect(jsonPath("$[0].phoneNumber").value("+11111111111"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("Maria"))
                .andExpect(jsonPath("$[1].birthDate").value("1999-12-02"))
                .andExpect(jsonPath("$[1].phoneNumber").value("+22222222222"));
    }

    @Test
    void shouldReturn201AndCreateGuest() throws Exception {
        GuestRequest request = new GuestRequest("Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");

        mockMvc.perform(post("/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.birthDate").value("1999-12-01"))
                .andExpect(jsonPath("$.phoneNumber").value("+11111111111"));

        assertThat(guestRepository.count()).isEqualTo(1);

        Guest savedGuest = guestRepository.findAll().get(0);
        assertThat(savedGuest.getName()).isEqualTo("Ivan");
        assertThat(savedGuest.getBirthDate()).isEqualTo(LocalDate.of(1999, 12, 1));
        assertThat(savedGuest.getPhoneNumber()).isEqualTo("+11111111111");
        assertThat(savedGuest.getId()).isNotNull();
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {
        GuestRequest request = new GuestRequest(" ",
                LocalDate.of(1999, 12, 1), "+11111111111");

        mockMvc.perform(post("/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnGuestWhenGuestExists() throws Exception {
        Guest guest = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");
        guestRepository.save(guest);

        mockMvc.perform(get("/guests/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.birthDate").value("1999-12-01"))
                .andExpect(jsonPath("$.phoneNumber").value("+11111111111"));
    }

    @Test
    void shouldReturn404WhenGuestNotFound() throws Exception {
        mockMvc.perform(get("/guests/{id}", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateGuest() throws Exception {
        Guest guest = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");
        guestRepository.save(guest);

        GuestRequest updRequest = new GuestRequest("Petr",
                LocalDate.of(1999, 12, 2), "+22222222222");

        mockMvc.perform(put("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Petr"))
                .andExpect(jsonPath("$.birthDate").value("1999-12-02"))
                .andExpect(jsonPath("$.phoneNumber").value("+22222222222"));

        Guest updatedGuest = guestRepository.findById("1").orElseThrow();
        assertThat(updatedGuest.getName()).isEqualTo("Petr");
        assertThat(updatedGuest.getBirthDate()).isEqualTo(LocalDate.of(1999, 12, 2));
        assertThat(updatedGuest.getPhoneNumber()).isEqualTo("+22222222222");
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentGuest() throws Exception {
        GuestRequest updRequest = new GuestRequest("Petr",
                LocalDate.of(1999, 12, 2), "+22222222222");

        mockMvc.perform(put("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenNewNameIsBlank() throws Exception {
        Guest guest = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");
        guestRepository.save(guest);

        GuestRequest updRequest = new GuestRequest(" ",
                LocalDate.of(1999, 12, 2), "+22222222222");

        mockMvc.perform(put("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updRequest)))
                .andExpect(status().isBadRequest());
    }
}
