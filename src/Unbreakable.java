import java.awt.*;
import java.awt.image.BufferedImage;

public class Unbreakable extends GameObject {

    private boolean is_background;
    private static BufferedImage background_img;
    private static BufferedImage unbreakable_wall_img;


    public static void setUnbreakable(BufferedImage image) {
        unbreakable_wall_img = image;
    }


    public static void setBackground(BufferedImage image) {
        background_img = image;
    }

    public Unbreakable() {

    }

    public Unbreakable(int x, int y, boolean is_background) {
        this.x = x;
        this.y = y;
        this.is_background = is_background;
        this.myRectangle = new Rectangle(x, y, 32, 32);

    }

    public void update() {

    }

    public void collision() {

    }

    public void drawImage(Graphics2D g2d) {

        if (this.is_background) {

            g2d.drawImage(background_img, x, y, null);

        } else {              //unbreakable wall
            g2d.drawImage(unbreakable_wall_img, x, y, null);
        }

    }


}