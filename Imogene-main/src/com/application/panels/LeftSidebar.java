package com.application.panels;

import com.API.FilterConnector;
import com.API.GenerationConnector;
import com.GA.ImageGenerator;
import com.GA.generation.RandomColorGeneration;
import com.utils.ImageUtils;
import java.util.Stack;
import com.utils.BitMapImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeftSidebar extends JPanel {

    // Singleton pattern
    private static final LeftSidebar instance = new LeftSidebar();

    public static LeftSidebar getInstance() {
        return instance;
    }

    private boolean remote = false;

    // Buttons moved out for visibility customisation
    private JButton btnImageSettings;
    private JButton generateRandom;
    private JButton generateColour;
    private JButton filterGrayscale;
    private JButton filterSmoothSoft;
    private JButton filterSmoothMedium;
    private JButton filterSmoothHard;
    private JButton filterInvert;
    private JButton redRebalance;
    private JButton greenRebalance;
    private JButton blueRebalance;
    private JButton btnRedOntoGreen;
    private JButton btnGreenOntoBlue;
    private JButton btnBlueOntoRed;
    private JButton btnHueOntoSaturation;
    private JButton btnSaturationOntoLightness;
    private JButton btnLightnessOntoHue;
    private JButton undoBtn;
    private JButton redoBtn;
    private Stack<BitMapImage> undoStack = new Stack<>();
    private Stack<BitMapImage> redoStack = new Stack<>();


    public LeftSidebar() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        undoBtn = new JButton("Undo");
        undoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!undoStack.isEmpty()) {
                    redoStack.push(imageCopy(ImageScreen.currentImage));
                    ImageScreen.currentImage = undoStack.pop();
                    ImageScreen.redraw();
                    updateButtonStates();
                }
            }
        });
        redoBtn = new JButton("Redo");
        redoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!redoStack.isEmpty()) {
                    undoStack.push(imageCopy(ImageScreen.currentImage));
                    ImageScreen.currentImage = redoStack.pop();
                    ImageScreen.redraw();
                    updateButtonStates();
                }
            }
        });

        JLabel lblGeneration = new JLabel("Generation");
        generateRandom = new JButton("Generate Random");
        generateRandom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = GenerationConnector.requestGeneration(GenerationConnector.RANDOM_BITMAP, ImageScreen.currentImageHeight, ImageScreen.currentImageWidth);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    ImageScreen.currentImage = ImageGenerator.randomPixels(ImageScreen.currentImageHeight, ImageScreen.currentImageWidth);

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });

        generateColour = new JButton("Generate Colour");
        generateColour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = GenerationConnector.requestGeneration(GenerationConnector.RANDOM_COLOUR, ImageScreen.currentImageHeight, ImageScreen.currentImageWidth);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {

                    ImageScreen.currentImage = (new RandomColorGeneration()).generate(ImageScreen.currentImageHeight, ImageScreen.currentImageWidth).getImage();

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });


        JLabel lblFilters = new JLabel("Filters");

        filterGrayscale = new JButton("Grayscale");
        filterGrayscale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = FilterConnector.requestFilter(FilterConnector.FILTER_GRAYSCALE, ImageScreen.currentImage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {

                    ImageScreen.currentImage = ImageUtils.grayscale(ImageScreen.currentImage);

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });
        /// /////
        filterSmoothSoft = new JButton("Smooth (soft)");
        filterSmoothSoft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = FilterConnector.requestFilter(FilterConnector.FILTER_SMOOTH_SOFT, ImageScreen.currentImage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {

                    ImageScreen.currentImage = ImageUtils.smoothFilter(ImageScreen.currentImage, 0.8, 0.025);

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });

        filterSmoothMedium = new JButton("Smooth (medium)");
        filterSmoothMedium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = FilterConnector.requestFilter(FilterConnector.FILTER_SMOOTH_MEDIUM, ImageScreen.currentImage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {

                    ImageScreen.currentImage = ImageUtils.smoothFilter(ImageScreen.currentImage, 0.5, 0.0625);


                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });

        filterSmoothHard = new JButton("Smooth (hard)");
        filterSmoothHard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = FilterConnector.requestFilter(FilterConnector.FILTER_SMOOTH_HARD, ImageScreen.currentImage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {

                    ImageScreen.currentImage = ImageUtils.smoothFilter(ImageScreen.currentImage, 0.2, 0.1);

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });

        filterInvert = new JButton("Invert");
        filterInvert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = FilterConnector.requestFilter(FilterConnector.FILTER_INVERT, ImageScreen.currentImage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {

                    ImageScreen.currentImage = ImageUtils.invert(ImageScreen.currentImage);

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });

        redRebalance = new JButton("Rebalance Red");
        redRebalance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = FilterConnector.requestFilter(FilterConnector.FILTER_REBALANCE_RED, ImageScreen.currentImage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {

                    ImageScreen.currentImage = ImageUtils.rgbBalancing(ImageScreen.currentImage, 0.6, 0.2, 0.2);

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });

        greenRebalance = new JButton("Rebalance Green");
        greenRebalance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = FilterConnector.requestFilter(FilterConnector.FILTER_REBALANCE_GREEN, ImageScreen.currentImage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {

                    ImageScreen.currentImage = ImageUtils.rgbBalancing(ImageScreen.currentImage, 0.2, 0.6, 0.2);

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });

        blueRebalance = new JButton("Rebalance Blue");
        blueRebalance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = FilterConnector.requestFilter(FilterConnector.FILTER_REBALANCE_BLUE, ImageScreen.currentImage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {

                    ImageScreen.currentImage = ImageUtils.rgbBalancing(ImageScreen.currentImage, 0.2, 0.2, 0.6);

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });

        JLabel lblProjections = new JLabel("Spectrum Projections");

        btnRedOntoGreen = new JButton("Red -> Green");
        btnRedOntoGreen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = FilterConnector.requestFilter(FilterConnector.FILTER_RED_TO_GREEN, ImageScreen.currentImage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    ImageScreen.currentImage = ImageUtils.spectralProjection(ImageScreen.currentImage, "Red", "Green");

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });

        btnGreenOntoBlue = new JButton("Green -> Blue");
        btnGreenOntoBlue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = FilterConnector.requestFilter(FilterConnector.FILTER_GREEN_TO_BLUE, ImageScreen.currentImage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {

                    ImageScreen.currentImage = ImageUtils.spectralProjection(ImageScreen.currentImage, "Green", "Blue");

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });

        btnBlueOntoRed = new JButton("Blue -> Red");
        btnBlueOntoRed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = FilterConnector.requestFilter(FilterConnector.FILTER_BLUE_TO_RED, ImageScreen.currentImage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {

                    ImageScreen.currentImage = ImageUtils.spectralProjection(ImageScreen.currentImage, "Blue", "Red");

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });

        btnHueOntoSaturation = new JButton("Hue -> Saturation (red)");
        btnHueOntoSaturation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = FilterConnector.requestFilter(FilterConnector.FILTER_HUE_TO_SATURATION, ImageScreen.currentImage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {

                    ImageScreen.currentImage = ImageUtils.spectralProjection(ImageScreen.currentImage, "Hue", "Saturation");

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });

        btnSaturationOntoLightness = new JButton("Saturation -> Lightness");
        btnSaturationOntoLightness.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = FilterConnector.requestFilter(FilterConnector.FILTER_SATURATION_TO_LIGHTNESS, ImageScreen.currentImage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {

                    ImageScreen.currentImage = ImageUtils.spectralProjection(ImageScreen.currentImage, "Saturation", "Lightness");

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });

        btnLightnessOntoHue = new JButton("Lightness -> Hue");
        btnLightnessOntoHue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStack.clear();
                undoStack.push(imageCopy(ImageScreen.currentImage));
                if(remote) {
                    try {
                        ImageScreen.currentImage = FilterConnector.requestFilter(FilterConnector.FILTER_LIGHTNESS_TO_HUE, ImageScreen.currentImage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {

                    ImageScreen.currentImage = ImageUtils.spectralProjection(ImageScreen.currentImage, "Lightness", "Hue");

                }
                updateButtonStates();
                ImageScreen.redraw();
            }
        });

        add(undoBtn);
        add(redoBtn);
        // Generation section
        add(lblGeneration);
        add(generateRandom);
        add(generateColour);

        // --- Added image controls section below generation ---
        JLabel imageControlsLabel = new JLabel("Image Controls");
        imageControlsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        imageControlsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(imageControlsLabel);


        JButton setImageSizeButton = new JButton("Set Image Size & Scale");
        setImageSizeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        setImageSizeButton.addActionListener(e -> {
            openImageSettingsDialog();
        });
        add(setImageSizeButton);

        this.btnImageSettings = setImageSizeButton;

        JButton fillToScreenButton = new JButton("Fill to Screen");
        fillToScreenButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fillToScreenButton.addActionListener(e -> ImageScreen.fillToScreen());
        add(fillToScreenButton);


        // Separator
        add(Box.createVerticalStrut(10));
        add(new JSeparator(SwingConstants.HORIZONTAL));

        // Filter section
        add(lblFilters);
        add(filterGrayscale);
        add(filterSmoothSoft);
        add(filterSmoothMedium);
        add(filterSmoothHard);
        add(filterInvert);
        //leftPanel.add(spectrumMaping);
        add(redRebalance);
        add(greenRebalance);
        add(blueRebalance);

        // Separator
        add(Box.createVerticalStrut(10));
        add(new JSeparator(SwingConstants.HORIZONTAL));

        // Spectrum projection section
        add(lblProjections);
        add(btnRedOntoGreen);
        add(btnGreenOntoBlue);
        add(btnBlueOntoRed);
        add(btnHueOntoSaturation);
        add(btnSaturationOntoLightness);
        add(btnLightnessOntoHue);

        // Center all buttons and labels of the left sidebar
        lblGeneration.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateRandom.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateColour.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblFilters.setAlignmentX(Component.CENTER_ALIGNMENT);
        filterGrayscale.setAlignmentX(Component.CENTER_ALIGNMENT);
        filterSmoothSoft.setAlignmentX(Component.CENTER_ALIGNMENT);
        filterSmoothMedium.setAlignmentX(Component.CENTER_ALIGNMENT);
        filterSmoothHard.setAlignmentX(Component.CENTER_ALIGNMENT);
        filterInvert.setAlignmentX(Component.CENTER_ALIGNMENT);
        //spectrumMaping.setAlignmentX(Component.CENTER_ALIGNMENT);
        redRebalance.setAlignmentX(Component.CENTER_ALIGNMENT);
        greenRebalance.setAlignmentX(Component.CENTER_ALIGNMENT);
        blueRebalance.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblProjections.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRedOntoGreen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGreenOntoBlue.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBlueOntoRed.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHueOntoSaturation.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSaturationOntoLightness.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLightnessOntoHue.setAlignmentX(Component.CENTER_ALIGNMENT);

        undoBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        redoBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set all button widths to full width of the left sidebar
        btnImageSettings.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnImageSettings.getPreferredSize().height));
        generateRandom.setMaximumSize(new Dimension(Integer.MAX_VALUE, generateRandom.getPreferredSize().height));
        generateColour.setMaximumSize(new Dimension(Integer.MAX_VALUE, generateColour.getPreferredSize().height));
        filterGrayscale.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterGrayscale.getPreferredSize().height));
        filterSmoothSoft.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterSmoothSoft.getPreferredSize().height));
        filterSmoothMedium.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterSmoothMedium.getPreferredSize().height));
        filterSmoothHard.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterSmoothHard.getPreferredSize().height));
        filterInvert.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterInvert.getPreferredSize().height));
        //spectrumMaping.setMaximumSize(new Dimension(Integer.MAX_VALUE, spectrumMaping.getPreferredSize().height));
        redRebalance.setMaximumSize(new Dimension(Integer.MAX_VALUE, redRebalance.getPreferredSize().height));
        greenRebalance.setMaximumSize(new Dimension(Integer.MAX_VALUE, greenRebalance.getPreferredSize().height));
        blueRebalance.setMaximumSize(new Dimension(Integer.MAX_VALUE, blueRebalance.getPreferredSize().height));
        // lblProjections.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblProjections.getPreferredSize().height));
        btnRedOntoGreen.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnRedOntoGreen.getPreferredSize().height));
        btnGreenOntoBlue.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnGreenOntoBlue.getPreferredSize().height));
        btnBlueOntoRed.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnBlueOntoRed.getPreferredSize().height));
        btnHueOntoSaturation.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnHueOntoSaturation.getPreferredSize().height));
        btnSaturationOntoLightness.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnSaturationOntoLightness.getPreferredSize().height));
        btnLightnessOntoHue.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnLightnessOntoHue.getPreferredSize().height));

        undoBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, undoBtn.getPreferredSize().height));
        redoBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, redoBtn.getPreferredSize().height));
    }
    /**
    * Creates a full copy of the bitmap image
     * */

    private BitMapImage imageCopy(BitMapImage image) {
        int[][][] originalRgb = image.getRgb();
        int height = image.getHeight();
        int width = image.getWidth();

        int[][][] newRgb = new int[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {
                    newRgb[y][x][c] = originalRgb[y][x][c];
                }
            }
        }
        return new BitMapImage(newRgb);
    }


    private void openImageSettingsDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField widthField = new JTextField(String.valueOf(ImageScreen.currentImageWidth));
        JTextField heightField = new JTextField(String.valueOf(ImageScreen.currentImageHeight));
        JSpinner scaleSpinner = new JSpinner(new SpinnerNumberModel(ImageScreen.UPSCALE_FACTOR, 1, 20, 1));

        panel.add(new JLabel("Image Width:"));
        panel.add(widthField);
        panel.add(new JLabel("Image Height:"));
        panel.add(heightField);
        panel.add(new JLabel("Upscale Factor:"));
        panel.add(scaleSpinner);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Set Image Size and Scaling",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                int newWidth = Integer.parseInt(widthField.getText());
                int newHeight = Integer.parseInt(heightField.getText());
                int newScale = (Integer) scaleSpinner.getValue();

                ImageScreen.UPSCALE_FACTOR = Math.max(1, newScale);
                ImageScreen.setImageSize(newWidth, newHeight);
                ImageScreen.redraw();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                        "Please enter valid integers.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    /**
     * turns the undo/redo buttons off and on based on whether they is something to undo or redo
     */
    private void updateButtonStates() {
        undoBtn.setEnabled(!undoStack.isEmpty());
        redoBtn.setEnabled(!redoStack.isEmpty());
    }

    public void setRemote(boolean remote) {
        if (remote) {
            generateRandom.setVisible(true);
            generateColour.setVisible(true);
            filterGrayscale.setVisible(true);
            filterSmoothSoft.setVisible(true);
            filterSmoothMedium.setVisible(true);
            filterSmoothHard.setVisible(true);
            filterInvert.setVisible(true);
            redRebalance.setVisible(true);
            greenRebalance.setVisible(true);
            blueRebalance.setVisible(true);
            btnRedOntoGreen.setVisible(true);
            btnGreenOntoBlue.setVisible(true);
            btnBlueOntoRed.setVisible(true);
            btnHueOntoSaturation.setVisible(true);
            btnSaturationOntoLightness.setVisible(true);
            btnLightnessOntoHue.setVisible(true);
            this.remote = true;
        } else {
            generateRandom.setVisible(true);
            generateColour.setVisible(true);
            filterGrayscale.setVisible(true);
            filterSmoothSoft.setVisible(true);
            filterSmoothMedium.setVisible(true);
            filterSmoothHard.setVisible(true);
            filterInvert.setVisible(true);
            redRebalance.setVisible(true);
            greenRebalance.setVisible(true);
            blueRebalance.setVisible(true);
            btnRedOntoGreen.setVisible(true);
            btnGreenOntoBlue.setVisible(true);
            btnBlueOntoRed.setVisible(true);
            btnHueOntoSaturation.setVisible(true);
            btnSaturationOntoLightness.setVisible(true);
            btnLightnessOntoHue.setVisible(true);

            this.remote = false;
        }
    }

    public boolean isRemote() {
        return remote;
    }

}
