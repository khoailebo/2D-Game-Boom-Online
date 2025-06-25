package ui;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Graphics;
import net.miginfocom.swing.MigLayout;
import javax.swing.JScrollBar;
import java.awt.event.AdjustmentListener;
import java.awt.Adjustable;
import java.awt.event.AdjustmentEvent;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import java.awt.Graphics2D; 
import javax.swing.text.StyleConstants;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
public class LogArea extends ScrollPanel{
    public static final Color WARNING = Color.red;
    public static final Color SUCCESS = Color.decode("#39ff5a");
    public static final Color MESSAGE = Color.decode("#218aff");
    private JTextArea logText;
    private JTextPane  logTextPane;
    public LogArea(){
        setPreferredSize(new Dimension(500,500));
        // setLayout(new MigLayout("fill","[fill]","[fill]"));
        // logText = new JTextArea(5, 5) 
        // {
        //     @Override
        //     protected void paintComponent(Graphics g) {
        //         Graphics2D g2 = (Graphics2D)g;
        //         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //         g2.setColor(Color.white);  // Example: Fill background with light gray color
        //         g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        //         super.paintComponent(g);
        //         // Custom painting code here
        //     }
        // };
        logTextPane = new JTextPane(){
            @Override
            protected void paintComponent(Graphics g) {
                // TODO Auto-generated method stub
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.white);  // Example: Fill background with light gray color
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        setBorder(BorderFactory.createEmptyBorder());
        logTextPane.setOpaque(false);
        setViewportView(logTextPane);
        setOpaque(false);
        logTextPane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        logTextPane.setEditable(false);
        logTextPane.setFont(logTextPane.getFont().deriveFont(14.0f));
        // setBorder(BorderFactory.createEmptyBorder());
        // logText.setOpaque(false);
        // setViewportView(logText);
        // logText.setWrapStyleWord(true);
        // setOpaque(false);
        // logText.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        // logText.setEditable(false);
        // logText.setFont(logText.getFont().deriveFont(14.0f));
    }
    public void logMsg(String msg,Color color){
        // logText.setForeground(color);
        // logText.append(msg + "\n");
        appendColoredText(logTextPane, msg + "\n", color);
        scrollToBottom();
    }
    private static void appendColoredText(JTextPane textPane, String text, Color color) {
        // Get the document and create a new style for the text
        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("ColorStyle", null);
        
        // Set the color for the style
        StyleConstants.setForeground(style, color);
        
        // Append the text with the specified style
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    public void logMsg(String msg){
        // logText.setForeground(Color.black);
        // logText.append(msg + "\n");
        appendColoredText(logTextPane, msg + "\n", Color.black);
        scrollToBottom();
    }
    public void clearLog(){
        logTextPane.setText("");
    }
    private void scrollToBottom() {
        JScrollBar verticalBar = getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);
    }
}
