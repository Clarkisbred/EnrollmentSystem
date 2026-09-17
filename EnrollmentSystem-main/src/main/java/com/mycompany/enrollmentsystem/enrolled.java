/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.enrollmentsystem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author bhilario
 */
public class enrolled {
    public boolean enroll_subject(int studid, int subjid){
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        
        try{
            PreparedStatement chk = b.con.prepareStatement(
            "SELECT COUNT(*) FROM enroll WHERE studid = ? AND subjid = ?"
            );
            chk.setInt(1, studid);
            chk.setInt(2, subjid);
            ResultSet crs = chk.executeQuery();
            if (crs.next() && crs.getInt(1) > 0){
                System.out.println("Already Enrolled!");
                return false;
            }
            PreparedStatement ps = b.con.prepareStatement(
            "INSERT INT enroll (studid, subjid) VALUES (?, ?)"
            );
            chk.setInt(1, studid);
            chk.setInt(2, subjid);
            int rows = ps.executeUpdate();
            if (rows > 0){
                System.out.println("Student " + studid + "enrolled to "+ subjid);
                return true;
            }
            
        }catch(Exception e){
            System.out.println("Not Successful!");
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean drop_subject(int studid, int subjid){
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        
        try{
            PreparedStatement ps = b.con.prepareStatement(
            "DELETE FROM enroll WHERE studid = ? AND subjid = ?"
            );
            ps.setInt(1, studid);
            ps.setInt(2, subjid);
            int rows = ps.executeUpdate();
            if (rows > 0){
                System.out.println("Student " + studid + "dropped from "+ subjid);
                return true;
            }
            
        }catch(Exception e){
            System.out.println("Not Successful!");
            e.printStackTrace();
        }
        return false;
    }
    
    public List<String[]> studentsOfSubject(int subjid) {
        List<String[]> out = new ArrayList<>();
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
 
        String query = "SELECT st.studid, st.studname, st.studadd, st.studcrs, st.studgender, st.studyrlvl "
                     + "FROM enroll e JOIN students st ON e.studid = st.studid "
                     + "WHERE e.subjid = ?";
        try {
            PreparedStatement ps = b.con.prepareStatement(query);
            ps.setInt(1, subjid);
           ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(new String[]{
                    rs.getString("studid"),
                    rs.getString("studname"),
                    rs.getString("studadd"),
                    rs.getString("studcrs"),
                    rs.getString("studgender"),
                    rs.getString("studyrlvl")
                });
            }
        } catch (Exception e) {
            System.out.println("Not successful!");
            e.printStackTrace();
        }
        return out;
    }
}
