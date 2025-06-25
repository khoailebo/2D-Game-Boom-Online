package ui;
import javax.swing.JScrollPane;
import javax.swing.JComponent;
import javax.swing.JScrollBar;

public class ScrollPanel extends JScrollPane{
    public ScrollPanel(){
        setVerticalScrollBar(new ModernScrollbar());
        setHorizontalScrollBar(new ModernScrollbar());
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }
}
