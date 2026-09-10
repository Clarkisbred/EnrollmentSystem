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
    public void newteacher(int teachid, String teachname, String teachadd,
                            String teachdept, String teachcontact, String teachstatus) {

        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        String query = "INSERT INTO teachers (teachid, teachname, teachadd, teachdept, teachcontact, teachstatus) "
                     + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            java.sql.PreparedStatement ps = b.con.prepareStatement(query);
            ps.setInt(1, teachid);
            ps.setString(2, teachname);
            ps.setString(3, teachadd);
            ps.setString(4, teachdept);
            ps.setString(5, teachcontact);
            ps.setString(6, teachstatus);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Teacher inserted successfully!");
            }
        } catch (Exception ex) {
            System.out.println("Not successful!");
            ex.printStackTrace();
        }
    }

    public void delete_teacher(int teachid) {
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        String query = "DELETE FROM teachers WHERE teachid = ?";
        try {
            java.sql.PreparedStatement ps = b.con.prepareStatement(query);
            ps.setInt(1, teachid);
            ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Not successful!");
            ex.printStackTrace();
        }
    }

    public void edit_teacher(int teachid, String teachname, String teachadd,
                              String teachdept, String teachcontact, String teachstatus) {
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        String query = "UPDATE teachers SET teachname = ?, teachadd = ?, teachdept = ?, "
                     + "teachcontact = ?, teachstatus = ? WHERE teachid = ?";
        try {
            java.sql.PreparedStatement ps = b.con.prepareStatement(query);
            ps.setString(1, teachname);
            ps.setString(2, teachadd);
            ps.setString(3, teachdept);
            ps.setString(4, teachcontact);
            ps.setString(5, teachstatus);
            ps.setInt(6, teachid);
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
