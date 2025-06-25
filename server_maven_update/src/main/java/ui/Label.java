package ui;

import javax.swing.JLabel;

public class Label extends JLabel{
    public Label(String text){
        super(text);
        setHorizontalTextPosition(JLabel.CENTER);
        setHorizontalAlignment(JLabel.CENTER);
        setFont(getFont().deriveFont(20.0f));
    }
}
