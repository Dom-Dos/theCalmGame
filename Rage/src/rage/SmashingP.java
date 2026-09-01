package rage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SmashingP extends Platform {
    int moveTo;
    private State currentState = State.original;
    int timer;
    int startY;
    int startTimer;
    private int velocityY; 
    int direction;
    BufferedImage stoneface;
    
  

    public SmashingP(int x, int y, int width, int height, int speed, int moveTo, int timer) {
        super(x, y, width, height, speed);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.moveTo = moveTo;
        this.timer = timer;
        this.startTimer = timer;
        this.startY = y;
        this.velocityY = 0;
        
   
    	stoneface = ResourceLoader.loadImage("/stoneface.png");
 
    }

    private enum State {
        original, stop, backOff, reset
    }

    public void update() {
        switch (currentState) {
            case original:
                direction = (startY <= moveTo) ? 1 : -1;
                velocityY = Math.abs(speed) * direction;
                y += velocityY;
                if (Math.abs(y - moveTo) <= Math.abs(speed)) {
                    y = moveTo;
                    velocityY = 0; 
                    currentState = State.stop;
                }
                break;

            case stop:
                velocityY = 0; 
                timer--;
                if (timer <= 0) {
                    currentState = State.backOff;
                    timer = startTimer;
                }
                break;

            case backOff:
                direction = (startY <= moveTo) ? -1 : 1;
                velocityY = Math.abs(speed) * direction;
                y += velocityY;
                if (Math.abs(y - startY) <= Math.abs(speed)) {
                    y = startY;
                    velocityY = 0; 
                    currentState = State.reset;
                }
                break;

            case reset:
                velocityY = 0; 
                timer--;
                if (timer <= 0) {
                    currentState = State.original;
                    timer = startTimer;
                }
                break;
        }
    }

    public int getVelocityY() {
        return velocityY;
    }

    @Override
    public void draw(Graphics2D g2) {
    	
    	if (stoneface!= null) {
    	g2.drawImage(stoneface, x, y, width, height,null);
    	}else {
        g2.setColor(Color.RED);
        g2.fillRect(x, y, width, height);
    	}
    }

    public boolean isSmashingDown() {
        return currentState == State.original && startY < moveTo;
    }
}