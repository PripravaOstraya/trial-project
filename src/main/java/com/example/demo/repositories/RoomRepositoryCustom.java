package com.example.demo.repositories;

import com.example.demo.entities.Room;

import java.util.Collection;
import java.util.List;

public interface RoomRepositoryCustom {
    List<Room> findByIdNotIn(Collection<String> ids);
}
