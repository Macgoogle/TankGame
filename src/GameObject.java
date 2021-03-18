import java.awt.*;
public abstract class GameObject {
    int x;
    int y;
    int vx;
    int vy;
    int angle;
    int height;
    int width;

    Rectangle myRectangle;
    void setX(int x){this.x = x;}
    int getX(){return this.x;}

    void setY(int y){this.y = y;}
    int getY(){return this.y;}

    void setVx(int vx){this.vx = vx;}
    int getVx(){return this.vx;}

    void setVy(int vy){this.vy = vy;}
    int getVy(){return this.vy;}

    void setAngle(int angle){this.angle = angle;}
    int getAngle(){return this.angle;}

    public abstract void update();
    public abstract void drawImage(Graphics2D g);
    public abstract void collision();

}
