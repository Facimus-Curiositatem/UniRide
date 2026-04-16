-- =============================================
-- UniRide - Script de inicialización
-- US-01: Registro y verificación institucional
-- =============================================

CREATE TYPE user_role AS ENUM ('PASSENGER', 'DRIVER', 'ADMIN');

CREATE TABLE IF NOT EXISTS users (
                                     id            BIGSERIAL PRIMARY KEY,
                                     full_name     VARCHAR(255)        NOT NULL,
    email         VARCHAR(255)        NOT NULL UNIQUE,
    password_hash VARCHAR(255)        NOT NULL,
    phone         VARCHAR(20)         NOT NULL UNIQUE,
    role          VARCHAR(20)         NOT NULL DEFAULT 'PASSENGER',
    rating        DOUBLE PRECISION    NOT NULL DEFAULT 5.0,
    total_ratings INTEGER             NOT NULL DEFAULT 0,
    created_at    TIMESTAMP           NOT NULL DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS trips (
                                     id          BIGSERIAL PRIMARY KEY,
                                     driver_id   BIGINT REFERENCES users(id) ON DELETE SET NULL,
    origin      VARCHAR(255),
    destination VARCHAR(255),
    departure   TIMESTAMP,
    seats       INTEGER,
    price       DOUBLE PRECISION,
    created_at  TIMESTAMP DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS bookings (
                                        id           BIGSERIAL PRIMARY KEY,
                                        trip_id      BIGINT REFERENCES trips(id) ON DELETE CASCADE,
    passenger_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    status       VARCHAR(50) DEFAULT 'PENDING',
    created_at   TIMESTAMP DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS reviews (
                                       id          BIGSERIAL PRIMARY KEY,
                                       reviewer_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    reviewed_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    rating      INTEGER CHECK (rating >= 1 AND rating <= 5),
    comment     TEXT,
    created_at  TIMESTAMP DEFAULT NOW()
    );

-- Usuario admin de prueba (password: admin123)
INSERT INTO users (full_name, email, password_hash, phone, role)
VALUES (
           'Admin UniRide',
           'admin@javeriana.edu.co',
           '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHy',
           '3000000000',
           'ADMIN'
       ) ON CONFLICT DO NOTHING;