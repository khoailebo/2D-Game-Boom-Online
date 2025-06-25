package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

import Enum.Direction;
import dtos.SendMsg;
import dtos.BoomDTO;
import dtos.PlayerDTO;
import main.GamePanel;
import main.MyKeyListener;
import socket.Client;
import socket.InvokeLater;
import ui.components.EntityInfoPanel;
import utility.GsonHelper;
import utility.UtilityHelper;

import java.lang.Thread;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Player extends Entity {

    public MyKeyListener KeyListener;

    private Player(int x, int y, int width, int height, int speed, int life, GamePanel gp, MyKeyListener keyListener,
            String name) {
        super(gp);
        this.Name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        Width = width;
        Height = height;
        DefaultSolidAreaX = 8;
        DefaultSolidAreaY = 14;
        MaxBoom = 1;
        CurrentBoomNumber = 0;
        SolidArea = new Rectangle(DefaultSolidAreaX, DefaultSolidAreaY, 32, 32);
        this.gamePanel = gp;
        KeyListener = keyListener;
        Life = life;
        getImage();
        getCurrentPlayerImage();
        getModelEntityInfoInstance();
        InforPanel = new EntityInfoPanel();
        InforPanel.setModel_data(getModelEntityInfoInstance());
        addInfoPanel(InforPanel, EntityInfoPanel.ComponentConstrains[Client.getInstance().ClientId]);

    }

    private Player(int x, int y, int width, int height, int speed, int life, String name) {
        super(null);
        this.Name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        Width = width;
        Height = height;
        DefaultSolidAreaX = 8;
        DefaultSolidAreaY = 14;
        MaxBoom = 1;
        CurrentBoomNumber = 0;
        SolidArea = new Rectangle(DefaultSolidAreaX, DefaultSolidAreaY, 32, 32);
        Life = life;
        getImage();
    }

    Direction direction;
    Direction beforDirection = Direction.NONE;

    @Override
    public void update() {
        synchronized (CurrentDirection) {

            CollisionOn = false;
            direction = Direction.NONE;
            if (KeyListener.DownPressed)
                direction = Direction.DOWN;
            else if (KeyListener.UpPressed)
                direction = Direction.UP;
            else if (KeyListener.RightPressed)
                direction = Direction.RIGHT;
            else if (KeyListener.LeftPressed)
                direction = Direction.LEFT;
            else
                direction = Direction.NONE;
            if (sendingLocation == false && !direction.equals(beforDirection)) {
                sendingLocation = true;
                beforDirection = direction;
                syncSendData("UpdateState", GsonHelper.toJsonObj(new PlayerDTO(x, y, speed, direction, Life, Name)),
                        new InvokeLater() {

                            @Override
                            public void call(Object... os) {
                                // TODO Auto-generated method stub
                                synchronized (CurrentDirection) {
                                    // showCurrentTime();
                                    PlayerDTO playerDTO = GsonHelper.<PlayerDTO>fromJsonToObj((String) os[0],
                                            PlayerDTO.class);
                                    CurrentDirection = playerDTO.direction;
                                    // x = playerDTO.x;
                                    // y = playerDTO.y;
                                    sendingLocation = false;
                                }
                            }

                        });
            }
            gamePanel.cChecker.checkCollisionToList(this, gamePanel.OpponentArr);
            super.update();

            if (CurrentBoomNumber < MaxBoom && KeyListener.EnterPressed && sendingBoom == false) {
                // generateBoom();
                sendBoomData();
                sendingBoom = true;
            }
        }

    }

    static Player player = null;

    public static void initPlayer(GamePanel gp, InvokeLater callback) {
        Client.getInstance().sendEvent("GetLocation", "", new InvokeLater() {

            @Override
            public void call(Object... os) {
                Gson gson = new Gson();
                PlayerDTO tank = gson.fromJson((String) os[0], PlayerDTO.class);

                player = new Player(tank.x, tank.y, 48, 48, tank.speed, tank.life, gp, gp.KeyListener, tank.name);
                callback.call(player);
            }

        });

    }

    public void sendBoomData() {
        syncSendData("AddBoom", null, new InvokeLater() {

            @Override
            public void call(Object... os) {
                Player.this.generateBoom();
            }

        });
    }

    @Override
    public void drawGraphics(Graphics2D g2) {
        // TODO Auto-generated method stub
        super.drawGraphics(g2);
        g2.drawImage(currentPlayer, x, y - 48, null);

    }

    public void getCurrentPlayerImage() {
        try {
            currentPlayer = ImageIO.read(getClass().getResourceAsStream("/player/current_player.png"));
            currentPlayer = UtilityHelper.scaleImage(currentPlayer, 48, 35);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
