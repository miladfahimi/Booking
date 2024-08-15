-- Insert data into club table
INSERT INTO club (id,name, address, phone, email, description) VALUES
(uuid_generate_v4(), 'Tennis Club 1', '123 Main St', '3334567890', 'club1@example.com', 'Best club in town'),
(uuid_generate_v4(), 'Tennis Club 2', '456 Main St', '1114567890', 'club2@example.com', 'A big tennis club in the west city'),
(uuid_generate_v4(), 'Tennis Club 3', '789 Main St', '2224567890', 'club3@example.com', 'Tournament club');

-- Insert data into court table
INSERT INTO court (name, type, availability, club_id) VALUES
('Court 1', 'CLAY', true, (SELECT id FROM club WHERE name = 'Tennis Club 1')),
('Court 2', 'GRASS', true, (SELECT id FROM club WHERE name = 'Tennis Club 2'));

-- Insert data into feedback table
INSERT INTO feedback (comment, rating, created_at, club_id, court_id) VALUES
('Great club!', 5, '2024-07-01 10:00:00',(SELECT id FROM club WHERE name = 'Tennis Club 1'), NULL),
('Nice court', 4, '2024-07-02 11:00:00', NULL, 1);

-- Insert data into reservation table
INSERT INTO reservation (id, reservation_date, start_time, end_time, status, user_id, court_id) VALUES
(uuid_generate_v4(), '2024-07-02', '10:00', '11:00', 'CONFIRMED', '2fa85f64-5717-4562-b3fc-2c963f66afa7', 1),
(uuid_generate_v4(), '2024-07-03', '12:00', '13:00', 'PENDING', '2fa85f64-5717-4562-b3fc-2c963f66afa7', 2);
