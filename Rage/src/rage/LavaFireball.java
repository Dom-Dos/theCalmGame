package rage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class LavaFireball {
	int size = 50;
	int jumpHeight = 100;
	double velocity ;
	double gravity = 0.3;
	int x;
	int y;
	int startY;
	int directionCounter = 0;
	boolean active = true;


	public LavaFireball(int x, int y) {
	    this.x = x;
	    this.y = y;
	    this.startY = y;
	    this.velocity = -10 - Math.random() * 4;  
	}

	public void update() {                  
	    velocity += gravity;
	    y += (int) velocity;

	    directionCounter = (directionCounter + 1) % 60;
	    x += (directionCounter < 30) ? -2 : 2;

	    if(velocity > 0 && y > startY) {
	    	active = false;
	    };          
	}
	
	public Rectangle getBounds(){
		return new Rectangle(x,y,size,size);
	}
	
	public void draw(Graphics2D g2){
		g2.setColor(Color.ORANGE);
		g2.fillOval(x, y, size, size);
	}
	

}
