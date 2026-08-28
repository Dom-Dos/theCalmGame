package rage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Ball {

    public double x, y;
    public double startX, startY;
    public int size;
    public double speed;

    public Ball(double startX, double startY, int size, double speed) {
        this.startX = startX;
        this.startY = startY;
        this.x = startX;
        this.y = startY;
        this.size = size;
        this.speed = speed;
    }


    public void update(double targetX, double targetY) {
        double diffX = targetX - x;
        double diffY = targetY - y;
        double distance = Math.sqrt(diffX * diffX + diffY * diffY);
        if (distance > 0) {
            x += (diffX / distance) * speed;
            y += (diffY / distance) * speed;
        }
    }

    public Ellipse2D.Double getBounds() {
        return new Ellipse2D.Double(x, y, size, size);
    }

    public void reset() {
        this.x = startX;
        this.y = startY;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fillOval((int) x, (int) y, size, size);
    }
}