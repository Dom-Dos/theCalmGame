package rage;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Message {

    private GamePanel gp;
    private Font fontSub;
    private Font fontTitle;
    private BufferedImage titlePicture;

    private String currentMessage = "";
    private boolean messageOn = false;
    private int messageCounter = 0;

    public Message(GamePanel gp) {
        this.gp = gp;
        fontTitle = new Font("Arial", Font.BOLD, 36);
        fontSub = new Font("Arial", Font.PLAIN, 20);
    }

  
    public void showFloatingMessage(String text) {
        this.currentMessage = text;
        this.messageOn = true;
        this.messageCounter = 0; 
    }

    
    public void drawFloating(Graphics2D g2, int playerX, int playerY, int tileSize, int multiplier) {
        if (!messageOn) return;

        g2.setFont(new Font("Arial", Font.BOLD, 14* multiplier));
        FontMetrics metrics = g2.getFontMetrics();

        int textWidth = metrics.stringWidth(currentMessage);
        int x = playerX + (tileSize / 2) - (textWidth / 2);
        int y = playerY - 15;

        g2.setColor(Color.YELLOW);
        g2.drawString(currentMessage, x, y);

        messageCounter++;
        if (messageCounter > 120) {
            messageOn = false;
            messageCounter = 0;
        }
    }
    
    String[] options =  new String[] {
    		"Controls",
    		"Sound options-Not Available",
    		"Start Game"
    };

    public void Center(Graphics2D g2, String text, int y, Font font, Color color, boolean drawBackground) {
        g2.setFont(font);
        FontMetrics metrics = g2.getFontMetrics(font);
        int x = (gp.screenWidth - metrics.stringWidth(text)) / 2;

        if (drawBackground) {
            int padding = 20;
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(x - padding, y - metrics.getAscent(), metrics.stringWidth(text) + (padding * 2), metrics.getHeight());
        }

        g2.setColor(color);
        g2.drawString(text, x, y);
    }
    // game over screen
    public void drawGameOverScreen(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        Center(g2, "GAME OVER", gp.screenHeight / 2 - 40, fontTitle, Color.RED, true);
        Center(g2, "Drücke 'R' zum Neustarten", gp.screenHeight / 2 + 30, fontSub, Color.WHITE, false);
    }
    
 // game over screen
    public void startScreen(Graphics2D g2, int highlightmenu) {
    	drawTitlePicture(g2);
    	Center(g2, "Press space to start", gp.screenHeight / 2 -80, fontTitle, Color.CYAN, false);
        Center(g2, "The Calm game", gp.screenHeight / 2 - 40, fontTitle, Color.white, false);
        Center(g2, "--------------", gp.screenHeight / 2 , fontTitle, Color.white, false);
        int startY = gp.screenHeight / 2 + 90;
        int lineSpacing = 30;

        for (int i = 0; i < options.length; i++) {
            boolean isHighlighted = (highlightmenu == i + 1);

            Font font = isHighlighted ? fontTitle : fontSub;
            Color color = isHighlighted ? Color.YELLOW : Color.RED;

            Center(g2, options[i], startY + (i * lineSpacing), font, color,false);
        }

    }
    public void drawTitlePicture(Graphics2D g2) {
    	titlePicture = ResourceLoader.loadImage("/BGP1.jpg");
    	if (titlePicture != null) {
            g2.drawImage(titlePicture, 0, 0, gp.screenWidth*2, gp.screenHeight, null);
        } else {
        	g2.setColor(Color.BLUE);
            g2.fillOval(0, 0, gp.screenWidth, gp.screenHeight);
        }
    	
    }
    
    
}