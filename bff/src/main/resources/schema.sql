CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create reservation table
DROP TABLE IF EXISTS reservation CASCADE;
CREATE TABLE IF NOT EXISTS reservation (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    -- Foreign Key References by ID
    user_id UUID,
    provider_id UUID,
    service_id UUID,
    payment_id UUID,

    -- Date/Time Fields
    reservation_date TIMESTAMP,
    start_time TIME,
    end_time TIME,
    booked_at TIMESTAMP DEFAULT NOW(),
    expiration_date TIMESTAMP,
    reminder_enabled BOOLEAN DEFAULT FALSE,
    reminder_sent_at TIMESTAMP,

    -- Status and Workflow
    status VARCHAR(50),
    payment_status VARCHAR(50),

    -- Integration Fields
    external_id UUID,
    third_party_booking_id UUID,

    -- Security and Auditing
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    -- Audit Fields
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);
