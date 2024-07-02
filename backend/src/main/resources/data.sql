INSERT INTO APP_USER (username, password, email, phone, first_name, last_name, role) VALUES 
('john_doe', 'password', 'john@example.com', '1234567890', 'John', 'Doe', 'USER');

INSERT INTO CLUB (name, location, owner, address, description, email, phone) VALUES 
('Tennis Club 1', '123 Main St', 'Alice', '123 Main St', 'Best club in town', 'club1@example.com', '1234567890'),
('Tennis Club 2', '456 Elm St', 'Bob', '456 Elm St', 'Another great club', 'club2@example.com', '0987654321');

INSERT INTO COURT (name, type, availability, club_id) VALUES 
('Court 1', 'Clay', true, 1),
('Court 2', 'Hard', true, 1),
('Court 3', 'Grass', true, 2);

INSERT INTO RESERVATION (reservation_date, start_time, end_time, status, user_id, court_id) VALUES 
('2024-07-02', '10:00', '11:00', 'CONFIRMED', 1, 1),
('2024-07-03', '12:00', '13:00', 'PENDING', 1, 2);
