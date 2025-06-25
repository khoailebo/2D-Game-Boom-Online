package main;

import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener {
    public boolean UpPressed = false;
    public boolean DownPressed = false;
    public boolean LeftPressed = false;
    public boolean RightPressed = false;
    public boolean EnterPressed = false;
    public GamePanel gamePanel;

    public MyKeyListener(GamePanel gp) {
        this.gamePanel = gp;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            UpPressed = true;
        } else if (keyCode == KeyEvent.VK_A) {
            LeftPressed = true;
        } else if (keyCode == KeyEvent.VK_S) {
            DownPressed = true;
        } else if (keyCode == KeyEvent.VK_D) {
            RightPressed = true;
        }
        if(keyCode == KeyEvent.VK_B)
        {
            gamePanel.Debug = !gamePanel.Debug;
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            EnterPressed = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            UpPressed = false;
            gamePanel.player.sendingLocation =false;
        }
        if (keyCode == KeyEvent.VK_D) {
            gamePanel.player.sendingLocation =false;
            RightPressed = false;
        }
        if (keyCode == KeyEvent.VK_A) {
            LeftPressed = false;
            gamePanel.player.sendingLocation =false;
        }
        if (keyCode == KeyEvent.VK_S) {
            DownPressed = false;
            gamePanel.player.sendingLocation =false;
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            EnterPressed = false;
            gamePanel.player.sendingBoom = false;
        }
    }

}
