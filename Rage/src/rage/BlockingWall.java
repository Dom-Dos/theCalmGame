package rage;

import java.awt.Color;
import java.awt.Graphics2D;

public class BlockingWall extends Platform {

    public double originalY;
    public double currentY;
    public int detectionRangeX = 250;
    public int maxBlockDistance = 250;
    private boolean moveUpToBlock;

   
    public BlockingWall(int x, int y, int width, int height,int speed, boolean moveUpToBlock) {
        super(x, y, width, height,speed); 
        this.originalY = y;
        this.currentY = y;
        this.moveUpToBlock = moveUpToBlock;
    }

    public void update(int playerX, int playerY) {
        int distanceX = Math.abs(playerX - x);
        double targetY = originalY;
        if (moveUpToBlock) {
            targetY = originalY - maxBlockDistance; 
        } else {
            targetY = originalY + maxBlockDistance; 
        }
        if (distanceX < detectionRangeX) {
            if (moveUpToBlock) {
                if (currentY > targetY) {
                    currentY -= speed;
                    if (currentY < targetY) currentY = targetY;
                }
            } else {
                if (currentY < targetY) {
                    currentY += speed;
                    if (currentY > targetY) currentY = targetY;
                }
            }
        } 
        else {
            if (currentY < originalY) {
                currentY += 2.0;
                if (currentY > originalY) currentY = originalY;
            } else if (currentY > originalY) {
                currentY -= 2.0;
                if (currentY < originalY) currentY = originalY;
            }
        }
        this.y = (int) currentY;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(200, 70, 30));
        g2.fillRect(x, y, width, height);


        g2.setColor(Color.ORANGE);
        g2.drawRect(x, y, width, height);
    }
}