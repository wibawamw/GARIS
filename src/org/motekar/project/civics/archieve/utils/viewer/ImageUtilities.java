package org.motekar.project.civics.archieve.utils.viewer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 *
 * @author Muhamad Wibawa
 */
public class ImageUtilities {

    public static BufferedImage createCompatibleImage(int width, int height, boolean alpha) {
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;

        String bool = System.getProperty("java.awt.headless");

        if (!GraphicsEnvironment.isHeadless() && !Boolean.parseBoolean(bool)) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            try {
                // Determine the type of transparency of the new buffered image
                int transparency = Transparency.OPAQUE;
                if (alpha) {
                    transparency = Transparency.TRANSLUCENT;
                }

                // Create the buffered image
                GraphicsDevice gs = ge.getDefaultScreenDevice();
                GraphicsConfiguration gc = gs.getDefaultConfiguration();
                bimage = gc.createCompatibleImage(width, height, transparency);
            } catch (HeadlessException e) {
                // The system does not have a screen
            }
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (alpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(width, height, type);
        }
        return bimage;
    }

    /**
     * Progressive bilinear scaling: for any downscale size, scale iteratively
     * by halves using BILINEAR filtering until the proper size is reached.
     */
    public static BufferedImage getOptimalScalingImage(BufferedImage inputImage, int width, int height) {
        int currentWidth = inputImage.getWidth();
        int currentHeigth = inputImage.getHeight();
        BufferedImage currentImage = inputImage;
        int deltaX = currentWidth - width;
        int deltaY = currentHeigth - height;
        int nextPow2X = currentWidth >> 1;
        int nextPow2Y = currentHeigth >> 1;
        while (currentWidth > 1 && currentHeigth > 1) {
            if (deltaX <= nextPow2X || deltaY <= nextPow2Y) {
                if (currentWidth != width || currentHeigth != height) {
                    BufferedImage tmpImage = createCompatibleImage(width, height, true);
                    Graphics g = tmpImage.getGraphics();
                    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g.drawImage(currentImage, 0, 0, tmpImage.getWidth(), tmpImage.getHeight(), null);
                    currentImage = tmpImage;
                }
                return currentImage;
            } else {
                BufferedImage tmpImage = createCompatibleImage(currentWidth >> 1, currentHeigth >> 1, true);
                Graphics g = tmpImage.getGraphics();
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g.drawImage(currentImage, 0, 0, tmpImage.getWidth(), tmpImage.getHeight(), null);
                currentImage = tmpImage;
                currentWidth = currentImage.getWidth();
                currentHeigth = currentImage.getHeight();
                deltaX = currentWidth - width;
                deltaY = currentHeigth - height;
                nextPow2X = currentWidth >> 1;
                nextPow2Y = currentHeigth >> 1;
            }
        }
        return currentImage;
    }

    public static BufferedImage createDropShadow(
            BufferedImage image, int size) {
        BufferedImage shadow = new BufferedImage(
                image.getWidth() + 4 * size,
                image.getHeight() + 4 * size,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = shadow.createGraphics();
        g2.drawImage(image, size * 2, size * 2, null);
        g2.setComposite(AlphaComposite.SrcIn);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, shadow.getWidth(), shadow.getHeight());
        g2.dispose();
        shadow = getGaussianBlurFilter(size, true).
                filter(shadow, null);
        shadow = getGaussianBlurFilter(size, false).
                filter(shadow, null);
        return shadow;
    }

    public static ConvolveOp getGaussianBlurFilter(int radius,
            boolean horizontal) {
        if (radius < 1) {
            throw new IllegalArgumentException(
                    "Radius must be >= 1");
        }
        int size = radius * 2 + 1;
        float[] data = new float[size];
        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;
        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            int index = i + radius;
            data[index] = (float) Math.exp(-distance / twoSigmaSquare)
                    / sigmaRoot;
            total += data[index];
        }
        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }
        Kernel kernel = null;
        if (horizontal) {
            kernel = new Kernel(size, 1, data);
        } else {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }

    public static BufferedImage resizeToNewWidth(int width, BufferedImage i) {
        int newHeigth = (i.getHeight() * width) / i.getWidth();
        return getOptimalScalingImage(i, width, newHeigth);
    }

    public static BufferedImage resizeToNewHeigth(int heigth, BufferedImage i) {
        int newWidth = (i.getWidth() * heigth) / i.getHeight();
        return getOptimalScalingImage(i, newWidth, heigth);
    }
}
