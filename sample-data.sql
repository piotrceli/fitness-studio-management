use fitness_studio_management;

INSERT INTO role (name)
VALUES ('ADMIN'), ('USER');

INSERT INTO app_user (username, password, first_name, last_name, email, date_of_birth, enabled)
VALUES ('admin', '$2a$10$RCisbeq2PYJyQ6NKD17GK.SG3WsxvGUYIvn4YefKefeW/BThBwb6S', 'admin', 'admin', 'admin@email.com', '1987-02-02', 1),
('user', '$2a$10$Hmqwxieg4dBZC/aJbfPcjulosT31ld8FZd7BvP.LFUrFC12TCzQtO', 'user', 'user', 'user@email.com', '1976-03-01', 1),
('tom', '$2a$10$W6YnXBnxAGIDkK/wTpwwpuS.4CIBd7NsnNcfYpwVln4GymPFLTHIm', 'tom', 'jones', 'tom@email.com', '2000-01-14', 1),
('anna', '$2a$10$Ptk32HOIvBntwgQ3dvw1Ouev1wZQqEV7RcmjfJYs3wTz9VbVC0BlK', 'anna', 'smith', 'anna@email.com', '1988-07-11', 1),
('brian', '$2a$10$Zwo9jcdFvH2m7dCr2LmesOJatKAn2sRs23lu7fwUhrwRB1X0dItAi', 'brian', 'johnson', 'brian@email.com', '1976-02-19', 1),
('oliver', '$2a$10$5gNu4cr/IP9v1dO/Fwrahuc9zNimF8jsCofjn0p32adMd8/lYSIma', 'oliver', 'williams', 'oliver@email.com', '1999-03-12', 1),
('harper', '$2a$10$xYQxK8F.YFykYmhgpEfuKOIbs.Qr1exZ70iy96Qk/uYK/7.MXcyre', 'harper', 'brown', 'harper@email.com', '1988-07-11', 1),
('mia', '$2a$10$RYknGe.77A8bwqSsCEXVyuPCC1ro50ko3oHDNIWAgxSZ8bTX9KxaG', 'mia', 'miller', 'mia@email.com', '1985-04-11', 1),
('emma', '$2a$10$204hXd.8/pSfGRoKyOxoy.RmqD/4dQmnKFJBqj.hO0oNGxwbkDxRq', 'emma', 'davis', 'emma@email.com', '1975-05-01', 1),
('charlotte', '$2a$10$3AiGJlZ2vghDPG4mrnXQRuvHoZsd3dcOYr5HavkcsTXnsnAAH31YC', 'charlotte', 'martinez', 'charlotte@email.com', '2001-03-10', 1),
('sophia', '$2a$10$fv/eZeI/blOLCTJlb54jj.0fC83k.PcWMyVUZYQLM4Xp/c6BuZEWm', 'sophia', 'lopez', 'sophia@email.com', '1976-12-11', 1);

INSERT INTO user_role 
VALUES (1,1), (2,2), (3,2), (4,2), (5,2), (6,2), (7,2), (8,2), (9,2), (10,2), (11,2);

INSERT INTO trainer (first_name, last_name, email, description)
VALUES('james', 'taylor', 'james@gmail.com', 'crossfit trainer with 10 years of experience'),
('noah', 'white', 'noah@gmail.com', 'yoga trainer'),
('amelia', 'harris', 'amelia@gmail.com', 'personal trainer'),
('ava', 'lewis', 'ava@gmail.com', 'boxing trainer'),
('olivia', 'sanchez', 'olivia@gmail.com', 'cycling trainer');

INSERT INTO fitness_class (name, difficulty_level, description)
VALUES ('Crossfit', 1, 'crossfit class for intermadiate'),
('Yoga', 2, 'yoga class for advanced'),
('FBW', 0, 'full body workout class for beginners'),
('Boxing', 0, 'boxing class for beginners'),
('Cycling', 2, 'cycling class for advanced');

INSERT INTO fitness_class_trainer
VALUES (1,1), (2,2), (3,3), (4,4), (5,5);

INSERT INTO gym_event (start_time, end_time, duration, participants_limit, current_participants_number, fitness_class_id)
VALUES ('2022-12-10 12:00', '2022-12-10 13:00', '01:00', 10, 5, 1),
('2022-12-11 14:00', '2022-12-11 16:00', '02:00', 5, 4, 2),
('2022-12-11 15:30', '2022-12-11 17:00', '01:30', 5, 2, 3),
('2022-12-12 09:00', '2022-12-12 10:30', '01:30', 10, 1, 4),
('2022-12-12 12:00', '2022-12-12 13:00', '01:00', 10, 0, 5);

INSERT INTO event_user 
VALUES (1, 8), (1, 2), (1, 3), (1, 4), (1, 5), 
(2, 9), (2, 6), (2, 7), (2, 10),
(3, 9), (3, 11), 
(4, 2);



















