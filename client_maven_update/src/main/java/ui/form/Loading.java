package ui.form;

import javax.swing.JLabel;
import javax.swing.JPanel;

import main.GamePanel;
import net.miginfocom.swing.MigLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.Color;
public class Loading extends JPanel{
    private JLabel label;
    public Loading(){
        setPreferredSize(new Dimension(GamePanel.ScreenWidth,GamePanel.ScreenHeight));
        setLayout(new MigLayout("fill","0[center]0","0[center]0"));
        label = new JLabel();
        label.setIcon(new ImageIcon(getClass().getResource("/icon/loading.gif")));
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(27,27,27));
        add(label,"grow");
        setOpaque(false);
    }
    

}
