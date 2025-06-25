package ui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollBar;

public class ModernScrollbar extends JScrollBar{
    public ModernScrollbar(){
        setUI(new MordernUI());
        setUnitIncrement(20);
        setPreferredSize(new Dimension(5,5));
        setBackground(Color.white);
    }
}