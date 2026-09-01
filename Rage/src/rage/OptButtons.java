package rage;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class OptButtons {
    private GamePanel gp;
    private KeyHandler keyH;
    private BufferedImage titlePicture;
    private Font fontSub;
    private Font fontTitle;
    
    private KeyBinding[] bindings;
    public KeyBinding bindingToRebind = null;
    

    public OptButtons(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        fontTitle = new Font("Arial", Font.BOLD, 36);
        fontSub = new Font("Arial", Font.PLAIN, 20);
        titlePicture = ResourceLoader.loadImage("/BGP1.jpg");


        bindings = new KeyBinding[] {
            new KeyBinding("Right", keyH.codeRight),
            new KeyBinding("Left", keyH.codeLeft),
            new KeyBinding("Jump", keyH.codeUp),
            new KeyBinding("Sword", keyH.codeSword),
            new KeyBinding("Fireball", keyH.codeShot),
            new KeyBinding("Dash", keyH.codeDash),
            new KeyBinding("Interact", keyH.codeInteract)
        };
    }
    
    

    public void rebindKey(int highlightIndex) {
        int index = highlightIndex - 1;
        if (index >= 0 && index < bindings.length) {
            bindingToRebind = bindings[index];
            keyH.startRebinding(this, keyH.codeInteract, highlightIndex);
        }
    }
    
    public void finishRebind(int newKeyCode) {
        if (bindingToRebind != null) {
            bindingToRebind.setKeyCode(newKeyCode);
            bindingToRebind = null;
        }
    }


    public void optionScreen(Graphics2D g2, int highlightOption) {
        drawTitlePicture(g2);
        
        Center(g2, "Input options", gp.screenHeight / 2 - 80, fontTitle, Color.CYAN);
        Center(g2, "The Calm game", gp.screenHeight / 2 - 40, fontTitle, Color.WHITE);
        Center(g2, "--------------", gp.screenHeight / 2, fontTitle, Color.WHITE);
        Center(g2, "Controls", gp.screenHeight / 2 + 60, fontTitle, Color.CYAN);

        int startY = gp.screenHeight / 2 + 90;
        int lineSpacing = 30;

        for (int i = 0; i < bindings.length; i++) {
            boolean isHighlighted = (highlightOption == i + 1);

            Font font = isHighlighted ? fontTitle : fontSub;
            Color color = isHighlighted ? Color.YELLOW : Color.RED;

            Center(g2, bindings[i].getDisplayText(), startY + (i * lineSpacing), font, color);
        }
    }

    public void Center(Graphics2D g2, String text, int y, Font font, Color color) {
        g2.setFont(font);
        FontMetrics metrics = g2.getFontMetrics(font);
        int x = (gp.screenWidth - metrics.stringWidth(text)) / 2;
        g2.setColor(color);
        g2.drawString(text, x, y);
    }

    public void drawTitlePicture(Graphics2D g2) {
        if (titlePicture != null) {
            g2.drawImage(titlePicture, 0, 0, gp.screenWidth * 2, gp.screenHeight, null);
        } else {
            g2.setColor(Color.BLUE);
            g2.fillOval(0, 0, gp.screenWidth, gp.screenHeight);
        }
    }
}