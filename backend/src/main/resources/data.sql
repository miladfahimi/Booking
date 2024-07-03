INSERT INTO app_user (username, password, email, phone, first_name, last_name, role) VALUES
('john_doe', 'password', 'john@example.com', '1234567890', 'John', 'Doe', 'USER');

INSERT INTO club (name, address, phone, email, description) VALUES
('Tennis Club 1', '123 Main St', '1234567890', 'club1@example.com', 'Best club in town');

INSERT INTO court (name, type, availability, club_id) VALUES
('Court 1', 'CLAY', true, 1),
('Court 2', 'GRASS', true, 1);

INSERT INTO feedback (comment, rating, created_at, club_id, court_id) VALUES
('Great club!', 5, '2024-07-01 10:00:00', 1, NULL),
('Nice court', 4, '2024-07-02 11:00:00', NULL, 1);

INSERT INTO reservation (reservation_date, start_time, end_time, status, user_id, court_id) VALUES
('2024-07-02', '10:00', '11:00', 'CONFIRMED', 1, 1),
('2024-07-03', '12:00', '13:00', 'PENDING', 1, 2);
