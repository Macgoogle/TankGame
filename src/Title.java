import java.awt.*;
public class Title {
    public void drawImage(Graphics grph){
        Font f = new Font("arial",Font.BOLD,70);
        grph.setColor(Color.ORANGE);
        grph.setFont(f);
        grph.drawString("Press Any Key",1000,900);
    }
}
