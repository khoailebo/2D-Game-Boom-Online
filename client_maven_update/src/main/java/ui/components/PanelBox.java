package ui.components;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Graphics;
public class PanelBox extends JPanel{
    private int Width,Height;
    private Color BackgroundColor;
    public PanelBox(int width,int height,Color color){
        this.Width = width;
        this.Height = height;
        this.BackgroundColor = color;
        setPreferredSize(new Dimension(Width,Height));
        setBackground(color);
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        super.paintComponent(g);
    }
}
