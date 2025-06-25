
import ui.CustomBtn;
import ui.Frame;
import ui.Label;
import ui.LogArea;
import javax.swing.SwingUtilities;

import server.Server;

import java.awt.Dimension;

public class Main {

    public static void main(String[] args) {
        // server.run();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Frame frame = new Frame();
                frame.showGraphics();
            }

        });

    }
}
