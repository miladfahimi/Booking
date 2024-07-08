CREATE TABLE app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(50),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    role VARCHAR(50)
);

CREATE TABLE club (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(50),
    email VARCHAR(100),
    description TEXT
);

CREATE TABLE item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE court (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50),
    availability BOOLEAN,
    club_id BIGINT,
    FOREIGN KEY (club_id) REFERENCES club(id)
);

CREATE TABLE FEEDBACK (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment VARCHAR(255),
    rating INT,
    created_at TIMESTAMP,
    club_id BIGINT,
    court_id BIGINT,
    FOREIGN KEY (club_id) REFERENCES club(id),
    FOREIGN KEY (court_id) REFERENCES court(id)
);


CREATE TABLE reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_date TIMESTAMP,
    start_time TIME,
    end_time TIME,
    status VARCHAR(50),
    court_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (court_id) REFERENCES court(id),
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);