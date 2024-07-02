CREATE TABLE CLUB (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    owner VARCHAR(255),
    address VARCHAR(255),
    description VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(20)
);

CREATE TABLE APP_USER (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    role VARCHAR(50)
);

CREATE TABLE COURT (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50),
    availability BOOLEAN,
    club_id BIGINT,
    FOREIGN KEY (club_id) REFERENCES CLUB(id)
);

CREATE TABLE RESERVATION (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_date DATE,
    start_time TIME,
    end_time TIME,
    status VARCHAR(50),
    user_id BIGINT,
    court_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES APP_USER(id),
    FOREIGN KEY (court_id) REFERENCES COURT(id)
);
