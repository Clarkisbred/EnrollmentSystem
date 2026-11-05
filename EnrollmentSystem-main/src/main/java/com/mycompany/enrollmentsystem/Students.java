/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.enrollmentsystem;
import java.sql.*;
/**
 *
 * @author bhilario
 */

public class Students {

 public int newstudent(String studname, String studadd,
                       String studcrs, String studgender, String yrlvl) {

    EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        int newId = -1;

        try {
            String query = "INSERT INTO students (studname, studadd, studcrs, studgender, yrlvl) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = b.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, studname);
            ps.setString(2, studadd);
            ps.setString(3, studcrs);
            ps.setString(4, studgender);
            ps.setString(5, yrlvl);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    newId = keys.getInt(1);
                    String cleanName = studname.replaceAll("\\s+", "").toLowerCase();
                    String username = newId + cleanName; 
                    String password = cleanName; 
                    String dbName = EnrollmentSystem.db;
                    if (dbName == null || dbName.isEmpty()) {
                        dbName = "enrollment_2026_2027_1stsem";
                        }
                    
                    
                    EnrollmentSystem.grantUserADDED(username, password, dbName, "SELECT"); 
                }
                System.out.println("Student inserted successfully!");
            }
        } catch (Exception ex) {
            System.out.println("Operation failed: " + ex.getMessage());
            ex.printStackTrace();
        }
        return newId;
}

    public void delete_student(int studid) {
    EnrollmentSystem b = new EnrollmentSystem();

    if (!b.DBConnect()) {
        System.out.println("Database connection failed.");
        return;
    }

    try {
        b.con.setAutoCommit(false);

        // Delete grades belonging to this student's enrollments
        String deleteGrades =
            "DELETE FROM grades WHERE enroll_eid IN " +
            "(SELECT eid FROM enroll WHERE studid = ?)";

        try (PreparedStatement ps = b.con.prepareStatement(deleteGrades)) {
            ps.setInt(1, studid);
            ps.executeUpdate();
        }

        // Delete the student's enrollments
        String deleteEnrollments =
            "DELETE FROM enroll WHERE studid = ?";

        try (PreparedStatement ps = b.con.prepareStatement(deleteEnrollments)) {
            ps.setInt(1, studid);
            ps.executeUpdate();
        }

        // Delete the student
        String deleteStudent =
            "DELETE FROM students WHERE studid = ?";

        try (PreparedStatement ps = b.con.prepareStatement(deleteStudent)) {
            ps.setInt(1, studid);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                b.con.commit();
                System.out.println("Student deleted successfully!");
            } else {
                b.con.rollback();
                System.out.println("Student ID not found.");
            }
        }

    } catch (Exception ex) {
        try {
            b.con.rollback();
        } catch (Exception rollbackEx) {
            rollbackEx.printStackTrace();
        }

        System.out.println("Operation failed: " + ex.getMessage());
        ex.printStackTrace();

    } finally {
        try {
            b.con.setAutoCommit(true);
            b.con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

    public void edit_student(int studid, String studname, String studadd,
                   String studcrs, String studgender, String yrlvl){
    EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        
        String query = "UPDATE students SET studname = ?, studadd = ?, studcrs = ?, studgender = ?, yrlvl = ? WHERE studid = ?";
        try {
            PreparedStatement ps = b.con.prepareStatement(query);
            ps.setString(1, studname);
            ps.setString(2, studadd);
            ps.setString(3, studcrs);
            ps.setString(4, studgender);
            ps.setString(5, yrlvl);
            ps.setInt(6, studid);
            
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            }
        } catch(Exception ex) {
            System.out.println("Operation failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}