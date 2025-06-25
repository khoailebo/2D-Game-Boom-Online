package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.lang.Math;
import java.awt.image.BufferedImage;
import Enum.Direction;
import main.GamePanel;
import tile.ObstacleTile;

public class Boom extends Entity {
    Entity User;
    BufferedImage explode_center, explode_vertical, explode_hirozontal, explode_up, explode_down, explode_left,
            explode_right;
    int DefaultLife = 90;
    public int ExplodeSize = 1;
    public int ExplodeMaxSizeUp = 0;
    public int ExplodeMaxSizeDown = 0;
    public int ExplodeMaxSizeLeft = 0;
    public int ExplodeMaxSizeRight = 0;

    public int ExplodeSizeUp;
    public int ExplodeSizeDown;
    public int ExplodeSizeLeft;
    public int ExplodeSizeRight;
    public Area ExplodeArea;

    public int ExplodeCounter = 0;
    public boolean FinishExplode = false;

    public Boom(int x, int y, int width, int height, int boomExplodeSize, Entity user, GamePanel gp) {
        super(gp);
        this.x = x;
        this.y = y;
        this.Width = width;
        this.Height = height;
        this.DefaultSolidAreaX = 0;
        this.DefaultSolidAreaY = 0;
        this.Collision = false;
        this.gamePanel = gp;
        this.SolidArea = new Rectangle(DefaultSolidAreaX, DefaultSolidAreaY, 48, 48);
        this.User = user;
        this.Life = DefaultLife;
        this.ExplodeSize = boomExplodeSize;
        this.ExplodeArea = getExplodeArea();
        CurrentDirection = Direction.DOWN;
        getBoomImage();
        // TODO Auto-generated constructor stub
    }

    public void getBoomImage() {
        down1 = setup("/boom/boom-01",62,62);
        down2 = setup("/boom/boom-02",62,62);
        explode_vertical = setup("/boom/explode-vertical");
        explode_hirozontal = setup("/boom/explode-hori");
        explode_down = setup("/boom/explode-down");
        explode_up = setup("/boom/explode-up");
        explode_left = setup("/boom/explode-left");
        explode_right = setup("/boom/explode-right");
        explode_center = setup("/boom/explode-center", 68, 68);
    }

    public void damgePlayer() {
        if (gamePanel.player != null && 
        !gamePanel.player.Damaged && gamePanel.cChecker.checkBoomExplodeToEntity(this, gamePanel.player)) {
            // gamePanel.player.Life--;
            gamePanel.player.updateLife(-1);
            gamePanel.player.Damaged = true;
        }
        for(int i = 0;i < gamePanel.OpponentArr.length;i++)
        {
            Opponent currentOpponent = gamePanel.OpponentArr[i];
            if(currentOpponent != null &&
                !currentOpponent.Damaged && 
                gamePanel.cChecker.checkBoomExplodeToEntity(this, currentOpponent))
            {
                currentOpponent.Damaged = true;
                currentOpponent.updateLife(-1);
            }
        }
    }

    @Override
    public void update() {
        if (!Explode) {
            CollisionOn = false;
            Life--;
            gamePanel.cChecker.checkCollision(this, User);
            Collision = !CollisionOn;
            SpriteCounter++;
            if (SpriteCounter > 10) {
                SpriteNum = (SpriteNum == 1 ? 2 : 1);
                SpriteCounter = 0;
            }

            if (Life == 0) {
                Explode = true;
                damgePlayer();
            }

        } else {
            ExplodeCounter++;
            int updateUnit = (FinishExplode ? 30 : 5);
            if (ExplodeCounter % updateUnit == 0) {
                ExplodeSizeUp = Math.min(++ExplodeSizeUp, ExplodeMaxSizeUp);
                ExplodeSizeDown = Math.min(++ExplodeSizeDown, ExplodeMaxSizeDown);
                ExplodeSizeLeft = Math.min(++ExplodeSizeLeft, ExplodeMaxSizeLeft);
                ExplodeSizeRight = Math.min(++ExplodeSizeRight, ExplodeMaxSizeRight);

                if (FinishExplode) {
                    Alive = false;
                    User.CurrentBoomNumber--;
                    // System.out.println(User.CurrentBoomNumber);
                }
                if (ExplodeSizeUp == ExplodeMaxSizeUp &&
                        ExplodeSizeDown == ExplodeMaxSizeDown &&
                        ExplodeSizeLeft == ExplodeMaxSizeLeft &&
                        ExplodeSizeRight == ExplodeMaxSizeRight) {

                    FinishExplode = true;
                }
            }
        }
    }

