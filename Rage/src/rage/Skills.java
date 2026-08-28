package rage;

public class Skills {

    // Speichert, ob der Dash bereit ist
    private static boolean isDashOnCooldown = false;
    private static int dashCooldownTimer = 0;
    private static boolean isShotOnCooldown = false;
    private static int shotCooldownTimer = 0;

    /**
     * Führt den Dash aus, falls er nicht auf Cooldown ist.
     * @return true, wenn der Dash erfolgreich ausgeführt wurde.
     */
    public static boolean performDash() {
        if (!isDashOnCooldown) {
            isDashOnCooldown = true;
            dashCooldownTimer = 120; // 120 Frames Cooldown (ca. 2 Sekunden bei 60 FPS)
            return true; // Dash darf ausgeführt werden!
        }else {
        return false; // Dash ist noch gesperrt
        }
        }

    public static boolean performShot() {
        if (!isShotOnCooldown) {
            isShotOnCooldown = true;
            shotCooldownTimer = 90; // 90 Frames Cooldown 
            return true; // Dash darf ausgeführt werden!
        }else {
        return false; // Dash ist noch gesperrt
        }
    }
    
    public static void update() {
        if (isDashOnCooldown) {
            dashCooldownTimer--;
            if (dashCooldownTimer <= 0) {
                isDashOnCooldown = false; 
            }
        }
        if (isShotOnCooldown) {
            shotCooldownTimer--;
            if (shotCooldownTimer <= 0) {
                isShotOnCooldown = false; 
    }
 
        }
        }
 
}