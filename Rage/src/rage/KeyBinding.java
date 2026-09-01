package rage;

import java.awt.event.KeyEvent;

public class KeyBinding {
    private String actionName;
    private int keyCode;
    private String keyName;

    public KeyBinding(String actionName, int keyCode) {
        this.actionName = actionName;
        setKeyCode(keyCode);
    }

    public String getActionName() {
    	return actionName; 
    	}
    public int getKeyCode() {
    	return keyCode; 
    	}
    public String getKeyName() {
    	return keyName; 
    	}

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
        this.keyName = KeyEvent.getKeyText(keyCode); 
    }

    public String getDisplayText() {
        return actionName + " = " + keyName;
    }
}