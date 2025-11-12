package com.application.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawingPanel extends JPanel {
    private BufferedImage canvas;

    private Graphics2D g2;

    public DrawingPanel(int height, int width) {
        setLayout(new GridBagLayout());
        if (ImageScreen.UPSCALE) {
            width *= ImageScreen.UPSCALE_FACTOR;
            height *= ImageScreen.UPSCALE_FACTOR;
        }
        this.setSize(width, height);
        canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        clearCanvas();
    }

    public void resizeImageBuffer(int newWidth, int newHeight) {
        canvas = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        g2 = canvas.createGraphics();
        repaint();
    }


    public void setPixel(int x, int y, Color color) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            canvas.setRGB(x, y, color.getRGB());
            repaint();
        }
    }

    public void clearCanvas() {
        Graphics2D g2d = canvas.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Clears background each repaint

        if (ImageScreen.currentImage == null) return;

        int scale = ImageScreen.UPSCALE_FACTOR;
        int[][][] rgb = ImageScreen.currentImage.getRgb();
        int height = rgb.length;
        int width = rgb[0].length;

        // Calculate scaled dimensions
        int scaledWidth = width * scale;
        int scaledHeight = height * scale;

        // Fill the panel background
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw pixels manually 
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = rgb[y][x][0];
                int gg = rgb[y][x][1];
                int b = rgb[y][x][2];
                g.setColor(new Color(r, gg, b));
                g.fillRect(x * scale, y * scale, scale, scale);
            }
        }
    }
}
