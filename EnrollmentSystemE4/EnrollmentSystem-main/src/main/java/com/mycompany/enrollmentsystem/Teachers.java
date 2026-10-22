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
public class Teachers {

    public int newteacher(String tname, String tadd, String tdept, String tcontact, String tstatus) {
        int newId = -1;
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        String query = "INSERT INTO teachers (tname, tadd, tdept, tcontact, tstatus) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = b.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tname);
            ps.setString(2, tadd);
            ps.setString(3, tdept);
            ps.setString(4, tcontact);
            ps.setString(5, tstatus);
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    newId = keys.getInt(1);
                    
                    String cleanName = tname.replaceAll("\\s+", "").toLowerCase();
                    String username = newId + cleanName; 
                    String password = cleanName;         
String dbName = EnrollmentSystem.db;
                    if (dbName == null || dbName.trim().isEmpty()) {
                        dbName = "enrollment_2026_2027_1stsem";
                    }

                    EnrollmentSystem.grantUserADDED(username, password, dbName, "SELECT, INSERT, UPDATE");
                }
                System.out.println("Teacher inserted successfully!");
            }
        } catch (Exception ex) {
            System.out.println("Not successful!");
            ex.printStackTrace();
        }
        return newId;
    }

    public void delete_teacher(int tid) {
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        String tname = ""; 
        try (PreparedStatement nps = b.con.prepareStatement("SELECT tname FROM teachers WHERE tid = ?")) { 
            nps.setInt(1, tid); 
            ResultSet nrs = nps.executeQuery(); 
            if (nrs.next()) {
                tname = nrs.getString(1); 
            }
        } catch (Exception e) {
            System.out.println("Could not retrieve teacher name before deletion: " + e.getMessage());
        }

        String query = "DELETE FROM teachers WHERE tid = ?";
        try {
            PreparedStatement ps = b.con.prepareStatement(query);
            ps.setInt(1, tid);
            int rows = ps.executeUpdate(); 

            if (rows > 0) {
                System.out.println("Teacher deleted successfully from table!");
            }
        } catch (Exception ex) {
            System.out.println("Not successful!");
            ex.printStackTrace();
        }
    }

    public void edit_teacher(int tid, String tname, String tadd, String tdept, String tcontact, String tstatus) {
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        String query = "UPDATE teachers SET tname = ?, tadd = ?, tdept = ?, tcontact = ?, tstatus = ? WHERE tid = ?";
        try {
            PreparedStatement ps = b.con.prepareStatement(query);
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