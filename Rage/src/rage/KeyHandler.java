package rage;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed, dashPressed, shotPressed,swordPressed,gPressed;
    public boolean restartPressed = false;

    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // W / Pfeil nach oben
        if (code == KeyEvent.VK_SPACE) {
            upPressed = true;
        }
        // S / Pfeil nach unten
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        // A / Pfeil nach links
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        // D / Pfeil nach rechts
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
            
        }
        // Enter zum Dashen
        if (code == KeyEvent.VK_I) {
            dashPressed = true;
        }
        // R zum Spiel restart
        if (code == KeyEvent.VK_R) {
            restartPressed = true;
        }
        // F Drücken zum schießen
        if (code == KeyEvent.VK_K) {
           shotPressed = true;
        }
     // E Drücken zum schlagen
        if (code == KeyEvent.VK_J) {
           swordPressed = true;
        }
     // g zum interagieren
        if (code == KeyEvent.VK_L) {
           gPressed = true;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_R) {
            restartPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_I) {
            dashPressed = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
     // F Drücken zum schießen
        if (code == KeyEvent.VK_K) {
            shotPressed = false;
        }
        // E Drücken zum schlagen
        if (code == KeyEvent.VK_J) {
           swordPressed = false;
        }
     // g zum interagieren
        if (code == KeyEvent.VK_L) {
           gPressed = false;
        }
    }
}