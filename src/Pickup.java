import java.awt.*;
import java.awt.image.BufferedImage;

public class Pickup extends GameObject {
    boolean isPicked;
    boolean isActive = true;
    static private BufferedImage pickup;

    public Pickup(int x, int y, boolean isPicked){
        this.x = x;
        this.y = y;
        this.myRectangle = new Rectangle(x, y, 40, 40);
        this.isPicked = isPicked;

    }
    static void setPickup(BufferedImage img){
        Pickup.pickup = img;
    }


    public void update() {

    }

    public void drawImage(Graphics2D g) {
        if(this.isPicked){
            g.drawImage(pickup, x,y, 40,40 , null);
        }
    }

    public void collision() {
        this.isActive = false;
    }
}
