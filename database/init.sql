-- =============================================
-- UniRide - Init limpio (alineado con JPA)
-- =============================================

-- =====================
-- USERS
-- =====================
CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     full_name VARCHAR(255) NOT NULL,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     password_hash VARCHAR(255) NOT NULL,
                                     phone VARCHAR(20) NOT NULL UNIQUE,
                                     role VARCHAR(20) NOT NULL,
                                     rating DOUBLE PRECISION NOT NULL DEFAULT 5.0,
                                     total_ratings INTEGER NOT NULL DEFAULT 0,
                                     created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- =====================
-- TRIPS
-- =====================
CREATE TABLE IF NOT EXISTS trips (
                                     id BIGSERIAL PRIMARY KEY,
                                     driver_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
                                     origin VARCHAR(255) NOT NULL,
                                     destination VARCHAR(255) NOT NULL,
                                     departure TIMESTAMP NOT NULL,
                                     seats INTEGER NOT NULL,
                                     price DOUBLE PRECISION NOT NULL,
                                     only_women BOOLEAN NOT NULL DEFAULT FALSE,
                                     has_ac BOOLEAN NOT NULL DEFAULT FALSE,
                                     status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                                     created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- =====================
-- BOOKINGS
-- =====================
CREATE TABLE IF NOT EXISTS bookings (
                                        id BIGSERIAL PRIMARY KEY,
                                        trip_id BIGINT NOT NULL REFERENCES trips(id) ON DELETE CASCADE,
                                        passenger_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                        status VARCHAR(20) NOT NULL,
                                        created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- =====================
-- REVIEWS (opcional, no lo tocamos)
-- =====================
CREATE TABLE IF NOT EXISTS reviews (
                                       id BIGSERIAL PRIMARY KEY,
                                       reviewer_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
                                       reviewed_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
                                       rating INTEGER CHECK (rating >= 1 AND rating <= 5),
                                       comment TEXT,
                                       created_at TIMESTAMP DEFAULT NOW()
);

-- =====================
-- DATA INICIAL
-- =====================

-- Admin
INSERT INTO users (full_name, email, password_hash, phone, role)
VALUES (
           'Admin UniRide',
           'admin@uniride.com',
           '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHy',
           '3000000000',
           'ADMIN'
       ) ON CONFLICT DO NOTHING;

-- Usuarios conductores (pero pueden ser todo)
INSERT INTO users (full_name, email, password_hash, phone, role, rating, total_ratings)
VALUES
    ('María Rodríguez', 'maria@uniride.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHy', '3001111111', 'USER', 4.9, 127),
    ('Andrea López',    'andrea@uniride.com','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHy', '3002222222', 'USER', 5.0, 89),
    ('Carlos Martínez', 'carlos@uniride.com','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHy', '3003333333', 'USER', 4.8, 203)
ON CONFLICT DO NOTHING;

-- Trips de prueba
INSERT INTO trips (driver_id, origin, destination, departure, seats, price, only_women, has_ac, status)
SELECT id, 'Universidad Javeriana', 'Centro Chía', NOW() + INTERVAL '2 hours', 3, 4000, FALSE, TRUE, 'ACTIVE'
FROM users WHERE email = 'maria@uniride.com'
ON CONFLICT DO NOTHING;

INSERT INTO trips (driver_id, origin, destination, departure, seats, price, only_women, has_ac, status)
SELECT id, 'Universidad Javeriana', 'Portal Norte', NOW() + INTERVAL '3 hours', 2, 5500, TRUE, FALSE, 'ACTIVE'
FROM users WHERE email = 'andrea@uniride.com'
ON CONFLICT DO NOTHING;

INSERT INTO trips (driver_id, origin, destination, departure, seats, price, only_women, has_ac, status)
SELECT id, 'Universidad Javeriana', 'Suba', NOW() + INTERVAL '4 hours', 4, 6000, FALSE, FALSE, 'ACTIVE'
FROM users WHERE email = 'carlos@uniride.com'
ON CONFLICT DO NOTHING;