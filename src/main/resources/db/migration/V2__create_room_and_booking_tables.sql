CREATE TABLE IF NOT EXISTS room (
    id           VARCHAR(255) PRIMARY KEY,
    floor        INTEGER NOT NULL,
    number       VARCHAR(255) NOT NULL,
    capacity     INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS booking (
    id                VARCHAR(255) PRIMARY KEY,
    check_in_date     TIMESTAMP NOT NULL,
    check_out_date    TIMESTAMP NOT NULL,
    room_id           VARCHAR(255) NOT NULL,
    CONSTRAINT fk_booking_room FOREIGN KEY (room_id) REFERENCES room(id)
);

CREATE TABLE IF NOT EXISTS booking_guest (
    booking_id       VARCHAR(255) NOT NULL,
    guest_id         VARCHAR(255) NOT NULL,
    CONSTRAINT fk_booking_guest_booking FOREIGN KEY (booking_id) REFERENCES booking(id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_guest_guest FOREIGN KEY (guest_id) REFERENCES guest(id),
    PRIMARY KEY (booking_id, guest_id)
);