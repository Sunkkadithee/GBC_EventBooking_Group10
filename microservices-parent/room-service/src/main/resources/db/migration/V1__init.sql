CREATE TABLE t_rooms (
    id BIGSERIAL NOT NULL,
    room_name VARCHAR(255) NOT NULL,
    capacity INTEGER,
    feature VARCHAR(255),
    availability BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);
