-- Users
INSERT INTO users (username, password, role) VALUES ('alice', '{noop}password', 'ROLE_USER');
INSERT INTO users (username, password, role) VALUES ('bob', '{noop}password', 'ROLE_USER');

-- Movies
INSERT INTO movies (title, language, genre) VALUES ('Inception', 'English', 'Sci-Fi');
INSERT INTO movies (title, language, genre) VALUES ('The Dark Knight', 'English', 'Action');

-- Cinemas
INSERT INTO cinemas (name, location, latitude, longitude) VALUES ('PVR Cinemas', 'Mumbai', 19.0760, 72.8777);
INSERT INTO cinemas (name, location, latitude, longitude) VALUES ('INOX', 'Delhi', 28.7041, 77.1025);

-- Shows
-- Show 1: Inception at PVR
INSERT INTO shows (movie_id, cinema_id, start_time) VALUES (1, 1, '2026-10-01 18:00:00');
-- Show 2: The Dark Knight at PVR
INSERT INTO shows (movie_id, cinema_id, start_time) VALUES (2, 1, '2026-10-01 21:00:00');
-- Show 3: Inception at INOX
INSERT INTO shows (movie_id, cinema_id, start_time) VALUES (1, 2, '2026-10-02 19:00:00');

-- Seats for Show 1 (Inception at PVR)
INSERT INTO seats (show_id, seat_number, status) VALUES (1, 'A1', 'AVAILABLE');
INSERT INTO seats (show_id, seat_number, status) VALUES (1, 'A2', 'AVAILABLE');
INSERT INTO seats (show_id, seat_number, status) VALUES (1, 'A3', 'AVAILABLE');

-- Seats for Show 2 (The Dark Knight at PVR)
INSERT INTO seats (show_id, seat_number, status) VALUES (2, 'A1', 'AVAILABLE');
INSERT INTO seats (show_id, seat_number, status) VALUES (2, 'A2', 'AVAILABLE');

-- Seats for Show 3 (Inception at INOX)
INSERT INTO seats (show_id, seat_number, status) VALUES (3, 'VIP1', 'AVAILABLE');
INSERT INTO seats (show_id, seat_number, status) VALUES (3, 'VIP2', 'AVAILABLE');
