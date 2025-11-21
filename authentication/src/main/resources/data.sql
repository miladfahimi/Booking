TRUNCATE TABLE refresh_token CASCADE;
TRUNCATE TABLE verification_token CASCADE;
TRUNCATE TABLE app_user CASCADE;

-- Insert data into app_user table
INSERT INTO app_user (id, username, email, phone, password, role, created_at, last_login, otp, otp_expiration_time) VALUES
(uuid_generate_v4(), 'john_doe', 'john_doe@example.com', '1111111111', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'USER', NOW(), NULL, NULL, NULL),
(uuid_generate_v4(), '2', '2@me.com', '222222222', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'USER', NOW(), NULL, NULL, NULL),
(uuid_generate_v4(), 'jane_smith', 'jane_smith@example.com', '2222222222', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'USER', NOW(), NULL, NULL, NULL),
(uuid_generate_v4(), 'david_jackson', 'david_jackson@example.com', '3333333333', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'PROVIDER_OWNER', NOW(), NULL, NULL, NULL),
(uuid_generate_v4(), 'emma_jones', 'emma_jones@example.com', '4444444444', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'PROVIDER_OWNER', NOW(), NULL, NULL, NULL),
(uuid_generate_v4(), 'admin', 'admin@example.com', '5555555555', '$2a$10$06UBqKaMpOkdfjHQVeJ9k.Z2OG0ImrPIA2l2vJ3H0U8E05gn48yVS', 'ADMIN', NOW(), NULL, NULL, NULL);

-- Seed verification tokens tied to specific users
INSERT INTO verification_token (id, token, expiry_date, used, created_at, updated_at, consumed_at, user_id)
SELECT uuid_generate_v4(), 'verify-john-token', NOW() + INTERVAL '2 days', FALSE, NOW(), NOW(), NULL, id
FROM app_user WHERE username = 'john_doe';

INSERT INTO verification_token (id, token, expiry_date, used, created_at, updated_at, consumed_at, user_id)
SELECT uuid_generate_v4(), 'verify-jane-token', NOW() + INTERVAL '2 days', FALSE, NOW(), NOW(), NULL, id
FROM app_user WHERE username = 'jane_smith';

-- Seed refresh tokens with long-lived expirations and active status
INSERT INTO refresh_token (id, token, expiry_date, revoked, remember_me, created_at, updated_at, user_id)
SELECT uuid_generate_v4(), 'refresh-john-token', NOW() + INTERVAL '30 days', FALSE, FALSE, NOW(), NOW(), id
FROM app_user WHERE username = 'john_doe';

INSERT INTO refresh_token (id, token, expiry_date, revoked, remember_me, created_at, updated_at, user_id)
SELECT uuid_generate_v4(), 'refresh-admin-token', NOW() + INTERVAL '30 days', FALSE, FALSE, NOW(), NOW(), id
FROM app_user WHERE username = 'admin';