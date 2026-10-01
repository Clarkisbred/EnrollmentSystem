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
    public int newteacher(String tname, String tadd,
                            String tdept, String tcontact, String tstatus) {
        int newId = -1;
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        String query = "INSERT INTO teachers (tname, tadd, tdept, tcontact, tstatus) "
                     + "VALUES (?, ?, ?, ?, ?)";
        try {
            java.sql.PreparedStatement ps = b.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tname);
            ps.setString(2, tadd);
            ps.setString(3, tdept);
            ps.setString(4, tcontact);
            ps.setString(5, tstatus);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                 if (keys.next()){
                     newId = keys.getInt(1);
                     
                     String cleanName = tname.replaceAll("\\s+", "").toLowerCase();
                    String username = newId + cleanName; 
                    String password = cleanName;         
                    String dbName = EnrollmentSystem.db;
                    if (dbName == null) dbName = "enrollmentsystem"; //ADDED

                    try (Connection rootCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/?zeroDateTimeBehavior=CONVERT_TO_NULL", "root", "root");
                         Statement rootSt = rootCon.createStatement()) {
                        
                        String query1 = "CREATE USER IF NOT EXISTS '" + username + "'@'localhost' IDENTIFIED BY '" + password + "'";
                        
                        String query2 = "GRANT SELECT, INSERT, UPDATE ON `" + dbName + "`.* TO '" + username + "'@'localhost'";
                        
                        rootSt.executeUpdate(query1);
                        rootSt.executeUpdate(query2);
                        rootSt.executeUpdate("FLUSH PRIVILEGES");

                        System.out.println("Teacher inserted successfully!");
                        System.out.println("User '" + username + "' created with SELECT, INSERT, and UPDATE access on database `" + dbName + "`.");
                    } catch (SQLException rootEx) {
                        System.err.println("Teacher added to table, but failed to create MySQL user account: " + rootEx.getMessage());
                    }
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

        String query = "DELETE FROM teachers WHERE tid = ?";
        try {
            java.sql.PreparedStatement ps = b.con.prepareStatement(query);
            ps.setInt(1, tid);
            String tname = ""; 
            try (PreparedStatement nps = b.con.prepareStatement("SELECT tname FROM teachers WHERE tid = ?")) { nps.setInt(1, tid); ResultSet nrs = nps.executeQuery(); if (nrs.next()) tname = nrs.getString(1); } //ADDED
            ps.executeUpdate();
            int rows = ps.getUpdateCount(); 
            
             if (rows > 0) {
                System.out.println("Teacher deleted successfully from table!");
                
               
                if (!tname.isEmpty()) {
                    String cleanName = tname.replaceAll("\\s+", "").toLowerCase();
                    String username = tid + cleanName;
                    String dbName = EnrollmentSystem.db;
                    if (dbName == null) dbName = "enrollmentsystem"; 
                    
                    try (Connection rootCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/?zeroDateTimeBehavior=CONVERT_TO_NULL", "root", "root");
                         Statement rootSt = rootCon.createStatement()) {
                        
                        String revokeQuery = "REVOKE ALL PRIVILEGES ON `" + dbName + "`.* FROM '" + username + "'@'localhost'";
                        
                        rootSt.executeUpdate(revokeQuery);
                        rootSt.executeUpdate("FLUSH PRIVILEGES");
                        
                        System.out.println("Access revoked for user '" + username + "' on database `" + dbName + "`.");
                    } catch (SQLException rootEx) {
                        System.err.println("Teacher deleted from table, but failed to revoke privileges: " + rootEx.getMessage());
                    }
                }
            }
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