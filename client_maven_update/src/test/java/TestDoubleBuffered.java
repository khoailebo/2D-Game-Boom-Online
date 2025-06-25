import javax.imageio.ImageIO;
import javax.swing.*;

import ui.components.EntityInfoPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TestDoubleBuffered extends JPanel {

    private BufferedImage buffer;
    private int screenWidth = 800;
    private int screenHeight = 600;
    private int gameWidth = 400;
    private int gameHeight = 300;

    public TestDoubleBuffered() {
        // Create a buffered image for double buffering
        buffer = new BufferedImage(gameWidth, gameHeight, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Get the graphics from the buffered image
        Graphics2D g2d = buffer.createGraphics();

        // Clear the background on the off-screen buffer
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, screenWidth, screenHeight);

        // Draw the image on the buffer
        BufferedImage tempImage = null;
        try {
            tempImage = ImageIO.read(getClass().getResourceAsStream("/boom/boom-01.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        g2d.drawImage(tempImage, screenWidth / 2 - gameWidth / 2, screenHeight / 2 - gameHeight / 2, null);

        // Draw the buffer on the actual screen (on the JPanel)
        g.setColor(Color.red);
        g.fillRect(0, 0, screenWidth, screenHeight);
        g.drawImage(buffer, 0, 0, null);
        // Dispose the off-screen graphics to free resources
        g2d.dispose();
    }

    // Main method to create the frame and display the panel
    public static void main(String[] args) {
        JFrame frame = new JFrame("Double Buffering Example");
        TestDoubleBuffered panel = new TestDoubleBuffered();
        frame.add(new EntityInfoPanel());
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Game loop to repaint continuously
        new Timer(16, e -> panel.repaint()).start();  // Repaint ~60 FPS
    }
}
