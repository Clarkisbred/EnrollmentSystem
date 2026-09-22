/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.enrollmentsystem;

/**
 *
 * @author bhilario
 */
public class Teachers {
    public void newteacher(int tid, String tname, String tadd,
                            String tdept, String tcontact, String tstatus) {

        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        String query = "INSERT INTO teachers (tid, tname, tadd, tdept, tcontact, tstatus) "
                     + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            java.sql.PreparedStatement ps = b.con.prepareStatement(query);
            ps.setInt(1, tid);
            ps.setString(2, tname);
            ps.setString(3, tadd);
            ps.setString(4, tdept);
            ps.setString(5, tcontact);
            ps.setString(6, tstatus);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Teacher inserted successfully!");
            }
        } catch (Exception ex) {
            System.out.println("Not successful!");
            ex.printStackTrace();
        }
    }

    public void delete_teacher(int tid) {
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        String query = "DELETE FROM teachers WHERE tid = ?";
        try {
            java.sql.PreparedStatement ps = b.con.prepareStatement(query);
            ps.setInt(1, tid);
            ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Not successful!");
            ex.printStackTrace();
        }
    }

    public void edit_teacher(int tid, String tname, String tadd,
                              String tdept, String tcontact, String tstatus) {
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        String query = "UPDATE teachers SET tname = ?, tadd = ?, tdept = ?, "
                     + "tcontact = ?, tstatus = ? WHERE tid = ?";
        try {
            java.sql.PreparedStatement ps = b.con.prepareStatement(query);
            ps.setString(1, tname);
            ps.setString(2, tadd);
            ps.setString(3, tdept);
            ps.setString(4, tcontact);
            ps.setString(5, tstatus);
            ps.setInt(6, tid);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Teacher updated successfully!");
            }
        } catch (Exception ex) {
            System.out.println("Not successful!");
            ex.printStackTrace();
        }
    }

}