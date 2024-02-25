package com.agentdid127.resourcepack.library.utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class ImageConverter {
    // Instance Variables
    protected int imageWidth = 0;
    protected int imageHeight = 0;
    protected int defaultW = 1;
    protected int defaultH = 1;
    protected BufferedImage image;
    protected BufferedImage newImage;
    protected Path location;
    protected Graphics2D g2d;

    // Default Constructor
    public ImageConverter(int defaultWIn, int defaultHIn, Path locationIn) throws IOException {
        image = ImageIO.read(locationIn.toFile());
        location = locationIn;
        defaultW = defaultWIn;
        defaultH = defaultHIn;
        if (imageIsPowerOfTwo()) {
            newImage = image;
            location = locationIn;
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
        } else {
            Logger.log("Image (" + image.getWidth() + "x" + image.getHeight() + ") '" + locationIn.getFileName() + "' resolution size is not a power of 2. Converting to be so.");
            newImage = new BufferedImage((int) Math.ceil(Math.log(image.getWidth()) / Math.log(2)), (int) Math.ceil(Math.log(image.getHeight()) / Math.log(2)), image.getType());
            imageWidth = (int) Math.ceil(Math.log(image.getWidth()) / Math.log(2));
            imageHeight = (int) Math.ceil(Math.log(image.getHeight()) / Math.log(2));
            Graphics2D g = newImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(image, 0, 0, imageWidth, imageHeight, 0, 0, image.getWidth(), image.getHeight(), null);
            g.dispose();
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
        }
    }

    public void slice_and_save(int sx, int sy, int width, int height, Path path) throws IOException {
        BufferedImage original = this.newImage;
        Graphics2D graphics = this.g2d;

        newImage(width, height);
        subImage(sx, sy, sx + width, sy + height);
        store(path);

        this.newImage = original;
        this.g2d = graphics;
    }

    public void setImage(int defaultWIn, int defaultHIn) throws IOException {
        if (!imageIsPowerOfTwo(newImage)) {
            Logger.log("Image '" + location.getFileName() + "' is not a power of 2");
            return;
        }
        image = newImage;
        defaultW = defaultWIn;
        defaultH = defaultHIn;
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
    }

    // Creates a new Image to store
    public void newImage(int newWidth, int newHeight) {
        newImage = new BufferedImage(newWidth * getWidthMultiplier(), newHeight * getHeightMultiplier(), BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D) newImage.getGraphics();
    }

    public void addImage(Path imagePath, int x, int y) throws IOException {
        if (!imagePath.toFile().exists())
            return;
        BufferedImage image = ImageIO.read(imagePath.toFile());
        g2d.drawImage(image, x * getWidthMultiplier(), y * getHeightMultiplier(), null);
    }

    // Takes part of an image and stores it in the new image
    public void subImage(int x, int y, int x2, int y2, int storex, int storey) {
        int wMultiplier = getWidthMultiplier();
        int hMultiplier = getHeightMultiplier();

        int width2 = x2 * wMultiplier - x * wMultiplier;
        int height2 = y2 * hMultiplier - y * hMultiplier;
        int x3 = x == 0 ? 0 : x * wMultiplier;
        int y3 = y == 0 ? 0 : y * hMultiplier;

        BufferedImage part = getSubImage(x3, y3, width2, height2);
        g2d.drawImage(part, storex * wMultiplier, storey * hMultiplier, null);
    }

    public void subImage(int x, int y, int x2, int y2) {
        subImage(x, y, x2, y2, 0, 0);
    }

    // Takes a part of an image and flips it either horizontally or vertically
    public void subImage(int x, int y, int x2, int y2, int storex, int storey, boolean flip) {
        int wMultiplier = getWidthMultiplier();
        int hMultiplier = getHeightMultiplier();
        
        int width2 = x2 * wMultiplier - x * wMultiplier;
        int height2 = y2 * hMultiplier - y * hMultiplier;
        int x3 = x == 0 ? 0 : x * wMultiplier;
        int y3 = y == 0 ? 0 : y * hMultiplier;

        BufferedImage part = getSubImage(x3, y3, width2, height2);
        g2d.drawImage(createFlipped(part, flip), storex * wMultiplier, storey * hMultiplier, null);
    }

    // Only allows for the number 1 and flips it both horizontally and vertically
    public void subImage(int x, int y, int x2, int y2, int storex, int storey, int flip) {
        subImage(x, y, x2, y2, storex, storey, flip == 0 ? false : true);
    }

    public void colorize(Color rgb) {
        g2d.setPaint(rgb);
        g2d.drawImage(image, 0, 0, null);
        g2d.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
    }

    public void grayscale() {
        BufferedImage gray = new BufferedImage(newImage.getWidth(), newImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        gray.createGraphics().drawImage(image, 0, 0, null);
        newImage = gray;
    }
    
    // Allows for the flip to happen
    private static BufferedImage createFlipped(BufferedImage image2, int flip) {
        AffineTransform at = new AffineTransform();
        if (flip != 1)
            return image2;
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image2.getHeight()));
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image2.getWidth(), 0));
        return createTransformed(image2, at);
    }

    // Does the flip for the image (boolean version)
    private static BufferedImage createFlipped(BufferedImage image2, boolean flip) {
        AffineTransform at = new AffineTransform();
        if (flip) {
            at.concatenate(AffineTransform.getScaleInstance(1, -1));
            at.concatenate(AffineTransform.getTranslateInstance(0, -image2.getHeight()));
        } else {
            at.concatenate(AffineTransform.getScaleInstance(-1, 1));
            at.concatenate(AffineTransform.getTranslateInstance(-image2.getWidth(), 0));
        }
        return createTransformed(image2, at);
    }

    // Transforms the BufferedImage
    private static BufferedImage createTransformed(BufferedImage image, AffineTransform at) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    // Stores the image
    public boolean store(Path locationIn) throws IOException {
        ImageIO.write(newImage, "png", locationIn.toFile());
        return true;
    }

    public boolean store() throws IOException {
        return store(location);
    }

    // Gets a sub image
    private BufferedImage getSubImage(int x, int y, int width, int height) {
        return image.getSubimage(x, y, width, height);
    }

    // Returns the Width and Height variables
    public int getWidth() {
        return imageWidth;
    }

    public int getHeight() {
        return imageHeight;
    }

    // Returns the Width Multiplier and Height Multiplier variables
    public int getWidthMultiplier() { 
        int wMultiplier = imageWidth / defaultW;
        // Make sure to not have 0 multiplier or cause issues!
        wMultiplier = wMultiplier == 0 ? 1 : wMultiplier;
        return wMultiplier;
    }

    public int getHeightMultiplier() {
        int hMultiplier = imageHeight / defaultH;
        // Make sure to not have 0 multiplier or cause issues!
        hMultiplier = hMultiplier == 0 ? 1 : hMultiplier;
        return hMultiplier;
    }

    public boolean imageIsPowerOfTwo() {
        return (isPowerOfTwo(image.getWidth()) && isPowerOfTwo(image.getHeight()));
    }

    public boolean imageIsPowerOfTwo(BufferedImage image) {
        return (isPowerOfTwo(image.getWidth()) && isPowerOfTwo(image.getHeight()));
    }

    // Detects if file is a power of two.
    private boolean isPowerOfTwo(int n) {
        return n > 0 && n == Math.pow(2, Math.round(Math.log(n) / Math.log(2)));
    }

    // Detects if file is a square
    public boolean isSquare() {
        return imageWidth == imageHeight;
    }
}
