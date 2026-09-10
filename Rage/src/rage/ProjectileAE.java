package rage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ProjectileAE {
    public double x, y;
    public int size = 10;
    
    private double velX, velY;
    private double speed = 3.0; 

    public ProjectileAE(int x, int y, int playerX, int playerY) {
        this.x = x;
        this.y = y;
        calculateDirection(playerX, playerY);
    }

    private void calculateDirection(int playerX, int playerY) {
        double diffX = playerX - x;
        double diffY = playerY+20 - y;

        double angle = Math.atan2(diffY, diffX);

        velX = Math.cos(angle) * speed;
        velY = Math.sin(angle) * speed;
        System.out.println(angle+" " + Math.cos(angle)+" "+ Math.sin(angle));
    }

    public void update() {
        x += velX;
        y += velY;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }
    public void draw(Graphics g2) {
    	g2.setColor(Color.BLUE);
    	g2.fillOval((int)x, (int)y, size, size);
    }
}