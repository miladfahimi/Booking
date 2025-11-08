-- Insert data into provider table
INSERT INTO provider (id, name, address, phone, email, description, rating, logo_url, created_at, updated_at, status, social_links) VALUES
-- Hardcoded Provider for Testing
('11111111-1111-1111-1111-111111111111', 'تنیس ارائه دهنده تست', 'خیابان اصلی ۱', '3334567890', 'testprovider@example.com', 'ارائه دهنده برتر تنیس برای تست', 5.0, '/images/test_provider_logo.png', NOW(), NOW(), 'active', '{"facebook": "http://facebook.com/testprovider", "instagram": "http://instagram.com/testprovider"}'),
-- Additional Providers
(uuid_generate_v4(), 'تنیس ارائه دهنده ۱', 'خیابان مرکزی ۱۲۳', '3334567890', 'provider1@example.com', 'بهترین ارائه دهنده در شهر', 4.5, '/images/provider1_logo.png', NOW(), NOW(), 'active', '{"facebook": "http://facebook.com/provider1"}'),
(uuid_generate_v4(), 'تنیس ارائه دهنده ۲', 'خیابان فرعی ۴۵۶', '1114567890', 'provider2@example.com', 'یک ارائه دهنده بزرگ در شهر غرب', 4.0, '/images/provider2_logo.png', NOW(), NOW(), 'active', '{"twitter": "http://twitter.com/provider2"}'),
(uuid_generate_v4(), 'تنیس ارائه دهنده ۳', 'خیابان جدید ۷۸۹', '2224567890', 'provider3@example.com', 'ارائه دهنده مسابقات', 3.8, '/images/provider3_logo.png', NOW(), NOW(), 'active', '{"instagram": "http://instagram.com/provider3"}'),
(uuid_generate_v4(), 'تنیس ارائه دهنده ۴', 'خیابان قدیمی ۳۲۱', '4444567890', 'provider4@example.com', 'ارائه دهنده تخصصی تنیس خاک رس', 4.7, '/images/provider4_logo.png', NOW(), NOW(), 'active', '{"linkedin": "http://linkedin.com/provider4"}'),
(uuid_generate_v4(), 'تنیس ارائه دهنده ۵', 'خیابان کنار دریا ۶۵۴', '5554567890', 'provider5@example.com', 'ارائه دهنده تنیس چمن مصنوعی', 4.3, '/images/provider5_logo.png', NOW(), NOW(), 'active', '{"facebook": "http://facebook.com/provider5"}');

-- Insert data into service table
INSERT INTO service (id, name, type, availability, provider_id, price, currency, duration, start_time, end_time, slot_duration, slot_gap_duration, image_url, rating, created_at, updated_at, status, max_capacity, tags) VALUES
-- Services for Hardcoded Provider (تنیس ارائه دهنده تست)
(uuid_generate_v4(), 'زمین ۱', 'خاک رس', true, '11111111-1111-1111-1111-111111111111', 100.00, 'USD', 60, '08:00:00', '18:00:00', 30, 0, '/images/service1.png', 4.5, NOW(), NOW(), 'available', 10, '["مبتدی", "بیرون"]'),
(uuid_generate_v4(), 'زمین ۲', 'چمن مصنوعی', true, '11111111-1111-1111-1111-111111111111', 120.00, 'USD', 90, '09:00:00', '20:00:00', 45, 0, '/images/service2.png', 4.2, NOW(), NOW(), 'available', 15, '["پیشرفته", "بیرون"]'),
-- Services for Additional Providers
(uuid_generate_v4(), 'زمین ۳', 'خاک رس', true, '11111111-1111-1111-1111-111111111111', 110.00, 'USD', 75, '07:00:00', '19:00:00', 30, 0, '/images/service3.png', 4.0, NOW(), NOW(), 'available', 12, '["مبتدی", "بیرون"]'),
(uuid_generate_v4(), 'زمین ۴', 'چمن مصنوعی', true, '11111111-1111-1111-1111-111111111111', 130.00, 'USD', 60, '10:00:00', '22:00:00', 30, 0, '/images/service4.png', 4.1, NOW(), NOW(), 'available', 20, '["پیشرفته", "بیرون"]'),
(uuid_generate_v4(), 'زمین ۵', 'خاک رس', true, '11111111-1111-1111-1111-111111111111', 95.00, 'USD', 50, '06:00:00', '16:00:00', 25, 5, '/images/service5.png', 3.9, NOW(), NOW(), 'available', 8, '["مبتدی", "بیرون"]'),
(uuid_generate_v4(), 'زمین ۶', 'چمن مصنوعی', true, '11111111-1111-1111-1111-111111111111', 140.00, 'USD', 120, '11:00:00', '23:00:00', 60, 0, '/images/service6.png', 4.6, NOW(), NOW(), 'available', 25, '["پیشرفته", "بیرون"]'),
(uuid_generate_v4(), 'زمین ۷', 'خاک رس', true, (SELECT id FROM provider WHERE name = 'تنیس ارائه دهنده ۵'), 105.00, 'USD', 65, '08:30:00', '18:30:00', 35, 0, '/images/service7.png', 4.3, NOW(), NOW(), 'available', 14, '["مبتدی", "بیرون"]');

-- Insert data into feedback table
INSERT INTO feedback (id, comment, rating, created_at, provider_id, service_id) VALUES
(uuid_generate_v4(), 'ارائه دهنده عالی!', 5, '2024-07-01 10:00:00', (SELECT id FROM provider WHERE name = 'تنیس ارائه دهنده تست'), NULL),
(uuid_generate_v4(), 'سرویس خوب', 4, '2024-07-02 11:00:00', NULL, (SELECT id FROM service WHERE name = 'سرویس ۲')),
(uuid_generate_v4(), 'خدمات عالی', 5, NOW(), (SELECT id FROM provider WHERE name = 'تنیس ارائه دهنده ۱'), (SELECT id FROM service WHERE name = 'سرویس ۳')),
(uuid_generate_v4(), 'تجربه خوب', 4, NOW(), (SELECT id FROM provider WHERE name = 'تنیس ارائه دهنده ۲'), (SELECT id FROM service WHERE name = 'سرویس ۴')),
(uuid_generate_v4(), 'سرویس قابل قبول', 3, NOW(), (SELECT id FROM provider WHERE name = 'تنیس ارائه دهنده ۳'), (SELECT id FROM service WHERE name = 'سرویس ۵')),
(uuid_generate_v4(), 'عالی بودن سرویس', 5, NOW(), (SELECT id FROM provider WHERE name = 'تنیس ارائه دهنده ۴'), (SELECT id FROM service WHERE name = 'سرویس ۶')),
(uuid_generate_v4(), 'خیلی خوب', 4, NOW(), (SELECT id FROM provider WHERE name = 'تنیس ارائه دهنده ۵'), (SELECT id FROM service WHERE name = 'سرویس ۷'));