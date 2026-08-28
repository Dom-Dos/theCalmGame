package rage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities; // Wichtig!

public class Main {
    public static void main(String[] args) {

        // Spiele-GUIs immer auf dem Event Dispatch Thread (EDT) starten!
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("The Calm-game");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);

            GamePanel gamePanel = new GamePanel();
            window.add(gamePanel);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            gamePanel.startGameThread();
        });
    }
}
