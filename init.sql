CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name varchar(255) UNIQUE NOT NULL
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email varchar(255) UNIQUE NOT NULL,
    password varchar(255) NOT NULL
);

CREATE TABLE users_roles (
    user_id BIGINT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    location VARCHAR(255),
    date timestamp NOT NULL
);

CREATE TABLE tickets (
    id SERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    price NUMERIC NOT NULL,
    status VARCHAR(255) NOT NULL,
    seat VARCHAR(255),
    FOREIGN KEY (event_id) REFERENCES events(id)
);