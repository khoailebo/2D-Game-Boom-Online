package utility;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
public class UtilityHelper {
    public static BufferedImage scaleImage(BufferedImage image,int width,int height){
        BufferedImage scaleImage = new BufferedImage(width, height, image.getType());
        Graphics2D g2 = scaleImage.createGraphics();
        g2.drawImage(image, 0, 0,width,height,null);
        return scaleImage;
    }
    public static BufferedImage scaleImage(BufferedImage image,int xOffset,int yOffset,int scaleWidth,int scaleHeight,int width,int height){
        BufferedImage scaleImage = new BufferedImage(width, height, image.getType());
        Graphics2D g2 = scaleImage.createGraphics();
        g2.drawImage(image, xOffset, yOffset,scaleWidth,scaleHeight,null);
        return scaleImage;
    }
}
