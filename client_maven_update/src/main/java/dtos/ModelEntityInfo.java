package dtos;

import javax.swing.ImageIcon;

public class ModelEntityInfo {
    public int MaxBoom;
    public int Life;
    public int Speed;
    public int BoomSize;
    public int getBoomSize() {
        return BoomSize;
    }
    public void setBoomSize(int boomSize) {
        BoomSize = boomSize;
    }
    public ImageIcon Image;
    
    public ModelEntityInfo(int maxBoom, int life, int speed,int boomSize, ImageIcon image) {
        BoomSize = boomSize;
        MaxBoom = maxBoom;
        Life = life;
        Speed = speed;
        Image = image;
    }
    public int getMaxBoom() {
        return MaxBoom;
    }
    public void setMaxBoom(int maxBoom) {
        MaxBoom = maxBoom;
    }
    public int getLife() {
        return Life;
    }
    public void setLife(int life) {
        Life = life;
    }
    public int getSpeed() {
        return Speed;
    }
    public void setSpeed(int speed) {
        Speed = speed;
    }
    public ImageIcon getImage() {
        return Image;
    }
    public void setImage(ImageIcon image) {
        Image = image;
    }
    
}
