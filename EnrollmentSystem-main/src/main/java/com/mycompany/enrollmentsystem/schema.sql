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
