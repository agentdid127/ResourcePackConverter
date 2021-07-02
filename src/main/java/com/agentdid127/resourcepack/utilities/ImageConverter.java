package com.agentdid127.resourcepack.utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
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
    private int wMultiplier = 1;
    private int hMultiplier = 1;

    //Default Constructor
    public ImageConverter(int defaultWIn, int defaultHIn, Path locationIn) throws IOException {
        image = ImageIO.read(locationIn.toFile());
        if (isPowerOfTwo(image.getWidth()) && isPowerOfTwo(image.getHeight())){
               newImage = image;
               defaultW = defaultWIn;
               width = image.getWidth();
               location = locationIn;
               defaultH = defaultHIn;
               height = image.getHeight();
               wMultiplier = image.getWidth() / defaultW;
               hMultiplier = image.getHeight() / defaultH;
           }
           else System.out.println("File is not a power of 2");
    }

    public void setImage(int defaultWIn, int defaultHIn) throws IOException {
        image = newImage;
        if (isPowerOfTwo(image.getWidth()) && isPowerOfTwo(image.getHeight())){
            defaultW = defaultWIn;
            width = image.getWidth();
            defaultH = defaultHIn;
            height = image.getHeight();
            wMultiplier = image.getWidth() / defaultW;
            hMultiplier = image.getHeight() / defaultH;
        }
        else System.out.println("File is not a power of 2");
    }

    //Creates a new Image to store
    public void newImage(int newWidth, int newHeight)
    {
        newImage = new BufferedImage(newWidth * wMultiplier, newHeight * hMultiplier, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D) newImage.getGraphics();
    }

    //Takes part of an image and stores it in the new image
    public void subImage (int x, int y, int x2, int y2, int storex, int storey)
    {

        int x3;
        int y3;
        int width2 = x2*wMultiplier - x*wMultiplier;
        int height2 = y2*hMultiplier - y*hMultiplier;
        if (x == 0) x3 = 0; else x3 = x*wMultiplier;
        if (y == 0) y3= 0; else y3 = y*hMultiplier;
        BufferedImage part = subImage2(x3, y3, width2, height2);
        g2d.drawImage( part, storex*wMultiplier, storey*hMultiplier, null);
    }

    //Takes a part of an image and flips it either horizontally or vertically
    public void subImage (int x, int y, int x2, int y2, int storex, int storey, boolean flip)
    {
        int x3;
        int y3;
        int width2 = x2*wMultiplier - x*wMultiplier;
        int height2 = y2*hMultiplier - y*hMultiplier;
        if (x == 0) x3 = 0; else x3 = x*wMultiplier;
        if (y == 0) y3= 0; else y3 = y*hMultiplier;
        BufferedImage part = subImage2(x3, y3, width2, height2);
        g2d.drawImage( createFlipped(part, flip), storex*wMultiplier, storey*hMultiplier, null);
    }
    //Only allows for the number 1 and flips it both horizontally and vertically
    public void subImage (int x, int y, int x2, int y2, int storex, int storey, int flip)
    {
        int x3;
        int y3;
        int width2 = x2*wMultiplier - x*wMultiplier;
        int height2 = y2*hMultiplier - y*hMultiplier;
        if (x == 0) x3 = 0; else x3 = x*wMultiplier;
        if (y == 0) y3= 0; else y3 = y*hMultiplier;
        BufferedImage part = subImage2(x3, y3, width2, height2);
        g2d.drawImage( createFlipped(part, flip), storex*wMultiplier, storey*hMultiplier, null);
    }
    //Allows for the flip to happen
    private static BufferedImage createFlipped(BufferedImage image2, int flip)
    {
        AffineTransform at = new AffineTransform();
        if (flip == 1)
        {
            at.concatenate(AffineTransform.getScaleInstance(1, -1));
            at.concatenate(AffineTransform.getTranslateInstance(0, -image2.getHeight()));
            at.concatenate(AffineTransform.getScaleInstance(-1, 1));
            at.concatenate(AffineTransform.getTranslateInstance(-image2.getWidth(), 0));
            return createTransformed(image2, at);
        }
        else return image2;

    }
    //Does the flip for the image (boolean version)
    private static BufferedImage createFlipped(BufferedImage image2, boolean flip)
    {
        AffineTransform at = new AffineTransform();
        if (flip)
        {
            at.concatenate(AffineTransform.getScaleInstance(1, -1));
            at.concatenate(AffineTransform.getTranslateInstance(0, -image2.getHeight()));
        }
        if (!flip)
        {
            at.concatenate(AffineTransform.getScaleInstance(-1, 1));
            at.concatenate(AffineTransform.getTranslateInstance(-image2.getWidth(), 0));
        }
        return createTransformed(image2, at);
    }
    //Transforms the BufferedImage
    private static BufferedImage createTransformed(
            BufferedImage image, AffineTransform at)
    {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
    //Stores the image
    public boolean store() throws IOException {
        ImageIO.write(newImage, "png", location.toFile());
        return true;
    }
    public boolean store(Path locationIn) throws IOException {
        ImageIO.write(newImage, "png", locationIn.toFile());
        return true;
    }
    //Gets a sub image
    private BufferedImage subImage2(int x, int y, int width, int height)
    {
        return image.getSubimage(x, y, width, height);
    }


    //Returns the Width and Height variables
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
