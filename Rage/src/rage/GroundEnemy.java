package rage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

public class GroundEnemy {
    public int x;
    public int y;
    public int moveRadius;
    public int sizeX;
    public int sizeY;
    public int speed;
    public boolean movingRight = true;
    public int originalSpeed;
    public int negativSpeed;
    public int originalX = 0;
    public boolean isGrounded = false;
    public double velocityY = 0;
    public double gravity = 0.5;

    public GroundEnemy(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.moveRadius = 0;
        this.speed = speed;
        this.originalSpeed = speed;
        this.negativSpeed = speed * -1;
        this.originalX = x;
        this.sizeX = 20;
        this.sizeY = 20;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, sizeX, sizeY);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.gray);
        g2.fillRect(x, y, sizeX, sizeY);
    }

    // Übergabe der Plattformen-Liste aus dem GamePanel
    public void update(ArrayList<Platform> platforms) {
        velocityY += gravity;
        y += velocityY;
        isGrounded = false;

   
        for (Platform pf : platforms) {
            Rectangle pf_bounds = pf.getBounds();
            if (this.getBounds().intersects(pf_bounds)) {
                if (velocityY > 0 && (y + sizeY - velocityY) <= pf.y) {
                    y = pf.y - sizeY; 
                    velocityY = 0;
                    isGrounded = true;
                    if (moveRadius == 0) {
                        moveRadius = pf.width; 
                    }
                }
            }
        }

        if (isGrounded) {
            if (movingRight) {
                speed = originalSpeed;
                x += speed;
                if (x >= originalX + moveRadius - sizeX) {
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
    }
  
}