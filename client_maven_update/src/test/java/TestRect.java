import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.RenderingHints;
import utility.UtilityHelper;

import java.awt.image.BufferedImage;
import java.io.IOException;
public class TestRect {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new MyPanel());
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        

    }
}
class MyPanel extends JPanel{
    public MyPanel(){
        setPreferredSize(new Dimension(200,200));
    }
    // paintComponent: custom giao diện đồ họa 
    // của component
    @Override
    protected void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        super.paintComponent(g);
        BufferedImage tempImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tempImage.createGraphics();
        BufferedImage image = null;
        try {
             image = ImageIO.read(getClass().getResource("/boom/boom-01.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Rectangle a = new Rectangle(20,0,20,60);
        // Rectangle b = new Rectangle(0,20,60,20);
        // Area joinArea = new Area(a);
        // joinArea.add(new Area(b));
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        image = UtilityHelper.scaleImage(image, 48, 48);
        g2.setColor(Color.red);
        g2.drawRect(0,0,48,48);
        g2.drawImage(image, 0,0,48,48,null);
        g2.dispose();
        g.drawImage(tempImage, 0, 0, null);
    }
}