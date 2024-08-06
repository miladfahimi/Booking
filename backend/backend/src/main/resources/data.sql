-- Insert data into user_profile table
INSERT INTO user_profile (user_id, email, first_name, last_name, phone_number, address, date_of_birth, profile_picture, preferences) VALUES
('3fa85f64-5717-4562-b3fc-2c963f66afa6', 'john.doe@example.com', 'John', 'Doe', '1234567890', '123 Main St', '1990-01-01', '/images/profile1.jpg', 'email'),
('2fa85f64-5717-4562-b3fc-2c963f66afa7', 'jane.smith@example.com', 'Jane', 'Smith', '0987654321', '890 Main St', '1992-10-02', '/images/profile2.jpg', 'sms'),
('1fa85f64-5717-4562-b3fc-2c963f66afa8', 'david.jackson@example.com', 'David', 'Jackson', '0987654321', '678 Main St', '1990-09-22', '/images/profile3.jpg', 'email'),
('0fa85f64-5717-4562-b3fc-2c963f66afa9', 'emma.jones@example.com', 'Emma', 'Jones', '0987654321', '4543 Main St', '1982-03-11', '/images/profile4.jpg', 'sms'),
('4fa85f64-5717-4562-b3fc-2c963f66afaa', 'admin@example.com', 'Admin', 'Admin', '0987654321', '453456 Main St', '1972-08-30', '/images/profile5.jpg', 'sms');

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
INSERT INTO reservation (reservation_date, start_time, end_time, status, user_profile_id, court_id) VALUES
('2024-07-02', '10:00', '11:00', 'CONFIRMED', 1, 1),
('2024-07-03', '12:00', '13:00', 'PENDING', 1, 2);

-- Insert data into user_subscription table
INSERT INTO user_subscription (user_profile_id, subscription_plan, start_date, end_date, status) VALUES
(1, 'Premium', '2024-01-01', '2024-12-31', 'Active'),
(2, 'Basic', '2024-01-01', '2024-12-31', 'Active'),
(3, 'Basic', '2024-01-01', '2024-12-31', 'Active'),
(4, 'Basic', '2024-01-01', '2024-12-31', 'Active'),
(5, 'Premium', '2024-01-01', '2024-12-31', 'Active');

-- Insert data into user_booking_history table
INSERT INTO user_booking_history (user_profile_id, court_id, booking_date, status) VALUES
(1, 1, '2024-07-01 10:00:00', 'Confirmed'),
(2, 2, '2024-07-02 11:00:00', 'Pending'),
(2, 2, '2024-07-02 11:00:00', 'Confirmed'),
(3, 2, '2024-07-02 11:00:00', 'Pending'),
(3, 2, '2024-07-02 11:00:00', 'Pending');