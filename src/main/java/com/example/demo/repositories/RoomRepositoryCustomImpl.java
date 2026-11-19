package com.example.demo.repositories;

import com.example.demo.entities.Room;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RoomRepositoryCustomImpl implements RoomRepositoryCustom {
    private JdbcTemplate jdbcTemplate;

    public RoomRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Room> findByIdNotIn(Collection<String> ids) {
        String sql = "SELECT * FROM room";

        if (!ids.isEmpty()) {
            String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
            sql = "SELECT * FROM room WHERE id NOT IN (" + placeholders + ")";
        }

        return jdbcTemplate.query(sql,
                this::mapRoom,
                ids.toArray());
    }

    @Override
    public Optional<Room> findByIdForUpdate(String id) {
        String sql = "SELECT * FROM room WHERE id = ? FOR UPDATE";

        return jdbcTemplate.query(sql,
                this::mapRoom,
                id).stream().findFirst();
    }

    private Room mapRoom(ResultSet rs, int rowNum) throws SQLException {
        return new Room(
                rs.getString("id"),
                rs.getInt("floor"),
                rs.getString("number"),
                rs.getInt("capacity")
        );
    }
}
