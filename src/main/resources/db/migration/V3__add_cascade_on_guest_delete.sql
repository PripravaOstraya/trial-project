ALTER TABLE booking_guest
DROP CONSTRAINT fk_booking_guest_guest,
ADD CONSTRAINT fk_booking_guest_guest
FOREIGN KEY (guest_id) REFERENCES guest(id) ON DELETE CASCADE;