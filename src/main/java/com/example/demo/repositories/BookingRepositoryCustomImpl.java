package com.example.demo.repositories;

import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

public class BookingRepositoryCustomImpl implements BookingRepositoryCustom {
    private JdbcTemplate jdbcTemplate;

    public BookingRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> findOccupiedRoomIds(LocalDateTime from, LocalDateTime to) {
        String sql = "SELECT DISTINCT room_id FROM booking WHERE check_in_date < ? AND check_out_date > ?";

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> rs.getString("room_id"),
                to,
                from);
    }
}
