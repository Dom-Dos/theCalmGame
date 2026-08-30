package rage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;

public class SpikeTraps {
    int x;
    int y;
    int startY;
    int speed;
    int sizeY = 15;
    int sizeX;

    int timer = 0;
    private final int spikeWidth = 15;
    
    
    private enum State {
        EXTENDING,   
        WAIT_TOP,    
        RETRACTING, 
        WAIT_BOTTOM  
    }
    
    private State currentState = State.EXTENDING;

    public SpikeTraps(int x, int y, int sizeX, int speed) {
        this.x = x;
        this.y = y;
        this.startY = y;
        this.speed = speed;
        this.sizeY = 15;
        this.sizeX = sizeX;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, sizeX, sizeY);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED); 
        int numberOfSpikes = sizeX / spikeWidth;

        for (int i = 0; i < numberOfSpikes; i++) {
            int currentX = x + (i * spikeWidth);

            int[] xPoints = { currentX, currentX + (spikeWidth / 2), currentX + spikeWidth };
            int[] yPoints = { y + sizeY, y, y + sizeY };

            Polygon spike = new Polygon(xPoints, yPoints, 3);
            g2.fillPolygon(spike);
        }
    }

    public void update() {
        int targetYUpper = startY - sizeY; 

        switch (currentState) {
            case EXTENDING:
                y -= speed;
                if (y <= targetYUpper) {
                    y = targetYUpper; 
                    currentState = State.WAIT_TOP;
                    timer = 60;
                }
                break;

            case WAIT_TOP:
                timer--;
                if (timer <= 0) {
                    currentState = State.RETRACTING;
                }
                break;

            case RETRACTING:
                y += speed;
                if (y >= startY) {
                    y = startY; 
                    currentState = State.WAIT_BOTTOM;
                    timer = 60; 
                }
                break;

            case WAIT_BOTTOM:
                timer--;
                if (timer <= 0) {
                    currentState = State.EXTENDING;
                }
                break;
        }
    }
}