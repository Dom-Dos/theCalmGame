package rage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ResourceLoader {

    public static BufferedImage loadImage(String path) {
        try (InputStream is = ResourceLoader.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Bild konnte nicht gefunden werden: " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}