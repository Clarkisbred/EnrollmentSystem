/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.enrollmentsystem;

/**
 *
 * @author bhilario
 */

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Calendar;
import java.time.*;
import javax.swing.*;

public class EnrollmentSystem {
    public static String currentUser = "";
    public static String currentPassword = "";
    public static boolean isAdmin = false;
    public static int currentTeacherId = -1;
    public static String currentTeacherName = "";

    Connection con;

    Statement st;

    static ResultSet rs;
    static String db;
    static String userRole = "";

    public void currentDB(String db){
        this.db = db;
    }
    
   public String newdb(String term) {
    if (!DBConnect()) {
        return null;
    }
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
          
        } catch (Exception ex) {
    ex.printStackTrace();
    return null;
}

return term + "_" + schyear;
    }

    public static void main(String[] args) {
    try {
        // 1. accent color (must come BEFORE setup)
        com.formdev.flatlaf.FlatLaf.setGlobalExtraDefaults(
            java.util.Collections.singletonMap("@accentColor", "#4F46E5"));

        // 2. turn FlatLaf on
        com.formdev.flatlaf.FlatLightLaf.setup();

        // 3. tweaks (must come AFTER setup)
        javax.swing.UIManager.put("Button.arc", 12);
        javax.swing.UIManager.put("Component.arc", 12);
        javax.swing.UIManager.put("TextComponent.arc", 12);
        javax.swing.UIManager.put("Table.showHorizontalLines", true);
        javax.swing.UIManager.put("Table.showVerticalLines", false);
        javax.swing.UIManager.put("Table.rowHeight", 28);
        javax.swing.UIManager.put("Panel.background", java.awt.Color.decode("#FAFAFA"));
        javax.swing.UIManager.put("Table.alternateRowColor", java.awt.Color.decode("#F9FAFB"));
        javax.swing.UIManager.put("Button.font", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        javax.swing.UIManager.put("Label.font", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        javax.swing.UIManager.put("TextField.font", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    } catch (Exception e) {
        e.printStackTrace();
    }

    java.awt.EventQueue.invokeLater(() -> {
        LoginForm login = new LoginForm();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
    });
}

    public boolean DBConnect() {
    String targetDB = (db != null && !db.trim().isEmpty())
            ? db.trim()
            : "";

    String user = (currentUser != null && !currentUser.trim().isEmpty())
            ? currentUser
            : "root";

    String pass = currentPassword;

    if (pass == null || pass.isEmpty()) {
        JOptionPane.showMessageDialog(null,
                "MySQL password is missing. Please log in again.");
        return false;
    }

    return DBConnect(user, pass, targetDB);
}

public boolean DBConnect(String username, String password) {
    String targetDB = (db != null && !db.trim().isEmpty()
            && !db.equalsIgnoreCase("null"))
            ? db.trim()
            : "";

    return DBConnect(username, password, targetDB);
}

public boolean DBConnect(String username, String password, String semesterDB) {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String dbName = (semesterDB != null
                && !semesterDB.trim().isEmpty()
                && !semesterDB.equalsIgnoreCase("null"))
                ? semesterDB.trim()
                : "";

        String url = "jdbc:mysql://localhost:3306/" + dbName
                + "?useSSL=false"
                + "&allowPublicKeyRetrieval=true"
                + "&serverTimezone=UTC"
                + "&zeroDateTimeBehavior=CONVERT_TO_NULL";

        System.out.println("Connecting to: " + url);
        System.out.println("Username: " + username);

        con = DriverManager.getConnection(url, username, password);
        st = con.createStatement();

        currentUser = username;
        currentPassword = password;

        if (!dbName.isEmpty()) {
            db = dbName;
            String currentSemesterDB = dbName;
        }

        System.out.println("Connected to database successfully!");

        return true;

    } catch (ClassNotFoundException ex) {
        System.err.println("MySQL JDBC Driver not found!");
        ex.printStackTrace();
        return false;

    } catch (SQLException ex) {
        System.err.println("MySQL connection failed!");
        System.err.println("Message: " + ex.getMessage());
        System.err.println("SQL State: " + ex.getSQLState());
        System.err.println("Error Code: " + ex.getErrorCode());
        return false;

    } catch (Exception ex) {
        System.err.println("Connection failed!");
        ex.printStackTrace();
        return false;
    }
}
    
    public static boolean createSemesterDatabase(String semesterSuffix) {
        
        int currentYear = LocalDate.now().getYear();
        int nextYear = currentYear + 1;
        String dbName = "enrollment_" + currentYear + "_" + nextYear + "_" + semesterSuffix;
 
        String url = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
 
        try (Connection conn = DriverManager.getConnection(url, "root", currentPassword);
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
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS assign ("
                    + "SubjID INT NOT NULL UNIQUE, "
                    + "TID INT NOT NULL, "
                    + "FOREIGN KEY (SubjID) REFERENCES subjects(subjid), "
                    + "FOREIGN KEY (TID) REFERENCES teachers(tid))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS enroll ("
                    + "eid INT NOT NULL AUTO_INCREMENT, "
                    + "studid INT NULL DEFAULT NULL, "
                    + "subjid INT NULL DEFAULT NULL, "
                    + "evaluation VARCHAR(255) DEFAULT NULL, "
                    + "PRIMARY KEY (eid), "
                    + "UNIQUE (studid, subjid), "
                    + "FOREIGN KEY (studid) REFERENCES students(studid), "
                    + "FOREIGN KEY (subjid) REFERENCES subjects(subjid))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS grades ("
                    + "gradeid INT NOT NULL AUTO_INCREMENT, "
                    + "enroll_eid INT NOT NULL UNIQUE, "
                    + "prelim VARCHAR(10) NULL DEFAULT NULL, "
                    + "midterm VARCHAR(10) NULL DEFAULT NULL, "
                    + "prefinal VARCHAR(10) NULL DEFAULT NULL, "
                    + "final VARCHAR(10) NULL DEFAULT NULL, "
                    + "PRIMARY KEY (gradeid), "
                    + "FOREIGN KEY (enroll_eid) REFERENCES enroll(eid))");
            new EnrollmentSystem().createConflictProc(stmt);
 
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
            "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
        "root", currentPassword);
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
   public void createConflictProc(Statement s) throws SQLException {
    s.executeUpdate("DROP PROCEDURE IF EXISTS routine1");
    s.executeUpdate("""
        CREATE PROCEDURE routine1 (IN sid INT, IN subid INT, OUT message VARCHAR(100))
        BEGIN
            DECLARE nd VARCHAR(10);
            DECLARE ns TIME;
            DECLARE ne TIME;
            SET message = 'OK';
            SELECT REPLACE(UPPER(SUBSTRING_INDEX(subjsched, ' ', 1)), 'TH', 'R'),
                   CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(subjsched, ' ', -1), '-', 1) AS TIME),
                   CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(subjsched, ' ', -1), '-', -1) AS TIME)
            INTO nd, ns, ne
            FROM subjects WHERE subjid = subid;
            SELECT CONCAT('Conflict with ', x.subjsched) INTO message
            FROM (SELECT s.subjsched,
                         REPLACE(UPPER(SUBSTRING_INDEX(s.subjsched, ' ', 1)), 'TH', 'R') AS d,
                         CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(s.subjsched, ' ', -1), '-', 1) AS TIME) AS t1,
                         CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(s.subjsched, ' ', -1), '-', -1) AS TIME) AS t2
                  FROM enroll e JOIN subjects s ON e.subjid = s.subjid
                  WHERE e.studid = sid AND e.subjid <> subid) x
            WHERE ns < x.t2 AND x.t1 < ne
              AND ((LOCATE('M', nd) > 0 AND LOCATE('M', x.d) > 0)
                OR (LOCATE('T', nd) > 0 AND LOCATE('T', x.d) > 0)
                OR (LOCATE('W', nd) > 0 AND LOCATE('W', x.d) > 0)
                OR (LOCATE('R', nd) > 0 AND LOCATE('R', x.d) > 0)
                OR (LOCATE('F', nd) > 0 AND LOCATE('F', x.d) > 0)
                OR (LOCATE('S', nd) > 0 AND LOCATE('S', x.d) > 0))
            LIMIT 1;
        END
        """);
    
}
   public String checkConflictADDED(int studid, int subjid) {
    EnrollmentSystem b = new EnrollmentSystem();
    b.DBConnect();
    try {
        b.createConflictProc(b.st);
        CallableStatement cs = b.con.prepareCall("{call routine1(?, ?, ?)}");
        cs.setInt(1, studid);
        cs.setInt(2, subjid);
        cs.registerOutParameter(3, Types.VARCHAR);
        cs.execute();
        String msg = cs.getString(3);
        if (msg != null && msg.startsWith("Conflict")) {
            return msg;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
   public void createTeacherConflictProc(Statement s) throws SQLException {
    s.executeUpdate("DROP PROCEDURE IF EXISTS routine2");
    s.executeUpdate("""
        CREATE PROCEDURE routine2 (IN tid_in INT, IN subid INT, OUT message VARCHAR(100))
        BEGIN
            DECLARE nd VARCHAR(10);
            DECLARE ns TIME;
            DECLARE ne TIME;
            SET message = 'OK';
            SELECT REPLACE(UPPER(SUBSTRING_INDEX(subjsched, ' ', 1)), 'TH', 'R'),
                   CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(subjsched, ' ', -1), '-', 1) AS TIME),
                   CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(subjsched, ' ', -1), '-', -1) AS TIME)
            INTO nd, ns, ne
            FROM subjects WHERE subjid = subid;
            SELECT CONCAT('Conflict with ', x.subjsched) INTO message
            FROM (SELECT s.subjsched,
                         REPLACE(UPPER(SUBSTRING_INDEX(s.subjsched, ' ', 1)), 'TH', 'R') AS d,
                         CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(s.subjsched, ' ', -1), '-', 1) AS TIME) AS t1,
                         CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(s.subjsched, ' ', -1), '-', -1) AS TIME) AS t2
                  FROM assign a JOIN subjects s ON a.subjid = s.subjid
                  WHERE a.tid = tid_in AND a.subjid <> subid) x
            WHERE ns < x.t2 AND x.t1 < ne
              AND ((LOCATE('M', nd) > 0 AND LOCATE('M', x.d) > 0)
                OR (LOCATE('T', nd) > 0 AND LOCATE('T', x.d) > 0)
                OR (LOCATE('W', nd) > 0 AND LOCATE('W', x.d) > 0)
                OR (LOCATE('R', nd) > 0 AND LOCATE('R', x.d) > 0)
                OR (LOCATE('F', nd) > 0 AND LOCATE('F', x.d) > 0)
                OR (LOCATE('S', nd) > 0 AND LOCATE('S', x.d) > 0))
            LIMIT 1;
        END
        """);
}

public String checkTeacherConflictADDED(int tid, int subjid) {
    EnrollmentSystem b = new EnrollmentSystem();
    b.DBConnect();
    try {
        b.createTeacherConflictProc(b.st);
        CallableStatement cs = b.con.prepareCall("{call routine2(?, ?, ?)}");
        cs.setInt(1, tid);
        cs.setInt(2, subjid);
        cs.registerOutParameter(3, Types.VARCHAR);
        cs.execute();
        String msg = cs.getString(3);
        if (msg != null && msg.startsWith("Conflict")) {
            return msg;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

}