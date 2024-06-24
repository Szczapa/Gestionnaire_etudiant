CREATE SCHEMA IF NOT EXISTS Student;

CREATE TABLE IF NOT EXISTS Student.Student
(
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    classNumber INT NOT NULL,
    diplomaDate DATE NOT NULL
);

INSERT INTO Student.Student (first_name, last_name, classNumber, diplomaDate)
VALUES ('John', 'Doe', 1, '2022-06-01'),
       ('Jane', 'Doe', 1, '2022-06-01'),
       ('Jim', 'Brown', 1, '2022-06-01'),
       ('Jill', 'Smith', 1, '2022-06-01'),
       ('Jack', 'Johnson', 1, '2022-06-01'),
       ('Julia', 'Davis', 2, '2023-06-01'),
       ('Jerry', 'Miller', 2, '2023-06-01'),
       ('Jenny', 'Wilson', 2, '2023-06-01'),
       ('Jeff', 'Moore', 2, '2023-06-01'),
       ('Jasmine', 'Taylor', 2, '2023-06-01'),
       ('Jacob', 'Anderson', 3, '2024-06-01'),
       ('Joseph', 'Thomas', 3, '2024-06-01'),
       ('Joshua', 'Jackson', 3, '2024-06-01'),
       ('James', 'White', 3, '2024-06-01'),
       ('Jessica', 'Harris', 3, '2024-06-01');