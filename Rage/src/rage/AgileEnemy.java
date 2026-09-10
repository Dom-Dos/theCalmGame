package rage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;


public class AgileEnemy {

	public int x,y,sizeX,sizeY;
	public int playerX,playerY;
	boolean createBullet = true;
	State currentState = State.Standing;
	int timer ;

	int shootDelay = 3;
	int speed = 4;
	double  gravity = 0.5, velocityY = 0;
	ArrayList <Platform> pf = GamePanel.platforms;
	boolean isGrounded = false;
	int facingTo; //später für die animation 
	boolean gettingReady =  false;
	Color color ; // für farbwechsel vor dem einbauen von animationen
	Rectangle att;
	int health;
	int maxHealt = 30;
	int healthBarWidth = 2;
	int healthBarHeight = 10;


	public AgileEnemy(int x,int y,int sizeX,int sizeY){
		this.x= x;
		this.y= y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.health = maxHealt;

	}
	
	public Rectangle getBounds() {
		return new  Rectangle(x,y,sizeX,sizeY);
	}
	public enum State{
		Standing,
		Shooting,
		Walking,
		Attack,
		Dash
	}
	
	public void update() {
		velocityY += gravity;
		
		for (Platform pfs : pf) {
			if(pfs.getBounds().intersects(getBounds()) && !isGrounded){
				y = pfs.y - sizeY;
				velocityY = 0;
				isGrounded = true;
			}
		}
		y += velocityY;
		switch (currentState){
		case Standing:
			timer --;
			//Vielleicht später eine Idle standanimation ein bauen;
			if(timer <= 0) {
				giveRandomAction();
				}
			
			break;
		case Shooting:
			timer --;
			if(timer >= 0 && distanceToPlayer() <= 500) {
				System.out.println("Peng Peng");
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
			if (GamePanel.playerY < y  && isGrounded) {
				velocityY -= 10;
			}
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
			else {
				giveRandomAction();
			}
			break;
	
		case Attack:
			timer --;
			if (timer > 60) {
		        gettingReady = true;
		        att = null;
		    } 
		    else if (timer > 0) {
		        if (att == null && distanceToPlayer() < 200) {
		            if (x < GamePanel.playerX) {
		                att = new Rectangle(x + sizeX, y, 100, sizeY);
		            } else {
		                att = new Rectangle(x - 100, y, 100, sizeY);
		            }
		        }
		    } 
		    else {
		        att = null;
		        gettingReady = false;
		        giveRandomAction();
		    }
		    break;
		case Dash:
			timer--;
			if(distanceToPlayer() < 100 && timer >= 0 ) {
				if(x < GamePanel.playerX) {
					x-= speed *3;
					//hier noch anmationen hinzufügen
				}else {
					x+=speed*3;
				}
			}else {
				giveRandomAction();
			}
		}
			
			
			
			
		System.out.println(currentState);
		isGrounded = false;
		y += velocityY;
	}
	
	public void draw(Graphics g2) {
		if(gettingReady) {
			color = Color.red;
		}else {
			color = Color.DARK_GRAY;
		}
		
		g2.setColor(color);
		g2.fillRect(x, y, sizeX, sizeY);
	
		int centerX = x + sizeX/2;
		int startHealthBar = centerX - ( (maxHealt /2) * healthBarWidth);
		for (int i = 0; i <= maxHealt- 1; i ++) {
			if(i <= health) {
				g2.setColor(Color.red);
				
			}else {
				g2.setColor(Color.cyan);
				
			}
			g2.fillRect(startHealthBar+(i*healthBarWidth), y -30, healthBarWidth, healthBarHeight);
		}
		
		if (att != null) {
	        g2.setColor(Color.YELLOW); 
	        g2.fillRect(att.x, att.y, att.width, att.height);
			}
	}
	public double distanceToPlayer() {
		double playerCenterX = GamePanel.playerX + GamePanel.tileSize/2;
	    double playerCenterY = GamePanel.playerY + (GamePanel.tileSize/2);
	    double distanceX = x - playerCenterX;
	    double distanceY = y - playerCenterY;
	    double absolutDistance = Math.sqrt((distanceY*distanceY) + (distanceX * distanceX));
	    return absolutDistance;
	}
	public void giveRandomAction() {

		double random = Math.random();
		System.out.println(random);
		if (random < 0.2) {
	        	currentState = State.Walking;  
	        	timer = 50;
		}else if (random < 0.4) {
				timer = 100;
				currentState = State.Attack;
	    } else if (random < 0.6) {
	        	currentState = State.Shooting; 
	        	timer = 50;
	    } else if(random < 0.8){
	    	currentState = State.Dash;
	    }else {
	        	currentState = State.Standing;
	        			timer = 40;}  
	}
}
