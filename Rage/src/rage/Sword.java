package rage;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Sword {
	private BufferedImage sword;
    public int x, y;
    public int width = 30;
    public int height = 32;
    public boolean active = true;
    public int swordCurrentY = 0;
    
    private int duration = 14*4; 
    private int speedY = 1;    

    public Sword(int x, int y) {
        this.x = x;
        this.y = y - 30; 
    }

    public void update() {
    	int playerY = GamePanel.getPlayerY() -20;
        y = playerY + swordCurrentY; 
        swordCurrentY += speedY;
        duration--;

        if (duration <= 0) {
            active = false; 
        }
    }

    	
    
    public void draw(Graphics2D g2) {
        g2.setColor(java.awt.Color.YELLOW);
        g2.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    public void draw1(Graphics2D g2) {
    	int compare = GamePanel.getDir();
    	if ( compare == 1) {
    		sword = ResourceLoader.loadImage("/swordRight.png");
    	}else if(compare == -1) {
    		sword = ResourceLoader.loadImage("/swordLeft.png");
    	}
    	
    	if (sword != null) {
            g2.drawImage(sword, x, y, width, height, null);
        } else {
        	g2.setColor(java.awt.Color.YELLOW);
            g2.fillOval(x, y,  width, height);
        }
}
}