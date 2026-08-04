package com.example.demo;

import com.example.demo.controllers.GuestController;
import com.example.demo.entities.Guest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class GuestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GuestController guestController;

    @BeforeEach
    void setUp() {
        guestController.getTestData().clear();
    }

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

        guestController.getTestData().put(guest1.getId(), guest1);
        guestController.getTestData().put(guest2.getId(), guest2);

        mockMvc.perform(get("/guests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder("1", "2")))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Ivan", "Maria")))
                .andExpect(jsonPath("$[*].birthDate", containsInAnyOrder("1999-12-02", "1999-12-01")))
                .andExpect(jsonPath("$[*].phoneNumber", containsInAnyOrder("+11111111111", "+22222222222")));
    }

    @Test
    void shouldReturn201AndCreateGuest() throws Exception {
        Guest guest = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");

        mockMvc.perform(post("/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.birthDate").value("1999-12-01"))
                .andExpect(jsonPath("$.phoneNumber").value("+11111111111"));
    }

    @Test
    void shouldSaveGuest() throws Exception {
        Guest guest = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");

        mockMvc.perform(post("/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guest)))
                .andExpect(status().isCreated());

        Guest savedGuest = guestController.getTestData().get("1");
        assertNotNull(savedGuest);
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {
        Guest guest = new Guest("1", " ",
                LocalDate.of(1999, 12, 1), "+11111111111");

        mockMvc.perform(post("/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnGuestWhenGuestExists() throws Exception {
        Guest guest = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");
        guestController.getTestData().put(guest.getId(), guest);

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
        guestController.getTestData().put(guest.getId(), guest);

        Guest updGuest = new Guest("1", "Petr",
                LocalDate.of(1999, 12, 2), "+22222222222");

        mockMvc.perform(put("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updGuest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Petr"))
                .andExpect(jsonPath("$.birthDate").value("1999-12-02"))
                .andExpect(jsonPath("$.phoneNumber").value("+22222222222"));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentGuest() throws Exception {
        Guest updGuest = new Guest("1", "Petr",
                LocalDate.of(1999, 12, 2), "+22222222222");

        mockMvc.perform(put("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updGuest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenNewNameIsBlank() throws Exception {
        Guest guest = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");
        guestController.getTestData().put(guest.getId(), guest);

        Guest updGuest = new Guest("1", " ",
                LocalDate.of(1999, 12, 2), "+22222222222");

        mockMvc.perform(put("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updGuest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateMultipleFieldsAndKeepOthersIntact() throws Exception {
        Guest guest = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");
        guestController.getTestData().put(guest.getId(), guest);

        Guest patchData = new Guest(null, "Petr",
                LocalDate.of(1999, 12, 2), null);

        mockMvc.perform(patch("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Petr"))
                .andExpect(jsonPath("$.birthDate").value("1999-12-02"))
                .andExpect(jsonPath("$.phoneNumber").value("+11111111111"));
    }

    @Test
    void shouldReturn404WhenPatchingNonExistentGuest() throws Exception {
        Guest patchData = new Guest(null, "Petr",
                LocalDate.of(1999, 12, 2), null);

        mockMvc.perform(patch("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchData)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUnchangedGuestWhenPatchBodyIsEmpty() throws Exception {
        Guest guest = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");
        guestController.getTestData().put(guest.getId(), guest);

        Guest patchData = new Guest(null, null,
                null, null);

        mockMvc.perform(patch("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.birthDate").value("1999-12-01"))
                .andExpect(jsonPath("$.phoneNumber").value("+11111111111"));
    }

    @Test
    void shouldReturn204AndDeleteGuest() throws Exception {
        Guest guest = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");
        guestController.getTestData().put(guest.getId(), guest);

        mockMvc.perform(delete("/guests/{id}", "1"))
                .andExpect(status().isNoContent());

        Guest savedGuest = guestController.getTestData().get("1");
        assertNull(savedGuest);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentGuest() throws Exception {
        mockMvc.perform(delete("/guests/{id}", "1"))
                .andExpect(status().isNotFound());
    }
}
