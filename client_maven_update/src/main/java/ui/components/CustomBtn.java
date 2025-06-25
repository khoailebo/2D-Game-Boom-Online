package ui.components;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class CustomBtn extends JButton{
    static Color BackgroundColor = Color.decode("#e66e54");
    static Color ClickColor = Color.decode("#d4573c");
    static Color MouseInColor = Color.decode("#fa876f");
    static Color DisableColor = Color.gray;
    public CustomBtn(String text){
        super(text);
        setPreferredSize(new Dimension(200,40));
        setForeground(Color.white);
        setFont(getFont().deriveFont(15f));
        setVerticalAlignment(JButton.CENTER);
        setBackground(BackgroundColor);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(ClickColor);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(MouseInColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(BackgroundColor);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(MouseInColor);
            }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(getBackground());
        if(!isEnabled()){
            g2.setColor(DisableColor);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, getHeight(), getHeight());
        super.paintComponent(g);
    }
}
