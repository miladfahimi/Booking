-- Insert data into app_user table
INSERT INTO app_user (username, password, email, phone, first_name, last_name, role, otp, otp_expiration_time) VALUES
('john_doe', '$2a$10$4Xi2lPlzJwVIaTtNVVeYBOJV39OSS/8czwyu1GH/TRwcXCsRqH7/q', 'john@example.com', '1234567890', 'John', 'Doe', 'USER', NULL, NULL),
('David_jackson', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'david@example.com', '1111567890', 'David', 'Jackson', 'ADMIN', NULL, NULL),
('Ali_koorvet', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'ali@example.com', '122227890', 'Ali', 'Koorvet', 'USER', NULL, NULL);
