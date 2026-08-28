package rage;


import java.awt.Color;
import java.awt.Graphics2D;

public class AvoidingPlatform extends Platform {

    public double originalX;
    public double currentX;
    public int detectionRange = 150; 
    public int maxDistance = 200;
    public int originalSpeed;
    public int negativSpeed;

    public AvoidingPlatform(int x, int y, int width, int height,int speed) {
        super(x, y, width, height,speed);
        this.originalX = x;
        this.currentX = x;
        originalSpeed = speed;
        negativSpeed = speed *-1;
    }

    public void update(int playerX, int playerY) {
        
        double platformCenterX = currentX + (width / 2.0);
        double platformCenterY = y + (height / 2.0);
        
        double distanceX = playerX - platformCenterX;
        double distanceY = playerY - platformCenterY;
        
        
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

     
        if (distance < detectionRange) {
            if (playerX < platformCenterX) {
                if (currentX < originalX + maxDistance) {
                	speed = originalSpeed;
                    currentX += speed;
                }
            } else {
                if (currentX > originalX - maxDistance) {
                	speed = negativSpeed;
                    currentX += speed;
                }
            }
        } 
        else {
            if (currentX < originalX) {
                currentX += 1.5;
                if (currentX > originalX) currentX = originalX;
            } else if (currentX > originalX) {
                currentX -= 1.5;
                if (currentX < originalX) currentX = originalX;
            }
        }
        this.x = (int) currentX;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(220, 80, 80));
        g2.fillRect(x, y, width, height);
    }
}