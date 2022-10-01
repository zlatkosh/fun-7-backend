-- CREATE TABLES
CREATE TABLE country_data
(
    country_id      SERIAL NOT NULL PRIMARY KEY,
    country_name    TEXT UNIQUE NOT NULL,
    two_letter_code TEXT UNIQUE NOT NULL
);

CREATE TABLE user_data
(
    username   TEXT    NOT NULL PRIMARY KEY,
    password   TEXT    NOT NULL,
    country_id INTEGER NOT NULL,
    time_zone  TEXT    NOT NULL,
    CONSTRAINT fk_user_to_country
        FOREIGN KEY (country_id)
            REFERENCES country_data (country_id)
);

CREATE TABLE role
(
    username    TEXT NOT NULL,
    role_name   TEXT NOT NULL,
    description TEXT,
    PRIMARY KEY (username, role_name),
    CONSTRAINT fk_role_to_user
        FOREIGN KEY (username)
            REFERENCES user_data (username)
);

CREATE UNIQUE INDEX idx_role_username ON role (username, role_name);
--INSERT DATA in country
INSERT INTO country_data (country_name, two_letter_code)
VALUES ('United States of America', 'US');
INSERT INTO country_data (country_name, two_letter_code)
VALUES ('Slovenia', 'SI');
INSERT INTO country_data (country_name, two_letter_code)
VALUES ('Denmark', 'DK');
--INSERT DATA in user_data
INSERT INTO user_data (username, password, country_id, time_zone)
VALUES ('admin_user'::text, '{noop}password'::text, 1, 'America/New_York');
INSERT INTO user_data (username, password, country_id, time_zone)
VALUES ('user_US'::text, '{noop}password_US'::text, 1, 'America/New_York');
INSERT INTO user_data (username, password, country_id, time_zone)
VALUES ('user_SI'::text, '{noop}password_SI'::text, 2, 'Europe/Ljubljana');
--INSERT DATA in role
INSERT INTO role (username, role_name, description)
VALUES ('admin_user', 'ADMIN', 'Admin role having admin API permissions.');
INSERT INTO role (username, role_name, description)
VALUES ('user_US', 'USER', 'User role having Check services API permissions.');
INSERT INTO role (username, role_name, description)
VALUES ('user_SI', 'USER', 'User role having Check services API permissions.');
