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
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('AVAILABLE', 'IN_BASKET', 'CONFIRMED', 'PENDING', 'CANCELED' , 'EXPIRED', 'MAINTENANCE', 'ADMIN_HOLD')),
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

DROP TABLE IF EXISTS reservation_basket_item CASCADE;
CREATE TABLE IF NOT EXISTS reservation_basket_item (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    provider_id UUID NOT NULL,
    service_id UUID NOT NULL,
    service_name VARCHAR(255),
    slot_id VARCHAR(255) NOT NULL,
    reservation_date DATE NOT NULL,
    reservation_date_persian VARCHAR(32),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    price NUMERIC(19, 2),
    duration_minutes INTEGER,
    status VARCHAR(50) NOT NULL DEFAULT 'IN_BASKET' CHECK (status IN ('AVAILABLE', 'IN_BASKET', 'CONFIRMED', 'PENDING', 'CANCELED' , 'EXPIRED', 'MAINTENANCE', 'ADMIN_HOLD')),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_basket_user_slot UNIQUE (user_id, slot_id)
);