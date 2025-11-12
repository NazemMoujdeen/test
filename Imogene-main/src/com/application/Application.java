package com.application;

import com.GA.GeneticAlgorithm;
import com.GA.IndividualImage;
import com.application.panels.ApplicationWindow;
import com.application.panels.ImageScreen;
import com.utils.ImageRW;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

public class Application {

    public static Random rng = new Random();

    public static void main(String[] args) {

        // Set up fonts to be used in the GUI
        FontManager.configureFonts();

        // Create and display the application window
        ApplicationWindow window = ApplicationWindow.getInstance();
        window.setVisible(true);

    }



}
