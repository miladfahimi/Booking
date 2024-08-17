CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create provider table
DROP TABLE IF EXISTS provider CASCADE;
CREATE TABLE IF NOT EXISTS provider (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(50),
    email VARCHAR(100),
    description TEXT,
    rating DECIMAL(2, 1),  -- Overall rating for the provider (e.g., 4.5)
    logo_url VARCHAR(255),  -- URL to the provider's logo or image
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),  -- Timestamp when the provider was created
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),  -- Timestamp when the provider's details were last updated
    status VARCHAR(50) DEFAULT 'active',  -- Operational status of the provider
    social_links JSON  -- JSON to store social media links or external profiles
);

-- Create service table
DROP TABLE IF EXISTS service CASCADE;
CREATE TABLE IF NOT EXISTS service (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50),
    availability BOOLEAN,
    provider_id UUID,
    price DECIMAL(10, 2),  -- Cost of the service
    currency VARCHAR(10),  -- Currency of the service price
    duration INT,  -- Duration of the service in minutes
    image_url VARCHAR(255),  -- URL to an image representing the service
    rating DECIMAL(2, 1),  -- Overall rating for the service (e.g., 4.5)
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),  -- Timestamp when the service was created
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),  -- Timestamp when the service details were last updated
    status VARCHAR(50) DEFAULT 'available',  -- Current availability status of the service
    max_capacity INT,  -- Maximum capacity for the service
    tags JSON,  -- Tags or categories for filtering and searching services
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