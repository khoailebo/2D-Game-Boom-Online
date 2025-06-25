package main;

import javax.swing.JFrame;
import java.net.URL;
import java.awt.Image;
import java.awt.Toolkit;
public class GameFrame extends JFrame {
    public GameFrame(){
        URL iconurl = null;
        try{
            iconurl = getClass().getResource("/icon/icon.png");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        Image icon = Toolkit.getDefaultToolkit().createImage(iconurl);
        this.setIconImage(icon);
    }
}
