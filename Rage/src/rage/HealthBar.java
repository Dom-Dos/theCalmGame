package rage;

import java.awt.Color;
import java.awt.Graphics2D;

public class HealthBar {

	public static void display(int count, Graphics2D g2) {
		g2.setColor(Color.RED);

        // Zeichnet 'count' viele Herzen/Ovale nebeneinander
        for (int i = 0; i < count; i++) {
            // i = 0 -> X: 0
            // i = 1 -> X: 60
            // i = 2 -> X: 120
            g2.fillOval(i * 60, 0, 40, 40);
        }
    }
}

