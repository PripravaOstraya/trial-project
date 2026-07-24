package com.example.demo;

import com.example.demo.controllers.GuestController;
import com.example.demo.entities.Guest;
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
    private GuestController guestController;

    @BeforeEach
    void setUp() {
        guestController.clearTestData();
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

        guestController.addTestData(guest1);
        guestController.addTestData(guest2);

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
        String guestJson = """
                {
                "id": "1",
                "name": "Ivan",
                "birthDate": "1999-12-01",
                "phoneNumber": "+11111111111"
                }
                """;

        mockMvc.perform(post("/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(guestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.birthDate").value("1999-12-01"))
                .andExpect(jsonPath("$.phoneNumber").value("+11111111111"));
    }

    @Test
    void shouldSaveGuest() throws Exception {
        String guestJson = """
                {
                "id": "1",
                "name": "Ivan",
                "birthDate": "1999-12-01",
                "phoneNumber": "+11111111111"
                }
                """;

        mockMvc.perform(post("/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(guestJson))
                .andExpect(status().isCreated());

        Guest savedGuest = guestController.getGuestByIdForTest("1");
        assertNotNull(savedGuest);
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {
        String guestJson = """
                {
                "id": "1",
                "name": " ",
                "birthDate": "1999-12-01",
                "phoneNumber": "+11111111111"
                }
                """;

        mockMvc.perform(post("/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(guestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnGuestWhenGuestExists() throws Exception {
        Guest guest = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");
        guestController.addTestData(guest);

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
        guestController.addTestData(guest);

        String updGuestJson = """
                {
                "id": "1",
                "name": "Petr",
                "birthDate": "1999-12-02",
                "phoneNumber": "+22222222222"
                }
                """;

        mockMvc.perform(put("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updGuestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Petr"))
                .andExpect(jsonPath("$.birthDate").value("1999-12-02"))
                .andExpect(jsonPath("$.phoneNumber").value("+22222222222"));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentGuest() throws Exception {
        String updGuestJson = """
                {
                "id": "1",
                "name": "Petr",
                "birthDate": "1999-12-02",
                "phoneNumber": "+22222222222"
                }
                """;

        mockMvc.perform(put("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updGuestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenNewNameIsBlank() throws Exception {
        Guest guest = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");
        guestController.addTestData(guest);

        String updGuestJson = """
                {
                "id": "1",
                "name": " ",
                "birthDate": "1999-12-02",
                "phoneNumber": "+22222222222"
                }
                """;

        mockMvc.perform(put("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updGuestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateMultipleFieldsAndKeepOthersIntact() throws Exception {
        Guest guest = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");
        guestController.addTestData(guest);

        String patchDataJson = """
                {
                "name": "Petr",
                "birthDate": "1999-12-02"
                }
                """;

        mockMvc.perform(patch("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchDataJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Petr"))
                .andExpect(jsonPath("$.birthDate").value("1999-12-02"))
                .andExpect(jsonPath("$.phoneNumber").value("+11111111111"));
    }

    @Test
    void shouldReturn404WhenPatchingNonExistentGuest() throws Exception {
        String patchDataJson = """
                {
                "name": "Petr",
                "birthDate": "1999-12-02"
                }
                """;

        mockMvc.perform(patch("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchDataJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUnchangedGuestWhenPatchBodyIsEmpty() throws Exception {
        Guest guest = new Guest("1", "Ivan",
                LocalDate.of(1999, 12, 1), "+11111111111");
        guestController.addTestData(guest);

        String patchDataJson = """
                {
                }
                """;

        mockMvc.perform(patch("/guests/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchDataJson))
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
        guestController.addTestData(guest);

        mockMvc.perform(delete("/guests/{id}", "1"))
                .andExpect(status().isNoContent());

        Guest savedGuest = guestController.getGuestByIdForTest("1");
        assertNull(savedGuest);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentGuest() throws Exception {
        mockMvc.perform(delete("/guests/{id}", "1"))
                .andExpect(status().isNotFound());
    }
}
