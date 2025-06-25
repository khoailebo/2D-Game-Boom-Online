package ui.components;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import socket.Client;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
public class ConnectionState extends JPanel{
    private boolean ServerRunning = false;
    private int Offset = 4;
    private int Height = 30;
    private int Diameter = Height - 4 * Offset;
    private int Padding = 10;
    private int Width = 3 * Diameter + Padding * 2 + Offset * 4;
    public ConnectionState(){
        setOpaque(false);
        updateState();
        setPreferredSize(new Dimension(Width,Height));
    }
    public void updateState(){
        ServerRunning = Client.getInstance().isConnected();
        setBackground(ServerRunning ? Color.green : Color.red);
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int x = 0;
        int y = 0;
        g2.setColor(Color.white);
        g2.fillRoundRect(x + Offset, y + Offset, Width - Offset * 2, Height - Offset * 2, Height - 2*Offset, Height - 2*Offset);
        g2.setColor(getBackground());
        
        for(int i = 0;i < 3;i++)
        {
            g2.fillOval(x + 2 * Offset, y + 2 * Offset, Diameter, Diameter);
            x += Diameter + (i < 2 ? Padding : 0);
        }
        // TODO Auto-generated method stub
        super.paintComponent(g);
    }
}
