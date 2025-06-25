package main;

import tile.OT_WarningSign;

public class AssetSetter {
    GamePanel gamePanel;
    public AssetSetter(GamePanel gp)
    {
        this.gamePanel = gp;
    }
    public void setObstacleTile(){
        int i = 0;
        // for(int row = 0; row < gamePanel.Row;row++)
        // {
        //     gamePanel.obstacleTileList[i++] = new OT_WarningSign(row, 0, gamePanel);
        //     gamePanel.obstacleTileList[i++] = new OT_WarningSign(row, gamePanel.Row - 1, gamePanel);
        // }
        // for(int col = 0;col < gamePanel.Row;col++)
        // {
        //     gamePanel.obstacleTileList[i++] = new OT_WarningSign(0, col, gamePanel);
        //     gamePanel.obstacleTileList[i++] = new OT_WarningSign(gamePanel.Row - 1, col, gamePanel);
        // }
        gamePanel.obstacleTileList[i++] = new OT_WarningSign(2, 2, gamePanel);
        gamePanel.obstacleTileList[i++] = new OT_WarningSign(9, 2, gamePanel);
        gamePanel.obstacleTileList[i++] = new OT_WarningSign(2, 9, gamePanel);
        gamePanel.obstacleTileList[i++] = new OT_WarningSign(9, 9, gamePanel);
    }
}
