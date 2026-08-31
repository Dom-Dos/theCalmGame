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
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        // S / Pfeil nach unten
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        // A / Pfeil nach links
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        // D / Pfeil nach rechts
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
            
        }
        // Enter zum Dashen
        if (code == KeyEvent.VK_SPACE) {
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
        if (code == KeyEvent.VK_I) {
           gPressed = true;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_R) {
            restartPressed = false;
        }
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S ) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A ) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            dashPressed = false;
        }
        if (code == KeyEvent.VK_D ) {
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
        if (code == KeyEvent.VK_I) {
           gPressed = false;
        }
    }
}