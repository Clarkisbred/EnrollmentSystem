/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.enrollmentsystem;

/**
 *
 * @author bhilario
 */
public class Subjects {
    public void newsubject(int subjid, String subjcode, String subjdesc,
                            String subjunits, String subjsched) {

        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        String query = "INSERT INTO subjects (subjid, subjcode, subjdesc, subjunits, subjsched) "
                     + "VALUES (?, ?, ?, ?, ?)";
        try {
            java.sql.PreparedStatement ps = b.con.prepareStatement(query);
            ps.setInt(1, subjid);
            ps.setString(2, subjcode);
            ps.setString(3, subjdesc);
            ps.setString(4, subjunits);
            ps.setString(5, subjsched);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Subject inserted successfully!");
            }
        } catch (Exception ex) {
            System.out.println("Not successful!");
            ex.printStackTrace();
        }
    }

    public void delete_subject(int subjid) {
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        String query = "DELETE FROM subjects WHERE subjid = ?";
        try {
            java.sql.PreparedStatement ps = b.con.prepareStatement(query);
            ps.setInt(1, subjid);
            ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Not successful!");
            ex.printStackTrace();
        }
    }

    public void edit_subject(int subjid, String subjcode, String subjdesc,
                            String subjunits, String subjsched) {
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        String query = "UPDATE subjects SET subjcode = ?, subjdesc = ?, subjunits = ?, "
             + "subjsched = ? WHERE subjid = ?";
        try {
            java.sql.PreparedStatement ps = b.con.prepareStatement(query);
            ps.setString(1, subjcode);
            ps.setString(2, subjdesc);
            ps.setString(3, subjunits);
            ps.setString(4, subjsched);
            ps.setInt(5, subjid);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Subject updated successfully!");
            }
        } catch (Exception ex) {
            System.out.println("Not successful!");
            ex.printStackTrace();
        }
    }
}
