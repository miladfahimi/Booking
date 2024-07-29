

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
    user_id BIGINT,
    FOREIGN KEY (court_id) REFERENCES court(id),
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);
