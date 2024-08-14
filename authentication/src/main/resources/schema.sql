CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create app_user table with OTP fields and additional columns for device and OS info
DROP TABLE IF EXISTS app_user CASCADE;
CREATE TABLE IF NOT EXISTS app_user (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(50),
    role VARCHAR(50),
    otp VARCHAR(6),
    otp_expiration_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    last_login_device_model VARCHAR(255), -- New column for device model on last login
    last_login_os VARCHAR(255),           -- New column for OS on last login
    last_login_browser VARCHAR(255),      -- New column for browser on last login
    sign_up_device_model VARCHAR(255),    -- New column for device model on sign up
    sign_up_os VARCHAR(255),              -- New column for OS on sign up
    sign_up_browser VARCHAR(255)          -- New column for browser on sign up
);

-- Create verification_token table with USED column
DROP TABLE IF EXISTS verification_token CASCADE;
CREATE TABLE IF NOT EXISTS verification_token (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    user_id UUID NOT NULL,
    CONSTRAINT fk_verification_token_user FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE
);
