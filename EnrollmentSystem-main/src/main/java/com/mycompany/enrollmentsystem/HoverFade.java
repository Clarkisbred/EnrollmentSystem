/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.enrollmentsystem;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.Timer;
/**
 *
 * @author BreDuck
 */
public class HoverFade {
    public static void apply(JButton btn, Color normal, Color hover) {
        btn.setBackground(normal);
        final float[] t = {0f};
        final boolean[] over = {false};
        Timer timer = new Timer(12, null);
        timer.addActionListener(e -> {
            t[0] += over[0] ? 0.12f : -0.12f;
            t[0] = Math.max(0f, Math.min(1f, t[0]));
            btn.setBackground(blend(normal, hover, t[0]));
            if (t[0] == 0f || t[0] == 1f) timer.stop();
        });
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { over[0] = true; timer.start(); }
            public void mouseExited(MouseEvent e)  { over[0] = false; timer.start(); }
        });
    }
 
    private static Color blend(Color a, Color b, float t) {
        return new Color(
            (int) (a.getRed()   + (b.getRed()   - a.getRed())   * t),
            (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t),
            (int) (a.getBlue()  + (b.getBlue()  - a.getBlue())  * t));
    }
}
