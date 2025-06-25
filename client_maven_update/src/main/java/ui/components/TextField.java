/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author btdun
 */
public class TextField extends JTextField {

    Color Current = Color.white;
    Timer EffectTimer;
    boolean mouseIn = false;
    Point MouseClick;
    int alpha_speed = 20;
    int Max_alpha = 250;
    int Min_alpha = 0;
    int alpha = 250;
    Color Choosen = new Color(48, 152, 255);
    boolean Empty = false;


    public void setEmpty(boolean empty) {
        Empty = empty;
    }

    public TextField() {
        setOpaque(false);
        // setFocusable(false);
        setFocusable(false);
        // setFocusable(true);
        setBackground(new Color(0, 0, 0, 0));
        setEditable(true);
        setBorder(new EmptyBorder(5, 20, 5, 10));
        setText("");
        setFont(getFont().deriveFont(15f));
        setForeground(new Color(200, 200, 200));
        setCaretColor(new Color(150, 150, 150));
        EffectTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // throw new UnsupportedOperationException("Not supported yet."); // Generated
                // from
                // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                if (mouseIn) {
                    // System.out.println(alpha);
                    if (alpha > Min_alpha)
                        alpha -= alpha_speed;
                    else {
                        EffectTimer.stop();
                    }
                    alpha = (alpha < Min_alpha) ? Min_alpha : alpha;
                    repaint();
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // super.mouseClicked(e); // Generated from
                // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
                setEmpty(false);
                focus();
                mouseIn = true;
                EffectTimer.start();
            }

        });
    }
    public void emptyEffect(){
        setEmpty(true);
        focus();
    }
    public void focus() {
        // System.out.println("hello");
        setFocusable(true);
        requestFocus();
        new Thread(() -> {
            repaint();
        }).start();
    }
    public boolean isEmpty(){
        return getText().length() == 0;
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Choosen);
        g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, getHeight(), getHeight());
        g2.setColor(!mouseIn ? Current : new Color(Current.getRed(), Current.getGreen(), Current.getBlue(), alpha));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, getHeight(), getHeight());
        if (Empty) {
            g2.setColor(Color.red);
            g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, getHeight(), getHeight());
        }
        super.paintComponent(g); // Generated from
                                 // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

}
