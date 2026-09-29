package com.example.demo.mappers;

import com.example.demo.dto.responses.RoomResponse;
import com.example.demo.entities.Room;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomMapper {
    public RoomResponse toResponse(Room room) {
        return new RoomResponse(room.getId(),
                room.getFloor(),
                room.getNumber(),
                room.getCapacity());
    }

    public List<RoomResponse> toResponseList(List<Room> rooms) {
        return rooms.stream().map(this::toResponse).toList();
    }
}
