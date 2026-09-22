/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.enrollmentsystem;

/**
 *
 * @author bhilario
 */

public class Students {

 public void newstudent(int studid, String studname, String studadd,
                       String studcrs, String studgender, String yrlvl) {

    EnrollmentSystem b = new EnrollmentSystem();
    b.DBConnect();

    try {
        String query = "INSERT INTO students (studid, studname, studadd, studcrs, studgender, yrlvl) "
                     + "VALUES (?, ?, ?, ?, ?, ?)";
        java.sql.PreparedStatement ps = b.con.prepareStatement(query);
        ps.setInt(1, studid);
        ps.setString(2, studname);
        ps.setString(3, studadd);
        ps.setString(4, studcrs);
        ps.setString(5, studgender);
        ps.setString(6, yrlvl);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Student inserted successfully!");
        }

    } catch (Exception ex) {
        System.out.println("Not successful!");
        ex.printStackTrace();
    }
}

    public void delete_student(int studid){
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        String query = "DELETE FROM students WHERE studid = ?";
        try {
            java.sql.PreparedStatement ps = b.con.prepareStatement(query);
            ps.setInt(1, studid);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            }
        }
        catch(Exception ex) {
            System.out.println("Not successful!");
            ex.printStackTrace();
        }
    }

    public void edit_student(int studid, String studname, String studadd,
                   String studcrs, String studgender, String yrlvl){
    EnrollmentSystem b = new EnrollmentSystem();
    b.DBConnect();
    String query = "UPDATE students SET studname = ?, studadd = ?, studcrs = ?, "
                 + "studgender = ?, yrlvl = ? WHERE studid = ?";
    try {
        java.sql.PreparedStatement ps = b.con.prepareStatement(query);
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
        System.out.println("Not successful!");
        ex.printStackTrace();
    }
}
}