    public Area getExplodeArea() {
        ExplodeMaxSizeDown = getExplodeMaxsizeAlongAxis(ExplodeSize, Direction.DOWN);
        ExplodeMaxSizeUp = getExplodeMaxsizeAlongAxis(ExplodeSize, Direction.UP);
        ExplodeMaxSizeLeft = getExplodeMaxsizeAlongAxis(ExplodeSize, Direction.LEFT);
        ExplodeMaxSizeRight = getExplodeMaxsizeAlongAxis(ExplodeSize, Direction.RIGHT);
        Rectangle verticalRect = new Rectangle(x, y - gamePanel.RealTile *
                ExplodeMaxSizeUp, gamePanel.RealTile, gamePanel.RealTile *
                        (ExplodeMaxSizeUp + ExplodeMaxSizeDown + 1));
        Rectangle horizonRect = new Rectangle(x - ExplodeMaxSizeLeft *
                gamePanel.RealTile, y, (ExplodeMaxSizeLeft + ExplodeMaxSizeRight + 1) *
                        gamePanel.RealTile,
                gamePanel.RealTile);
        Area joinArea = new Area(verticalRect);
        joinArea.add(new Area(horizonRect));
        return joinArea;
    }

    public int getExplodeMaxsizeAlongAxis(int ExplodeSize, Direction direction) {
        int maxSize = 0;
        int row = getRow();
        int col = getCol();
        switch (direction) {
            case UP:
                for (int i = 0; i < ExplodeSize; i++) {
                    row--;
                    if (obstacleAhead(row, col))
                        break;

                    maxSize++;
                }
                break;
            case DOWN:
                for (int i = 0; i < ExplodeSize; i++) {
                    row++;
                    if (obstacleAhead(row, col))
                        break;

                    maxSize++;
                }
                break;
            case LEFT:
                for (int i = 0; i < ExplodeSize; i++) {
                    col--;
                    if (obstacleAhead(row, col))
                        break;

                    maxSize++;
                }
                break;
            case RIGHT:
                for (int i = 0; i < ExplodeSize; i++) {
                    col++;
                    if (obstacleAhead(row, col))
                        break;

                    maxSize++;
                }
                break;
        }
        return maxSize;
    }

    public boolean obstacleAhead(int row, int col) {
        int tileIndex = gamePanel.tileManager.Map[row][col];
        Rectangle tempRect = new Rectangle(col * gamePanel.RealTile,
                                row * gamePanel.RealTile,48,48);
        boolean collision = false;
        collision = gamePanel.tileManager.TileList[tileIndex].Collision;
        for(int i = 0;i < gamePanel.obstacleTileList.length;i++)
        {
            ObstacleTile ot = gamePanel.obstacleTileList[i];
            if(ot != null)
            {
                ot.SolidArea.x += ot.x;
                ot.SolidArea.y += ot.y;
                if(ot.SolidArea.intersects(tempRect)){
                    collision = true;
                }
                ot.SolidArea.x = ot.DefaultSolidAreaX;
                ot.SolidArea.y = ot.DefaultSolidAreaY;
                if(collision)break;
            }
        }
        return collision;
    }

    public void drawExplode(BufferedImage imageBody,BufferedImage imageHead,int ExplodeSize, int direction, int axis, Graphics2D g2) {
        int a = 0;
        int b = direction * gamePanel.RealTile;
        if (axis == 1) {
            a = 0;
            b = direction * gamePanel.RealTile;
        } else {
            int c = a;
            a = b;
            b = c;
        }
        for (int i = 1; i < ExplodeSize; i++) {

            g2.drawImage(imageBody,x + i * a, y + i * b, null);
        }
        if (a == 0)
            b = b * ExplodeSize;
        else
            a = a * ExplodeSize;
        g2.drawImage(imageHead,x + a, y + b,null);
        // if (a == 0)
        //     b += -12 * (direction - 1);
        // else
        //     a += -12 * (direction - 1);
        // g2.fillRect(x + a, y + b, Width + (a == 0 ? 0 : -24), Height + (b == 0 ? 0 : -24));
    }

    @Override
    public void drawGraphics(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (Explode) {
            // g2.fillOval(x - 15, y - 15, Width + 30, Height + 30);
            if (ExplodeSizeUp != 0) {
                drawExplode(explode_vertical,explode_up,ExplodeSizeUp, -1, 1, g2);
            }
            if (ExplodeSizeDown != 0) {
                drawExplode(explode_vertical,explode_down,ExplodeSizeDown, 1, 1, g2);
            }
            if (ExplodeSizeLeft != 0) {
                drawExplode(explode_hirozontal,explode_left,ExplodeSizeLeft, -1, 0, g2);
            }
            if (ExplodeSizeRight != 0) {
                drawExplode(explode_hirozontal,explode_right,ExplodeSizeRight, 1, 0, g2);
            }
            g2.drawImage(explode_center, x - 10,y - 10, null);

            // g2.fill(horizonRect);
            // g2.fill(verticalRect);
        } else {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            BufferedImage image = null;
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
            g2.drawImage(image, x - 7, y -14, null);
            // super.drawGraphics(g2);
        }
    }
}
