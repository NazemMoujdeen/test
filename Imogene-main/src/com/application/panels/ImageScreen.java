package com.application.panels;

import com.GA.GeneticAlgorithm;
import com.GA.crossover.BlendCrossover;
import com.GA.crossover.CrossoverFunction;
import com.GA.crossover.EnsembleCrossoverFunction;
import com.GA.crossover.PixelwiseRGBCrossover;
import com.GA.fitness.*;
import com.GA.fitness.adjustment.FitnessAdjustment;
import com.GA.fitness.adjustment.NormalisationAdjustment;
import com.GA.generation.GenerationFunction;
import com.GA.generation.RandomBitmapGeneration;
import com.GA.mutation.*;
import com.GA.selection.RouletteWheelSelection;
import com.GA.selection.SelectionFunction;
import com.application.Application;
import com.utils.BitMapImage;
import com.utils.ImageRW;
import com.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImageScreen extends JPanel {

    public static DrawingPanel drawingPanel;

    public static boolean UPSCALE = true;
    public static int UPSCALE_FACTOR = 6; //default scale factor

    public static final int MAX_IMAGE_WIDTH = 500; // maximum width
    public static final int MAX_IMAGE_HEIGHT = 500; // maximum height

    public static int currentImageHeight = 100;
    public static int currentImageWidth = 133;

    public static BitMapImage currentImage;
    public static GeneticAlgorithm currentGA;
    public static boolean halt;

    public static int getUpscaleFactor() {
        return UPSCALE_FACTOR;
    }

    public static void setImageSize(int width, int height) {

        width = Math.max(1, Math.min(width, MAX_IMAGE_WIDTH));
        height = Math.max(1, Math.min(height, MAX_IMAGE_HEIGHT));

        currentImageWidth = width;
        currentImageHeight = height;

        // resize drawing panel to match new scale + size

        if (drawingPanel != null) {
            drawingPanel.setPreferredSize(new Dimension(
                    currentImageWidth * UPSCALE_FACTOR,
                    currentImageHeight * UPSCALE_FACTOR
            ));

        // resize pixel buffer so new size renders correctly

            drawingPanel.resizeImageBuffer(
                    currentImageWidth * UPSCALE_FACTOR,
                    currentImageHeight * UPSCALE_FACTOR
            );

            drawingPanel.revalidate();
            drawingPanel.repaint();
        }
        // resize existing image buffer if image already exists
        if (currentImage != null) {
            currentImage = ImageUtils.resize(currentImage, currentImageHeight, currentImageWidth);
        } else {
            // resize existing image buffer if image already exists
            currentImage = new BitMapImage(currentImageWidth, currentImageHeight);
        }

        redraw();
    }

    public static int getCurrentImageHeight() { return currentImageHeight; }
    public static int getCurrentImageWidth() { return currentImageWidth; }

    // Singleton pattern
    private static final ImageScreen instance = new ImageScreen();
    public static ImageScreen getInstance() { return instance; }

    public ImageScreen() {

        super(new BorderLayout());

        JPanel leftPanel = LeftSidebar.getInstance();
        drawingPanel = new DrawingPanel(currentImageHeight, currentImageWidth);
        drawingPanel.setLayout(new GridBagLayout());
        JPanel rightPanel = RightSidebar.getInstance();

        JSplitPane splitLeftCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, drawingPanel);
        splitLeftCenter.setResizeWeight(0.10);
        JSplitPane splitAll = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitLeftCenter, rightPanel);
        splitAll.setResizeWeight(0.85);

        setLayout(new BorderLayout()); // sets layout for full window
        add(splitAll, BorderLayout.CENTER);

        // create initial blank white image so window isn't empty
        if (currentImage == null) {
            currentImage = new BitMapImage(currentImageWidth, currentImageHeight);
            for (int y = 0; y < currentImageHeight; y++) {
                for (int x = 0; x < currentImageWidth; x++) {
                    currentImage.setPixel(x, y, 255, 255, 255);
                }
            }

            redraw();
        }
    }

    public static void drawPixel(int x, int y, Color color) {
        int scale = UPSCALE ? getUpscaleFactor() : 1;
        int px = x * scale;
        int py = y * scale;

        for (int xx = px; xx < px + scale; xx++) {
            for (int yy = py; yy < py + scale; yy++) {
                drawingPanel.setPixel(xx, yy, color);
            }
        }
    }

    public static void redraw() { paintImage(currentImage); }

    private static void paintImage(BitMapImage image) {
        if (image == null) {
            currentImage = new BitMapImage(currentImageWidth, currentImageHeight);
            image = currentImage;
        }

        int[][][] bitmap = image.getRgb();

        if (bitmap.length != currentImageHeight || bitmap[0].length != currentImageWidth) {
            currentImage = ImageUtils.resize(image, currentImageHeight, currentImageWidth);
            bitmap = currentImage.getRgb();
        }

        for (int y = 0; y < currentImageHeight; y++) {
            for (int x = 0; x < currentImageWidth; x++) {
                int r = bitmap[y][x][0];
                int g = bitmap[y][x][1];
                int b = bitmap[y][x][2];
                drawPixel(x, y, new Color(r, g, b));
            }
        }
    }
    // Auto-scale to fill available window
    public static void fillToScreen() {
        if (currentImage == null || drawingPanel == null) return;

        int panelWidth = drawingPanel.getWidth();
        int panelHeight = drawingPanel.getHeight();

        int imgWidth = currentImage.getWidth();
        int imgHeight = currentImage.getHeight();

        int scaleX = Math.max(1, panelWidth / imgWidth);
        int scaleY = Math.max(1, panelHeight / imgHeight);
        int newScale = Math.max(1, Math.min(scaleX, scaleY));

        UPSCALE_FACTOR = newScale;

        drawingPanel.resizeImageBuffer(
                imgWidth * newScale,
                imgHeight * newScale
        );

        redraw();
    }
}
