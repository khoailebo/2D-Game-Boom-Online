package ui.form;

import java.awt.Dimension;
import java.awt.Graphics;
import java.net.URL;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;

import entity.Player;

import java.awt.Color;
import main.GamePanel;
import main.Main;
import net.miginfocom.swing.MigLayout;
import socket.Client;
import socket.ConnectionListener;
import socket.InvokeLater;
import ui.components.ConnectionState;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import ui.components.CustomBtn;
import ui.components.Label;
import javax.swing.ImageIcon;
import ui.components.PanelBox;
import ui.components.ConnectionState;
import ui.components.TextField;
import utility.UtilityHelper;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MenuForm extends JPanel {
    private Label Textlabel;
    private Label ImageLabel;
    private TextField ServerIPTextField;
    private TextField ServerPortTextField;
    private Label ServerIPLabel;
    private Label ServerPortLabel;
    private CustomBtn ConnectToServerBtn;
    private CustomBtn PlayGameBtn;
    private ConnectionState connectionState;
    private PanelBox panelBox;

    public MenuForm() {
        initComponent();
        initEvent();
    }

    public void initEvent() {
        Client.getInstance().setConnectionListener(new ConnectionListener() {
            @Override
            public void onConnect() {
                connectionState.updateState();
            }
        });
        ConnectToServerBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ServerIPTextField.isEmpty()) {
                    ServerIPTextField.emptyEffect();
                } else if (ServerPortTextField.isEmpty()) {
                    ServerPortTextField.emptyEffect();
                } else {
                    Client.getInstance().connectToServer(ServerIPTextField.getText(),
                            Integer.parseInt(ServerPortTextField.getText()));
                    if (Client.getInstance().isConnected()) {
                        PlayGameBtn.setEnabled(true);
                    }
                }
            }
        });

        PlayGameBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Main.window.removeAll();
                if(PlayGameBtn.isEnabled())
                {
                    // Main.gamePanel.setVisible(true);
                    Main.menuForm.setVisible(false);
                    Main.loading.setVisible(true);
                    Main.gamePanel.setVisible(false);
                    repaint();
                    revalidate();
                    new Thread(new Runnable() {
    
                        @Override
                        public void run() {
                            // Main.gamePanel.setUpConnecttion();
                            Main.gamePanel.setUpGame();
                            Main.gamePanel.newThread();
                        }
    
                    }).start();
                }
            }
        });
    }
    public void reload(){
        PlayGameBtn.setEnabled(false);
    }
    public void initComponent() {
        panelBox = new PanelBox(350, 250, Color.gray.darker());
        ServerIPLabel = new Label("Server IP", 18, Color.white);
        ServerIPTextField = new TextField();
        ServerPortTextField = new TextField();
        ConnectToServerBtn = new CustomBtn("Connect");
        PlayGameBtn = new CustomBtn("Play Game");
        ServerPortLabel = new Label("Server port", 18, Color.white);
        Textlabel = new Label("Boom Online", 100);
        ServerIPTextField.setPreferredSize(new Dimension(200, 35));
        ServerPortTextField.setPreferredSize(new Dimension(200, 35));
        connectionState = new ConnectionState();
        PlayGameBtn.setEnabled(false);
        ImageLabel = new Label("", 50){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                super.paintComponent(g);
            }
        };
        URL iconurl = null;
        try {
            iconurl = getClass().getResource("/icon/icon-100.png");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ImageLabel.setIcon(new ImageIcon(iconurl));
        panelBox.setLayout(new MigLayout("fill", "[][]", "[50!][][][]"));
        panelBox.add(connectionState, "wrap,span 2,right");
        panelBox.add(ServerIPLabel);
        panelBox.add(ServerIPTextField, "wrap");
        panelBox.add(ServerPortLabel);
        panelBox.add(ServerPortTextField, "wrap");
        panelBox.add(ConnectToServerBtn);
        panelBox.add(PlayGameBtn);
        setPreferredSize(new Dimension(GamePanel.ScreenWidth, GamePanel.ScreenHeight));
        setLayout(new MigLayout("fill", "[center]", "[50%,top][50%,top][100%,center]"));
        add(Textlabel, "wrap");
        add(ImageLabel, "wrap");
        add(panelBox);
    }
}
