package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.RenderingHints;
import Enum.Direction;
import dtos.ModelEntityInfo;
import dtos.PlayerDTO;
import main.GamePanel;
import socket.InvokeLater;
import ui.components.EntityInfoPanel;
import utility.GsonHelper;
import utility.UtilityHelper;
import java.awt.AlphaComposite;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.google.errorprone.annotations.ThreadSafe;

public abstract class Entity {
    public int x;
    public int y;
    public int Width;
    public boolean CollisionOn;
    public int Height;
    public int speed;
    public int Life;
    public boolean Collision = false;
    public EntityInfoPanel InforPanel;
    public boolean Alive = true;
    public Rectangle SolidArea;
    public BufferedImage currentPlayer,image, up1, up2, down1, down2, left1, left2, right1, right2;
    public int DefaultSolidAreaX;
    public int DefaultSolidAreaY;
    public int SpriteCounter;
    public boolean sendingBoom = false;
    public boolean sendingLocation = false;
    public String Name;
    public int SpriteNum;
    public boolean Explode = false;
    public int Id = -1;
    public int MaxBoom;
    public boolean Damaged = false;
    public int DamagedCounter = 0;
    public int CurrentBoomNumber;
    public GamePanel gamePanel;
    public int BoomExplodeSize = 1;
    public Direction CurrentDirection = Direction.NONE;
    public ModelEntityInfo ModelEntityInfoInstance;
    public InvokeLater UpdateInforPanel;
    public ModelEntityInfo getModelEntityInfoInstance() {
        if (ModelEntityInfoInstance == null) {
            ModelEntityInfoInstance = new ModelEntityInfo(MaxBoom, Life, speed,BoomExplodeSize, new ImageIcon(down1));
        }
        return ModelEntityInfoInstance;
    }

    public void addInfoPanel(EntityInfoPanel panel, String componentConstraint) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                gamePanel.add(panel, componentConstraint);
                gamePanel.repaint();
                gamePanel.revalidate();
            }

        }).start();
    }

    public void setModelEntityInfoInstance() {
        ModelEntityInfoInstance.setLife(Life);
        ModelEntityInfoInstance.setMaxBoom(MaxBoom);
        ModelEntityInfoInstance.setSpeed(speed);

    }

    public Entity(GamePanel gp) {
        this.gamePanel = gp;
        DefaultSolidAreaX = 0;
        DefaultSolidAreaY = 0;
        SolidArea = new Rectangle(DefaultSolidAreaX, DefaultSolidAreaY, 48, 48);
        Collision = true;
        UpdateInforPanel = new InvokeLater() {

            @Override
            public void call(Object... os) {
                InforPanel.updateState(getModelEntityInfoInstance());
            }
            
        };
    }

    public int getLeftX() {
        return SolidArea.x + x;
    }

    public int getRightX() {
        return SolidArea.x + x + SolidArea.width;
    }

    public int getTopY() {
        return SolidArea.y + y;
    }

    public int getBottomY() {
        return SolidArea.y + y + SolidArea.height;
    }

    public void getImage() {
        up1 = setup("/player/" + Name + "_up_1");
        up2 = setup("/player/" + Name + "_up_2");

        down1 = setup("/player/" + Name + "_down_1");
        down2 = setup("/player/" + Name + "_down_2");

        left1 = setup("/player/" + Name + "_left_1");
        left2 = setup("/player/" + Name + "_left_2");

        right1 = setup("/player/" + Name + "_right_1");
        right2 = setup("/player/" + Name + "_right_2");

        image = down1;
    }

    public void update() {
        gamePanel.cChecker.checkCollisionToList(this, gamePanel.BoomList);
        gamePanel.cChecker.checkCollisionToList(this, gamePanel.obstacleTileList);
        gamePanel.cChecker.checkCollisionToTiles(this);
        if (!CollisionOn) {
            switch (CurrentDirection) {
                case DOWN: {
                    y += speed;
                }
                    break;
                case UP: {
                    y -= speed;
                }
                    break;
                case RIGHT: {
                    x += speed;
                }
                    break;
                case LEFT: {
                    x -= speed;
                }
                    break;
                default:
                    break;
            }
        }
        SpriteCounter++;
        if (SpriteCounter > 10) {
            SpriteNum = (SpriteNum == 1 ? 2 : 1);
            SpriteCounter = 0;
        }
        if(Damaged)
        {
            DamagedCounter++;
            if(DamagedCounter >= 90)
            {
                DamagedCounter = 0;
                Damaged = false;
            }
        }
    };

    public void drawGraphics(Graphics2D g2) {
        // g2.fillRect(x, y, Width, Height);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        switch (CurrentDirection) {
            case UP:
                if (SpriteNum == 1) {
                    image = up1;
                } else
                    image = up2;
                break;
            case DOWN:
                if (SpriteNum == 1) {
                    image = down1;
                } else
                    image = down2;
                break;
            case LEFT:
                if (SpriteNum == 1) {
                    image = left1;
                } else
                    image = left2;
                break;
            case RIGHT:
                if (SpriteNum == 1) {
                    image = right1;
                } else
                    image = right2;
                break;
            default:
                break;
        }
        if(Damaged)
        {

                setAlphaComposite(g2, 0.5f);
        }
        g2.drawImage(image, x, y, null);
        setAlphaComposite(g2, 1f);

    }

    public void setAlphaComposite(Graphics2D g2,float alpha)
    {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
    }

    public Boom setUpBoom() {
        int boomX = 0;
        int boomY = 0;
        int playerCenterX = x + Width / 2;
        int playerCenterY = y + Height / 2;
        int colIndex = playerCenterX / gamePanel.RealTile;
        int rowIndex = playerCenterY / gamePanel.RealTile;
        boomX = colIndex * gamePanel.RealTile;
        boomY = rowIndex * gamePanel.RealTile;
        return new Boom(boomX, boomY, gamePanel.RealTile, gamePanel.RealTile, BoomExplodeSize, this, gamePanel);

    }

    public static void showCurrentTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Định dạng thời gian với mili giây
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDateTime = currentDateTime.format(formatter);

        // In ra thời gian hiện tại kèm mili giây
        System.out.println("Thời gian hiện tại (kèm mili giây): " + formattedDateTime);
    }

    public void generateBoom() {
        if (CurrentBoomNumber < MaxBoom) {
            // showCurrentTime();
            Boom boom = setUpBoom();
            gamePanel.BoomList.add(boom);
            CurrentBoomNumber++;
        }
    }

    public int getCol() {
        return x / gamePanel.RealTile;
    }

    public int getRow() {
        return y / gamePanel.RealTile;
    }

    public BufferedImage setup(String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return UtilityHelper.scaleImage(image, gamePanel.RealTile, gamePanel.RealTile);
    }

    public BufferedImage setup(String imagePath, int width, int height) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return UtilityHelper.scaleImage(image, width, height);
    }

    public void updateLife(int lifeUpdate) {
        Life += lifeUpdate;
        ModelEntityInfoInstance.Life = Life;
        InforPanel.updateState(ModelEntityInfoInstance);
        // PlayerDTO playerDTO = new PlayerDTO();
        // playerDTO.setLife(Life);
        // syncSendData("UpdateOpponentLife", GsonHelper.<PlayerDTO>toJsonObj(playerDTO), new InvokeLater() {

        //     @Override
        //     public void call(Object... os) {
        //         // TODO Auto-generated method stub
        //         Life += lifeUpdate;
        //     }

        // });
    }

    public void syncSendData(String eventName, String data, InvokeLater callback) {
        this.gamePanel.client.sendEvent(eventName, data, callback);

    }
}
