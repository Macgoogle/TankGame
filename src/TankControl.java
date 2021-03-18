import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class TankControl implements KeyListener {

    private Tank tB;
    private final int up;
    private final int down;
    private final int right;
    private final int left;
    private final int shoot;

    public TankControl(Tank tB, int up, int down, int left, int right, int shoot) {
        this.tB = tB;
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.shoot = shoot;
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        if (keyPressed == up) {
            this.tB.toggleUpPressed();
        }
        if (keyPressed == down) {
            this.tB.toggleDownPressed();
        }
        if (keyPressed == left) {
            this.tB.toggleLeftPressed();
        }
        if (keyPressed == right) {
            this.tB.toggleRightPressed();
        }
        if (keyPressed == shoot) {
            this.tB.ShootPressed();
        }

    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        if (keyReleased == up) {
            this.tB.unToggleUpPressed();
        }
        if (keyReleased == down) {
            this.tB.unToggleDownPressed();
        }
        if (keyReleased == left) {
            this.tB.unToggleLeftPressed();
        }
        if (keyReleased == right) {
            this.tB.unToggleRightPressed();
        }
        if (keyReleased == shoot) {
            this.tB.Shoot_UN_Pressed();
        }


    }
}
