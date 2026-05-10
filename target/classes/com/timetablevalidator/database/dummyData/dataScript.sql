-- Create Database
CREATE DATABASE IF NOT EXISTS timetable_validator;
USE timetable_validator;

-- Create Tables
CREATE TABLE IF NOT EXISTS teachers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS rooms (
    id INT PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(50) NOT NULL,
    capacity INT NOT NULL
);

CREATE TABLE IF NOT EXISTS student_groups (
    id INT PRIMARY KEY AUTO_INCREMENT,
    group_name VARCHAR(100) NOT NULL,
    size INT NOT NULL
);

CREATE TABLE IF NOT EXISTS time_slots (
    id INT PRIMARY KEY AUTO_INCREMENT,
    day VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

CREATE TABLE IF NOT EXISTS assignments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(50) NOT NULL,
    course_title VARCHAR(200) NOT NULL,
    teacher_id INT NOT NULL,
    room_id INT NOT NULL,
    student_group_id INT NOT NULL,
    time_slot_id INT NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES teachers(id),
    FOREIGN KEY (room_id) REFERENCES rooms(id),
    FOREIGN KEY (student_group_id) REFERENCES student_groups(id),
    FOREIGN KEY (time_slot_id) REFERENCES time_slots(id)
);

-- Insert Dummy Teachers
INSERT INTO teachers (name, department) VALUES
('Dr. Ahmad Hassan', 'Computer Science'),
('Prof. Sarah Johnson', 'Computer Science'),
('Dr. Muhammad Ali', 'Information Technology'),
('Ms. Emily Chen', 'Software Engineering'),
('Dr. Robert Smith', 'Data Science'),
('Prof. Lisa Anderson', 'Cybersecurity'),
('Dr. James Wilson', 'Database Systems'),
('Ms. Fatima Khan', 'Web Development');

-- Insert Dummy Rooms
INSERT INTO rooms (room_number, capacity) VALUES
('101', 30),
('102', 30),
('103', 50),
('104', 50),
('201', 25),
('202', 25),
('203', 40),
('Lab-A', 20),
('Lab-B', 20),
('Auditorium', 150);

-- Insert Dummy Student Groups
INSERT INTO student_groups (group_name, size) VALUES
('CS-2024-A', 30),
('CS-2024-B', 28),
('CS-2023-A', 25),
('CS-2023-B', 25),
('IT-2024-A', 32),
('IT-2024-B', 30),
('SE-2023-A', 24),
('SE-2023-B', 26);

-- Insert Dummy Time Slots
INSERT INTO time_slots (day, start_time, end_time) VALUES
('Monday', '08:00:00', '09:30:00'),
('Monday', '09:45:00', '11:15:00'),
('Monday', '11:30:00', '13:00:00'),
('Monday', '14:00:00', '15:30:00'),
('Tuesday', '08:00:00', '09:30:00'),
('Tuesday', '09:45:00', '11:15:00'),
('Tuesday', '11:30:00', '13:00:00'),
('Tuesday', '14:00:00', '15:30:00'),
('Wednesday', '08:00:00', '09:30:00'),
('Wednesday', '09:45:00', '11:15:00'),
('Wednesday', '11:30:00', '13:00:00'),
('Wednesday', '14:00:00', '15:30:00'),
('Thursday', '08:00:00', '09:30:00'),
('Thursday', '09:45:00', '11:15:00'),
('Thursday', '11:30:00', '13:00:00'),
('Thursday', '14:00:00', '15:30:00'),
('Friday', '08:00:00', '09:30:00'),
('Friday', '09:45:00', '11:15:00'),
('Friday', '11:30:00', '13:00:00');

-- Insert Dummy Assignments
INSERT INTO assignments (course_code, course_title, teacher_id, room_id, student_group_id, time_slot_id) VALUES
('CS101', 'Introduction to Programming', 1, 1, 1, 1),
('CS101', 'Introduction to Programming', 1, 2, 2, 2),
('CS201', 'Data Structures', 2, 3, 1, 3),
('CS202', 'Algorithms', 2, 3, 2, 4),
('IT101', 'Database Fundamentals', 3, 1, 5, 5),
('IT101', 'Database Fundamentals', 3, 2, 6, 6),
('CS301', 'Web Development', 4, 2, 3, 7),
('DS101', 'Big Data Analytics', 5, 3, 7, 8),
('CS401', 'Advanced Databases', 7, 4, 4, 9),
('SE201', 'Software Design Patterns', 4, 1, 8, 10),
('CS302', 'Operating Systems', 6, 8, 5, 11),
('IT202', 'Network Security', 6, 8, 6, 12),
('CS303', 'Compiler Design', 7, 9, 3, 13),
('SE301', 'Software Testing', 8, 9, 4, 14),
('DS201', 'Machine Learning', 5, 3, 7, 15),
('CS302', 'Artificial Intelligence', 1, 4, 8, 16);

-- Verify Data
SELECT 'Teachers:' AS 'Data Check';
SELECT COUNT(*) FROM teachers;

SELECT 'Rooms:' AS 'Data Check';
SELECT COUNT(*) FROM rooms;

SELECT 'Student Groups:' AS 'Data Check';
SELECT COUNT(*) FROM student_groups;

SELECT 'Time Slots:' AS 'Data Check';
SELECT COUNT(*) FROM time_slots;

SELECT 'Assignments:' AS 'Data Check';
SELECT COUNT(*) FROM assignments;
