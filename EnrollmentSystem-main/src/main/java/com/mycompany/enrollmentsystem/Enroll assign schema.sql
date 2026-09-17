/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  bhilario
 * Created: Sep 17, 2026
 */
-- Run this once in MySQL. It adds the two link tables the new
-- requirement needs, on top of your existing students/subjects/teachers.

USE enrollmentsystem;

-- Student <-> Subject  (drives "Enrolled Subjects" on StudentsForm
-- and "Class List" on SubjectsForm)
CREATE TABLE IF NOT EXISTS enroll (
    eid    INT AUTO_INCREMENT PRIMARY KEY,
    studid INT,
    subjid INT
);

-- Teacher <-> Subject  (drives "Assigned Subjects" on TeachersForm)
CREATE TABLE IF NOT EXISTS assign (
    subjid INT,
    tid    INT
);
