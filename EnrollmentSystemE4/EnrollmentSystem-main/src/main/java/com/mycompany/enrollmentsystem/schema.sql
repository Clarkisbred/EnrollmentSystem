-- Run this once against your local MySQL server before starting the app.

CREATE DATABASE IF NOT EXISTS enrollmentsystem;
USE enrollmentsystem;

CREATE TABLE IF NOT EXISTS students (
    studid      VARCHAR(20) PRIMARY KEY,
    studname    VARCHAR(100) NOT NULL,
    studadd     VARCHAR(150),
    studcourse  VARCHAR(100),
    studgender  VARCHAR(10),
    yrlvl       VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS teachers (
    teachid      VARCHAR(20) PRIMARY KEY,
    teachname    VARCHAR(100) NOT NULL,
    teachadd     VARCHAR(150),
    subject      VARCHAR(100),
    teachgender  VARCHAR(10)
);

-- Grant Teachers full rights EXCEPT DELETE on ALL databases
CREATE USER IF NOT EXISTS 'teacher_user'@'localhost' IDENTIFIED BY 'teacher_password';
GRANT SELECT, INSERT, UPDATE ON *.* TO 'teacher_user'@'localhost';

-- Grant Students SELECT rights ONLY on ALL databases
CREATE USER IF NOT EXISTS 'student_user'@'localhost' IDENTIFIED BY 'student_password';
GRANT SELECT ON *.* TO 'student_user'@'localhost';

FLUSH PRIVILEGES;
