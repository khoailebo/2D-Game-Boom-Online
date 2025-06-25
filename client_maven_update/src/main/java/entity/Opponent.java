package entity;

import main.GamePanel;
import ui.components.EntityInfoPanel;

import java.awt.Rectangle;

public class Opponent extends Entity {

    @Override
    public void update() {
        synchronized(CurrentDirection)
        {
            // System.out.println("Update Opponent");
            CollisionOn = false;
            gamePanel.cChecker.checkCollision(this, gamePanel.player);
            for(int i = 0;i < gamePanel.OpponentArr.length;i++)
            {
                Opponent opponent = gamePanel.OpponentArr[i];
                if(opponent != null && opponent.Id != Id)
                {
                    gamePanel.cChecker.checkCollision(this, opponent);
                }
            }
            super.update();
        }
    }
    public Opponent(int x,int y,int width,int height,int speed,int life,GamePanel gp,String name,int id){
        super(gp);
        this.Name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        Width = width;
        Height = height;
        this.gamePanel = gp;
        this.Life = life;
        DefaultSolidAreaX = 8;
        DefaultSolidAreaY = 14;
        MaxBoom = 1;
        CurrentBoomNumber = 0;
        SolidArea = new Rectangle(DefaultSolidAreaX, DefaultSolidAreaY, 32, 32);
        getImage();
        getModelEntityInfoInstance();
        InforPanel = new EntityInfoPanel();
        InforPanel.setModel_data(getModelEntityInfoInstance());
        addInfoPanel(InforPanel, EntityInfoPanel.ComponentConstrains[id]);
        Id = id;
    }
}
