/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.enrollmentsystem;

/**
 *
 * @author bhilario
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.time.*;
import javax.swing.*;

public class EnrollmentSystem {
    public static String currentUser = "";
    public static boolean isAdmin = false;

    Connection con;

    Statement st;

    static ResultSet rs;
    static String db;
    static String userRole = "";

    public void currentDB(String db){
        this.db = db;
    }
    
   public String newdb(String term){
        DBConnect();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String schyear = "SY" + year + "_" + (year + 1);
            
        try {
            String query = "CREATE DATABASE IF NOT EXISTS " + term + "_" + schyear;
            st.executeUpdate(query);
            String query2 = "USE " + term + "_" + schyear;
            st.executeUpdate(query2);
            
            String query3 = """
                            CREATE TABLE IF NOT EXISTS students (
                                studid INT NOT NULL AUTO_INCREMENT,
                                studname VARCHAR(100) NOT NULL,
                                studadd VARCHAR(255) NULL,
                                studcrs VARCHAR(100) NULL,
                                studgender VARCHAR(20) NULL,
                                studyrlvl VARCHAR(20) NULL,
                                PRIMARY KEY (studid));
                                """;
            st.executeUpdate(query3);
            String query4 = """
                            CREATE TABLE IF NOT EXISTS subjects (
                                    subjid INT NOT NULL AUTO_INCREMENT,
                                    subjcode VARCHAR(50) NULL DEFAULT NULL,
                                    subjdesc VARCHAR(255) NULL DEFAULT NULL,
                                    subjunits INT NULL DEFAULT NULL,
                                    subjsched VARCHAR(100) NULL,
                                    PRIMARY KEY (subjid));
                            """;
            st.executeUpdate(query4);
            String query5 = """
                           CREATE TABLE IF NOT EXISTS teachers (
                                    tid INT NOT NULL AUTO_INCREMENT,
                                    tname VARCHAR(100) NULL DEFAULT NULL,
                                    tdept VARCHAR(100) NULL DEFAULT NULL,
                                    tadd VARCHAR(255) NULL,
                                    tcontact VARCHAR(50) NULL,
                                    tstatus VARCHAR(50) NULL,
                                    PRIMARY KEY (tid)
                                );
                            """;
            st.executeUpdate(query5);
            String query6 = """
                            CREATE TABLE IF NOT EXISTS assign (
                                SubjID INT NOT NULL UNIQUE,
                                TID INT NOT NULL,
                                FOREIGN KEY (SubjID) REFERENCES subjects(subjid),
                                FOREIGN KEY (TID) REFERENCES teachers(tid)
                            );
                            """;
            st.executeUpdate(query6);
            String query7 = """
                            CREATE TABLE IF NOT EXISTS enroll (
                                eid INT NOT NULL AUTO_INCREMENT,
                                studid INT NULL DEFAULT NULL,
                                subjid INT NULL DEFAULT NULL,
                                evaluation VARCHAR(255) DEFAULT NULL,
                                PRIMARY KEY (eid),
                                UNIQUE (studid, subjid),
                                FOREIGN KEY (studid) REFERENCES students(studid),
                                FOREIGN KEY (subjid) REFERENCES subjects(subjid)
                            );
                            """;
            st.executeUpdate(query7);
            String query8 = """
                            CREATE TABLE IF NOT EXISTS grades (
                                gradeid INT NOT NULL AUTO_INCREMENT,
                                enroll_eid INT NOT NULL UNIQUE,
                                prelim VARCHAR(10) NULL DEFAULT NULL,
                                midterm VARCHAR(10) NULL DEFAULT NULL,
                                prefinal VARCHAR(10) NULL DEFAULT NULL,
                                final VARCHAR(10) NULL DEFAULT NULL,
                                PRIMARY KEY (gradeid),
                                FOREIGN KEY (enroll_eid) REFERENCES enroll(eid)
                            );
                            """;
            st.executeUpdate(query8);
          
        } catch(Exception ex){
            System.out.println(ex);
        }
        return term + "_" + schyear;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            LoginForm login = new LoginForm();
            login.setLocationRelativeTo(null);
            login.setVisible(true);
        });
    }

    public boolean DBConnect(){

       try{

            Class.forName("com.mysql.cj.jdbc.Driver");

           // con = DriverManager.getConnection(
   // "jdbc:mysql://localhost:3306/enrollmentsystem?"
   // + "useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
   // + "&zeroDateTimeBehavior=CONVERT_TO_NULL",
  //  "root",
   // "root"
//);

       //     if (db != null && !db.isEmpty()) { con.close(); con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&zeroDateTimeBehavior=CONVERT_TO_NULL", "root", "root"); }
       String url = "jdbc:mysql://localhost:3306/" + (db != null && !db.isEmpty() ? db : "") 
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&zeroDateTimeBehavior=CONVERT_TO_NULL"; // ADDED
            con = DriverManager.getConnection(url, "root", "root");
            st = con.createStatement();

            System.out.println("Connected to database!");

        }catch (Exception ex) {

            System.out.print(ex);

            System.out.println("Connection failed");
            return false;
        }
         return true;

    }
    
    public static boolean createSemesterDatabase(String semesterSuffix) {
        int currentYear = LocalDate.now().getYear();
        int nextYear = currentYear + 1;
        String dbName = "enrollment_" + currentYear + "_" + nextYear + "_" + semesterSuffix;

        String url = "jdbc:mysql://localhost:3306/?user=root&password=root";
        url = url + "&useSSL=false&allowPublicKeyRetrieval=true"; 

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

       
            String sql = "CREATE DATABASE IF NOT EXISTS " + dbName;
            stmt.executeUpdate(sql);
            
          
            stmt.executeUpdate("USE " + dbName);
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS students ("
                    + "studid INT AUTO_INCREMENT PRIMARY KEY, "
                    + "studname VARCHAR(100), "
                    + "studadd VARCHAR(150), "
                    + "studcrs VARCHAR(50), "
                    + "studgender VARCHAR(10), "
                    + "yrlvl VARCHAR(10))");
            stmt.executeUpdate("ALTER TABLE students AUTO_INCREMENT = 1000"); 
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS teachers (tid INT AUTO_INCREMENT PRIMARY KEY, tname VARCHAR(100), tadd VARCHAR(255), tdept VARCHAR(100), tcontact VARCHAR(50), tstatus VARCHAR(50))"); //ADDED
            stmt.executeUpdate("ALTER TABLE teachers AUTO_INCREMENT = 3000"); 
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS subjects (subjid INT AUTO_INCREMENT PRIMARY KEY, subjcode VARCHAR(50), subjdesc VARCHAR(255), subjunits VARCHAR(20), subjsched VARCHAR(100))"); //ADDED
            stmt.executeUpdate("ALTER TABLE subjects AUTO_INCREMENT = 2000"); 
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS enroll (eid INT AUTO_INCREMENT PRIMARY KEY, studid INT, subjid INT)"); 
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS assign (subjid INT, tid INT)"); 

            javax.swing.JOptionPane.showMessageDialog(null, "Database '" + dbName + "' created successfully!");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
           JOptionPane.showMessageDialog(null, "Error creating database: " + e.getMessage());
            return false;
        }
    }
    
   public static void grantUserADDED(String username, String password, String dbName, String privs) { 
    try (Connection c = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "root"); 
         Statement s = c.createStatement()) { 
        
        s.executeUpdate("CREATE USER IF NOT EXISTS '" + username + "'@'localhost' IDENTIFIED BY '" + password + "'"); 
        s.executeUpdate("ALTER USER '" + username + "'@'localhost' IDENTIFIED BY '" + password + "'"); 
        
        s.executeUpdate("GRANT " + privs + " ON `" + dbName + "`.* TO '" + username + "'@'localhost'"); 
        s.executeUpdate("FLUSH PRIVILEGES"); 
        
        System.out.println("User '" + username + "' granted " + privs + " on database `" + dbName + "`."); 
    } catch (Exception ex) { 
        System.err.println("grantUserADDED failed: " + ex.getMessage()); 
    } 
}

}