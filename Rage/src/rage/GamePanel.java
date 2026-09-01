package rage;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    public int gameState;
    public final int playState = 1;
    public final int gameOverState = 2;
    public final int startState = 3;

    public int maxHealth = 5;
    public int currentHealth = maxHealth;

    private long playTimeMs = 0;
    private long lastTimeMs = System.currentTimeMillis();
    
    private int currentDiffLevel = 0;

    private BufferedImage playerImg;
    private BufferedImage playerImgRight;
    private BufferedImage playerImgLeft;
    private BufferedImage platformImg;
    private BufferedImage backgroundImg;
    private BufferedImage directionImg;
    private BufferedImage playerDashImg;

    boolean rotate= false;

    public final int originalTileSize = 16;
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale;

    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    private final int FPS = 60;
    private Thread gameThread;
    
    Sound music = new Sound();
    Sound hit = new Sound();

    KeyHandler keyH = new KeyHandler();

    Message msg = new Message(this);

    public int maxLife = 3;
    public int currentLife = maxLife;
    int life = 5;
    int playerDmgTimer = 0;
    static int playerX = 100;
    static int playerY = 100;
    int playerSpeed = 4;
    String lastFaced = "right";

    int cameraX = 0;

    double velocityY = 0;
    double dashVelocityX = 0;
    double gravity = 0.5;
    double dashBrake = 0.5;
    double dashStrength = 15;
    double jumpStrength = -12;
    boolean isGrounded = false;
    double dashtime = dashStrength/ dashBrake;

    ArrayList<Shot> playerBullets = new ArrayList<>();
    ArrayList<Sword> playerSword = new ArrayList<>();

    ArrayList<GroundEnemy> gEnemy = new ArrayList<>();
    public static int playerFacingDirection = 1;
    int shootCooldown = 0;
    final int SHOOT_DELAY = 100;

    int fallen_multiplier = 1;
   
    ArrayList<Hook> grabHook = new ArrayList<>();
    public boolean hookActive;
    private Hook currentHook = null;
    private boolean isHooked = false;

    ArrayList<Platform> platforms = new ArrayList<>();

    ArrayList<Ball> bullets = new ArrayList<>();
    
    ArrayList<SpikeTraps> spikes = new ArrayList <>();
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);
        
        this.gameState = startState;

     // ==================== ZONE 1: TUTORIAL & ERSTE HINDERNISSE (0 - 1500) ====================
        platforms.add(new Platform(0, 500, 600, 30, 180));
        gEnemy.add(new GroundEnemy(0, 280, 10));
        platforms.add(new Platform(200, 380, 140, 20, 0));
        platforms.add(new Platform(400, 280, 140, 20, 0));
        spikes.add(new SpikeTraps(400, 280, 140, 1));
       
        platforms.add(new Platform(700, 480, 400, 30, 0));

        platforms.add(new BlockingWall(1150, 350, 30, 180, 6, true));
        platforms.add(new AvoidingPlatform(1250, 420, 100, 20, 6));
        platforms.add(new AvoidingPlatform(1450, 320, 100, 20, 6));       

        // ==================== ZONE 2: DIE PARKOURLANDSCHAFT (1600 - 3500) ====================
        platforms.add(new BlockingWall(1650, 200, 30, 200, 6, false));
        platforms.add(new Platform(1750, 220, 160, 20, 6));
        gEnemy.add(new GroundEnemy(1750, 280, 10));
        platforms.add(new MovingPlatform(1850, 150, 150, 25, 300, 2));
        platforms.add(new AvoidingPlatform(2000, 400, 120, 20, 6));
        platforms.add(new Platform(2200, 500, 300, 30, 0));

        // NEU: Doppelte Stampfer-Falle
        platforms.add(new SmashingP(2300, 50, 100, 250, 5, 350, 20));
        platforms.add(new SmashingP(2420, 50, 100, 250, 5, 350, 40));

        platforms.add(new BlockingWall(2550, 380, 30, 160, 6, true));
        platforms.add(new BlockingWall(2650, 180, 30, 160, 6, false));
        platforms.add(new Platform(2750, 350, 120, 20, 0));
        platforms.add(new AvoidingPlatform(2950, 270, 90, 20, 6));
        platforms.add(new AvoidingPlatform(3150, 200, 90, 20, 3));
        platforms.add(new BlockingWall(3300, 320, 30, 180, 5, true));
        platforms.add(new Platform(3400, 480, 400, 30, 6));
        spikes.add(new SpikeTraps(3500, 480, 120, 1));

        // ==================== ZONE 3: DIE SCHWRE ZWISCHENZONE (3600 - 5200) ====================
        platforms.add(new AvoidingPlatform(3900, 400, 110, 20, 6));
        platforms.add(new AvoidingPlatform(4100, 300, 110, 20, 6));
        platforms.add(new BlockingWall(4300, 150, 30, 250, 6, false));
        platforms.add(new AvoidingPlatform(4400, 220, 100, 20, 6));
        platforms.add(new MovingPlatform(4550, 350, 140, 25, 250, 3));

        // NEU: Vertikale Zerquetsch-Passage mit gegeneinander arbeitenden Stampfern
        platforms.add(new SmashingP(4600, 0, 120, 180, 6, 300, 15));
        platforms.add(new BlockingWall(4750, 350, 30, 180, 6, true));
        platforms.add(new Platform(4850, 450, 300, 30, 0));
        gEnemy.add(new GroundEnemy(4900, 400, 12));

        // ==================== ZONE 4: HOCHGESCHWINDIGKEITS-PASSASE (5300 - 7600) ====================
        platforms.add(new AvoidingPlatform(5300, 380, 100, 20, 4));
        platforms.add(new MovingPlatform(5500, 250, 120, 20, 400, 2));
        platforms.add(new BlockingWall(5850, 120, 30, 280, 4, false));
        platforms.add(new AvoidingPlatform(6000, 420, 90, 20, 5));
        platforms.add(new MovingPlatform(6200, 300, 110, 20, 300, 3));
        platforms.add(new Platform(6450, 480, 350, 30, 0));

        // NEU: Breiter SmashingP-Riesenstempel
        platforms.add(new SmashingP(6600, 20, 200, 220, 4, 380, 25));

        platforms.add(new BlockingWall(6900, 300, 30, 200, 7, true));
        platforms.add(new AvoidingPlatform(7050, 220, 80, 20, 6));
        platforms.add(new AvoidingPlatform(7250, 180, 80, 20, 6));
        platforms.add(new MovingPlatform(7400, 320, 160, 25, 200, 4));
        platforms.add(new Platform(7700, 450, 500, 40, 0));

        // ==================== ZONE 5 (NEU): DAS ENDSPURT-INFERNO (7800 - 11000) ====================
        // Kombinierte Abgründe mit engen Timing-Plattformen
        platforms.add(new AvoidingPlatform(8300, 380, 90, 20, 7));
        platforms.add(new SmashingP(8500, 0, 150, 200, 7, 400, 10));
        platforms.add(new MovingPlatform(8700, 280, 130, 20, 350, 4));

        platforms.add(new BlockingWall(9000, 200, 30, 250, 8, true));
        platforms.add(new VPlatform(9100, 450, 400, 30, 150, 0)); // Verschwindende Plattform über großem Abgrund
        spikes.add(new SpikeTraps(9200, 450, 100, 1));

        platforms.add(new AvoidingPlatform(9600, 320, 100, 20, 6));
        platforms.add(new SmashingP(9800, 50, 120, 220, 8, 380, 15));
        platforms.add(new Platform(10000, 420, 200, 30, 0));
        gEnemy.add(new GroundEnemy(10050, 360, 15));

        // Finale Plattform vor dem Ende
        platforms.add(new Platform(10400, 480, 800, 50, 0));


        // ==================== HOOK-PUNKTE (ERWEITERT) ====================
        grabHook.add(new Hook(600, 100));
        grabHook.add(new Hook(1350, 120));
        grabHook.add(new Hook(2100, 150));
        grabHook.add(new Hook(2360, 80));  // NEU: Über den doppelten SmashingP
        grabHook.add(new Hook(2850, 100));
        grabHook.add(new Hook(3700, 180));
        grabHook.add(new Hook(4250, 80));
        grabHook.add(new Hook(4660, 90));  // NEU: Greifhaken zur Rettung aus dem Stampfer
        grabHook.add(new Hook(5100, 140));
        grabHook.add(new Hook(5750, 90));
        grabHook.add(new Hook(6700, 120));
        grabHook.add(new Hook(7350, 80));
        grabHook.add(new Hook(8400, 100)); // NEU
        grabHook.add(new Hook(8800, 90));  // NEU
        grabHook.add(new Hook(9400, 110)); // NEU: Über die lange VPlatform-Lücke
        grabHook.add(new Hook(9900, 80));  // NEU


        // ==================== PROJECTILE BULLETS (ANPASSUNG AN DIE WEITERE WELT) ====================
        bullets.add(new Ball(4100, -300, 18, 2));
        bullets.add(new Ball(8200/2, -300, 18, 2));
        bullets.add(new Ball(8500, -300, 14, 2));
        bullets.add(new Ball(8800, -300, 24, 2));
        bullets.add(new Ball(9800/2, -300, 24, 2));
        bullets.add(new Ball(9800, -300, 16, 2));
        bullets.add(new Ball(10500, -300, 22, 2));

        bullets.add(new Ball(-400, 320, 18, 3));
        bullets.add(new Ball(-900, 200, 16, 3));
        
        
        playerImg = ResourceLoader.loadImage("/south.png");
        playerImgRight = ResourceLoader.loadImage("/east.png");
        playerImgLeft  = ResourceLoader.loadImage("/west.png");
        platformImg = ResourceLoader.loadImage("/PF_Texture.png");
        backgroundImg = ResourceLoader.loadImage("/BGP1.jpg");
        playerDashImg = ResourceLoader.loadImage("/dash.png");
    
        playMusic("/BG_Music.wav");
    }

    public static int getPlayerX() {
    	return playerX;
    }

    public static int getPlayerY() {
    	return playerY;
    }
    
    public void playMusic(String soundPath) {
        music.setFile(soundPath);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }
    
    public void playHit(String soundPath) {
        hit.setFile(soundPath);
        hit.play();
    }

    public void stopHit() {
        hit.stop();
    }

    public Rectangle getPlayerBounds() {
        return new Rectangle(playerX, playerY, tileSize, tileSize);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void moveHorizontally(int totalDeltaX) {
        int step = totalDeltaX > 0 ? 1 : -1;
        int remaining = Math.abs(totalDeltaX);

        while (remaining > 0) {
            playerX += step;
            remaining--;

            Rectangle playerBounds = getPlayerBounds();
            for (Platform p : platforms) {
                if (blocksHorizontalMovement(playerBounds, p.getBounds())) {
                    playerX -= step;
                    dashVelocityX = 0;
                    return;
                }
            }
        }
    }

    private boolean blocksHorizontalMovement(Rectangle playerBounds, Rectangle pBounds) {
        if (!playerBounds.intersects(pBounds)) return false;

        int overlapTop    = Math.max(playerBounds.y, pBounds.y);
        int overlapBottom = Math.min(playerBounds.y + playerBounds.height, pBounds.y + pBounds.height);
        int overlapY = overlapBottom - overlapTop;

        final int TOLERANCE = 4;
        return overlapY > TOLERANCE;
    }

    public void difficulty(long playTimeMs) {
        long seconds = playTimeMs / 1000;
        int targetLevel = (int) (seconds / 30); 

        if (targetLevel > currentDiffLevel) {
            currentDiffLevel = targetLevel;
            
            int ballSize = 30 + (currentDiffLevel * 20);
            bullets.add(new Ball(1000, 3000, ballSize, 2.5 / 2));
            msg.showFloatingMessage("STUFE " + currentDiffLevel + "!");
        }
    }

    public boolean playerTookDamage() {
        if (playerDmgTimer > 0) {
            return false;
        }
        currentHealth--;
        playHit("/scream2.wav");
        playerDmgTimer = 60; 
        return true;
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / (double) FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public static int getDir() {
    	return playerFacingDirection;
    }

    public void update() {
        playerDmgTimer --;
        if (shootCooldown > 0) {
            shootCooldown--;
        }

    	long currentTimeMs = System.currentTimeMillis();
    	if (gameState == playState) {
    	    playTimeMs += (currentTimeMs - lastTimeMs);
    	}
    	lastTimeMs = currentTimeMs;
    	Skills.update();
        if (gameState == gameOverState) {
            if (keyH.restartPressed) {
            	playHit("/restart.wav");
                resetGame();
            }
            return;
        }
        if (gameState == startState) {
            if (keyH.dashPressed) {
            	playHit("/restart.wav");
                resetGame();
            }
            return;
        }

        if (!isHooked) {
            for (Hook gh : grabHook) {
                if (gh.getDistance(playerX, playerY) <= 300) {
                    msg.showFloatingMessage("-I-");

                    if (keyH.gPressed) {
                        currentHook = gh;
                        isHooked = true;
                        keyH.gPressed = false; 
                        break;
                    }
                }
            }
        } 

        if (isHooked && currentHook != null) {
            velocityY = 0; 

            if (currentHook.getDistance(playerX, playerY) > 350) {
                isHooked = false;
                currentHook = null;
            } else {
                if (playerX > currentHook.x) playerX -= 5;
                else if (playerX < currentHook.x) playerX += 5;

                if (playerY > currentHook.y) playerY -= 7;
                else if (playerY < currentHook.y) playerY += 7;

                if (currentHook.getDistance(playerX, playerY) <= 25) {
                    isHooked = false;
                    currentHook = null;
                    velocityY = -10; 
                }
            }
        }

        for (Platform p : platforms) {
            if (p instanceof AvoidingPlatform) {
                ((AvoidingPlatform) p).update(playerX, playerY);
            } else if (p instanceof BlockingWall) {
                ((BlockingWall) p).update(playerX, playerY);
            }else if (p instanceof MovingPlatform) {
                ((MovingPlatform) p).update();
            }else if (p instanceof SmashingP) {
            	((SmashingP) p).update();
            	if (p instanceof SmashingP) {
            	    SmashingP sp = (SmashingP) p;
            	    sp.update();

            	    if (sp.getBounds().intersects(getPlayerBounds())) {
            	        
            	        if (sp.isSmashingDown()) {
            	            currentHealth = 0; 
            	            msg.showFloatingMessage("Du bist Matsche");
            	            playHit("/angryStone.wav");
            	            gameState = gameOverState;
            	        }
            	    }
            	}
            }
        }
        
        for (int i = platforms.size() - 1; i >= 0; i--) {
            Platform p = platforms.get(i);
            if (p instanceof VPlatform) {
                VPlatform vp = (VPlatform) p;
                if (vp.update()) {
                    platforms.remove(i);
                }
            }
        }

        if (keyH.leftPressed && playerX > 0) {
            playerFacingDirection = -1;

            if (keyH.dashPressed && Skills.performDash()) {
                dashVelocityX = dashStrength;
                msg.showFloatingMessage("DASH!");
            }

            moveHorizontally(-(playerSpeed + (int) dashVelocityX));
        } 
        else if (keyH.rightPressed) {
            playerFacingDirection = 1;

            if (keyH.dashPressed && Skills.performDash()) {
                dashVelocityX = dashStrength;
                msg.showFloatingMessage("DASH!");
            }
            moveHorizontally(playerSpeed + (int) dashVelocityX);
        }

        if (isGrounded) {
        	gravity = 0;
        } else {
            gravity = 0.5;
        }

        if (dashVelocityX > 0) {
            dashVelocityX -= dashBrake; 
            gravity = 0;
            velocityY = 0;
            if (dashVelocityX < 0) {
                dashVelocityX = 0;
            }
        }

        velocityY += gravity;
        playerY += velocityY;
        isGrounded = false; 

        if (dashVelocityX > 0) {
            directionImg = playerDashImg;
        } else if (keyH.leftPressed) {
            directionImg = playerImgLeft;
        } else if (keyH.rightPressed) {
            directionImg = playerImgRight;
        } else {
            directionImg = playerImg; 
        }
        

        	
        Rectangle playerBounds = getPlayerBounds();
        for (Platform p : platforms) {
            Rectangle pBounds = p.getBounds();
            if (playerBounds.intersects(pBounds)) {
                if (velocityY > 0 && (playerY + tileSize - velocityY) <= p.y) {
                    playerY = p.y - tileSize; 
                    isGrounded = true;
                    velocityY = 0;
                }
                if (isGrounded) {
                    if (p instanceof AvoidingPlatform) {
                        moveHorizontally(((AvoidingPlatform) p).speed);
                    } else if (p instanceof MovingPlatform ) {
                        moveHorizontally(((MovingPlatform) p).speed);
                    }else if (p instanceof VPlatform) {
                    	((VPlatform) p).isPlayerStandingOn = true;
                    } }else if (p instanceof SmashingP) {
                    	playerY = p.y - tileSize;
                    	
                }
            }
        }

        if (playerY >= screenHeight - tileSize) {
            respawnOnNextPlatformLeft();
            msg.showFloatingMessage("DU BIST KACKE");
            fallen_multiplier ++;
            playerTookDamage();
            
            if (currentHealth <= 0) {
            	playHit("/gameOver.wav");
                gameState = gameOverState;
            }
        }

        if (keyH.upPressed && isGrounded) { 
        	playHit("/jump.wav");
            velocityY = jumpStrength;
            isGrounded = false;
        }

        cameraX = playerX - (screenWidth / 2) + (tileSize / 2);
        if (cameraX < 0) {
            cameraX = 0;
        }

        double playerCenterX = playerX + (tileSize / 2.0);
        double playerCenterY = playerY + (tileSize / 2.0);

        for (Ball bullet : bullets) {
            bullet.update(playerCenterX, playerCenterY);

            if (bullet.getBounds().intersects(getPlayerBounds())) {
            	if (playerTookDamage()) {
                    //bullet.reset();
            		bullet.size = 0;
                    msg.showFloatingMessage("No Hands");
                }
                if (currentHealth <= 0) {
                    gameState = gameOverState;
                }
            }
        }

        if (keyH.shotPressed && shootCooldown == 0) {
            int bulletX = (playerFacingDirection == 1) ? playerX + tileSize : playerX - 10;
            int bulletY = playerY + (tileSize / 2) - 5;
            playerBullets.add(new Shot(bulletX, bulletY, playerFacingDirection));
            shootCooldown = SHOOT_DELAY;
            playHit("/fireBall.wav"); 
        }

        if (keyH.swordPressed && shootCooldown == 0) {
            int swordX = (playerFacingDirection == 1) ? playerX + tileSize : playerX - 16;
            int swordY = playerY;
            playerSword.add(new Sword(swordX, swordY));
            playHit("/slash-sword.wav");
            shootCooldown = SHOOT_DELAY;
        }

        for (int i = 0; i < playerBullets.size(); i++) {
            Shot pb = playerBullets.get(i);
            pb.update();

            for (Ball bullet : bullets) {
                if (bullet.getBounds().intersects(pb.getBounds())) {
                    //bullet.reset();
                	bullet.size = 0;
                    pb.active = false;
                    break;
                }
            }

            if (!pb.active) {
                playerBullets.remove(i);
                i--;
            }
        }

        for (int j = 0; j < playerSword.size(); j++) {
            Sword sw = playerSword.get(j);
            sw.update();

            for (Ball bullet : bullets) {
                if (bullet.getBounds().intersects(sw.getBounds())) {
                    //bullet.reset();
                	bullet.size = 0;
                    break;
                }
            }

            if (!sw.active) {
                playerSword.remove(j);
                j--;
            }
        }

        for (int i = gEnemy.size() - 1; i >= 0; i--) {
            GroundEnemy ge = gEnemy.get(i);
            ge.update(platforms);

            boolean enemyDestroyed = false;

            if (getPlayerBounds().intersects(ge.getBounds())) {
                playerTookDamage();
            }

            for (Sword sw : playerSword) {
                if (sw.getBounds().intersects(ge.getBounds())) {
                    enemyDestroyed = true;
                    break;
                }
            }
            if (!enemyDestroyed) {
                for (int j = playerBullets.size() - 1; j >= 0; j--) {
                    Shot pb = playerBullets.get(j);
                    if (pb.getBounds().intersects(ge.getBounds())) {
                        enemyDestroyed = true;
                        pb.active = false;
                        break;
                    }
                }
            }

            if (enemyDestroyed) {
                gEnemy.remove(i);
            }
        }
        for(SpikeTraps spike: spikes) {
        	spike.update();
        	if (getPlayerBounds().intersects(spike.getBounds())) {
                playerTookDamage();
        }
    }
        if (currentHealth <= 0) {
        	playHit("/gameOver.wav");
            gameState = gameOverState;
        }
    }
    public void respawnOnNextPlatformLeft() {
        Platform targetPlatform = null;
        int closestX = -1; 

        for (Platform p : platforms) {
            int platformRightEdge = p.x + p.width;

            if (platformRightEdge <= playerX && platformRightEdge > closestX) {
                closestX = platformRightEdge;
                targetPlatform = p;
            }
        }
        if (targetPlatform != null) {
            playerX = (targetPlatform.x + targetPlatform.width) - tileSize - 20;
            playerY = targetPlatform.y - tileSize - 10;
        } else {
            playerX = 100;
            playerY = 100;
        }

        velocityY = 0; 
    }

    public void resetGame() {
        playerX = 100;
        playerY = 100;
        velocityY = 0;
        fallen_multiplier = 1;
        currentHealth = maxHealth;
        playTimeMs = 0;
        currentDiffLevel = 0;
     
        for (Ball bullet : bullets) {
            bullet.reset();
        }
        
        gameState = playState; 
    }

    private void drawPlayTime(Graphics2D g2) {
        long totalSeconds = playTimeMs / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        String timeText = String.format("%02d:%02d", minutes, seconds);

        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 20F));
        g2.setColor(Color.WHITE);

        int textWidth = g2.getFontMetrics().stringWidth(timeText);
        int margin = 20;
        int x = screenWidth - textWidth - margin;
        int y = screenHeight - margin;

        g2.drawString(timeText, x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int bgCameraX = (int) (cameraX * 0.2);
        if (backgroundImg != null) {
            g2.translate(-bgCameraX, 0);
            
            int bgWidth = backgroundImg.getWidth();
            for (int x = -bgWidth; x < screenWidth + cameraX; x += bgWidth) {
            	g2.drawImage(backgroundImg, x, 0, 15716, 1000, null);
            }
            g2.translate(bgCameraX, 0);
        }

        g2.translate(-cameraX, 0);
        for(SpikeTraps spike: spikes) {
        	spike.draw(g2);
        }

        for (Platform p : platforms) {
            if (p instanceof AvoidingPlatform || p instanceof BlockingWall|| p instanceof MovingPlatform || p instanceof VPlatform||p instanceof SmashingP) {
                p.draw(g2);
            } else if (platformImg != null) {
                g2.drawImage(platformImg, p.x, p.y, p.width, p.height, null);
            } else {
                p.draw(g2);
            }
        }
        for (Ball bullet : bullets) {
            bullet.draw(g2);
        }

        if (directionImg != null) {
            g2.drawImage(directionImg, playerX, playerY, tileSize, tileSize, null);
        } else {
            g2.setColor(java.awt.Color.WHITE);
            g2.fillRect(playerX, playerY, tileSize, tileSize);
        }

        for(Hook gh : grabHook) {
        	gh.draw(g2);
        }

        for(GroundEnemy ge: gEnemy) {
        	ge.draw(g2);
        }

        msg.drawFloating(g2, playerX, playerY, tileSize,fallen_multiplier);
        for (Shot pb : playerBullets) {
            pb.draw1(g2);
        }
        for (Sword sw : playerSword) {
            sw.draw1(g2);
        }

        g2.translate(cameraX, 0);
        
        drawPlayTime(g2);
        
        HealthBar.display(currentHealth,g2);
        if (gameState == gameOverState) {
            msg.drawGameOverScreen(g2);
        }
        if (gameState == startState) {
        	msg.startScreen(g2);
        }
        g2.dispose();
    }
}