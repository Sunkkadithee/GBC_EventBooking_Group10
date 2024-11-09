CREATE TABLE t_users (
       id BIGSERIAL NOT NULL,
       name VARCHAR(255) NOT NULL,
       email VARCHAR(255) UNIQUE NOT NULL,
       role VARCHAR(255),
       user_type VARCHAR(255) NOT NULL,
       PRIMARY KEY (id)
);