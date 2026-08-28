package rage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Hook {
	public int x;
	public int y;
	public int hookSize;
	public Hook(int x, int y) {
		this.x= x;
		this.y= y;
		hookSize = 20;
	}
	public Rectangle getbounds() {
		return new Rectangle(x, y, hookSize, hookSize);
	}
	public double getDistance(int playerX, int playerY) {
		 	double platformCenterX = x+ (hookSize / 2.0);
	        double platformCenterY = y + (hookSize / 2.0);
	        
	        double distanceX = playerX - platformCenterX;
	        double distanceY = playerY - platformCenterY;
	        
	        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
	        return distance;
		
	}
	public void draw(Graphics2D g2) {
		g2.setColor(Color.green);
		g2.fillRect(x, y, hookSize, hookSize);
		
	}
	
}