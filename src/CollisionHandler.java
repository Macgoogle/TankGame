import java.awt.*;
import java.util.ArrayList;

public class CollisionHandler {
    ArrayList<GameObject> HandleCollisions(ArrayList<GameObject> game_objects) {

        for (int i = 0; i < game_objects.size(); i++) {

            for (int j = i; j < game_objects.size(); j++) {
                GameObject obj_at_i = game_objects.get(i);
                GameObject obj_at_j = game_objects.get(j);

                if (i != j) {



                    if (obj_at_i instanceof Rocket && obj_at_j instanceof Tank && !(((Rocket) obj_at_i).getOwner().equals(((Tank) obj_at_j).getTag())) && !((Rocket) obj_at_i).collided) { // we make sure the bullet hasn't already collided(so we don't call collisions again)
                        if (obj_at_i.myRectangle.intersects(obj_at_j.myRectangle)) {
                            obj_at_i.collision();
                            ((Rocket) obj_at_i).setSmall_explosion(false);
                            obj_at_j.collision();
                        }


                    }
                    if (obj_at_i instanceof Tank && obj_at_j instanceof Rocket && !((Rocket) obj_at_j).getOwner().equals(((Tank) obj_at_i).getTag()) && !((Rocket) obj_at_j).collided) {
                        if (obj_at_i.myRectangle.intersects(obj_at_j.myRectangle)) {
                            ((Rocket) obj_at_j).setSmall_explosion(false);
                            obj_at_j.collision();
                            obj_at_i.collision();
                        }

                    }

                    if (((obj_at_j instanceof Rocket && obj_at_i instanceof Breakable && !((Rocket) obj_at_j).collided))) {
                        if (obj_at_i.myRectangle.intersects(obj_at_j.myRectangle)) {
                            obj_at_j.collision();
                            obj_at_i.collision();
                        }

                    }


                    if (obj_at_i instanceof Tank && obj_at_j instanceof Breakable) {
                        Rectangle r1 = ((Tank) obj_at_i).getOffsetBounds();
                        if (r1.intersects(obj_at_j.myRectangle)) {

                            ((Tank) obj_at_i).setdont_move(true);

                        }

                    }

                    if (obj_at_i instanceof Breakable && obj_at_j instanceof Tank) {

                        Rectangle r2 = ((Tank) obj_at_j).getOffsetBounds();
                        if (r2.intersects(obj_at_i.myRectangle)) {

                            ((Tank) obj_at_j).setdont_move(true);

                        }

                    }
                    if (obj_at_i instanceof Unbreakable && obj_at_j instanceof Tank) {

                        Rectangle r2 = ((Tank) obj_at_j).getOffsetBounds();
                        if (r2.intersects(obj_at_i.myRectangle)) {

                            ((Tank) obj_at_j).setdont_move(true);

                        }

                    }

                    if (obj_at_i instanceof Tank && obj_at_j instanceof Pickup) {
                        if (obj_at_i.myRectangle.intersects(obj_at_j.myRectangle)) {
                            if (((Pickup) obj_at_j).isPicked) {
                                ((Tank) obj_at_i).setHealth(((Tank)obj_at_i).getHealth()*2);
                                System.out.println("Health power up picked up");
                                game_objects.remove(j);

                            }
                        }

                    }
                }

            }
        }


        return game_objects;
    }
}
