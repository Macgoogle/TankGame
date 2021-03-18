import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GameWorld extends JPanel{
    private static final int Screen_Height = 960;
    private static final int Screen_Width = 1920;
    static final int WORLD_Height = 1920;
    static final int WORLD_Width = 1920;
    //team blue tank spawn
    private final int TankB_spawn_x = 300;
    private final int TankB_spawn_y = 1700;
    private final int TankB_spawn_angle = 315;
    //team red tank spawn
    private final int TankR_spawn_x = 1700;
    private final int TankR_spawn_y = 300;
    private final int TankR_spawn_angle = 135;
    private BufferedImage world;
    private Graphics2D buffer;
    private JFrame jf;
    private Tank tB;
    private Tank tR;


    private static BufferedImage title_img;
    private boolean teamBWon = false;
    private boolean teamRWon = false;

    private CollisionHandler CH;
    static private Title t;


    void addGame_object(GameObject obj){this.game_objects.add(obj);}

    private ArrayList<GameObject> game_objects = new ArrayList<>();
    private int PlayerBLives = 2;
    private int PlayerRLives = 2;

    enum gameState{
        title, game, exit
    }
    static gameState state = gameState.title;

    public static void main(String[] args){
        Thread x;
        GameWorld sR = new GameWorld();
        sR.CH = new CollisionHandler();
        sR.init();

        try{
            while(true){
                sR.repaint();

                if(GameWorld.state == gameState.game){
                    for(int i = 0; i<sR.game_objects.size(); i++){
                        if(sR.game_objects.get(i) instanceof Rocket){
                           if(((Rocket)sR.game_objects.get(i)).getIsInactive()){
                               sR.game_objects.remove(i);
                               i--;
                           }
                           else{
                               sR.game_objects.get(i).update();
                           }
                        }
                        if(sR.game_objects.get(i) instanceof Tank){
                            if(((Tank)sR.game_objects.get(i)).getHealth()==0){
                                if((((Tank)sR.game_objects.get(i)).getTag()).equals("TankB")){
                                    if(sR.PlayerBLives > 1){
                                        sR.PlayerBLives--;

                                        ((Tank)sR.game_objects.get(i)).setHealth(100);
                                        sR.game_objects.get(i).setX(sR.TankB_spawn_x);
                                        sR.game_objects.get(i).setY(sR.TankB_spawn_y);
                                        sR.game_objects.get(i).setAngle(sR.TankB_spawn_angle);

                                    }
                                    else{
                                        sR.PlayerBLives = 0;
                                        sR.teamRWon = true;
                                        break;
                                    }
                                }
                                if((((Tank)sR.game_objects.get(i)).getTag()).equals("TankR")){
                                    if(sR.PlayerRLives > 1){
                                        sR.PlayerRLives--;

                                        ((Tank)sR.game_objects.get(i)).setHealth(100);
                                        sR.game_objects.get(i).setX(sR.TankR_spawn_x);
                                        sR.game_objects.get(i).setY(sR.TankR_spawn_y);
                                        sR.game_objects.get(i).setAngle(sR.TankR_spawn_angle);

                                    }
                                    else{
                                        sR.PlayerRLives = 0;
                                        sR.teamBWon = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if(((sR.game_objects.get(i)instanceof Breakable)&&((Breakable)sR.game_objects.get(i)).getHealth()==0)){
                            sR.game_objects.remove(i);
                        }
                    }
                    sR.game_objects = sR.CH.HandleCollisions(sR.game_objects);

                    sR.tB.update();
                    sR.tR.update();
                    Thread.sleep(1000/144);
                }
                else if (state == gameState.exit){
                    sR.jf.dispose();
                    System.exit(0);
                }
            }
        }
        catch(InterruptedException ignored){

        }
    }

    private void init(){
        this.jf = new JFrame("League Of Tanks");
        this.world = new BufferedImage(GameWorld.WORLD_Width, GameWorld.WORLD_Height, BufferedImage.TYPE_INT_RGB);
        BufferedImage tBImg = null, tRImg = null, rocketImg, backgroundImg, breakabalWallImg, unbreakableWallImg, smallExplosionImg, largeExplosionImg;

        try{
            tBImg = ImageIO.read(new FileInputStream("resources/Tank2.gif"));
            tRImg = ImageIO.read(new FileInputStream("resources/Tank1.gif"));

            backgroundImg = ImageIO.read(new FileInputStream("resources/Background.bmp"));
            Unbreakable.setBackground(backgroundImg);

            unbreakableWallImg = ImageIO.read(new FileInputStream("resources/Wall2.gif"));
            Unbreakable.setUnbreakable(unbreakableWallImg);

            breakabalWallImg = ImageIO.read(new FileInputStream("resources/Wall1.gif"));
            Breakable.setBreakable(breakabalWallImg);

            rocketImg = ImageIO.read(new FileInputStream("resources/Rocket.gif"));
            Rocket.setBufferedImage(rocketImg);

            smallExplosionImg = ImageIO.read(new FileInputStream("resources/Explosion_small.gif"));
            Rocket.setSmallExplosion(smallExplosionImg);

            largeExplosionImg = ImageIO.read(new FileInputStream("resources/Explosion_large.gif"));
            Rocket.setLargeExplosion(largeExplosionImg);

            Pickup.setPickup(ImageIO.read(new FileInputStream("resources/Pickup.gif")));

            GameWorld.title_img = ImageIO.read(new FileInputStream("resources/Title.bmp"));
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        tB = new Tank(TankB_spawn_x, TankB_spawn_y,0,0,TankB_spawn_angle, tBImg);
        tB.setTag("TankB");

        tR = new Tank(TankR_spawn_x, TankR_spawn_y,0,0,TankR_spawn_angle, tRImg);
        tR.setTag("TankR");

        t = new Title();

        for(int i = 0; i<WORLD_Width;i=i+320){
            for(int j=0; j< WORLD_Height; j=j+320){
                game_objects.add(new Unbreakable(i,j, true));
            }
        }

        int[] new_map_array = {
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,2,0,0,0,1,
            1,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,2,2,2,0,0,0,1,
            1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,1,
            1,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,1,
            1,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,1,
            1,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,1,
            1,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,1,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,
            1,0,0,0,2,2,2,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,2,2,2,2,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,
            1,0,0,0,2,1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
        };
        int column = 0;
        int entire_index = 0;

        for(int i=0; i<60;i++){
            for(int j = 0; j<60;j++){
                if(column==60){
                    column = 0;
                }
                int temp_val = new_map_array[entire_index];
                if(temp_val !=0){
                    if(temp_val ==2){
                        game_objects.add(new Breakable(j*32, i*32));
                    }
                    else{
                        game_objects.add(new Unbreakable(j*32,i*32, false));
                    }
                }
                column++;
                entire_index++;
            }
        }
        game_objects.add(tB);
        tB.setGW(this);
        game_objects.add(tR);
        tR.setGW(this);

        Pickup pickup1 = new Pickup(320,960, true);
        Pickup pickup2 = new Pickup(960,320, true);
        Pickup pickup3 = new Pickup(1600,960, true);
        Pickup pickup4 = new Pickup(960,1600, true);
        game_objects.add(pickup1);
        game_objects.add(pickup2);
        game_objects.add(pickup3);
        game_objects.add(pickup4);

        TankControl tcB = new TankControl(tB, KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_A,KeyEvent.VK_D,KeyEvent.VK_SPACE);
        TankControl tcR = new TankControl(tR, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);

        this.jf.setLayout(new BorderLayout());
        this.jf.add(this);

        this.jf.addKeyListener(tcB);
        this.jf.addKeyListener(tcR);
        this.addMouseListener(new MouseReader());

        this.jf.setSize(GameWorld.Screen_Width +18, GameWorld.Screen_Height+39);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setVisible(true);


    }
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        buffer = world.createGraphics();
        super.paintComponent(g2);

        if(GameWorld.state ==gameState.title){
            (g).drawImage(title_img,0,0,Screen_Width+2,Screen_Height, null);
            t.drawImage(g);
        }
        else if(GameWorld.state == gameState.game){
            for(int i=0; i< game_objects.size(); i++){
                game_objects.get(i).drawImage(buffer);
            }

            int playerB_x_Coord = tB.getX();
            int playerR_x_Coord = tR.getX();
            int playerB_y_Coord = tB.getY();
            int playerR_y_Coord = tR.getY();

            if (playerB_x_Coord < Screen_Width / 4) {
                playerB_x_Coord = Screen_Width / 4;
            }
            if (playerR_x_Coord < Screen_Width / 4) {
                playerR_x_Coord = Screen_Width / 4;
            }
            if (playerB_x_Coord > WORLD_Width - Screen_Width / 4) {
                playerB_x_Coord = WORLD_Width - Screen_Width / 4;
            }
            if (playerR_x_Coord > WORLD_Width - Screen_Width / 4) {
                playerR_x_Coord = WORLD_Width - Screen_Width / 4;
            }
            if (playerB_y_Coord < Screen_Height / 2) {
                playerB_y_Coord = Screen_Height / 2;
            }
            if (playerR_y_Coord < Screen_Height / 2) {
                playerR_y_Coord = Screen_Height / 2;
            }
            if (playerB_y_Coord > WORLD_Height - Screen_Height / 2) {
                playerB_y_Coord = WORLD_Height - Screen_Height / 2;
            }
            if (playerR_y_Coord > WORLD_Height - Screen_Height / 2) {
                playerR_y_Coord = WORLD_Height - Screen_Height / 2;
            }
            BufferedImage left_split_screen = world.getSubimage(playerB_x_Coord - Screen_Width / 4, playerB_y_Coord - Screen_Height / 2, Screen_Width / 2, Screen_Height);
            BufferedImage right_split_screen = world.getSubimage(playerR_x_Coord - Screen_Width / 4, playerR_y_Coord - Screen_Height / 2, Screen_Width / 2, Screen_Height);

            g2.drawImage(left_split_screen,0,0,null);
            g2.drawImage(right_split_screen, Screen_Width / 2+5,0,null);

            g2.drawImage(world, Screen_Width /2 -GameWorld.WORLD_Width/6/2, Screen_Height- GameWorld.Screen_Height/3, GameWorld.WORLD_Width / 6, WORLD_Height / 6, null);
            g2.setFont(new Font("SansSerif", Font.BOLD, 30));
            g2.setColor(Color.WHITE);
            g2.drawString("Player1 lives: " + this.PlayerBLives, 10, 28);
            g2.drawString("Player2 lives: " + this.PlayerRLives, Screen_Width / 2 + 10, 28);


            g2.drawString("[", 10, 58);
            g2.drawString("[", Screen_Width / 2 + 10, 58);
            g2.drawString("]", 230, 58);
            g2.drawString("]", Screen_Width / 2 + 230, 58);
            g2.setColor(Color.green);


            g2.fillRect(25, 40, 2 * tB.getHealth(), 20);
            g2.fillRect(Screen_Width / 2 + 25, 40, 2 * tR.getHealth(), 20);


            if (teamBWon) {
                g2.drawString("Blue Team Wins!", 336,480);
                g2.drawString("Blue Team Wins!", 1300,480);
            }
            if (teamRWon) {
                g2.drawString("Red Team Wins!", 336,480);
                g2.drawString("Red Team Wins!", 1300,480);
            }
        }
    }
}
