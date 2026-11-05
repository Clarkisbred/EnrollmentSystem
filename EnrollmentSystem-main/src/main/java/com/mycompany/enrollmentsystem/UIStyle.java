/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.enrollmentsystem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
/**
 *
 * @author BreDuck
 */
public class UIStyle {
    private static final Color ACCENT       = Color.decode("#4F46E5");
    private static final Color ACCENT_HOVER = Color.decode("#4338CA");
    private static final Color SOFT         = Color.decode("#F3F4F6");
    private static final Color SOFT_HOVER   = Color.decode("#E5E7EB");
    private static final Color TEXT         = Color.decode("#1F2937");
    private static final Color DANGER       = Color.decode("#DC2626");
    private static final Color BG           = Color.decode("#FAFAFA");
 
    // buttons that get the accent color (main actions)
    private static final Set<String> PRIMARY = new HashSet<>(Arrays.asList(
            "saveBtn", "SaveBtn", "Loginbutton", "submitbutton"));
    // buttons styled as destructive
    private static final Set<String> DESTRUCTIVE = new HashSet<>(Arrays.asList(
            "deleteBtn", "deletebutton", "Dropbutton"));
 
    public static void apply(JFrame f) {
        f.getContentPane().setBackground(BG);
        walk(f.getContentPane());
    }
 
    private static void walk(Container c) {
        for (Component k : c.getComponents()) {
            if (k instanceof JButton) {
                style((JButton) k);
            } else if (k instanceof JTable) {
                JTable t = (JTable) k;
                t.setRowHeight(28);
                t.setShowVerticalLines(false);
                t.setShowHorizontalLines(true);
                t.setGridColor(Color.decode("#E5E7EB"));
                t.getTableHeader().setReorderingAllowed(false);
            }
            if (k instanceof Container) walk((Container) k);
        }
    }
 
    private static void style(JButton b) {
        b.putClientProperty("JButton.buttonType", "roundRect");
        b.setFocusPainted(false);
        String n = b.getName() == null ? "" : b.getName();
        // NetBeans doesn't set names, so fall back to the button text
        String text = b.getText() == null ? "" : b.getText().toLowerCase();
        boolean primary = PRIMARY.contains(n) || text.equals("save") || text.equals("login") || text.equals("submit");
        boolean danger  = DESTRUCTIVE.contains(n) || text.equals("delete") || text.equals("drop");
        if (primary) {
            b.setForeground(Color.WHITE);
            HoverFade.apply(b, ACCENT, ACCENT_HOVER);
        } else {
            b.setForeground(danger ? DANGER : TEXT);
            HoverFade.apply(b, SOFT, SOFT_HOVER);
        }
    }
}
