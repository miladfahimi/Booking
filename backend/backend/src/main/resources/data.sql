-- Insert data into app_user table
INSERT INTO app_user (username, password, email, phone, first_name, last_name, role, otp, otp_expiration_time) VALUES
('john_doe', '$2a$10$4Xi2lPlzJwVIaTtNVVeYBOJV39OSS/8czwyu1GH/TRwcXCsRqH7/q', 'john@example.com', '1234567890', 'John', 'Doe', 'USER', NULL, NULL),
('David_jackson', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'david@example.com', '1111567890', 'David', 'Jackson', 'ADMIN', NULL, NULL),
('Ali_koorvet', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'ali@example.com', '122227890', 'Ali', 'Koorvet', 'USER', NULL, NULL);

-- Insert data into club table
INSERT INTO club (name, address, phone, email, description) VALUES
('Tennis Club 1', '123 Main St', '3334567890', 'club1@example.com', 'Best club in town'),
('Tennis Club 2', '456 Main St', '1114567890', 'club2@example.com', 'A big tennis club in the west city'),
('Tennis Club 3', '789 Main St', '2224567890', 'club3@example.com', 'Tournament club');

-- Insert data into court table
INSERT INTO court (name, type, availability, club_id) VALUES
('Court 1', 'CLAY', true, 1),
('Court 2', 'GRASS', true, 1);

-- Insert data into feedback table
INSERT INTO feedback (comment, rating, created_at, club_id, court_id) VALUES
('Great club!', 5, '2024-07-01 10:00:00', 1, NULL),
('Nice court', 4, '2024-07-02 11:00:00', NULL, 1);

-- Insert data into reservation table
INSERT INTO reservation (reservation_date, start_time, end_time, status, user_id, court_id) VALUES
('2024-07-02', '10:00', '11:00', 'CONFIRMED', 1, 1),
('2024-07-03', '12:00', '13:00', 'PENDING', 1, 2);
