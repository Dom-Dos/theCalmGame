package rage;

import java.awt.Color;
import java.awt.Graphics2D;

public class Explosion {
	int startX;
	int startY;
	int size;
	int x;
	int y;
	int timer;
	int damage;
	
	public Explosion(int startX, int startY, int sizeX, int sizeY, int timer, int damage) {
		this.startX = startX;
		this.startY = startY;
		this.size = sizeX;
		this.x = startX - sizeX/2;
		this.y = startY - sizeY/2;
		this.timer = timer;
		this.damage = damage;
	}
	public void update() {
		timer--;
		if (timer % 10 == 0) {
			size *= 1.1;
			x = startX - size/2;
		}
		if (timer <= 0) {
			GamePanel.explosions.remove(this);
		}
	}
	public void draw(Graphics2D g2) {
		if (timer % 4 ==  0) {
			g2.setColor(Color.ORANGE);
		}else if (timer % 5 == 0) {
			g2.setColor(Color.RED);
		}else if (timer % 8 == 0) {
			g2.setColor(Color.YELLOW);
		}else if (timer % 15 == 0) {
				g2.setColor(Color.WHITE);
			}else {
				g2.setColor(Color.ORANGE);
			}
		g2.fillOval(x, y, size, size);
	}
	
	
}
