-- Create club table
DROP TABLE IF EXISTS club CASCADE;
CREATE TABLE IF NOT EXISTS club (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(50),
    email VARCHAR(100),
    description TEXT
);

-- Create court table
DROP TABLE IF EXISTS court CASCADE;
CREATE TABLE IF NOT EXISTS court (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50),
    availability BOOLEAN,
    club_id BIGINT,
    FOREIGN KEY (club_id) REFERENCES club(id)
);

-- Create feedback table
DROP TABLE IF EXISTS feedback CASCADE;
CREATE TABLE IF NOT EXISTS feedback (
    id BIGSERIAL PRIMARY KEY,
    comment VARCHAR(255),
    rating INT,
    created_at TIMESTAMP,
    club_id BIGINT,
    court_id BIGINT,
    FOREIGN KEY (club_id) REFERENCES club(id),
    FOREIGN KEY (court_id) REFERENCES court(id)
);

-- Create reservation table
DROP TABLE IF EXISTS reservation CASCADE;
CREATE TABLE IF NOT EXISTS reservation (
    id BIGSERIAL PRIMARY KEY,
    reservation_date TIMESTAMP,
    start_time TIME,
    end_time TIME,
    status VARCHAR(50),
    court_id BIGINT,
    user_profile_id BIGINT,
    FOREIGN KEY (court_id) REFERENCES court(id),
    FOREIGN KEY (user_profile_id) REFERENCES user_profile(id)
);

-- Create user_profile table
DROP TABLE IF EXISTS user_profile CASCADE;
CREATE TABLE IF NOT EXISTS user_profile (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    email VARCHAR(100),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(50),
    address TEXT,
    date_of_birth DATE,
    profile_picture VARCHAR(255),
    preferences TEXT
);

-- Create user_subscription table
DROP TABLE IF EXISTS user_subscription CASCADE;
CREATE TABLE IF NOT EXISTS user_subscription (
    id BIGSERIAL PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,  -- Ensure this column is NOT NULL
    subscription_plan VARCHAR(100),
    start_date DATE,
    end_date DATE,
    status VARCHAR(50),
    FOREIGN KEY (user_profile_id) REFERENCES user_profile(id)
);

-- Create user_booking_history table
DROP TABLE IF EXISTS user_booking_history CASCADE;
CREATE TABLE IF NOT EXISTS user_booking_history (
    id BIGSERIAL PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,
    court_id BIGINT NOT NULL,
    booking_date TIMESTAMP,
    status VARCHAR(50),
    FOREIGN KEY (court_id) REFERENCES court(id),
    FOREIGN KEY (user_profile_id) REFERENCES user_profile(id)
);
