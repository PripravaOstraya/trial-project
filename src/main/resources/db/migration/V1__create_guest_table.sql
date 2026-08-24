CREATE TABLE IF NOT EXISTS guest (
    id           VARCHAR(255) PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    birth_date   DATE
);