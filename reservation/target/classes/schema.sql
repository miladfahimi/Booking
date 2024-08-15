CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create reservation table
DROP TABLE IF EXISTS reservation CASCADE;
CREATE TABLE IF NOT EXISTS reservation (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    reservation_date TIMESTAMP,
    start_time TIME,
    end_time TIME,
    status VARCHAR(50),
    provider_id UUID,
    service_id UUID,
    user_id UUID -- No foreign key constraint here
);
