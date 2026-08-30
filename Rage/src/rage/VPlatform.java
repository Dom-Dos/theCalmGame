package rage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class VPlatform extends Platform{
	public  int  timeToDespawn;
	public boolean  isPlayerStandingOn = false;


	public VPlatform(int x, int y,int width,int height, int timedToDespawn, int speed) {
		super(x, y, width, height,speed);
		this.x = x;
		this.y=y;
		this.width = width;
		this.speed = speed;
		timeToDespawn = timedToDespawn;
	}
	public Rectangle getBounds() {
		return new Rectangle(x,y,width,height);
	}
	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.RED);
		g2.fillRect(x,y,width,height);
	}
	public boolean update() {
		if(isPlayerStandingOn) {
		timeToDespawn --;
		}
		if (timeToDespawn <= 0) {
			return true;
		}else {return false;}
		
		
	}
		
}
