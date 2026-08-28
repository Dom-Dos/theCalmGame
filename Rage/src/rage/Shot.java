package rage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class Shot {
	
	private BufferedImage fireball;
	
    public int x, y;
    public int speed = 5;
    public int size = 40;
    public int direction; // 1 = Rechts, -1 = Links
    public boolean active = true;

    public Shot(int x, int y, int direction) {
        this.x = x;
        this.y = y-20;
        this.direction = direction;
    }

    public void update() {
        x += speed * direction; 
        int startX = GamePanel.getPlayerX();
        if (x < startX-250 || x > startX + 300) {
            active = false;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(java.awt.Color.YELLOW);
        g2.fillOval(x, y, size, size);
    }


    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }
    public void draw1(Graphics2D g2) {
    	fireball = ResourceLoader.loadImage("/fireball.png");
    	if (fireball != null) {
            g2.drawImage(fireball, x, y, size, size, null);
        } else {
        	g2.setColor(java.awt.Color.YELLOW);
            g2.fillOval(x, y, size, size);
        }
    	
    }
}