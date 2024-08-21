-- Insert data into reservation table for Service 1 (زمین 1)
INSERT INTO reservation (reservation_date, start_time, end_time, status, user_id, service_id, provider_id) VALUES
-- زمین 1 (Provider f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9)
(CURRENT_DATE + INTERVAL '1 day', '06:00:00', '07:00:00', 'CONFIRMED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', '13dd0543-ab7a-428d-a6a3-5d6ade751e9f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '7 day', '07:00:00', '08:00:00', 'PENDING', '60ab16a5-4469-41e3-8833-7c8a5eef5956', '13dd0543-ab7a-428d-a6a3-5d6ade751e9f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '2 day', '08:00:00', '09:00:00', 'CANCELED', 'c40ecaa8-2b08-4e15-bb01-19a9cf7fdc65', '13dd0543-ab7a-428d-a6a3-5d6ade751e9f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '3 day', '09:00:00', '10:00:00', 'CONFIRMED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', '13dd0543-ab7a-428d-a6a3-5d6ade751e9f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '3 day', '10:00:00', '11:00:00', 'CONFIRMED', '67fa8970-a87b-4845-9fb8-20140e014470', '13dd0543-ab7a-428d-a6a3-5d6ade751e9f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '4 day', '11:00:00', '12:00:00', 'PENDING', '60ab16a5-4469-41e3-8833-7c8a5eef5956', '13dd0543-ab7a-428d-a6a3-5d6ade751e9f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '4 day', '12:00:00', '13:00:00', 'CANCELED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', '13dd0543-ab7a-428d-a6a3-5d6ade751e9f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '3 day', '17:00:00', '18:00:00', 'CONFIRMED', 'c40ecaa8-2b08-4e15-bb01-19a9cf7fdc65', '13dd0543-ab7a-428d-a6a3-5d6ade751e9f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '6 day', '14:00:00', '15:00:00', 'PENDING', '67fa8970-a87b-4845-9fb8-20140e014470', '13dd0543-ab7a-428d-a6a3-5d6ade751e9f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '6 day', '15:00:00', '16:00:00', 'CONFIRMED', '60ab16a5-4469-41e3-8833-7c8a5eef5956', '13dd0543-ab7a-428d-a6a3-5d6ade751e9f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '1 day', '16:00:00', '17:00:00', 'CANCELED', 'c40ecaa8-2b08-4e15-bb01-19a9cf7fdc65', '13dd0543-ab7a-428d-a6a3-5d6ade751e9f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '7 day', '17:00:00', '18:00:00', 'CONFIRMED', '67fa8970-a87b-4845-9fb8-20140e014470', '13dd0543-ab7a-428d-a6a3-5d6ade751e9f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9');

-- Insert data into reservation table for Service 2 (زمین 2)
INSERT INTO reservation (reservation_date, start_time, end_time, status, user_id, service_id, provider_id) VALUES
-- زمین 2 (Provider f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9)
(CURRENT_DATE + INTERVAL '1 day', '08:00:00', '09:00:00', 'CONFIRMED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', '54a6935d-2492-4eed-901c-77b12e93bf57', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '7 day', '19:00:00', '20:00:00', 'PENDING', '60ab16a5-4469-41e3-8833-7c8a5eef5956', '54a6935d-2492-4eed-901c-77b12e93bf57', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '4 day', '14:00:00', '15:00:00', 'CANCELED', 'c40ecaa8-2b08-4e15-bb01-19a9cf7fdc65', '54a6935d-2492-4eed-901c-77b12e93bf57', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '2 day', '11:00:00', '12:00:00', 'CONFIRMED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', '54a6935d-2492-4eed-901c-77b12e93bf57', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '3 day', '12:00:00', '13:00:00', 'CONFIRMED', '67fa8970-a87b-4845-9fb8-20140e014470', '54a6935d-2492-4eed-901c-77b12e93bf57', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '7 day', '13:00:00', '14:00:00', 'PENDING', '60ab16a5-4469-41e3-8833-7c8a5eef5956', '54a6935d-2492-4eed-901c-77b12e93bf57', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '4 day', '14:00:00', '15:00:00', 'CANCELED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', '54a6935d-2492-4eed-901c-77b12e93bf57', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '5 day', '15:00:00', '16:00:00', 'CONFIRMED', 'c40ecaa8-2b08-4e15-bb01-19a9cf7fdc65', '54a6935d-2492-4eed-901c-77b12e93bf57', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '1 day', '16:00:00', '17:00:00', 'PENDING', '67fa8970-a87b-4845-9fb8-20140e014470', '54a6935d-2492-4eed-901c-77b12e93bf57', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '6 day', '17:00:00', '18:00:00', 'CONFIRMED', '60ab16a5-4469-41e3-8833-7c8a5eef5956', '54a6935d-2492-4eed-901c-77b12e93bf57', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '1 day', '10:00:00', '11:00:00', 'CANCELED', 'c40ecaa8-2b08-4e15-bb01-19a9cf7fdc65', '54a6935d-2492-4eed-901c-77b12e93bf57', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '7 day', '19:00:00', '20:00:00', 'CONFIRMED', '67fa8970-a87b-4845-9fb8-20140e014470', '54a6935d-2492-4eed-901c-77b12e93bf57', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9');

-- Insert data into reservation table for Service 3 (زمین 3)
INSERT INTO reservation (reservation_date, start_time, end_time, status, user_id, service_id, provider_id) VALUES
-- زمین 3 (Provider f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9)
(CURRENT_DATE + INTERVAL '1 day', '10:00:00', '11:00:00', 'CONFIRMED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', '567cb1bc-471a-43aa-bb6e-afabacbf8074', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '6 day', '14:00:00', '15:00:00', 'PENDING', '60ab16a5-4469-41e3-8833-7c8a5eef5956', '567cb1bc-471a-43aa-bb6e-afabacbf8074', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '2 day', '12:00:00', '13:00:00', 'CANCELED', 'c40ecaa8-2b08-4e15-bb01-19a9cf7fdc65', '567cb1bc-471a-43aa-bb6e-afabacbf8074', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '6 day', '13:00:00', '14:00:00', 'CONFIRMED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', '567cb1bc-471a-43aa-bb6e-afabacbf8074', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '3 day', '14:00:00', '15:00:00', 'CONFIRMED', '67fa8970-a87b-4845-9fb8-20140e014470', '567cb1bc-471a-43aa-bb6e-afabacbf8074', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '4 day', '10:00:00', '11:00:00', 'PENDING', '60ab16a5-4469-41e3-8833-7c8a5eef5956', '567cb1bc-471a-43aa-bb6e-afabacbf8074', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '4 day', '16:00:00', '17:00:00', 'CANCELED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', '567cb1bc-471a-43aa-bb6e-afabacbf8074', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '5 day', '17:00:00', '18:00:00', 'CONFIRMED', 'c40ecaa8-2b08-4e15-bb01-19a9cf7fdc65', '567cb1bc-471a-43aa-bb6e-afabacbf8074', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '1 day', '18:00:00', '19:00:00', 'PENDING', '67fa8970-a87b-4845-9fb8-20140e014470', '567cb1bc-471a-43aa-bb6e-afabacbf8074', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '6 day', '19:00:00', '20:00:00', 'CONFIRMED', '60ab16a5-4469-41e3-8833-7c8a5eef5956', '567cb1bc-471a-43aa-bb6e-afabacbf8074', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9');

-- Insert data into reservation table for Service 4 (زمین 4)
INSERT INTO reservation (reservation_date, start_time, end_time, status, user_id, service_id, provider_id) VALUES
-- زمین 4 (Provider f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9)
(CURRENT_DATE + INTERVAL '1 day', '08:00:00', '09:00:00', 'CONFIRMED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', 'c6307f44-a11d-44f9-89be-b960aa18d4a4', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '1 day', '07:00:00', '08:00:00', 'PENDING', '60ab16a5-4469-41e3-8833-7c8a5eef5956', 'c6307f44-a11d-44f9-89be-b960aa18d4a4', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '2 day', '08:00:00', '09:00:00', 'CANCELED', 'c40ecaa8-2b08-4e15-bb01-19a9cf7fdc65', 'c6307f44-a11d-44f9-89be-b960aa18d4a4', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '7 day', '19:00:00', '20:00:00', 'CONFIRMED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', 'c6307f44-a11d-44f9-89be-b960aa18d4a4', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '3 day', '10:00:00', '11:00:00', 'CONFIRMED', '67fa8970-a87b-4845-9fb8-20140e014470', 'c6307f44-a11d-44f9-89be-b960aa18d4a4', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '1 day', '11:00:00', '12:00:00', 'PENDING', '60ab16a5-4469-41e3-8833-7c8a5eef5956', 'c6307f44-a11d-44f9-89be-b960aa18d4a4', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '4 day', '12:00:00', '13:00:00', 'CANCELED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', 'c6307f44-a11d-44f9-89be-b960aa18d4a4', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '5 day', '13:00:00', '14:00:00', 'CONFIRMED', 'c40ecaa8-2b08-4e15-bb01-19a9cf7fdc65', 'c6307f44-a11d-44f9-89be-b960aa18d4a4', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '3 day', '14:00:00', '15:00:00', 'PENDING', '67fa8970-a87b-4845-9fb8-20140e014470', 'c6307f44-a11d-44f9-89be-b960aa18d4a4', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '6 day', '10:00:00', '11:00:00', 'CONFIRMED', '60ab16a5-4469-41e3-8833-7c8a5eef5956', 'c6307f44-a11d-44f9-89be-b960aa18d4a4', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '1 day', '16:00:00', '17:00:00', 'CANCELED', 'c40ecaa8-2b08-4e15-bb01-19a9cf7fdc65', 'c6307f44-a11d-44f9-89be-b960aa18d4a4', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '7 day', '17:00:00', '18:00:00', 'CONFIRMED', '67fa8970-a87b-4845-9fb8-20140e014470', 'c6307f44-a11d-44f9-89be-b960aa18d4a4', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9');

-- Insert data into reservation table for Service 5 (زمین 5)
INSERT INTO reservation (reservation_date, start_time, end_time, status, user_id, service_id, provider_id) VALUES
-- زمین 5 (Provider f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9)
(CURRENT_DATE + INTERVAL '1 day', '06:00:00', '07:00:00', 'CONFIRMED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', 'e20d1434-4d4b-4e2e-8d58-017e772c5b2f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '7 day', '17:00:00', '18:00:00', 'PENDING', '60ab16a5-4469-41e3-8833-7c8a5eef5956', 'e20d1434-4d4b-4e2e-8d58-017e772c5b2f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '2 day', '08:00:00', '09:00:00', 'CANCELED', 'c40ecaa8-2b08-4e15-bb01-19a9cf7fdc65', 'e20d1434-4d4b-4e2e-8d58-017e772c5b2f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '6 day', '19:00:00', '20:00:00', 'CONFIRMED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', 'e20d1434-4d4b-4e2e-8d58-017e772c5b2f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '3 day', '10:00:00', '11:00:00', 'CONFIRMED', '67fa8970-a87b-4845-9fb8-20140e014470', 'e20d1434-4d4b-4e2e-8d58-017e772c5b2f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '1 day', '11:00:00', '12:00:00', 'PENDING', '60ab16a5-4469-41e3-8833-7c8a5eef5956', 'e20d1434-4d4b-4e2e-8d58-017e772c5b2f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '4 day', '12:00:00', '13:00:00', 'CANCELED', '769df378-2cf1-411f-b5f5-0c69cb7d4016', 'e20d1434-4d4b-4e2e-8d58-017e772c5b2f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '7 day', '10:00:00', '11:00:00', 'CONFIRMED', 'c40ecaa8-2b08-4e15-bb01-19a9cf7fdc65', 'e20d1434-4d4b-4e2e-8d58-017e772c5b2f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '6 day', '14:00:00', '15:00:00', 'PENDING', '67fa8970-a87b-4845-9fb8-20140e014470', 'e20d1434-4d4b-4e2e-8d58-017e772c5b2f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '6 day', '15:00:00', '16:00:00', 'CONFIRMED', '60ab16a5-4469-41e3-8833-7c8a5eef5956', 'e20d1434-4d4b-4e2e-8d58-017e772c5b2f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '1 day', '16:00:00', '17:00:00', 'CANCELED', 'c40ecaa8-2b08-4e15-bb01-19a9cf7fdc65', 'e20d1434-4d4b-4e2e-8d58-017e772c5b2f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9'),
(CURRENT_DATE + INTERVAL '7 day', '17:00:00', '18:00:00', 'CONFIRMED', '67fa8970-a87b-4845-9fb8-20140e014470', 'e20d1434-4d4b-4e2e-8d58-017e772c5b2f', 'f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9');
