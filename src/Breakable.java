import java.awt.*;
import java.awt.image.BufferedImage;

public class Breakable extends Unbreakable {
    private int health = 100;
    private static BufferedImage breakable_wall_img;
    private boolean dead = false;

    Breakable(int x, int y) {
        this.x = x;
        this.y = y;
        this.myRectangle = new Rectangle(x, y, breakable_wall_img.getWidth(), breakable_wall_img.getHeight());
    }

    private void removeHealth(int val) {
        if (health - val < 0) {
            health = 0;
            dead = true;
        } else {
            health -= val;
        }
    }

    int getHealth() {
        return this.health;
    }


    static void setBreakable(BufferedImage image) {
        Breakable.breakable_wall_img = image;
    }

    public void update() {

    }

    public void collision() {
        this.removeHealth(50);

    }

    public void drawImage(Graphics2D g2d) {

        if (!dead) {
            g2d.drawImage(breakable_wall_img, x, y, null);
        }

    }


}