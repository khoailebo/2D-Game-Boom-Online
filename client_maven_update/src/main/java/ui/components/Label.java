package ui.components;

import javax.swing.JLabel;
import java.awt.Color;
public class Label extends JLabel{
    public Label(String text,float textSize){
        super(text);
        setHorizontalTextPosition(JLabel.RIGHT);
        setHorizontalAlignment(JLabel.CENTER);
        setFont(getFont().deriveFont(textSize));
    }
    public Label(String text,float textSize,Color color){
        super(text);
        setForeground(color);
        setHorizontalTextPosition(JLabel.RIGHT);
        setHorizontalAlignment(JLabel.CENTER);
        setFont(getFont().deriveFont(textSize));
    }
}
