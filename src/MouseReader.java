import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



public class MouseReader implements MouseListener{


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

                GameWorld.state = GameWorld.gameState.game;

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}