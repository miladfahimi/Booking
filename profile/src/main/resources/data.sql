-- Insert data into user_profile table
INSERT INTO user_profile (
    user_id, email, first_name, last_name, phone, address, date_of_birth,
    profile_picture_url, preferences, gender, nationality, language_preference,
    timezone, account_status, email_verified, profile_visibility,
    notifications_enabled, is_user_profiles_initiated, created_by, last_updated_by
) VALUES
('2fa85f64-5717-4562-b3fc-2c963f66afa7', 'jane.smith@example.com', 'Jane', 'Smith', '0987654321', '890 Main St', '1992-10-02', '/images/profile2.jpg', 'sms', 'Female', 'US', 'English', 'EST', 'active', TRUE, 'public', TRUE, FALSE, 'admin', 'admin'),
('1fa85f64-5717-4562-b3fc-2c963f66afa8', 'david.jackson@example.com', 'David', 'Jackson', '0987654321', '678 Main St', '1990-09-22', '/images/profile3.jpg', 'email', 'Male', 'US', 'English', 'EST', 'active', TRUE, 'public', TRUE, FALSE, 'admin', 'admin'),
('0fa85f64-5717-4562-b3fc-2c963f66afa9', 'emma.jones@example.com', 'Emma', 'Jones', '0987654321', '4543 Main St', '1982-03-11', '/images/profile4.jpg', 'sms', 'Female', 'US', 'English', 'EST', 'active', TRUE, 'public', TRUE, FALSE, 'admin', 'admin'),
('4fa85f64-5717-4562-b3fc-2c963f66afaa', 'admin@example.com', 'Admin', 'Admin', '0987654321', '453456 Main St', '1972-08-30', '/images/profile5.jpg', 'sms', 'Male', 'US', 'English', 'EST', 'active', TRUE, 'public', TRUE, FALSE, 'admin', 'admin'),
('f05e550c-5e34-4af2-b1db-e64889c697dd', 'john_doe@example.com', 'John', 'Doe', '09876454565', '451116 Main St', '1972-08-30', '/images/profile5.jpg', 'sms', 'Male', 'US', 'English', 'EST', 'active', TRUE, 'public', TRUE, FALSE, 'admin', 'admin');

-- Insert data into user_subscription table
INSERT INTO user_subscription (
    user_id, subscription_plan, start_date, end_date, status,
    is_auto_renew, created_by, last_updated_by
) VALUES
('2fa85f64-5717-4562-b3fc-2c963f66afa7', 'Basic', '2024-01-01', '2024-12-31', 'Active', TRUE, 'admin', 'admin'),
('1fa85f64-5717-4562-b3fc-2c963f66afa8', 'Basic', '2024-01-01', '2024-12-31', 'Active', TRUE, 'admin', 'admin'),
('0fa85f64-5717-4562-b3fc-2c963f66afa9', 'Basic', '2024-01-01', '2024-12-31', 'Active', TRUE, 'admin', 'admin'),
('4fa85f64-5717-4562-b3fc-2c963f66afaa', 'Premium', '2024-01-01', '2024-12-31', 'Active', TRUE, 'admin', 'admin');

-- Insert data into user_booking_history table
INSERT INTO user_booking_history (
    id, user_id, provider_id, service_id, booking_date, status, created_at, updated_at,
    is_it_archived, is_user_notified, notes, booking_type,
    created_by, last_updated_by, reservation_id
) VALUES
(uuid_generate_v4(), '2fa85f64-5717-4562-b3fc-2c963f66afa7', uuid_generate_v4(),uuid_generate_v4(), '2024-07-02 11:00:00', 'Pending', '2024-07-02 10:00:00', '2024-07-02 10:00:00', FALSE, FALSE, 'User requested early check-in.', 'regular', 'admin', 'admin', uuid_generate_v4()),
(uuid_generate_v4(), '2fa85f64-5717-4562-b3fc-2c963f66afa7', uuid_generate_v4(),uuid_generate_v4(), '2024-07-02 11:00:00', 'Confirmed', '2024-07-02 11:00:00', '2024-07-02 11:00:00', FALSE, TRUE, '', 'regular', 'admin', 'admin', uuid_generate_v4()),
(uuid_generate_v4(), '1fa85f64-5717-4562-b3fc-2c963f66afa8', uuid_generate_v4(),uuid_generate_v4(), '2024-07-02 11:00:00', 'Pending', '2024-07-02 09:30:00', '2024-07-02 09:30:00', FALSE, FALSE, '', 'regular', 'admin', 'admin', uuid_generate_v4()),
(uuid_generate_v4(), '1fa85f64-5717-4562-b3fc-2c963f66afa8', uuid_generate_v4(),uuid_generate_v4(), '2024-07-02 11:00:00', 'Pending', '2024-07-02 09:45:00', '2024-07-02 09:45:00', FALSE, FALSE, '', 'regular', 'admin', 'admin', uuid_generate_v4());
