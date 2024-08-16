-- Insert data into provider table
INSERT INTO provider (id, name, address, phone, email, description, rating, logo_url, created_at, updated_at, status, social_links) VALUES
(uuid_generate_v4(), 'Tennis Provider 1', '123 Main St', '3334567890', 'provider1@example.com', 'Best provider in town', 4.5, '/images/provider1_logo.png', NOW(), NOW(), 'active', '{"facebook": "http://facebook.com/provider1"}'),
(uuid_generate_v4(), 'Tennis Provider 2', '456 Main St', '1114567890', 'provider2@example.com', 'A big tennis provider in the west city', 4.0, '/images/provider2_logo.png', NOW(), NOW(), 'active', '{"twitter": "http://twitter.com/provider2"}'),
(uuid_generate_v4(), 'Tennis Provider 3', '789 Main St', '2224567890', 'provider3@example.com', 'Tournament provider', 3.8, '/images/provider3_logo.png', NOW(), NOW(), 'active', '{"instagram": "http://instagram.com/provider3"}');

-- Insert data into service table
INSERT INTO service (id, name, type, availability, provider_id, price, currency, duration, image_url, rating, created_at, updated_at, status, max_capacity, tags) VALUES
(uuid_generate_v4(),'Service 1', 'CLAY', true, (SELECT id FROM provider WHERE name = 'Tennis Provider 1'), 100.00, 'USD', 60, '/images/service1.png', 4.5, NOW(), NOW(), 'available', 10, '["beginner", "outdoor"]'),
(uuid_generate_v4(),'Service 2', 'GRASS', true, (SELECT id FROM provider WHERE name = 'Tennis Provider 2'), 120.00, 'USD', 90, '/images/service2.png', 4.2, NOW(), NOW(), 'available', 15, '["advanced", "outdoor"]');

-- Insert data into feedback table
INSERT INTO feedback (comment, rating, created_at, provider_id, service_id) VALUES
('Great provider!', 5, '2024-07-01 10:00:00',(SELECT id FROM provider WHERE name = 'Tennis Provider 1'), NULL),
('Nice service', 4, '2024-07-02 11:00:00', NULL, (SELECT id FROM provider WHERE name = 'Service 2'));

-- Insert data into reservation table
INSERT INTO reservation (id, reservation_date, start_time, end_time, status, user_id, service_id) VALUES
(uuid_generate_v4(), '2024-07-02', '10:00', '11:00', 'CONFIRMED', '2fa85f64-5717-4562-b3fc-2c963f66afa7', 1),
(uuid_generate_v4(), '2024-07-03', '12:00', '13:00', 'PENDING', '2fa85f64-5717-4562-b3fc-2c963f66afa7', 2);
