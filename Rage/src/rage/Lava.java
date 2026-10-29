package rage;

import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Lava {
	int speed;
	int stage1;
	int stage2;
	int stage3;
	int y ;
	int x ;
	boolean wait = false;
	int counter = 0;
	enum LavaState {
		STAGE1, STAGE2, STAGE3
	}
	LavaState current ;
	public Lava(int speed, int stage1,int stage2,int stage3,int y,int x){
		this.speed = speed *(-1);
		this.stage1 = stage1;
		this.stage2 = stage2;
		this.stage3 = stage3;
		this.y = y;
		this.x = x;
		this.current = LavaState.STAGE1;
		
	}
	
	int sparkTimer = 0;

	public void update() {
	    int target;
	    switch (current) {
	        case STAGE1: target = stage1; break;
	        case STAGE2: target = stage2; break;
	        default:     target = stage3; break;
	    }

	    if (y <= target) {                
	        counter++;
	        if (counter >= 100) {
	            counter = 0;
	            if (current == LavaState.STAGE1)      current = LavaState.STAGE2;
	            else if (current == LavaState.STAGE2) current = LavaState.STAGE3;
	        }
	    } else {
	        y += speed;                    
	    }

	    sparkTimer++;
	    if (sparkTimer % 90 == 0) {
	        int fx = (int) (GamePanel.playerX + Math.random() * 1000);
	        GamePanel.lavaSparks.add(new LavaFireball(fx, y));
	    }
	}
	

	public Rectangle getBounds() {
	    return new Rectangle(x, y, 10000, 1000);
	}

	public void draw(java.awt.Graphics2D g2) {
	    g2.setColor(java.awt.Color.ORANGE);
	    g2.fillRect(x, y, 10000, 1000);
	}
}
