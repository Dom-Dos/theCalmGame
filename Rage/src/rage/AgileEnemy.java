package rage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;



public class AgileEnemy {

	public int x,y,sizeX,sizeY;
	public int playerX,playerY;
	boolean createBullet = true;
	State currentState = State.Standing;
	int timer ;
	boolean isShooting = false;
	int shootDelay = 3;
	int speed = 4;
	

	public AgileEnemy(int x,int y,int sizeX,int sizeY){
		this.x= x;
		this.y= y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}
	
	public Rectangle getBounds() {
		return new  Rectangle(x,y,sizeX,sizeY);
	}
	public enum State{
		Standing,
		Shooting,
		Walking,
		Jumping,
		Attack
	}
	
	public void update() {
		switch (currentState){
		case Standing:
			timer --;
			//Vielleicht später eine Idle standanimation ein bauen;
			if(timer<= 0) {
				double random = Math.random();
				if (random < 0.4) {
			        	currentState = State.Walking;   
			    } else if (random < 0.7) {
			        	currentState = State.Shooting; 
			        	timer = 0;
			    } else {
			        	currentState = State.Standing;  
			    }
			}
			break;
		case Shooting:
			timer --;
			if(timer <= 0 && distanceToPlayer() <= 500 && !isShooting) {
				
				GamePanel.aep.add(new ProjectileAE(x+(sizeX/2),y +(sizeY/2),GamePanel.playerX,GamePanel.playerY));
				timer = 20;
				shootDelay --;
				if (shootDelay <= 0) {
					timer = 90;
					shootDelay = 3;
					currentState = State.Standing;
				}

			break;
			
			}
		case Walking:
			timer --;
			if (timer > 0) {
			if(GamePanel.playerX > x+(sizeX/2)) {
				x += speed;
			}else if(GamePanel.playerX < x+(sizeX/2)) {
				x -= speed;
			}else {
				timer = 90;
				currentState = State.Standing;
			}
	
	}
	}
	}
	public void draw(Graphics g2) {
		g2.setColor(Color.darkGray);
		g2.fillRect(x, y, sizeX, sizeY);
	}
	public double distanceToPlayer() {
		double playerCenterX = GamePanel.playerX + GamePanel.tileSize/2;
	    double playerCenterY = GamePanel.playerY + (GamePanel.tileSize/2);
	    double distanceX = x - playerCenterX;
	    double distanceY = y - playerCenterY;
	    double absolutDistance = Math.sqrt((distanceY*distanceY) + (distanceX * distanceX));
	    return absolutDistance;
	}
}
