package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import java.net.URL;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import net.miginfocom.swing.MigLayout;
import server.Server;
import ui.event.LogEvent;
import ui.event.PublicEvent;
import ui.event.ServerEvent;

import java.awt.event.MouseAdapter;
import javax.swing.JPanel;
import java.awt.Graphics2D;

public class Frame extends JFrame{
    Label TextLabel = new Label("Server Controller");
    CustomBtn StartServerBtn = new CustomBtn("Run server");
    CustomBtn ResetServerBtn = new CustomBtn("Reset server");
    CustomBtn ClearLogBtn = new CustomBtn("Clear log");
    JPanel ButtonPanelBox;
    CustomBtn StopServerBtn = new CustomBtn("Stop server");
    ServerStatePanel serverStatePanel = new ServerStatePanel();
    LogArea logArea = new LogArea();
    public Frame(){
        initComponent();
        initEvent();
    }
    public void showGraphics(){
        setVisible(true);
    }
    public void initEvent(){
        PublicEvent.getInstance().setLogEvent(new LogEvent() {

            @Override
            public void logMsg(String msg) {
                // TODO Auto-generated method stub
                logArea.logMsg(msg);
            }

            @Override
            public void logMsg(String msg, Color color) {
                // TODO Auto-generated method stub
                logArea.logMsg(msg, color);
            }
            
        });
        PublicEvent.getInstance().setServerEvent(new ServerEvent(){

            @Override
            public void setServerState(boolean isRunning) {
                serverStatePanel.updateServerrState();
            }

        });
        StartServerBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Main.server.run();
                Server.getInstance().run();
            }
        });
        ResetServerBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Server.getInstance().resetServer();
                Server.getInstance().run();
            }
        });
        ClearLogBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logArea.clearLog();
            }
        });
        StopServerBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Server.getInstance().stopServer();
            }
        });
    }
    public void initComponent(){
        setTitle("Server controler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill","[100%,center]","[30!]0[50!]10[50!, center][fill]"));
        setResizable(false);
        ButtonPanelBox = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.white);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        ButtonPanelBox.setLayout(new MigLayout("fill","[100%,center][100%,center][100%,center][100%,center]","0[center]0"));
        ButtonPanelBox.setOpaque(false);
        add(serverStatePanel,"wrap,left");
        add(TextLabel,"wrap");
        ButtonPanelBox.add(StartServerBtn,"");
        ButtonPanelBox.add(ResetServerBtn,"");
        ButtonPanelBox.add(ClearLogBtn,"");
        ButtonPanelBox.add(StopServerBtn,"wrap");
        add(ButtonPanelBox,"wrap,grow");
        add(logArea,"grow");
        setSize(new Dimension(650,700));
        setLocationRelativeTo(null);
        URL iconurl = null;
        try{
            iconurl = getClass().getResource("/icon/server.png");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        Image icon = Toolkit.getDefaultToolkit().createImage(iconurl);
        this.setIconImage(icon);
    }
}
