package rage;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean menuUp, menuDown, menuBack, menuContinue;
    public boolean upPressed, downPressed, leftPressed, rightPressed,
                    dashPressed, shotPressed, swordPressed, interactPressed,startMenuPressed;
    public boolean restartPressed = false;

    public int codeUp = KeyEvent.VK_W;
    public int codeDown = KeyEvent.VK_S;
    public int codeLeft = KeyEvent.VK_A;
    public int codeRight = KeyEvent.VK_D;
    public int codeDash = KeyEvent.VK_SPACE;
    public int codeShot = KeyEvent.VK_K;
    public int codeSword = KeyEvent.VK_J;
    public int codeInteract = KeyEvent.VK_I;

    private OptButtons optButtons;
    private boolean isRebinding = false;
    private int rebindTriggerCode = -1;
    private int rebindingActionIndex = -1;

    public void startRebinding(OptButtons optButtons, int triggerCode ,int highlightIndex) {
        this.optButtons = optButtons;
        this.isRebinding = true;
        this.rebindTriggerCode = triggerCode;
        this.rebindingActionIndex = highlightIndex;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (isRebinding) {
            if (code == rebindTriggerCode) return; // Auto-Repeat der Interact-Taste ignorieren

            applyRebind(rebindingActionIndex, code); // echte Umbelegung erst JETZT
            optButtons.finishRebind(code);           // Anzeige im Menü aktualisieren
            isRebinding = false;
            rebindTriggerCode = -1;
            rebindingActionIndex = -1;
            return;
        }

        setActionState(code, true);
    }
    private void applyRebind(int highlightIndex, int newCode) {
        switch (highlightIndex) {
            case 1: codeRight    = newCode; break;
            case 2: codeLeft     = newCode; break;
            case 3: codeUp       = newCode; break;
            case 4: codeSword    = newCode; break;
            case 5: codeShot     = newCode; break;
            case 6: codeDash     = newCode; break;
            case 7: codeInteract = newCode; break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        setActionState(e.getKeyCode(), false);
    }

    private void setActionState(int code, boolean pressed) {
        if (code == KeyEvent.VK_UP) menuUp = pressed;
        if (code == KeyEvent.VK_DOWN) menuDown = pressed;
        if (code == KeyEvent.VK_BACK_SPACE) menuBack = pressed;
        if (code == KeyEvent.VK_ENTER) menuContinue = pressed;
        if (code == KeyEvent.VK_ESCAPE) startMenuPressed = pressed;

        if (code == codeUp) upPressed = pressed;
        if (code == codeDown) downPressed = pressed;
        if (code == codeLeft) leftPressed = pressed;
        if (code == codeRight) rightPressed = pressed;
        if (code == codeDash) dashPressed = pressed;
        if (code == codeShot) shotPressed = pressed;
        if (code == codeSword) swordPressed = pressed;
        if (code == codeInteract) interactPressed = pressed;
        if (code == KeyEvent.VK_R) restartPressed = pressed;
    }
}