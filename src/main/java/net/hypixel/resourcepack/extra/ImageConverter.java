package net.hypixel.resourcepack.extra;

import net.hypixel.resourcepack.pack.Pack;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ImageConverter {

    //Instance Variables
    private int width = 0;
    private int height = 0;
    private int defaultW = 1;
    private int defaultH = 1;
    private BufferedImage image;
    private BufferedImage newImage;
    private Path location;
    private Graphics2D g2d;

    //Default Constructor
    public ImageConverter(int defaultWIn, int defaultHIn, Path locationIn) throws IOException {
        image = ImageIO.read(locationIn.toFile());
        if (isPowerOfTwo(image.getWidth()) && isPowerOfTwo(image.getHeight())){
               defaultW = defaultWIn;
               width = image.getWidth();
               location = locationIn;
               defaultH = defaultHIn;
               height = image.getHeight();

           }
           else System.out.println("File is not a power of 2");
    }

    //Creates a new Image to store
    public void newImage(int newWidth, int newHeight)
    {
        int wMultiplier = width / defaultW;
        int hMultiplier = height / defaultH;
        newImage = new BufferedImage(newWidth*wMultiplier, newHeight*hMultiplier, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D) newImage.getGraphics();
    }

    public void subImage (int x, int y, int x2, int y2, int storex, int storey)
    {
        int wMultiplier = width / defaultW;
        int hMultiplier = height / defaultH;
        int width2 = x2*wMultiplier - x*wMultiplier;
        int height2 = y2*hMultiplier - y*hMultiplier;
        if (x == 0) x = 0; else x = x*wMultiplier;
        if (y == 0) y = 0; else y = y*hMultiplier;
        BufferedImage part = subImage2(x, y, width2, height2);
        g2d.drawImage( part, storex, storey, null);
    }

    public void store() throws IOException {
        ImageIO.write(newImage, "png", location.toFile());
    }
    private BufferedImage subImage2(int x, int y, int width, int height)
    {
        return image.getSubimage(x, y, width, height);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    //Detects if file is a power of two.
    private boolean isPowerOfTwo(int n) {
        return n>0 && n==Math.pow(2, Math.round(Math.log(n)/Math.log(2)));
    }

    //Detects if file is a square
    public boolean isSquare()
    {
        if (width == height) return true;
        else return false;
    }
}
