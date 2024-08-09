-- Create Extension for UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create user_profile table
DROP TABLE IF EXISTS user_profile CASCADE;
CREATE TABLE IF NOT EXISTS user_profile (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    email VARCHAR(100),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(50),
    address TEXT,
    date_of_birth DATE,
    profile_picture VARCHAR(255),
    preferences TEXT,
    is_user_profiles_initiated BOOLEAN DEFAULT FALSE
);

-- Create user_subscription table
DROP TABLE IF EXISTS user_subscription CASCADE;
CREATE TABLE IF NOT EXISTS user_subscription (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    subscription_plan VARCHAR(100),
    start_date DATE,
    end_date DATE,
    status VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES user_profile(user_id)
);

-- Create user_booking_history table
DROP TABLE IF EXISTS user_booking_history CASCADE;
CREATE TABLE IF NOT EXISTS user_booking_history (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    court_id BIGINT NOT NULL,
    booking_date TIMESTAMP,
    status VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_it_archived BOOLEAN DEFAULT FALSE,
    is_user_notified BOOLEAN DEFAULT FALSE,
    notes TEXT,
    reservation_id UUID DEFAULT uuid_generate_v4(),
    FOREIGN KEY (user_id) REFERENCES user_profile(user_id)
);
