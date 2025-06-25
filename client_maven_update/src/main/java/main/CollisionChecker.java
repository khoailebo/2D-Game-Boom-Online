package main;

import java.util.ArrayList;

import Enum.Direction;
import entity.Boom;
import entity.Entity;

public class CollisionChecker {
    GamePanel gamePanel;

    public CollisionChecker(GamePanel gp) {
        this.gamePanel = gp;
    }

    // Check if source collision with target
    public void checkCollision(Entity source, Entity target) {
        boolean collision = false;
        Direction direction = source.CurrentDirection;
        source.SolidArea.x += source.x;
        source.SolidArea.y += source.y;

        target.SolidArea.x += target.x;
        target.SolidArea.y += target.y;
        switch (direction) {
            case UP:
                source.SolidArea.y -= source.speed;
                break;
            case DOWN:
                source.SolidArea.y += source.speed;
                break;
            case LEFT:
                source.SolidArea.x -= source.speed;
                break;
            case RIGHT:
                source.SolidArea.x += source.speed;
                break;

            default:
                break;
        }
        if (source.SolidArea.intersects(target.SolidArea)) {
            source.CollisionOn = true;
        }
        source.SolidArea.x = source.DefaultSolidAreaX;
        source.SolidArea.y = source.DefaultSolidAreaY;

        target.SolidArea.x = target.DefaultSolidAreaX;
        target.SolidArea.y = target.DefaultSolidAreaY;

    }

    public void checkCollisionToTiles(Entity entity) {
        int topY = entity.getTopY();
        int bottomY = entity.getBottomY();
        int leftX = entity.getLeftX();
        int rightX = entity.getRightX();

        int topRowIndex = topY / gamePanel.RealTile;
        int bottomRowIndex = bottomY / gamePanel.RealTile;
        int leftColIndex = leftX / gamePanel.RealTile;
        int rightColIndex = rightX / gamePanel.RealTile;
        int tileIndex1 = -1;
        int tileIndex2 = -1;
        switch (entity.CurrentDirection) {
            case UP:
                int nextY = topY - entity.speed;
                int nextRowIndex = nextY / gamePanel.RealTile;
                tileIndex1 = gamePanel.tileManager.Map[nextRowIndex][leftColIndex];
                tileIndex2 = gamePanel.tileManager.Map[nextRowIndex][rightColIndex];

                break;
            case DOWN:
                nextY = bottomY + entity.speed;
                nextRowIndex = nextY / gamePanel.RealTile;
                tileIndex1 = gamePanel.tileManager.Map[nextRowIndex][leftColIndex];
                tileIndex2 = gamePanel.tileManager.Map[nextRowIndex][rightColIndex];
                break;
            case LEFT:
                int nextX = leftX - entity.speed;
                int nextColIndex = nextX / gamePanel.RealTile;
                tileIndex1 = gamePanel.tileManager.Map[topRowIndex][nextColIndex];
                tileIndex2 = gamePanel.tileManager.Map[bottomRowIndex][nextColIndex];
                break;
            case RIGHT:
                nextX = rightX + entity.speed;
                nextColIndex = nextX / gamePanel.RealTile;
                tileIndex1 = gamePanel.tileManager.Map[topRowIndex][nextColIndex];
                tileIndex2 = gamePanel.tileManager.Map[bottomRowIndex][nextColIndex];
                break;
            default:
                tileIndex1 = gamePanel.tileManager.Map[topRowIndex][leftColIndex];
                tileIndex2 = gamePanel.tileManager.Map[topRowIndex][leftColIndex];
                break;
        }
        if (gamePanel.tileManager.TileList[tileIndex1].Collision ||
                gamePanel.tileManager.TileList[tileIndex1].Collision)
            entity.CollisionOn = true;
    }

    public boolean checkBoomExplodeToEntity(Boom boom, Entity entity) {
        boolean collision = false;
        entity.SolidArea.x = entity.x + entity.SolidArea.x;
        entity.SolidArea.y = entity.y + entity.SolidArea.y;
        if (boom.ExplodeArea.intersects(entity.SolidArea)) {
            collision = true;
        }
        entity.SolidArea.x = entity.DefaultSolidAreaX;
        entity.SolidArea.y = entity.DefaultSolidAreaY;
        return collision;
    }

    public <T extends Entity> int checkCollisionToList(Entity source, ArrayList<T> targets) {
        source.SolidArea.x += source.x;
        source.SolidArea.y += source.y;
        switch (source.CurrentDirection) {
            case UP:
                source.SolidArea.y -= source.speed;
                break;
            case DOWN:
                source.SolidArea.y += source.speed;
                break;
            case LEFT:
                source.SolidArea.x -= source.speed;
                break;
            case RIGHT:
                source.SolidArea.x += source.speed;
                break;

            default:
                break;
        }
        int index = -1;
        boolean collision = false;
        for (int i = 0; i < targets.size(); i++) {
            T target = targets.get(i);
            if (target.Collision) {
                target.SolidArea.x += target.x;
                target.SolidArea.y += target.y;
                collision = source.SolidArea.intersects(target.SolidArea);
                target.SolidArea.x = target.DefaultSolidAreaX;
                target.SolidArea.y = target.DefaultSolidAreaY;
                if (collision) {
                    index = i;
                    break;
                }
            }
        }
        source.SolidArea.x = source.DefaultSolidAreaX;
        source.SolidArea.y = source.DefaultSolidAreaY;

        if (collision)
            source.CollisionOn = collision;

        return index;
    }
    public void checkCollisionToList(Entity source, Entity[] targets) {
        source.SolidArea.x += source.x;
        source.SolidArea.y += source.y;
        switch (source.CurrentDirection) {
            case UP:
                source.SolidArea.y -= source.speed;
                break;
            case DOWN:
                source.SolidArea.y += source.speed;
                break;
            case LEFT:
                source.SolidArea.x -= source.speed;
                break;
            case RIGHT:
                source.SolidArea.x += source.speed;
                break;

            default:
                break;
        }
        boolean collision = false;
        for (int i = 0; i < targets.length; i++) {
            Entity target = targets[i];
            if (target != null && target.Collision) {
                target.SolidArea.x += target.x;
                target.SolidArea.y += target.y;
                collision = source.SolidArea.intersects(target.SolidArea);
                target.SolidArea.x = target.DefaultSolidAreaX;
                target.SolidArea.y = target.DefaultSolidAreaY;
                if (collision) {
                    break;
                }
            }
        }
        source.SolidArea.x = source.DefaultSolidAreaX;
        source.SolidArea.y = source.DefaultSolidAreaY;

        if (collision)
            source.CollisionOn = collision;

        
    }
}
