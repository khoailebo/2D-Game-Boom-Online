package dtos;

import Enum.Direction;

public class PlayerDTO {
    public int x;
    public int y;
    public int speed;
    public Direction direction;
    public int life;
    public String name;
    public PlayerDTO(int x,int y,int speed,Direction direction,int life)
    {
        this.life = life;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = direction;
    }
    public PlayerDTO(int x,int y,int speed,Direction direction,int life,String name)
    {
        this.life = life;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = direction;
        this.name = name;
    }
    public PlayerDTO(){
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public Direction getDirection() {
        return direction;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public int getLife() {
        return life;
    }
    public void setLife(int life) {
        this.life = life;
    }
    
}
