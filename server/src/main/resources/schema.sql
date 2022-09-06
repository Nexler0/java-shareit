DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TYPE IF EXISTS status_enum CASCADE;

CREATE TYPE status_enum AS ENUM ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED');
-- CREATE CAST (CHARACTER VARYING as status_enum) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS users
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY UNIQUE,
    users_name  VARCHAR(255) NOT NULL,
    users_email VARCHAR(500) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (users_email)
);

CREATE TABLE IF NOT EXISTS items
(
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY UNIQUE,
    user_id           BIGINT,
    items_name        VARCHAR(255) NOT NULL,
    items_description VARCHAR(500) NOT NULL,
    items_available   BOOLEAN,
    request_id        BIGINT,
    CONSTRAINT FK_users_id_for_items FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id                  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    request_description VARCHAR(500),
    requester_id        BIGINT,
    created             TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT FK_requester_id_for_request FOREIGN KEY (requester_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY UNIQUE,
    start_time TIMESTAMP WITHOUT TIME ZONE,
    end_time   TIMESTAMP WITHOUT TIME ZONE,
    item_id    BIGINT NOT NULL,
    booker_id  BIGINT NOT NULL,
    status     VARCHAR default 'WAITING',
    CONSTRAINT FK_itemId FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT FK_bookerId FOREIGN KEY (booker_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text      VARCHAR(255),
    item_id   BIGINT,
    author_id BIGINT,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);