package rage;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    public int gameState;
    public final int playState = 1;
    public final int gameOverState = 2;
    public final int startState = 3;
    public final int keyBindState = 4;

    public int maxHealth = 20;
    public int currentHealth = maxHealth;

    private long playTimeMs = 0;
    private long lastTimeMs = System.currentTimeMillis();
    
    private int currentLevel = 3;
   
    public int getCurrentLevel() { 
    	return currentLevel; }
    public long getPlayTimeMs() {
    	return lastTimeMs; }
    
 // Beim Klicken auf "Speichern"
    public void saveProgress() {
        JsonSave.save(this);
    }

    // Beim Klicken auf "Laden"
    public void applySaveData() {
        SaveData infos = JsonSave.load();
        if (infos != null) {
            this.currentLevel = infos.currentLevel;
            this.lastTimeMs = infos.playTimeMs;
        }
    }
    private Image cutsceneGif;
    private BufferedImage playerImg;
    private BufferedImage playerImgRight;
    private BufferedImage playerImgLeft;
    private BufferedImage platformImg;
    private BufferedImage backgroundImg;
    private BufferedImage directionImg;
    private BufferedImage playerDashImg;

    boolean rotate= false;
    boolean fearGif = false;

    public static final int originalTileSize = 16;
    public static final int scale = 3;
    public static final int tileSize = originalTileSize * scale;

    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    private int FPS = 60;
    private Thread gameThread;
    
    Sound music = new Sound();
    Sound hit = new Sound();

    KeyHandler keyH = new KeyHandler();
    OptButtons oB = new OptButtons(this, keyH);
    public int highlightIndex = 1;
    public int delayMenuButtons = 0;
    public int highlightMenu = 1;
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
    boolean jumpScare = false;
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
    final int SHOOT_DELAY = 30;

    int fallen_multiplier = 1;
   
    ArrayList<Hook> grabHook = new ArrayList<>();
    public boolean hookActive;
    private Hook currentHook = null;
    private boolean isHooked = false;

    public static ArrayList<Platform> platforms = new ArrayList<>();

    ArrayList<Ball> bullets = new ArrayList<>();
    
    ArrayList<SpikeTraps> spikes = new ArrayList <>();
    
    ArrayList<AgileEnemy> aEnemys = new ArrayList <>();
    public static ArrayList<ProjectileAE> aep = new ArrayList <>();
    
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);
        
        this.gameState = startState;
        
        
        
        playerImg = ResourceLoader.loadImage("/south.png");
        playerImgRight = ResourceLoader.loadImage("/east.png");
        playerImgLeft  = ResourceLoader.loadImage("/west.png");
        platformImg = ResourceLoader.loadImage("/PF_Texture.png");
        backgroundImg = ResourceLoader.loadImage("/BGP1.jpg");
        playerDashImg = ResourceLoader.loadImage("/dash.png");
        cutsceneGif = new javax.swing.ImageIcon(getClass().getResource("/fearGif.gif")).getImage();
        loadLevel(currentLevel);
        playMusic("/BG_Music.wav");
    }
    public void loadLevel(int level) {
        platforms.clear();
        gEnemy.clear();
        spikes.clear();
        grabHook.clear();
        bullets.clear();
        playerBullets.clear();
        playerSword.clear();

        playerX = 100;
        playerY = 100;
        velocityY = 0;

    
        if (level == 1) {

            platforms.add(new Platform(0, 500, 600, 30, 0));
            platforms.add(new Platform(700, 420, 200, 20, 0));
            platforms.add(new Platform(1000, 350, 150, 20, 0));
            platforms.add(new MovingPlatform(1250, 350, 120, 20, 150, 2));
            platforms.add(new Platform(1500, 480, 600, 30, 0));

            gEnemy.add(new GroundEnemy(300, 440, 10));
            gEnemy.add(new GroundEnemy(1600, 420, 12));

            spikes.add(new SpikeTraps(1050, 350, 50, 1));
            grabHook.add(new Hook(850, 200));
            bullets.add(new Ball(1300, -200, 16, 2));
        } 
        else if (level == 2) {
        	// ==================== ZONE 1: START & BASICS (0 - 1500) ====================
        	platforms.add(new Platform(0, 500, 600, 30, 180));
        	gEnemy.add(new GroundEnemy(0, 280, 5)); // Verlangsamt
        	platforms.add(new Platform(200, 380, 160, 20, 0)); // Etwas breiter
        	platforms.add(new Platform(400, 280, 160, 20, 0));
        	spikes.add(new SpikeTraps(400, 280, 80, 1)); // Weniger Spikes

        	platforms.add(new Platform(700, 480, 400, 30, 0));

        	// HOOK 1: Hilft über den ersten größeren Abgrund
        	grabHook.add(new Hook(1000, 200)); 

        	platforms.add(new BlockingWall(1150, 350, 30, 180, 3, true));
        	platforms.add(new AvoidingPlatform(1250, 420, 120, 20, 3)); // Radius auf 3 gesenkt
        	platforms.add(new AvoidingPlatform(1450, 320, 120, 20, 3));       

        	// ==================== ZONE 2: DIE PARKOURLANDSCHAFT (1600 - 3500) ====================
        	platforms.add(new BlockingWall(1650, 200, 30, 200, 3, false));
        	platforms.add(new Platform(1750, 220, 180, 20, 3));
        	gEnemy.add(new GroundEnemy(1750, 280, 6));

        	// Bogen-Ball im Sprungbereich
        	bullets.add(new Ball(1900, -100, 16, 2));

        	platforms.add(new MovingPlatform(1850, 150, 160, 25, 300, 2));
        	platforms.add(new AvoidingPlatform(2000, 400, 130, 20, 3));
        	platforms.add(new Platform(2200, 500, 350, 30, 0));

        	// Entschärfte Stampfer-Falle (langsamer)
        	platforms.add(new SmashingP(2300, 50, 100, 250, 3, 350, 20));
        	platforms.add(new SmashingP(2420, 50, 100, 250, 3, 350, 40));

        	platforms.add(new BlockingWall(2550, 380, 30, 160, 3, true));
        	platforms.add(new BlockingWall(2650, 180, 30, 160, 3, false));
        	platforms.add(new Platform(2750, 350, 140, 20, 0));

        	// HOOK 2: Überbrückt die Lücke im mittleren Teil
        	grabHook.add(new Hook(3050, 150));

        	platforms.add(new AvoidingPlatform(2950, 270, 110, 20, 3));
        	platforms.add(new AvoidingPlatform(3150, 200, 110, 20, 2));
        	platforms.add(new BlockingWall(3300, 320, 30, 180, 3, true));
        	platforms.add(new Platform(3400, 480, 400, 30, 3));
        	spikes.add(new SpikeTraps(3500, 480, 80, 1));

        	// ==================== ZONE 3: DIE ZWISCHENZONE (3600 - 5200) ====================
        	platforms.add(new AvoidingPlatform(3900, 400, 130, 20, 3));
        	bullets.add(new Ball(4000, -150, 16, 2)); // Ball-Hindernis von oben

        	platforms.add(new AvoidingPlatform(4100, 300, 130, 20, 3));
        	platforms.add(new BlockingWall(4300, 150, 30, 250, 3, false));
        	platforms.add(new AvoidingPlatform(4400, 220, 120, 20, 3));
        	platforms.add(new MovingPlatform(4550, 350, 160, 25, 250, 2));

        	platforms.add(new SmashingP(4600, 0, 120, 180, 4, 300, 15/4));
        	platforms.add(new BlockingWall(4750, 350, 30, 180, 3, true));
        	platforms.add(new Platform(4850, 450, 350, 30, 0));
        	gEnemy.add(new GroundEnemy(4900, 400, 6));

        	// ==================== ZONE 4: HOCHGESCHWINDIGKEITS-PASSAGE (5300 - 7600) ====================
        	platforms.add(new AvoidingPlatform(5300, 380, 120, 20, 3));
        	platforms.add(new MovingPlatform(5500, 250, 140, 20, 400, 2));

        	bullets.add(new Ball(5650, -200, 18, 3)); // Ball von oben

        	platforms.add(new BlockingWall(5850, 120, 30, 280, 3, false));
        	platforms.add(new AvoidingPlatform(6000, 420, 110, 20, 3));
        	platforms.add(new MovingPlatform(6200, 300, 130, 20, 300, 2));
        	platforms.add(new Platform(6450, 480, 380, 30, 0));

        	platforms.add(new Platform(6600, 20, 400, 220, 3));

        	platforms.add(new BlockingWall(6900, 300, 30, 200, 4, true));
        	platforms.add(new AvoidingPlatform(7050, 220, 110, 20, 3));
        	platforms.add(new AvoidingPlatform(7250, 180, 110, 20, 3));
        	platforms.add(new MovingPlatform(7400, 320, 180, 25, 200, 3));
        	platforms.add(new Platform(7700, 450, 500, 40, 0));

        	// ==================== ZONE 5: DAS FINALE (7800 - 11000) ====================
        	platforms.add(new AvoidingPlatform(8300, 380, 110, 20, 3));
        	platforms.add(new SmashingP(8500, 0, 150, 200, 4, 400, 10/4));
        	platforms.add(new MovingPlatform(8700, 280, 150, 20, 350, 3));

        	// HOOK 3: Retter-Hook für das Ende über der verschwindenden Plattform
        	grabHook.add(new Hook(9150, 200));

        	platforms.add(new BlockingWall(9000, 200, 30, 250, 4, true));
        	platforms.add(new VPlatform(9100, 450, 400, 30, 150, 0));
        	spikes.add(new SpikeTraps(9200, 450, 80, 1));

        	bullets.add(new Ball(9400, -100, 16, 2)); // Finaler Ball

        	platforms.add(new AvoidingPlatform(9600, 320, 120, 20, 3));
        	platforms.add(new SmashingP(9800, 50, 120, 220, 4, 380, 15/4));
        	platforms.add(new Platform(10000, 420, 250, 30, 0));
        	gEnemy.add(new GroundEnemy(10050, 360, 7));

        	// Finale Plattform
        	platforms.add(new Platform(10400, 480, 800, 50, 0));
        	//Teststage für unseren AE
        }else if (currentLevel == 3) {
        	
        	// ==================== ARENA-BEGRENZUNG ====================
            // 1. Boden (Breite: 1200)
            platforms.add(new Platform(0, 500, 1200, 50, 0));
            
            // 2. Linke Wand (Verhindert Flucht nach links)
            platforms.add(new Platform(0, 100, 40, 400, 0));
            
            // 3. Rechte Wand (Verhindert Flucht nach rechts)
            platforms.add(new Platform(1160, 100, 40, 400, 0));
            
            // 4. Decke (Verhindert Drüberspringen/Dash nach oben)
            platforms.add(new Platform(0, 0, 1200, 40, 0));

            // ==================== PLATTMASCHINEN / PLATTFORMEN IN DER ARENA ====================
            // Kleine Plattformen in der Mitte für vertikales Ausweichen
            platforms.add(new Platform(300, 380, 150, 20, 0));
            platforms.add(new Platform(750, 380, 150, 20, 0));

            // ==================== GEGNER-SPAWNS (3x AgileEnemy) ====================
            // Gegner 1 (Links)
            aEnemys.add(new AgileEnemy(200, 300, 30, 60));
            
            // Gegner 2 (Mitte)
            aEnemys.add(new AgileEnemy(580, 300, 30, 60));
            
            // Gegner 3 (Rechts)
            aEnemys.add(new AgileEnemy(950, 300, 30, 60)); 
            
            }
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

        final int TOLERANCE = 2;
        return overlapY > TOLERANCE;
    }



    public boolean playerTookDamage() {
        if (playerDmgTimer > 0) {
            return false;
        }
        currentHealth--;
        playHit("/scream2.wav");
        playerDmgTimer = 60; 
        delayGame = 30;
        return true;
    }
    public int delayGame = 60;
    @Override
    public void run() {
    	
        
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
        	double drawInterval = 1000000000 / (double) FPS;
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                delayGame --;
           	 if (delayGame <= 0) {
                	FPS = 60;
                }else if (delayGame > 0) {
                	
                	FPS = 30;
                	//System.out.println("Delay");
                }
            }
        }
    }

    public static int getDir() {
    	return playerFacingDirection;
    }

    public void update() {
    	//System.out.println(playerX);
    	if (aEnemys.size() == 0) {
    		jumpScare =  true;
    		System.out.println("Warum nur?");
    	}
    	System.out.println(aEnemys.size());
    	if (fearGif) {
    		return;
    	}
    	
    	if (playerX >= 2000 && currentLevel ==1) {
    		currentLevel = 2;
    		saveProgress();
    		loadLevel(currentLevel);
    		msg.showFloatingMessage("Stage 2");
    		
    		return;
    	}else if (playerX >= 5000 && currentLevel ==2) {
    		currentLevel = 3;
    		saveProgress();
    		loadLevel(currentLevel);
    		fearGif = true;
    		stopMusic();
    		playHit("/jumpScare.wav");
    		
    		return;
    	}
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
        	/*
            if (keyH.menuContinue) {
            	playHit("/restart.wav");
                resetGame();
            }
            if(keyH.interactPressed) {
            	gameState = keyBindState;
            }
            */
        	delayMenuButtons --;
        	if (keyH.menuUp && 2 <= highlightMenu && delayMenuButtons <=0) {
        		highlightMenu -=1;
        		playHit("/up_down.wav");
        		//System.out.println(highlightMenu);
        		delayMenuButtons = 10;
        		
        	}
        	if (keyH.menuDown && msg.options.length > highlightMenu && delayMenuButtons <=0) {
        		highlightMenu +=1;
        		playHit("/up_down.wav");
        		//System.out.println(highlightMenu);
        		delayMenuButtons = 10;
        	}
        	if (keyH.menuContinue) {
        		switch(highlightMenu) {
        		case(1):
        			gameState = keyBindState;
        			break;
        		case(2):
        			System.out.println("Noch nicht verfügbar");
        			break;
        		case(3):
        			gameState = playState;
        		delayGame = 60;
        			break;
        		}
        	}
            return;
        }
        if(gameState == keyBindState) {
        	delayMenuButtons --;
            if(keyH.menuDown && highlightIndex < 7 && delayMenuButtons <= 0) {
                highlightIndex += 1;
                delayMenuButtons = 10;
                playHit("/up_down.wav");
            }
            if(keyH.menuUp && highlightIndex >= 2 && delayMenuButtons <= 0) {
                highlightIndex -= 1;
                delayMenuButtons = 10;
                playHit("/up_down.wav");
            }
            if(keyH.menuContinue && delayMenuButtons <= 0) {
                oB.rebindKey(highlightIndex);
                keyH.interactPressed = false; 
                delayMenuButtons = 10;
            }
            if(keyH.menuBack && delayMenuButtons <= 0) {
                gameState = startState;
  
            }
            return;
        }
        
        if(keyH.startMenuPressed) {
        	gameState = startState;
        }

        if (!isHooked) {
            for (Hook gh : grabHook) {
                if (gh.getDistance(playerX, playerY) <= 300) {
                    msg.showFloatingMessage("-I-");

                    if (keyH.interactPressed) {
                        currentHook = gh;
                        isHooked = true;
                        keyH.interactPressed = false; 
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
        
        for (int i = aEnemys.size() - 1; i >= 0; i--) {
            AgileEnemy ae = aEnemys.get(i);
            ae.update();
            if (ae.att != null && ae.att.intersects(getPlayerBounds())) {
                playerTookDamage();
            }
            if (ae.health <= 0) {
                aEnemys.remove(i);
            }
        }

        for(ProjectileAE pj : aep) {
        	pj.update();
        	if(pj.getBounds().intersects(getPlayerBounds())) {
        		playerTookDamage();
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
                    	// Hier weiter machen fehler beheben
                    	if(playerY >= p.y) {
                    		playerY = p.y - tileSize;
                    	}
                    	
                    	
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
            for(AgileEnemy ae : aEnemys) {
            	if(ae.getBounds().intersects(pb.getBounds())) {
            		ae.health --;
            		pb.active = false;
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
                	sw.active = false;
                    break;
                }
            }
            for(AgileEnemy ae : aEnemys) {
            	if(ae.getBounds().intersects(sw.getBounds())) {
            		ae.health --;
            		sw.active = false;
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
        for(AgileEnemy enemies : aEnemys) {
        	enemies.draw(g2);
        }
        for(ProjectileAE pj : aep) {
        	pj.draw(g2);
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
        	msg.startScreen(g2,highlightMenu);
        }
        if (gameState == keyBindState) {
        	oB.optionScreen(g2, highlightIndex);
        	
        }
        if (fearGif && cutsceneGif != null ) {
            g2.drawImage(cutsceneGif, 0, 0, screenWidth, screenHeight, this);
        }
        if (currentLevel == 3 && gEnemy.size()== 0 && jumpScare) {
            g2.drawImage(cutsceneGif, 0, 0, screenWidth, screenHeight, this);
        }
        g2.dispose();
    }
}