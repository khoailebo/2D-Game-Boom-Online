package tile;

import entity.Entity;
import main.GamePanel;

public class ObstacleTile extends Entity {

    public ObstacleTile(int row,int col, GamePanel gp) {
        super(gp);
        Collision = true;
        x = col * gamePanel.RealTile;
        y = row * gamePanel.RealTile;
        //TODO Auto-generated constructor stub
    }

}
