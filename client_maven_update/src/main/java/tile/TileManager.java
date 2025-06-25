package tile;

import main.GamePanel;
import utility.UtilityHelper;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TileManager {
    public int [][] Map;
    public Tile [] TileList = new Tile[30];
    public void setUpTile(){
        setUp(0, "/tile/tile-01", true);
        setUp(1, "/tile/tile-02", false);
        setUp(2, "/tile/tile-03", true);
        setUp(3, "/tile/tile-04", false);


    }
    public void loadMap(){
        Map = new int[gamePanel.Row][gamePanel.Row];
        for(int row = 0;row < gamePanel.Row;row++)
        {
            for(int col = 0;col < gamePanel.Row;col++)
            {
                if(row == 0 || col == 0 ||
                row == gamePanel.Row - 1 || 
                col == gamePanel.Row - 1)
                {
                    Map[row][col] = 0;
                }
                else {
                    if(col % 2 == 0)
                    Map[row][col] = 1;
                    else Map[row][col] = 3;
                }
            }
        }
    }
    public void setUp(int index,String imagePath,boolean collision)
    {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = UtilityHelper.scaleImage(image, gamePanel.RealTile, gamePanel.RealTile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TileList[index] = new Tile(image,collision);
    }
    GamePanel gamePanel;
    public TileManager(GamePanel gp){
        this.gamePanel = gp;
    }
    public void drawGraphics(Graphics2D g2){
        for(int i = 0;i < gamePanel.Row;i++)
        {
            for(int j = 0;j < gamePanel.Row;j++)
            {
                g2.drawImage(TileList[Map[i][j]].Image, j * gamePanel.RealTile, i * gamePanel.RealTile,null);
            }
        }
    }
}
