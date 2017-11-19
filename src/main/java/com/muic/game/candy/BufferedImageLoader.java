package com.muic.game.candy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

// Used for load image for the specific path

public class BufferedImageLoader {
    private BufferedImage image;

    public BufferedImage loadImage(String path) throws IOException {
        image = ImageIO.read(getClass().getResource(path));
        return image;
    }
}
