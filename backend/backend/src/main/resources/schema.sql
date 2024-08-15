CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create provider table
DROP TABLE IF EXISTS provider CASCADE;
CREATE TABLE IF NOT EXISTS provider (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(50),
    email VARCHAR(100),
    description TEXT
);

-- Create service table
DROP TABLE IF EXISTS service CASCADE;
CREATE TABLE IF NOT EXISTS service (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50),
    availability BOOLEAN,
    provider_id UUID,
    FOREIGN KEY (provider_id) REFERENCES provider(id)
);

-- Create feedback table
DROP TABLE IF EXISTS feedback CASCADE;
CREATE TABLE IF NOT EXISTS feedback (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    comment VARCHAR(255),
    rating INT,
    created_at TIMESTAMP,
    provider_id UUID,
    service_id UUID,
    FOREIGN KEY (provider_id) REFERENCES provider(id),
    FOREIGN KEY (service_id) REFERENCES service(id)
);

-- Create reservation table
DROP TABLE IF EXISTS reservation CASCADE;
CREATE TABLE IF NOT EXISTS reservation (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    reservation_date TIMESTAMP,
    start_time TIME,
    end_time TIME,
    status VARCHAR(50),
    service_id BIGINT,
    user_id UUID -- No foreign key constraint here
);
