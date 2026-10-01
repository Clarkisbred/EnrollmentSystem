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
        String query = "INSERT INTO students (studname, studadd, studcrs, studgender, yrlvl) "
                     + "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = b.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, studname);
        ps.setString(2, studadd);
        ps.setString(3, studcrs);
        ps.setString(4, studgender);
        ps.setString(5, yrlvl);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()){
                newId = keys.getInt(1);
                
                String cleanName = studname.replaceAll("\\s+", "").toLowerCase();
                    String username = newId + cleanName; 
                    String password = cleanName;       
                    String dbName = EnrollmentSystem.db;
                    if (dbName == null) dbName = "enrollmentsystem"; //ADDED

                   
                    try (Connection rootCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/?zeroDateTimeBehavior=CONVERT_TO_NULL", "root", "root");
                         Statement rootSt = rootCon.createStatement()) {
                        
                      
                        String query1 = "CREATE USER IF NOT EXISTS '" + username + "'@'localhost' IDENTIFIED BY '" + password + "'";
                        
                      
                        String query2 = "GRANT SELECT ON `" + dbName + "`.* TO '" + username + "'@'localhost'";
                        
             
                        rootSt.executeUpdate(query1);
                        rootSt.executeUpdate(query2);
                        rootSt.executeUpdate("FLUSH PRIVILEGES");

                        System.out.println("Student inserted successfully!");
                        System.out.println("User '" + username + "' created and granted SELECT access on database `" + dbName + "`.");
                    } catch (SQLException rootEx) {
                        System.err.println("Student added to table, but failed to create MySQL user account: " + rootEx.getMessage());
                    }
            }
            System.out.println("Student inserted successfully!");
        }

    } catch (Exception ex) {
        System.out.println("Not successful!");
        ex.printStackTrace();
    }
    return newId;
}

    public void delete_student(int studid) {
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        String query = "DELETE FROM students WHERE studid = ?";
        try {
            java.sql.PreparedStatement ps = b.con.prepareStatement(query);
            ps.setInt(1, studid);
            String studname = ""; 
            try (PreparedStatement nps = b.con.prepareStatement("SELECT studname FROM students WHERE studid = ?")) { nps.setInt(1, studid); ResultSet nrs = nps.executeQuery(); if (nrs.next()) studname = nrs.getString(1); } //ADDED
            ps.executeUpdate();
            int rows = ps.getUpdateCount(); 
            
             if (rows > 0) {
                System.out.println("Student deleted successfully from table!");
                
               
                if (!studname.isEmpty()) {
                    String cleanName = studname.replaceAll("\\s+", "").toLowerCase();
                    String username = studid + cleanName;
                    String dbName = EnrollmentSystem.db;
                    if (dbName == null) dbName = "enrollmentsystem"; 
                    
                    try (Connection rootCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/?zeroDateTimeBehavior=CONVERT_TO_NULL", "root", "root");
                         Statement rootSt = rootCon.createStatement()) {
                        
                        String revokeQuery = "REVOKE ALL PRIVILEGES ON `" + dbName + "`.* FROM '" + username + "'@'localhost'";
                        
                        rootSt.executeUpdate(revokeQuery);
                        rootSt.executeUpdate("FLUSH PRIVILEGES");
                        
                        System.out.println("Access revoked for user '" + username + "' on database `" + dbName + "`.");
                    } catch (SQLException rootEx) {
                        System.err.println("Student deleted from table, but failed to revoke privileges: " + rootEx.getMessage());
                    }
                }
            }
        } catch (Exception ex) {
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
            if (!studname.isEmpty()) {
                    String cleanName = studname.replaceAll("\\s+", "").toLowerCase();
                    String username = studid + cleanName;
                    String dbName = EnrollmentSystem.db;
                    if (dbName == null) dbName = "enrollmentsystem"; //ADDED
                    
                    try (Connection rootCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/?zeroDateTimeBehavior=CONVERT_TO_NULL", "root", "root");
                         Statement rootSt = rootCon.createStatement()) {
                        
                        String revokeQuery = "REVOKE ALL PRIVILEGES ON `" + dbName + "`.* FROM '" + username + "'@'localhost'";
                        
                        rootSt.executeUpdate(revokeQuery);
                        rootSt.executeUpdate("FLUSH PRIVILEGES");
                        
                        System.out.println("Access revoked for user '" + username + "' on database `" + dbName + "`.");
                    } catch (SQLException rootEx) {
                        System.err.println("Student deleted from table, but failed to revoke privileges: " + rootEx.getMessage());
                    }
                    try (Connection gc = DriverManager.getConnection("jdbc:mysql://localhost:3306/?zeroDateTimeBehavior=CONVERT_TO_NULL", "root", "root"); Statement gs = gc.createStatement()) { gs.executeUpdate("CREATE USER IF NOT EXISTS '" + username + "'@'localhost' IDENTIFIED BY '" + cleanName + "'"); gs.executeUpdate("GRANT SELECT ON `" + dbName + "`.* TO '" + username + "'@'localhost'"); gs.executeUpdate("FLUSH PRIVILEGES"); } catch (SQLException regrantEx) { regrantEx.printStackTrace(); } //ADDED
            }
            
        }
    } catch(Exception ex) {
        System.out.println("Not successful!");
        ex.printStackTrace();
    }
}
}