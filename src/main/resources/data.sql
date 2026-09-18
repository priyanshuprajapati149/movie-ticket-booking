-- Insert Users (Passwords are '{noop}password' to allow plain text password for demo purposes)
INSERT INTO users (username, password, role) VALUES ('alice', '{noop}password', 'ROLE_USER');
INSERT INTO users (username, password, role) VALUES ('bob', '{noop}password', 'ROLE_USER');

-- Insert Seats
INSERT INTO seats (seat_number, status) VALUES ('A1', 'AVAILABLE');
INSERT INTO seats (seat_number, status) VALUES ('A2', 'AVAILABLE');
INSERT INTO seats (seat_number, status) VALUES ('A3', 'AVAILABLE');
INSERT INTO seats (seat_number, status) VALUES ('B1', 'AVAILABLE');
INSERT INTO seats (seat_number, status) VALUES ('B2', 'AVAILABLE');
