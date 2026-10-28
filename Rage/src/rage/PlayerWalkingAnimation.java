package rage;

import java.awt.image.BufferedImage;

public class PlayerWalkingAnimation {

    private static int tickCounter = 0;
    private static int frameIndex = 0;

    private static final BufferedImage[] WALK_EAST = loadFrames("walkEast", 6);
    private static final BufferedImage[] WALK_WEST = loadFrames("walkWest", 6);
    private static final BufferedImage[] JUMP = loadFrames("jump", 6);
    private static final BufferedImage[] CAST_FIREBALL_LEFT = loadFrames("castFL", 6);
    private static final BufferedImage[] CAST_FIREBALL_RIGHT = loadFrames("castFR", 6);
    private static final BufferedImage[] DOWNED_LEFT = loadFrames("downL", 6);
    private static final BufferedImage[] DOWNED_RIGHT = loadFrames("downR", 6);
    private static final BufferedImage[] STRIKE_LEFT = loadFrames("strikeL", 6);
    private static final BufferedImage[] STRIKE_RIGHT = loadFrames("strikeR", 6);

    private static BufferedImage[] loadFrames(String prefix, int count) {
        BufferedImage[] frames = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            frames[i] = ResourceLoader.loadImage("/" + prefix + i + ".png");
        }
        return frames;
    }


    private static int getNextFrameIndex() {
        tickCounter++;
        if (tickCounter >= 10) {
            tickCounter = 0;
            frameIndex = (frameIndex + 1) % 6; 
        }
        return frameIndex;
    }

    public static BufferedImage walkingRight() {
        return WALK_EAST[getNextFrameIndex()];
    }

    public static BufferedImage walkingLeft() {
        return WALK_WEST[getNextFrameIndex()];
    }

    public static BufferedImage jumping() {
        return JUMP[getNextFrameIndex()];
    }

    public static BufferedImage castingFireballLeft() {
        return CAST_FIREBALL_LEFT[getNextFrameIndex()];
    }

    public static BufferedImage castingFireballRight() {
        return CAST_FIREBALL_RIGHT[getNextFrameIndex()];
    }

    public static BufferedImage downedleft() {
        return DOWNED_LEFT[getNextFrameIndex()];
    }

    public static BufferedImage downedRight() {
        return DOWNED_RIGHT[getNextFrameIndex()];
    }

    public static BufferedImage strikeLeft() {
        return STRIKE_LEFT[getNextFrameIndex()];
    }

    public static BufferedImage strikeRight() {
        return STRIKE_RIGHT[getNextFrameIndex()];
    }
}