package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import dtos.PlayerDTO;
import entity.Boom;
import entity.Entity;
import entity.Opponent;
import entity.Player;
import net.miginfocom.swing.MigLayout;
import socket.Client;
import socket.InvokeLater;
import tile.ObstacleTile;
import tile.TileManager;
import utility.GsonHelper;

public class GamePanel extends JPanel implements Runnable {
    public static int RealTile = 48;
    public static int Row = 12;
    public static int Col = 20;
    public static int ScreenWidth = Col * RealTile;
    public static int ScreenHeight = Row * RealTile;
    public static int GameWitdh = Row * RealTile;
    public static int GameHeight = Row * RealTile;
    public static final int PLAYING_STATE = 1;
    public static final int CONNECTING_STATE = 0;
    public static int GameState = 0;
    public static int SideBarWidth = 188;

    public static int getGameState() {
        return GameState;
    }

    public static void setGameState(int gameState) {
        GameState = gameState;
    }

    public Graphics2D g2;
    public boolean Debug = false;
    public MyKeyListener KeyListener = new MyKeyListener(this);
    public Player player;
    public Thread GameThread;
    public ArrayList<Boom> BoomList = new ArrayList<>();
    public BufferedImage TempImage;
    public ObstacleTile[] obstacleTileList = new ObstacleTile[100];
    public Client client;
    public HashMap<Integer, Opponent> OpponentMap = new HashMap<>();
    public Opponent opponent;
    public Opponent[] OpponentArr = new Opponent[4];
    public AssetSetter aSetter = new AssetSetter(this);
    public int OpponentNumbers = 0;
    public CollisionChecker cChecker;
    public TileManager tileManager = new TileManager(this);

