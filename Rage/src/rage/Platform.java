package rage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Platform {

    public int x, y, width, height,speed;

    public Platform(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.fillRect(x, y, width, height);
    }
}