package tile;
import java.awt.image.BufferedImage;
public class Tile {
    public BufferedImage Image;
    public boolean Collision;
    public Tile(BufferedImage image, boolean collision) {
        Image = image;
        Collision = collision;
    }
    
}
