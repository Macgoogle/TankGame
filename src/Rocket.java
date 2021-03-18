import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Rocket extends GameObject {
    private String bullet_owner;
    private boolean isInActive = false;
    private boolean small_explosion = true;
    boolean collided = false;
    static private BufferedImage bullet_img;
    static private BufferedImage smallExplosionImg;
    static private BufferedImage largeExplosionImg;
    private int iterations_since_collided = 0;

    String getOwner() {
        return this.bullet_owner;
    }

    void setOwner(String owner) {
        this.bullet_owner = owner;
    }

    boolean getIsInactive() {
        return this.isInActive;
    }


    static void setBufferedImage(BufferedImage img) {
        bullet_img = img;
    }

    static void setSmallExplosion(BufferedImage exp) { // used to set the explosion image
        smallExplosionImg = exp;
    }

    static void setLargeExplosion(BufferedImage e){
        largeExplosionImg = e;
    }

    void setSmall_explosion(boolean val){ //if val is true, we use the small explosion image, if val is false, we use the big explosion image
        this.small_explosion = val;
    }
    boolean getSmall_explosion(){
        return this.small_explosion;
    }

    Rocket(int x, int y, int angle) {
        this.x = x;
        this.y = y;
        this.vx = (int) Math.round(3 * Math.cos(Math.toRadians(angle)));
        this.vy = (int) Math.round(3 * Math.sin(Math.toRadians(angle)));
        this.angle = angle;
        this.myRectangle = new Rectangle(x, y, bullet_img.getWidth(), bullet_img.getHeight());
    }

    public void update() {
        if (!collided) {
            this.x = x + vx;
            this.y = y + vy;
            this.checkBorder();
        } else {
            iterations_since_collided++;
        }
        this.myRectangle.setLocation(x,y);
    }


    public void drawImage(Graphics2D g2d) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), bullet_img.getWidth() / 2.0, bullet_img.getHeight() / 2.0);

        if (collided && small_explosion) {

            g2d.drawImage(smallExplosionImg, rotation, null);

            if (iterations_since_collided >= 5) {
                this.isInActive = true;
            }

        }else if(collided && !small_explosion){ // large explosion
            g2d.drawImage(largeExplosionImg, rotation, null);

            if (iterations_since_collided >= 5) {
                this.isInActive = true;
            }
        } else {
            g2d.drawImage(bullet_img, rotation, null);
        }
    }

    public void collision() {
        collided = true;
    }

    private void checkBorder() {
        int left_limit = 30;
        if (x < left_limit) {
            this.isInActive = true;
        }
        int right_limit = GameWorld.WORLD_Width - 65;
        if (x >= right_limit) {
            this.isInActive = true;
        }
        int lower_limit = 40;
        if (y < lower_limit) {
            this.isInActive = true;
        }
        int upper_limit = GameWorld.WORLD_Height - 60;
        if (y >= upper_limit) {
            this.isInActive = true;
        }
    }
}