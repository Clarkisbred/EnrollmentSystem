/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.enrollmentsystem;
import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 *
 * @author bhilario
 */
public class Assign {
    public boolean assign_subject(int subjid, int tid){
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        
        try{
            PreparedStatement chk = b.con.prepareStatement(
            "SELECT COUNT(*) FROM assign WHERE subjid = ? AND tid = ?"
            );
            chk.setInt(1, subjid);
            chk.setInt(2, tid);
           ResultSet crs = chk.executeQuery();
            
            if(crs.next() && crs.getInt(1) > 0){
                System.out.println("Already assigned");
                return false;
            }
            PreparedStatement ps = b.con.prepareStatement(
            "INSERT INTO assign (subjid, tid) VALUES (?, ?)"
            );
            ps.setInt(1, subjid);
            ps.setInt(2, tid);
            int rows = ps.executeUpdate();
            if (rows > 0){
                System.out.println("Subject: "+ subjid + "assigned to teacher "+ tid );
                return true;
            }
        }catch(Exception e){
            System.out.println("Not Successful!");
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean delete_assignment(int subjid, int tid){
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        
        try{
            PreparedStatement ps = b.con.prepareStatement(
            "DELETE FROM assign WHERE subjid = ? AND tid = ?"
            );
            ps.setInt(1, subjid);
            ps.setInt(2, tid);
            int rows = ps.executeUpdate();
            if(rows > 0){
                System.out.println("Subject "+ subjid + " renived from teacher "+
                        tid);
                return true;
            }
        }catch(Exception e){
            System.out.println("Not Successful!");
            e.printStackTrace();
        }
        return false;
    }
    
    public List<String[]> subjectsOfTeacher(int tid){
        List<String[]> out = new ArrayList<>();
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        
        String query = "SELECT s.subjid, s.subjcode, s.subjdesc, s.subjunits, s.subjsched "
                + "FROM assign a JOIN subjects s ON a.subjid = s.subjid "
                + "WHERE a.tid = ?";
        try{
            PreparedStatement ps = b.con.prepareStatement(query);
            ps.setInt(1, tid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                out.add(new String[]{
                   rs.getString("subjid"), 
                   rs.getString("subjcode"), 
                   rs.getString("subjdesc"), 
                   rs.getString("subjunits"), 
                   rs.getString("subjsched"), 
                });
            }
        }catch(Exception e){
            System.out.println("Not Successful!");
            e.printStackTrace();
        }
        return out;
    }
}
