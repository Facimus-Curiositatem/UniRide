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

-- Agregar columna faculty a users
ALTER TABLE users ADD COLUMN IF NOT EXISTS faculty VARCHAR(100);

-- Agregar columnas extra a trips
ALTER TABLE trips ADD COLUMN IF NOT EXISTS only_women BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE trips ADD COLUMN IF NOT EXISTS has_ac BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE trips ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';

-- Conductores de prueba
INSERT INTO users (full_name, email, password_hash, phone, role, rating, total_ratings, faculty)
VALUES
    ('María Rodríguez', 'maria@javeriana.edu.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHy', '3001111111', 'DRIVER', 4.9, 127, 'Ingeniería'),
    ('Andrea López',    'andrea@javeriana.edu.co','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHy', '3002222222', 'DRIVER', 5.0, 89,  'Medicina'),
    ('Carlos Martínez', 'carlos@javeriana.edu.co','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHy', '3003333333', 'DRIVER', 4.8, 203, 'Administración')
    ON CONFLICT DO NOTHING;

-- Viajes de prueba
INSERT INTO trips (driver_id, origin, destination, departure, seats, price, only_women, has_ac, status)
SELECT id, 'Universidad Javeriana', 'Centro Chía',   NOW() + INTERVAL '2 hours', 3, 4000, FALSE, TRUE,  'ACTIVE' FROM users WHERE email = 'maria@javeriana.edu.co'
ON CONFLICT DO NOTHING;

INSERT INTO trips (driver_id, origin, destination, departure, seats, price, only_women, has_ac, status)
SELECT id, 'Universidad Javeriana', 'Portal Norte',  NOW() + INTERVAL '3 hours', 2, 5500, TRUE,  FALSE, 'ACTIVE' FROM users WHERE email = 'andrea@javeriana.edu.co'
ON CONFLICT DO NOTHING;

INSERT INTO trips (driver_id, origin, destination, departure, seats, price, only_women, has_ac, status)
SELECT id, 'Universidad Javeriana', 'Suba',          NOW() + INTERVAL '4 hours', 4, 6000, FALSE, FALSE, 'ACTIVE' FROM users WHERE email = 'carlos@javeriana.edu.co'
ON CONFLICT DO NOTHING;