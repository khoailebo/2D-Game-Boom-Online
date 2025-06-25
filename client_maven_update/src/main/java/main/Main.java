package main;

import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ui.form.Loading;
import ui.form.MenuForm;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.awt.CardLayout;
public class Main {
    public static JFrame window;
    public static GamePanel gamePanel = new GamePanel();
    public static MenuForm menuForm = new MenuForm();
    public static Loading loading = new Loading();
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Main();
            }
            
        });
    }
    public Main(){
        window = new GameFrame();
        window.setLayout(new CardLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Boom online");
        window.add(menuForm);
        window.add(loading);
        window.add(gamePanel);
        gamePanel.setVisible(false);
        menuForm.setVisible(true);
        // loading.setVisible(false);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);   
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gamePanel.client.sendEvent("Disconnect", "GoodBye", null);
                super.windowClosing(e);
            }
        });
        gamePanel.setUpConnecttion();
        // gamePanel.setUpGame();
    //     gamePanel.newThread();
    }
}
