CREATE TABLE t_rooms (
                         id BIGSERIAL NOT NULL PRIMARY KEY,
                         room_name VARCHAR(255),
                         capacity INT,
                         features VARCHAR(255),
                         price DECIMAL(19, 2),
                         availability BOOLEAN
);
