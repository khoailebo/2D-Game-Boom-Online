package dtos;

public class BoomDTO {
    int x;
    int y;
    int Life;
    boolean Collision;
    public BoomDTO(int x, int y, int life, boolean collision) {
        this.x = x;
        this.y = y;
        Life = life;
        Collision = collision;
    } 
}
