package com.agentdid127.resourcepack.library.utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class ImageConverter {
    protected int imageWidth = 0;
    protected int imageHeight = 0;
    protected int defaultW = 1;
    protected int defaultH = 1;
    protected BufferedImage image;
    protected BufferedImage newImage;
    protected Path location;
    protected Graphics2D g2d;

    /**
     * Default Constructor
     * 
     * @param defaultWIn
     * @param defaultHIn
     * @param locationIn
     * @throws IOException
     */
    public ImageConverter(Integer defaultWIn, Integer defaultHIn, Path locationIn) throws IOException {
        image = ImageIO.read(locationIn.toFile());
        location = locationIn;
        defaultW = defaultWIn == null ? image.getWidth() : defaultWIn;
        defaultH = defaultHIn == null ? image.getHeight() : defaultHIn;
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        if (!fileIsPowerOfTwo()) {
            Logger.log("Image (" + image.getWidth() + "x" + image.getHeight() + ") '" + locationIn.getFileName()
                    + "' resolution size is not a power of 2. Converting to be so.");

            int fixed_width = (int) Math.ceil(Math.log(image.getWidth()) / Math.log(2));
            if (fixed_width < 1)
                fixed_width = 1;

            int fixed_height = (int) Math.ceil(Math.log(image.getHeight()) / Math.log(2));
            if (fixed_height < 1)
                fixed_height = 1;

            newImage = new BufferedImage(fixed_width, fixed_height, image.getType());
            Graphics2D g = newImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(image, 0, 0, fixed_width, fixed_height, 0, 0, imageWidth, imageHeight, null);
            g.dispose();
        } else
            newImage = image;
    }

    /**
     * Save a section of the image to a new image.
     * 
     * @param sx
     * @param sy
     * @param width
     * @param height
     * @param path
     * @throws IOException
     */
    public void saveSlice(int sx, int sy, int width, int height, Path path) throws IOException {
        BufferedImage original = this.newImage;
        Graphics2D graphics = this.g2d;
        newImage(width, height);
        subImage(sx, sy, sx + width, sy + height);
        store(path);
        this.newImage = original;
        this.g2d = graphics;
    }

    /**
     * Fill a spot with nothing.
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void fillEmpty(int x, int y, int width, int height) {
        newImage((int) (imageWidth / getWidthMultiplier()), (int) (imageHeight / getHeightMultiplier()));
        g2d.drawImage(image, 0, 0, imageWidth, imageHeight, null);
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(scaleX(x), scaleY(y), scaleWidth(width), scaleHeight(height));
        g2d.setComposite(AlphaComposite.Src);
        image = newImage;
    }

    /**
     * Set the current image.
     * 
     * @param defaultWIn
     * @param defaultHIn
     * @throws IOException
     */
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

    /**
     * Creates a new Image to store
     * 
     * @param newWidth
     * @param newHeight
     */
    public void newImage(int newWidth, int newHeight, int type) {
        newImage = new BufferedImage(scaleWidth(newWidth), scaleHeight(newHeight), type);
        g2d = (Graphics2D) newImage.getGraphics();
    }

    public void newImage(int newWidth, int newHeight) {
        newImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Add a image to the current image.
     * 
     * @param imagePath
     * @param x
     * @param y
     * @throws IOException
     */
    public void addImage(Path imagePath, int x, int y) throws IOException {
        if (!imagePath.toFile().exists())
            return;
        BufferedImage image = ImageIO.read(imagePath.toFile());
        g2d.drawImage(image, scaleX(x), scaleY(y), null);
    }

    /**
     * Takes part of an image and stores it in the new image
     * 
     * @param x
     * @param y
     * @param x2
     * @param y2
     * @param storeX
     * @param storeY
     */
    public void subImage(int x, int y, int x2, int y2, int storeX, int storeY) {
        double scaleW = getWidthMultiplier();
        double scaleH = getHeightMultiplier();
        int width2 = (int) (x2 * scaleW - x * scaleW);
        int height2 = (int) (y2 * scaleH - y * scaleH);
        BufferedImage part = getSubImage(scaleX(x), scaleY(y), width2, height2);
        g2d.drawImage(part, Math.round((float) (storeX * scaleW)), Math.round((float) (storeY * scaleH)), null);
    }

    /**
     * Takes part of an image and stores it in the new image
     * 
     * @param x
     * @param y
     * @param x2
     * @param y2
     */
    public void subImage(int x, int y, int x2, int y2) {
        subImage(x, y, x2, y2, 0, 0);
    }

    /**
     * Takes a part of an image and flips it either horizontally or vertically
     * 
     * @param x
     * @param y
     * @param x2
     * @param y2
     * @param storeX
     * @param storeY
     * @param flip
     */
    public void subImage(int x, int y, int x2, int y2, int storeX, int storeY, boolean flip) {
        double scaleW = getWidthMultiplier();
        double scaleH = getHeightMultiplier();
        int width2 = (int) (x2 * scaleW - x * scaleW);
        int height2 = (int) (y2 * scaleH - y * scaleH);
        BufferedImage part = getSubImage(scaleX(x), scaleY(y), width2, height2);
        g2d.drawImage(createFlipped(part, flip), Math.round((float) (storeX * scaleW)),
                Math.round((float) (storeY * scaleH)), null);
    }

    /**
     * Only allows for the number 1 and flips it both horizontally and vertically
     * 
     * @param x
     * @param y
     * @param x2
     * @param y2
     * @param storeX
     * @param storeY
     * @param flip
     */
    public void subImage(int x, int y, int x2, int y2, int storeX, int storeY, int flip) {
        double scaleW = getWidthMultiplier();
        double scaleH = getHeightMultiplier();

        int width2 = (int) (x2 * scaleW - x * scaleW);
        int height2 = (int) (y2 * scaleH - y * scaleH);
        int x3 = (int) (x * scaleW);
        int y3 = (int) (y * scaleH);

        BufferedImage part = getSubImage(x3, y3, width2, height2);
        g2d.drawImage(createFlipped(part, flip), Math.round((float) (storeX * scaleW)),
                Math.round((float) (storeY * scaleH)), null);
    }

    public void subImageSized(int x, int y, int width, int height, int storeX, int storeY) {
        subImage(x, y, x + width, y + height, storeX, storeY);
    }

    public void subImageSized(int x, int y, int width, int height) {
        subImage(x, y, x + width, y + height, 0, 0);
    }

    /**
     * Recolor the entire image.
     * 
     * @param color
     */
    public void colorize(Color color) {
        g2d.setPaint(color);
        g2d.drawImage(image, 0, 0, null);
        g2d.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
    }

    /**
     * Recolor the grayscale image.
     * 
     * @param color
     */
    public void colorizeClipped(Color color) {
        this.newImage(this.getWidth(), this.getHeight());
        this.g2d.drawImage(this.image, 0, 0, null);
        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                int imageRGBA = newImage.getRGB(x, y);
                int alpha = (imageRGBA >> 24) & 0xFF;
                if (alpha == 0)
                    continue;
                int grayscaleValue = (imageRGBA >> 16) & 0xFF;
                int red = (grayscaleValue * color.getRed()) / 255;
                int green = (grayscaleValue * color.getGreen()) / 255;
                int blue = (grayscaleValue * color.getBlue()) / 255;
                this.newImage.setRGB(x, y, (alpha << 24) | (red << 16) | (green << 8) | blue);
            }
        }
    }

    /**
     * Grayscale the image
     */
    public void grayscale() {
        BufferedImage gray = new BufferedImage(newImage.getWidth(), newImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        gray.createGraphics().drawImage(image, 0, 0, null);
        newImage = gray;
    }

    /**
     * Flip The Image
     * 
     * @param image
     * @param flip
     * @return BufferedImage
     */
    private static BufferedImage createFlipped(BufferedImage image, int flip) {
        AffineTransform at = new AffineTransform();
        if (flip != 1)
            return image;
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
        return createTransformed(image, at);
    }

    /**
     * Flip The Image
     * 
     * @param image
     * @param flip
     * @return BufferedImage
     */
    private static BufferedImage createFlipped(BufferedImage image, boolean flip) {
        AffineTransform at = new AffineTransform();
        if (flip) {
            at.concatenate(AffineTransform.getScaleInstance(1, -1));
            at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
        } else {
            at.concatenate(AffineTransform.getScaleInstance(-1, 1));
            at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
        }
        return createTransformed(image, at);
    }

    /**
     * Transforms The BufferedImage
     * 
     * @param image
     * @param at
     * @return BufferedImage
     */
    private static BufferedImage createTransformed(BufferedImage image, AffineTransform at) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    /**
     * Copy pixels to a new image with a new alpha & ignoring transparent pixels
     * 
     * @param inputImage
     * @param outImage
     * @param alphaNew
     * @throws IOException
     */
    private void copyPixels(BufferedImage inputImage, BufferedImage outImage, Integer alphaNew)
            throws IOException {
        BufferedImage image = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        image.getGraphics().drawImage(inputImage, 0, 0, null);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgba = image.getRGB(x, y);
                int alpha = (rgba >> 24) & 0xFF;
                int red = (rgba >> 16) & 0xFF;
                int green = (rgba >> 8) & 0xFF;
                int blue = rgba & 0xFF;
                if ((red + blue + green + alpha) == 0)
                    continue;
                if (alphaNew != null)
                    alpha = alphaNew;
                outImage.setRGB(x, y, (alpha << 24) | (red << 16) | (green << 8) | blue);
            }
        }
    }

    /**
     * Copy pixels to a new image
     * 
     * @param inputImage
     * @param outImage
     * @throws IOException
     */
    private void copyPixels(BufferedImage inputImage, BufferedImage outImage) throws IOException {
        copyPixels(inputImage, outImage, null);
    }

    /**
     * Place a image in the background using transparency
     * 
     * @param background
     * @param alpha
     * @throws IOException
     */
    public void backgroundImage(Path background, int alpha) throws IOException {
        this.newImage(defaultW, defaultH);
        BufferedImage backgroundImage = ImageIO.read(background.toFile());
        this.copyPixels(backgroundImage, newImage, alpha);
        this.copyPixels(image, newImage);
    }

    /**
     * Place a image in the background using transparency
     * 
     * @param backgroundImage
     * @param alpha
     * @throws IOException
     */
    public void backgroundImage(BufferedImage backgroundImage, int alpha) throws IOException {
        this.newImage(defaultW, defaultH);
        this.copyPixels(backgroundImage, newImage, alpha);
        this.copyPixels(image, newImage);
    }

    /**
     * Save the image as the provided type at the provided location.
     * 
     * @param locationIn
     * @param type
     * @return boolean
     * @throws IOException
     */
    public boolean store(Path locationIn, String type) throws IOException {
        ImageIO.write(newImage, type, locationIn.toFile());
        return true;
    }

    /**
     * Save the image at the provided location.
     * 
     * @param locationIn
     * @return boolean
     * @throws IOException
     */
    public boolean store(Path locationIn) throws IOException {
        return store(locationIn, "png");
    }

    /**
     * Save the image.
     * 
     * @return boolean
     * @throws IOException
     */
    public boolean store() throws IOException {
        return store(location);
    }

    /**
     * Get a sub-part of the image using a rectangle.
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     * @return BufferedImage
     */
    private BufferedImage getSubImage(int x, int y, int width, int height) {
        return image.getSubimage(x, y, width, height);
    }

    /**
     * Get the Image Width
     * 
     * @return int
     */
    public int getWidth() {
        return imageWidth;
    }

    /**
     * Get the Image Height
     * 
     * @return int
     */
    public int getHeight() {
        return imageHeight;
    }

    /**
     * Get the Width Scale Multiplier (imageWidth/defaultWidth)
     * 
     * @return double
     */
    public double getWidthMultiplier() {
        double wMultiplier = (double) imageWidth / (double) defaultW;
        // Make sure to not have 0 multiplier or cause issues!
        wMultiplier = wMultiplier < 1 ? 1 : wMultiplier;
        return wMultiplier;
    }

    /**
     * Get the Height Scale Multiplier (imageHeight/defaultHeight)
     * 
     * @return double
     */
    public double getHeightMultiplier() {
        double hMultiplier = (double) imageHeight / (double) defaultH;
        // Make sure to not have 0 multiplier or cause issues!
        hMultiplier = hMultiplier < 1 ? 1 : hMultiplier;
        return hMultiplier;
    }

    public int scaleX(int x) {
        return (int) (x * getWidthMultiplier());
    }

    public int scaleY(int y) {
        return (int) (y * getHeightMultiplier());
    }

    public int scaleWidth(int width) {
        return (int) (width * getWidthMultiplier());
    }

    public int scaleHeight(int height) {
        return (int) (height * getHeightMultiplier());
    }

    public boolean imageIsPowerOfTwo(BufferedImage image) {
        return (isPowerOfTwo(image.getWidth()) && isPowerOfTwo(image.getHeight()));
    }

    public boolean fileIsPowerOfTwo() {
        return imageIsPowerOfTwo(image);
    }

    // Detects if file is a power of two.
    private boolean isPowerOfTwo(int n) {
        return n > 0 && n == Math.pow(2, Math.round(Math.log(n) / Math.log(2)));
    }

    /**
     * Returns true if the image is square.
     * 
     * @return boolean
     */
    public boolean isSquare() {
        return imageWidth == imageHeight;
    }
}
