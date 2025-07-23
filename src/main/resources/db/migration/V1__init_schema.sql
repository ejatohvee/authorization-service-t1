CREATE TABLE users (
                       id BIGINT PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            roles VARCHAR(255) NOT NULL
);

CREATE TABLE refresh_tokens (
                               id BIGINT PRIMARY KEY,
                               token VARCHAR(255) UNIQUE NOT NULL,
                               user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

INSERT INTO users (id, username, password, enabled) VALUES
                                                    (1, 'admin', '$2a$10$h89Zhrq2pkYJ4E4U1zAKfOK6kZYoWsQbSRFxiU5yPLaC1c2nhYBp2', true),  -- password: admin123
                                                    (2, 'guest', '$2a$10$M1r/f9A86ZqgiDAAOpB7su5JXN0xRiwMghYY2h3SnWS6F5Kh0SiG.', true); -- password: guest123

INSERT INTO user_roles (user_id, roles) VALUES
                                            (1, 'ADMIN'),
                                            (2, 'GUEST');
