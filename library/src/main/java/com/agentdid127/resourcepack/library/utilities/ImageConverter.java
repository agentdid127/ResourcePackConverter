package com.agentdid127.resourcepack.library.utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class ImageConverter {
    // Instance Variables
    protected int width = 0;
    protected int height = 0;
    protected int defaultW = 1;
    protected int defaultH = 1;
    protected BufferedImage image;
    protected BufferedImage newImage;
    protected Path location;
    protected Graphics2D g2d;
    protected int wMultiplier = 1;
    protected int hMultiplier = 1;

    // Default Constructor
    public ImageConverter(int defaultWIn, int defaultHIn, Path locationIn) throws IOException {
        image = ImageIO.read(locationIn.toFile());
        if (isPowerOfTwo(image.getWidth()) && isPowerOfTwo(image.getHeight())) {
            newImage = image;
            location = locationIn;
            defaultW = defaultWIn;
            defaultH = defaultHIn;
            width = image.getWidth();
            height = image.getHeight();
            wMultiplier = width / defaultW;
            hMultiplier = height / defaultH;
            // Make sure to not have 0 multiplier or cause issues!
            wMultiplier = wMultiplier == 0 ? 1 : wMultiplier;
            hMultiplier = hMultiplier == 0 ? 1 : hMultiplier;
        } else {
            Logger.log("Image '" + locationIn.getFileName() + "' is not a power of 2. Converting to be so.");
            newImage = new BufferedImage((int) Math.ceil(Math.log(image.getWidth()) / Math.log(2)),
                    (int) Math.ceil(Math.log(image.getHeight()) / Math.log(2)), image.getType());
            width = (int) Math.ceil(Math.log(image.getWidth()) / Math.log(2));
            defaultW = defaultWIn;
            defaultH = defaultHIn;
            height = (int) Math.ceil(Math.log(image.getHeight()) / Math.log(2));
            Graphics2D g = newImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(image, 0, 0, width, height, 0, 0, image.getWidth(), image.getHeight(), null);
            g.dispose();
            defaultW = defaultWIn;
            width = image.getWidth();
            height = image.getHeight();
            location = locationIn;
            wMultiplier = width / defaultW;
            hMultiplier = height / defaultH;
            // Make sure to not have 0 multiplier or cause issues!
            wMultiplier = wMultiplier == 0 ? 1 : wMultiplier;
            hMultiplier = hMultiplier == 0 ? 1 : hMultiplier;
        }
    }

    public boolean fileIsPowerOfTwo() {
        return (isPowerOfTwo(image.getWidth()) && isPowerOfTwo(image.getHeight()));
    }

    public void slice_and_save(int sx, int sy, int width, int height, Path path) throws IOException {
        BufferedImage oimg = this.newImage;
        Graphics2D oGraphics2d = this.g2d;

        newImage(width, height);
        subImage(sx, sy, sx + width, sy + height);
        store(path);

        this.newImage = oimg;
        this.g2d = oGraphics2d;
    }

    public void setImage(int defaultWIn, int defaultHIn) throws IOException {
        image = newImage;
        if (isPowerOfTwo(image.getWidth()) && isPowerOfTwo(image.getHeight())) {
            defaultW = defaultWIn;
            width = image.getWidth();
            defaultH = defaultHIn;
            height = image.getHeight();
            wMultiplier = image.getWidth() / defaultW;
            hMultiplier = image.getHeight() / defaultH;
        } else
            Logger.log("File is not a power of 2");
    }

    // Creates a new Image to store
    public void newImage(int newWidth, int newHeight) {
        newImage = new BufferedImage(newWidth * wMultiplier, newHeight * hMultiplier, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D) newImage.getGraphics();
    }

    public void addImage(Path imagePath, int x, int y) throws IOException {
        if (!imagePath.toFile().exists())
            return;
        BufferedImage image = ImageIO.read(imagePath.toFile());
        g2d.drawImage(image, x * wMultiplier, y * hMultiplier, null);
    }

    // Takes part of an image and stores it in the new image
    public void subImage(int x, int y, int x2, int y2, int storex, int storey) {
        int x3;
        int y3;
        int width2 = x2 * wMultiplier - x * wMultiplier;
        int height2 = y2 * hMultiplier - y * hMultiplier;
        x3 = x == 0 ? 0 : x * wMultiplier;
        y3 = y == 0 ? 0 : y * hMultiplier;
        BufferedImage part = subImage2(x3, y3, width2, height2);
        g2d.drawImage(part, storex * wMultiplier, storey * hMultiplier, null);
    }

    public void subImage(int x, int y, int x2, int y2) {
        subImage(x, y, x2, y2, 0, 0);
    }

    // Takes a part of an image and flips it either horizontally or vertically
    public void subImage(int x, int y, int x2, int y2, int storex, int storey, boolean flip) {
        int x3;
        int y3;
        int width2 = x2 * wMultiplier - x * wMultiplier;
        int height2 = y2 * hMultiplier - y * hMultiplier;
        x3 = x == 0 ? 0 : x * wMultiplier;
        y3 = y == 0 ? 0 : y * hMultiplier;
        BufferedImage part = subImage2(x3, y3, width2, height2);
        g2d.drawImage(createFlipped(part, flip), storex * wMultiplier, storey * hMultiplier, null);
    }

    // Only allows for the number 1 and flips it both horizontally and vertically
    public void subImage(int x, int y, int x2, int y2, int storex, int storey, int flip) {
        int width2 = x2 * wMultiplier - x * wMultiplier;
        int height2 = y2 * hMultiplier - y * hMultiplier;
        int x3 = x == 0 ? 0 : x * wMultiplier;
        int y3 = y == 0 ? 0 : y * hMultiplier;
        BufferedImage part = subImage2(x3, y3, width2, height2);
        g2d.drawImage(createFlipped(part, flip), storex * wMultiplier, storey * hMultiplier, null);
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

    private static BufferedImage toRGB(BufferedImage i) {
        BufferedImage rgb = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
        rgb.createGraphics().drawImage(i, 0, 0, null);
        return rgb;
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
    public boolean store() throws IOException {
        ImageIO.write(newImage, "png", location.toFile());
        return true;
    }

    public boolean store(Path locationIn) throws IOException {
        ImageIO.write(newImage, "png", locationIn.toFile());
        return true;
    }

    // Gets a sub image
    private BufferedImage subImage2(int x, int y, int width, int height) {
        return image.getSubimage(x, y, width, height);
    }

    // Returns the Width and Height variables
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // Returns the Width Multiplier and Height Multiplier variables
    public int getWidthMultiplier() {
        return wMultiplier;
    }

    public int getHeightMultiplier() {
        return hMultiplier;
    }

    // Detects if file is a power of two.
    private boolean isPowerOfTwo(int n) {
        return n > 0 && n == Math.pow(2, Math.round(Math.log(n) / Math.log(2)));
    }

    // Detects if file is a square
    public boolean isSquare() {
        return width == height;
    }
}
