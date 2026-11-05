/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.enrollmentsystem;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author bhilario
 */
public class Reports {
 
    // ---- EDIT THESE ----
    private static final String SCHOOL      = "University of BreDuck";
    private static final String OFFICE      = "Registrar's Office";
    private static final String PREPARED_BY = "PREPARED BY HILARIO, BRANDON  CLARK I.";
    private static final String CHECKED_BY  = "CHECKED BY THE NWORD";
    private static final String REGISTRAR   = "SIGMA, DUCK";
    // --------------------
 
    private static final Font TITLE  = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static final Font BOLD   = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
    private static final Font NORMAL = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);
    private static final Font SMALL  = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
    private static final Font UNDER  = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.UNDERLINE);
 
    // Reports go to <user home>/EnrollmentReports (put the logo there too)
    private static File folder() {
        File f = new File(System.getProperty("user.home"), "EnrollmentReports");
        if (!f.exists()) f.mkdirs();
        return f;
    }
 

    private static File findLogo() {
        File extraADDED = findLogoADDED(); if (extraADDED != null) return extraADDED;
        String[] names = {"breduck_logo", "breduck", "logo"};
        String[] exts = {".png", ".jpg", ".jpeg"};
        for (String n : names)
            for (String e : exts) {
                File f = new File(folder(), n + e);
                if (f.exists()) return f;
            }
        return null;
    }
 
    private static File findLogoADDED() {
        try {
            File inReports = new File(folder(), "breduck_logo.png");
            if (inReports.exists()) return inReports;
            String[] dirs = {System.getProperty("user.dir"), System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources"};
            for (String d : dirs) {
                File f = new File(d, "breduck_logo.png");
                if (f.exists()) return f;
            }
            try (java.io.InputStream in = Reports.class.getResourceAsStream("/breduck_logo.png")) {
                if (in != null) {
                    java.nio.file.Files.copy(in, inReports.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    return inReports;
                }
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    } 
 

    public static int currentStudentId() {
        String u = EnrollmentSystem.currentUser == null ? "" : EnrollmentSystem.currentUser;
        int i = 0;
        while (i < u.length() && Character.isDigit(u.charAt(i))) i++;
        return i == 0 ? -1 : Integer.parseInt(u.substring(0, i));
    }
 
    // ================= public entry points =================
 
    public static void openGradeReport(int studid) { openFile(buildGradeReport(studid)); }
 
    public static void openTOR(int studid) { openFile(buildTOR(studid)); }
 
    // ================= TOR =================
 
    private static class Row {
        String subjid, code, desc, fin, units;
    }
 
    private static File buildTOR(int studid) {
        File pdf = new File(folder(), "TOR_" + studid + ".pdf");
        Document doc = new Document(PageSize.A4, 40, 40, 36, 36);
        EnrollmentSystem b = new EnrollmentSystem();
        if (!b.DBConnect()) {
            JOptionPane.showMessageDialog(null, "Could not connect to the database.");
            return null;
        }
        try { b.con = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/" + (EnrollmentSystem.db == null ? "" : EnrollmentSystem.db) + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "root"); } catch (Exception exADDED) { exADDED.printStackTrace(); } //ADDED
        try {
            // ---- student info from the currently selected semester database ----
            String name = "", course = "", gender = "";
            try (PreparedStatement ps = b.con.prepareStatement(
                    "SELECT studname, studcrs, studgender FROM students WHERE studid = ?")) {
                ps.setInt(1, studid);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(null, "Student " + studid + " not found.");
                        return null;
                    }
                    name = nz(rs.getString(1));
                    course = nz(rs.getString(2));
                    gender = nz(rs.getString(3));
                }
            }
 
            // ---- find every semester database ----
            List<String> dbs = new ArrayList<>();
            try (Statement st = b.con.createStatement(); ResultSet rs = st.executeQuery("SHOW DATABASES")) {
                while (rs.next()) {
                    String d = rs.getString(1);
                    if (d.startsWith("enrollment_") && d.matches("[A-Za-z0-9_]+")) dbs.add(d);
                }
            }
            dbs.sort(Comparator.comparing(Reports::sortKey));
 
            PdfWriter.getInstance(doc, new FileOutputStream(pdf));
            doc.open();
            addHeader(doc, "Official Transcript of Records");
 
            // ---- student block ----
            PdfPTable info = new PdfPTable(new float[]{1f, 1f});
            info.setWidthPercentage(100f);
            info.setSpacingAfter(8f);
            cell(info, "Student ID: " + studid, BOLD, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER);
            cell(info, "Student Name: " + name, BOLD, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER);
            cell(info, "Student Course: " + course, BOLD, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER);
            cell(info, "Gender: " + gender, BOLD, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER);
            doc.add(info);
 
            // ---- grades table ----
            PdfPTable t = new PdfPTable(new float[]{1.4f, 1.8f, 3.4f, 1.6f, 1.1f});
            t.setWidthPercentage(100f);
            t.setHeaderRows(1);
            String[] heads = {"SubjID", "Subj Code", "Descriptive Title", "Final", "Credit"};
            for (String h : heads) cell(t, h, BOLD, Element.ALIGN_CENTER, 1, Rectangle.BOX);
 
            int total = 0;
            for (String db : dbs) {
                List<Row> rows = semesterRows(b, db, studid, name);
                if (rows.isEmpty()) continue;
                cell(t, semesterLabel(db), UNDER, Element.ALIGN_LEFT, 5, Rectangle.NO_BORDER);
                for (Row r : rows) {
                    PdfPCell idc = cellObj(r.subjid, NORMAL, Element.ALIGN_CENTER, 1, Rectangle.NO_BORDER);
                    idc.setPaddingLeft(20f);
                    t.addCell(idc);
                    cell(t, r.code, NORMAL, Element.ALIGN_CENTER, 1, Rectangle.NO_BORDER);
                    cell(t, r.desc, NORMAL, Element.ALIGN_CENTER, 1, Rectangle.NO_BORDER);
                    cell(t, r.fin, NORMAL, Element.ALIGN_CENTER, 1, Rectangle.NO_BORDER);
                    cell(t, r.units, NORMAL, Element.ALIGN_CENTER, 1, Rectangle.NO_BORDER);
                    total++;
                }
            }
            if (total == 0) cell(t, "No enrolled subjects.", NORMAL, Element.ALIGN_CENTER, 5, Rectangle.NO_BORDER);
            doc.add(t);
 
            // ---- footer ----
            Paragraph count = new Paragraph("Number of Subjects Listed: " + total, BOLD);
            count.setSpacingBefore(12f);
            doc.add(count);
            doc.add(new Chunk(new LineSeparator(0.8f, 100f, BaseColor.BLACK, Element.ALIGN_CENTER, 2f)));
 
            PdfPTable gs = new PdfPTable(new float[]{1f, 1f});
            gs.setWidthPercentage(100f);
            gs.setSpacingBefore(4f);
            String[] left = {"Grading System up to SY 1981-82", "95-100 = 1.0 = Excellent", "90-94 = 1.5 = Very Good",
                "85-89 = 2.0 = Good", "80-84 = 2.5 = Fair", "75-79 = 3.0 = Passed", "Below 75 = 5.0 = Failed"};
            String[] right = {"New Grading System (1st Sem. SY1982-83 & up)", "95-100 = 1.0 = Excellent", "90-94 = 1.5 = Very Good",
                "85-89 = 2.0 = Good", "80-84 = 2.5 = Fair", "75-79 = 3.0 = Passed", "Below 75 = 5.0 = Failed"};
            for (int i = 0; i < left.length; i++) {
                cell(gs, left[i], NORMAL, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER);
                cell(gs, right[i], NORMAL, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER);
            }
            doc.add(gs);
            doc.add(new Paragraph("FOR FCF Grades: O - Outstanding, HS - Highly Satisfactory, MS - Moderately Satisfactory, "
                    + "S - Satisfactory, F - Fair, P - Poor", NORMAL));
            doc.add(new Paragraph("Quality Point Equivalent: 1.0 = 4.0, 1.5 = 3.5, 2.0 = 3.0, 2.5 = 2.5, 3.0 = 2.0", NORMAL));
 
            Paragraph valid = new Paragraph("NOT VALID WITHOUT SCHOOL SEAL", BOLD);
            valid.setSpacingBefore(18f);
            valid.setSpacingAfter(14f);
            doc.add(valid);
 
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            PdfPTable sig = new PdfPTable(new float[]{1f, 1f});
            sig.setWidthPercentage(100f);
            sigCell(sig, "Prepared By: ", PREPARED_BY);
            sigCell(sig, "Checked By: ", CHECKED_BY);
            cell(sig, "Date: " + today, NORMAL, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER);
            sigCell(sig, "Registrar: ", REGISTRAR);
            doc.add(sig);
 
            doc.close();
            return pdf;
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not create TOR: " + ex.getMessage());
            return null;
        } finally {
            if (doc.isOpen()) doc.close();
            try { if (b.con != null) b.con.close(); } catch (Exception ignored) {}
        }
    }
 
    private static List<Row> semesterRows(EnrollmentSystem b, String db, int studid, String name) {
        List<Row> out = new ArrayList<>();
        String q = "SELECT s.subjid, s.subjcode, s.subjdesc, g.`final`, s.subjunits "
                + "FROM `" + db + "`.students st "
                + "JOIN `" + db + "`.enroll e ON e.studid = st.studid "
                + "JOIN `" + db + "`.subjects s ON s.subjid = e.subjid "
                + "LEFT JOIN `" + db + "`.grades g ON g.enroll_eid = e.eid "
                + "WHERE st.studid = ? AND st.studname = ? ORDER BY s.subjcode";
        try (PreparedStatement ps = b.con.prepareStatement(q)) {
            ps.setInt(1, studid);
            ps.setString(2, name);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Row r = new Row();
                    r.subjid = nz(rs.getString(1));
                    r.code = nz(rs.getString(2));
                    r.desc = nz(rs.getString(3));
                    String f = rs.getString(4);
                    r.fin = (f == null || f.trim().isEmpty()) ? "No Grade" : f.trim();
                    r.units = nz(rs.getString(5));
                    out.add(r);
                }
            }
        } catch (Exception e) {
            System.out.println("Skipping " + db + ": " + e.getMessage());
        }
        return out;
    }
 

    private static String semesterLabel(String db) {
        String[] p = db.split("_");
        if (p.length < 4) return db;
        String sem = p[3].replace("sem", "");
        return sem + "_sy" + p[1] + "_" + p[2];
    }
 

    private static String sortKey(String db) {
        String[] p = db.split("_");
        if (p.length < 4) return db;
        String s = p[3];
        int order = s.startsWith("1st") ? 1 : s.startsWith("2nd") ? 2 : 3;
        return p[1] + "_" + p[2] + "_" + order;
    }
 

 
    private static File buildGradeReport(int studid) {
        File pdf = new File(folder(), "Grades_" + studid + ".pdf");
        Document doc = new Document(PageSize.A4, 40, 40, 36, 36);
        EnrollmentSystem b = new EnrollmentSystem();
        if (!b.DBConnect()) {
            JOptionPane.showMessageDialog(null, "Could not connect to the database.");
            return null;
        }
        try {
            String name = "", course = "", year = "";
            try (PreparedStatement ps = b.con.prepareStatement(
                    "SELECT studname, studcrs, yrlvl FROM students WHERE studid = ?")) {
                ps.setInt(1, studid);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(null, "Student " + studid + " not found.");
                        return null;
                    }
                    name = nz(rs.getString(1));
                    course = nz(rs.getString(2));
                    year = nz(rs.getString(3));
                }
            }

            PdfWriter.getInstance(doc, new FileOutputStream(pdf));
            doc.open();
            addHeader(doc, "Student Grade Sheet");

            // ---- student block: left = ID/Name/Year, right = School Year/Course ----
            PdfPTable info = new PdfPTable(new float[]{1f, 1f});
            info.setWidthPercentage(100f);
            info.setSpacingAfter(14f);
            cell(info, "Student ID: " + studid, BOLD, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER);
            cell(info, "School Year: " + semesterLabelSafe(EnrollmentSystem.db), BOLD, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER);
            cell(info, "Student Name: " + name, BOLD, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER);
            cell(info, "Student Course: " + course, BOLD, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER);
            cell(info, "Student Year: " + year, BOLD, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER);
            cell(info, "", BOLD, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER);
            doc.add(info);

            float[] widths = {1.2f, 1.6f, 1.2f, 1.2f, 1.2f, 1.2f};

            // ---- header table (boxed) ----
            PdfPTable th = new PdfPTable(widths);
            th.setWidthPercentage(100f);
            String[] heads = {"SubjID", "Subj Code", "Prelim", "Midterm", "Prefinal", "Final"};
            for (String h : heads) cell(th, h, BOLD, Element.ALIGN_CENTER, 1, Rectangle.BOX);
            th.setSpacingAfter(5f);
            doc.add(th);

            // ---- body table (every cell boxed, missing grades show "null") ----
            PdfPTable t = new PdfPTable(widths);
            t.setWidthPercentage(100f);
            String sql = "SELECT s.subjid, s.subjcode, g.prelim, g.midterm, g.prefinal, g.`final` "
                    + "FROM enroll e JOIN subjects s ON e.subjid = s.subjid "
                    + "LEFT JOIN grades g ON g.enroll_eid = e.eid WHERE e.studid = ? ORDER BY s.subjid";
            int rows = 0;
            try (PreparedStatement ps = b.con.prepareStatement(sql)) {
                ps.setInt(1, studid);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        rows++;
                        for (int c = 1; c <= 6; c++) {
                            String v = rs.getString(c);
                            if (v == null || v.trim().isEmpty()) v = "null";
                            cell(t, v.trim(), NORMAL, Element.ALIGN_CENTER, 1, Rectangle.BOX);
                        }
                    }
                }
            }
            if (rows == 0) cell(t, "No enrolled subjects.", NORMAL, Element.ALIGN_CENTER, 6, Rectangle.BOX);
            doc.add(t);

            Paragraph count = new Paragraph("Number of Subjects Listed: " + rows, BOLD);
            count.setSpacingBefore(12f);
            doc.add(count);

            // ---- registrar signature, bottom right ----
            PdfPTable sig = new PdfPTable(new float[]{2.2f, 1.2f});
            sig.setWidthPercentage(100f);
            sig.setSpacingBefore(110f);
            PdfPCell blank = cellObj("", NORMAL, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER);
            sig.addCell(blank);
            PdfPCell line = cellObj("", NORMAL, Element.ALIGN_CENTER, 1, Rectangle.BOTTOM);
            sig.addCell(line);
            sig.addCell(cellObj("", NORMAL, Element.ALIGN_LEFT, 1, Rectangle.NO_BORDER));
            sig.addCell(cellObj("Registrar", BOLD, Element.ALIGN_CENTER, 1, Rectangle.NO_BORDER));
            doc.add(sig);

            doc.close();
            return pdf;
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not create PDF: " + ex.getMessage());
            return null;
        } finally {
            if (doc.isOpen()) doc.close();
            try { if (b.con != null) b.con.close(); } catch (Exception ignored) {}
        }
    }

    private static String semesterLabelSafe(String db) {
        return db == null ? "" : semesterLabel(db);
    }
 
    // ================= shared helpers =================
 

    private static void addHeader(Document doc, String title) throws Exception {
        PdfPTable head = new PdfPTable(new float[]{1.2f, 2.8f});
        head.setWidthPercentage(60f);
        head.setSpacingAfter(8f);
        File logo = findLogo();
        PdfPCell lc;
        if (logo != null) {
            Image img = Image.getInstance(logo.getAbsolutePath());
            img.scaleToFit(80, 80);
            lc = new PdfPCell(img, false);
        } else {
            lc = new PdfPCell(new Phrase(""));
        }
        lc.setBorder(Rectangle.NO_BORDER);
        lc.setHorizontalAlignment(Element.ALIGN_RIGHT);
        lc.setVerticalAlignment(Element.ALIGN_MIDDLE);
        head.addCell(lc);
 
        PdfPCell tc = new PdfPCell(new Phrase(SCHOOL + "\n" + OFFICE, NORMAL));
        tc.setBorder(Rectangle.NO_BORDER);
        tc.setVerticalAlignment(Element.ALIGN_MIDDLE);
        head.addCell(tc);
        doc.add(head);
 
        Paragraph p = new Paragraph(title, TITLE);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(12f);
        doc.add(p);
    }
 
    private static void sigCell(PdfPTable t, String label, String name) {
        Phrase ph = new Phrase();
        ph.add(new Chunk(label, NORMAL));
        ph.add(new Chunk(name, UNDER));
        PdfPCell c = new PdfPCell(ph);
        c.setBorder(Rectangle.NO_BORDER);
        c.setPadding(5f);
        c.setPaddingBottom(12f);
        t.addCell(c);
    }
 
    private static PdfPCell cellObj(String text, Font f, int align, int colspan, int border) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setHorizontalAlignment(align);
        c.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c.setColspan(colspan);
        c.setPadding(4f);
        c.setBorder(border);
        return c;
    }
 
    private static void cell(PdfPTable t, String text, Font f, int align, int colspan, int border) {
        t.addCell(cellObj(text, f, align, colspan, border));
    }
 
    private static String nz(String s) { return s == null ? "" : s; }
 
    private static void openFile(File f) {
        if (f == null) return;
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(f);
            } else {
                JOptionPane.showMessageDialog(null, "PDF saved to " + f.getAbsolutePath());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "PDF saved to " + f.getAbsolutePath());
        }
    }
}