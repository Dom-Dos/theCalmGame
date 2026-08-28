package rage;

import java.awt.Color;
import java.awt.Graphics2D;

public class MovingPlatform extends Platform {

    public int originalX;
    public  int range;
    public  int speed;
    public  boolean movingRight = true;
    public int originalSpeed;
    public int negativSpeed;

    public MovingPlatform(int x, int y, int width, int height, int range, int speed) {
        super(x, y, width, height,speed);
        this.originalX = x;
        this.range = range;
        this.speed = speed;
        originalSpeed = speed;
        negativSpeed = speed * -1;
        
    }

    public void update() {
        if (movingRight) {
        	speed = originalSpeed;
            x += speed;
            if (x >= originalX + range) {
                movingRight = false; 
            }
        } else {
        	speed = negativSpeed;
            x += speed;
            if (x <= originalX) {
                movingRight = true; 
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(220, 80, 80));
        g2.fillRect(x, y, width, height); 
    }
}