    public GamePanel() {
        this.setFocusable(true);
        this.setBackground(Color.gray);
        this.setPreferredSize(new Dimension(ScreenWidth, ScreenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(KeyListener);
        setLayout(new MigLayout("fill", "2[" + SideBarWidth + "!]2[100%]2[" + SideBarWidth + "!]2", "[]"));
        // newGame();
        // add(new CustomBtn("Hello"));
    }
    public void newGame(){
        Main.gamePanel.removeAll();
        repaint();
        revalidate();
        OpponentMap = new HashMap<>();
        OpponentNumbers = 0;
        OpponentArr = new Opponent[4];
    }
    public void newThread() {
        if(GameThread == null)
        {
            GameThread = new Thread(this);
            GameThread.start();
        }
    }

    public void setUpConnecttion() {
        client = Client.getInstance();
        client.on("ServerClosed",new InvokeLater() {

            @Override
            public void call(Object... os) {
                synchronized(Main.gamePanel)
                {
                    client.close();
                    Main.menuForm.reload();
                    Main.menuForm.setVisible(true);
                    Main.loading.setVisible(false);
                    Main.gamePanel.setVisible(false);
                    setGameState(CONNECTING_STATE);
                    repaint();
                    revalidate();
                    Main.menuForm.requestFocus();
                    newGame();
                }
            }
            
        });
        client.on("SetClientId", new InvokeLater() {

            @Override
            public void call(Object... os) {
                int id = Integer.parseInt((String) os[0]);
                client.ClientId = id;
            }

        });
        // client.on("GetLocation", new InvokeLater() {

        // @Override
        // public void call(Object... os) {
        // Gson gson = new Gson();
        // PlayerDTO tank = gson.fromJson((String) os[0], PlayerDTO.class);

        // // player = new Player(tank.x, tank.y, 48, 48, tank.speed, tank.life,
        // GamePanel.this, KeyListener,tank.name);
        // }

        // });
        client.on("SetOpponentLocation", new InvokeLater() {

            @Override
            public void call(Object... os) {
                // TODO Auto-generated method stub
                String receivedMsg = (String) os[0];
                String[] buffers = receivedMsg.split("_");
                PlayerDTO t = GsonHelper.<PlayerDTO>fromJsonToObj((String) buffers[0], PlayerDTO.class);
                Opponent opponent = new Opponent(t.x, t.y, 48, 48, t.speed, t.life, GamePanel.this, t.name,Integer.parseInt(buffers[1]));
                OpponentMap.put(Integer.parseInt(buffers[1]), opponent);
                OpponentArr[OpponentNumbers++] = opponent;

            }

        });
        client.on("UpdateOpponentLocation", new InvokeLater() {

            @Override
            public void call(Object... os) {
                // TODO Auto-generated method stub
                // Entity.showCurrentTime();
                String receivedMsg = (String) os[0];
                String[] buffers = receivedMsg.split("_");
                int opponentId = Integer.parseInt(buffers[1]);
                Opponent opponent = OpponentMap.get(opponentId);
                synchronized (opponent.CurrentDirection) {
                    PlayerDTO t = GsonHelper.<PlayerDTO>fromJsonToObj((String) buffers[0], PlayerDTO.class);
                    // opponent.x = t.x;
                    // opponent.y = t.y;
                    // opponent.speed = t.speed;
                    opponent.CurrentDirection = t.direction;
                }
            }

        });
        client.on("OpponentAddBoom", new InvokeLater() {

            @Override
            public void call(Object... os) {
                // System.out.println(opponent.CurrentBoomNumber);
                String receivedMsg = (String) os[0];
                String[] buffers = receivedMsg.split("_");
                int opponentId = Integer.parseInt(buffers[1]);
                Opponent opponent = OpponentMap.get(opponentId);
                opponent.generateBoom();
            }

        });
        // client.connectToServer("127.0.0.1",5000);
    }

    public void setUpGame() {
        setGameState(CONNECTING_STATE);
        tileManager.setUpTile();
        tileManager.loadMap();
        aSetter.setObstacleTile();
        TempImage = new BufferedImage(GameWitdh, GameHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = TempImage.createGraphics();
        cChecker = new CollisionChecker(this);
        Player.initPlayer(this, new InvokeLater() {

            @Override
            public void call(Object... os) {
                player = (Player) os[0];
                setGameState(PLAYING_STATE);
                Main.menuForm.setVisible(false);
                Main.loading.setVisible(false);
                Main.gamePanel.setVisible(true);
                repaint();
                revalidate();
                Main.gamePanel.requestFocus();
            }

        });
    }

    public void run() {

        long lastTime = System.nanoTime();
        double fps = 60.0;
        double spf = 1000000000 / fps;
        double delta = 0;
        long now = 0;
        long time = 0;
        int drawCount = 0;
        while (true) {
            now = System.nanoTime();
            delta += now / spf - lastTime / spf;
            time += now - lastTime;
            if (delta >= 1) {
                drawCount++;
                // GameLoop
                synchronized(Main.gamePanel)
                {
                    if (GameState == PLAYING_STATE) {
                        if (!hasFocus()) {
                            requestFocus();
                        }
                        update();
                        drawGraphics();
                    }
                }
                
                delta = 0;
            }
            if (time == 1000000000) {
                // System.out.println(drawCount);

                time = 0;
                drawCount = 0;
            }
            lastTime = now;
        }
    }

    public void update() {

        if (player != null)
            player.update();
        for(int i = 0;i < OpponentArr.length;i++)
        {
            Opponent currentOpponent = OpponentArr[i];
            if(currentOpponent != null)
            {
                currentOpponent.update();
            }
        }
        if (BoomList.size() > 0) {
            for (int i = 0; i < BoomList.size(); i++) {
                Boom boom = BoomList.get(i);
                if (boom.Alive) {
                    boom.update();
                } else {
                    BoomList.remove(i);
                }
            }
        }
    }

    // @Override
    // public void paintComponent(Graphics g) {
    // // TODO Auto-generated method stub
    // super.paintComponent(g);
    // g2.setColor(Color.white);
    // g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
    // RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    // tileManager.drawGraphics(g2);
    // if (player != null)
    // player.drawGraphics(g2);
    // if (opponent != null) {
    // opponent.drawGraphics(g2);
    // }
    // if (BoomList.size() > 0) {
    // for (Boom boom : BoomList)
    // boom.drawGraphics(g2);
    // }
    // for (int i = 0; i < obstacleTileList.length; i++) {
    // if (obstacleTileList[i] != null)
    // obstacleTileList[i].drawGraphics(g2);
    // }
    // g2.setColor(Color.black);
    // if (player != null)
    // g2.drawString("Player:" + player.x + "|" + player.y, 100, 100);
    // if (opponent != null)
    // g2.drawString("Opponent:" + opponent.x + "|" + opponent.y, 100, 200);
    // // Show TempImage on GamePanel
    // Graphics2D g2 = (Graphics2D) getGraphics();
    // g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
    // RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    // g2.drawImage(TempImage, ScreenWidth / 2 - GameWitdh / 2,
    // ScreenHeight / 2 - GameHeight / 2, null);
    // }

    public void drawGraphics() {
        g2.setColor(Color.white);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        tileManager.drawGraphics(g2);
        ArrayList<Entity> entities = new ArrayList<>();
        if (player != null) {
            entities.add(player);
        }
        for(int i = 0;i < OpponentArr.length;i++)
        {
            Opponent currentOpponent = OpponentArr[i];
            if(currentOpponent != null)
            {
                entities.add(currentOpponent);
            }
        }
        for (int i = 0; i < obstacleTileList.length; i++) {
            if (obstacleTileList[i] != null)
                entities.add(obstacleTileList[i]);
        }
        Collections.sort(entities, new Comparator<Entity>() {

            @Override
            public int compare(Entity o1, Entity o2) {
                // TODO Auto-generated method stub
                if (o1.y <= o2.y)
                    return -1;
                else
                    return 1;
            }

        });
        for (Entity entity : entities) {
            entity.drawGraphics(g2);
        }
        if (BoomList.size() > 0) {
            for (Boom boom : BoomList)
                boom.drawGraphics(g2);
        }
        g2.setColor(Color.black);
        if (Debug) {
            if (player != null)
                g2.drawString("Player:" + player.x + "|" + player.y + " " + player.getRow() + "|" + player.getCol(),
                        100, 100);
            if (opponent != null)
                g2.drawString("Opponent:" + opponent.x + "|" + opponent.y, 100, 150);
        }
        // Show TempImage on GamePanel
        Graphics2D g2 = (Graphics2D) getGraphics();
        // g2.setColor(Color.red);
        // g2.fillRect(0, 0, ScreenWidth, ScreenHeight);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(TempImage, ScreenWidth / 2 - GameWitdh / 2,
                ScreenHeight / 2 - GameHeight / 2, null);
        g2.dispose();
    }
}
