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
    rol VARCHAR(20) NOT NULL,
    vehicle_plate VARCHAR(10),
    vehicle_color VARCHAR(30),
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
-- REVIEWS
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
