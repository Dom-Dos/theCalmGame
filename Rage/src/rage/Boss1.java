package rage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Boss1 {
	int health;
	int sizeX;
	int sizeY;
	int speed;
	boolean isAlive;
	boolean isTargetable;
	int x;
	int y;
	int resetX;
	int resetY;
	boolean dangerous;
	BossState currentState;
	int explosionTime = 120;
	boolean isExploding = false;

	int maxHealt;
	int healthBarWidth = 2;
	int healthBarHeight = 10;
	BufferedImage currentImage;
	
	
	enum BossState {
		Idle,
		Attacking,
		Zooming,
		Charging,
		Chaos,
		Exploding
	}
	
	public Boss1(int health, int sizeX, int sizeY, int speed, int x, int y) {
		this.health = health;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.speed = speed;
		this.x = x;
		this.y = y;
		this.resetX = x;
		this.resetY = y;
		this.isAlive = true;
		this.isTargetable = true;
		this.currentState = BossState.Exploding;
		this.dangerous = false;
		this.maxHealt = health;
	}
	
	public void draw(Graphics2D g2) {
			g2.setColor(Color.RED);
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
}
	public Rectangle getBounds() {
		return new Rectangle(x, y, sizeX, sizeY);
	}
	private void resetPosition() {
		this.x = resetX;
		this.y = resetY;
	}
	public void takeDamage(int damage) {
		if (isTargetable) {
			health -= damage;
			if (health <= 0) {
				isAlive = false;
				currentState = BossState.Exploding;
		}
	}
}
public void update() {
		switch (currentState) {
		case Idle:
			// Implement idle behavior
			break;
		case Attacking:
			// Implement attacking behavior
			break;
		case Zooming:
			// Implement zooming behavior
			break;
		case Charging:
			// Implement charging behavior
			break;
		case Chaos:
			// Implement chaos behavior
			break;
		case Exploding:
			explosionTime--;
			System.out.println("Explosion time: " + explosionTime);
			if (!isExploding) {
				isExploding = true;
				for(int i = 0; i < 5; i++) {
					int offsetX = (int)(Math.random() * sizeX) - sizeX / 2;
					int offsetY = (int)(Math.random() * sizeY) - sizeY / 2;
					GamePanel.explosions.add(new Explosion(x + sizeX / 2 + offsetX, y + sizeY / 2 + offsetY, sizeX, sizeY, 120, 10));
				}
			}
				if(explosionTime <= 0) {
					isAlive = false;
					GamePanel.bosses.remove(this);
				}
			
			if (explosionTime <= 0) {
				isAlive = false;
			}
			break;
		default:
			currentState = BossState.Idle;
			break;
		}
	}
	
	
}