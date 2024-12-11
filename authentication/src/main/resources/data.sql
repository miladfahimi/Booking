-- Insert data into app_user table
INSERT INTO app_user (id, username, email, phone, password, role, created_at, last_login, otp, otp_expiration_time) VALUES
(uuid_generate_v4(), 'john_doe', 'john_doe@example.com', '1111111111', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'USER', NOW(), NULL, NULL, NULL),
(uuid_generate_v4(), '2', '2@me.com', '222222222', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'USER', NOW(), NULL, NULL, NULL),
(uuid_generate_v4(), 'jane_smith', 'jane_smith@example.com', '2222222222', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'USER', NOW(), NULL, NULL, NULL),
(uuid_generate_v4(), 'david_jackson', 'david_jackson@example.com', '3333333333', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'PROVIDER_OWNER', NOW(), NULL, NULL, NULL),
(uuid_generate_v4(), 'emma_jones', 'emma_jones@example.com', '4444444444', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'PROVIDER_OWNER', NOW(), NULL, NULL, NULL),
(uuid_generate_v4(), 'admin', 'admin@example.com', '5555555555', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'ADMIN', NOW(), NULL, NULL, NULL);
