package tile;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.GamePanel;

public class OT_WarningSign extends ObstacleTile{

    public OT_WarningSign(int row,int col,GamePanel gp) {
        super(row,col,gp);
        Width = 48;
        Height = 58;
        DefaultSolidAreaX = 0;
        DefaultSolidAreaY = 0;
        SolidArea = new Rectangle(DefaultSolidAreaX,DefaultSolidAreaY,Width,Width);
        image = setup("/tile/tile-warning_sign", Width, Height);
        //TODO Auto-generated constructor stub
    }
    @Override
    public void drawGraphics(Graphics2D g2) {
        g2.drawImage(image,x ,y - 10 , null);
    }

}
