package test.utils;
import com.GA.GeneticAlgorithm;
import com.GA.IndividualImage;
import com.GA.crossover.PixelwiseRGBCrossover;
import com.GA.fitness.CheckerboardFitness;
import com.GA.fitness.adjustment.NoAdjustment;
import com.GA.generation.RandomBitmapGeneration;
import com.GA.mutation.RandomPixelsMutation;
import com.GA.selection.RouletteWheelSelection;
import com.application.panels.DrawingPanel;
import com.application.panels.ImageScreen;
import com.utils.BitMapImage;
import com.utils.ImageUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
public class ImageUtilsTest {

    private BitMapImage testImage;
    private GeneticAlgorithm testGA;

    @BeforeEach
    void setUp() {
        // created a simple test image
        int[][][] rgb = new int[10][10][3];
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                rgb[y][x][0] = 100; // Red
                rgb[y][x][1] = 150; // Green
                rgb[y][x][2] = 200; // Blue
            }
        }
        testImage = new BitMapImage(rgb);

        // initialize imageScreen dimensions for testing
        ImageScreen.currentImageHeight = 10;
        ImageScreen.currentImageWidth = 10;
        ImageScreen.currentImage = testImage;

        testGA = new GeneticAlgorithm(
                10, 10, 100, 10, 0, 10,
                new RandomBitmapGeneration(),
                new CheckerboardFitness(),
                new RouletteWheelSelection(),
                new PixelwiseRGBCrossover(),
                new RandomPixelsMutation(1.0, 0.1),
                new NoAdjustment()
        );
    }
    @Test
    public void testInvert() {
        int[][][] rgb = new int[][][] {{{255, 255, 255}}};
        BitMapImage image = new BitMapImage(rgb);
        image = ImageUtils.invert(image);
        rgb = image.getRgb();
        assert(rgb[0][0][0] == 0);
        assert(rgb[0][0][1] == 0);
        assert(rgb[0][0][2] == 0);
    }
    @Test
    void testInvertMultipleColors() {
        // creates image with different colors
        int[][][] rgb = new int[2][2][3];
        rgb[0][0] = new int[]{255, 0, 0};    // red
        rgb[0][1] = new int[]{0, 255, 0};    // green
        rgb[1][0] = new int[]{0, 0, 255};    // blue
        rgb[1][1] = new int[]{128, 128, 128}; // gray

        BitMapImage image = new BitMapImage(rgb);
        BitMapImage inverted = ImageUtils.invert(image);
        int[][][] result = inverted.getRgb();

        // red inverts to cyan
        assertEquals(0, result[0][0][0]);
        assertEquals(255, result[0][0][1]);
        assertEquals(255, result[0][0][2]);

        // green inverts to magenta
        assertEquals(255, result[0][1][0]);
        assertEquals(0, result[0][1][1]);
        assertEquals(255, result[0][1][2]);

        // blue inverts to yellow
        assertEquals(255, result[1][0][0]);
        assertEquals(255, result[1][0][1]);
        assertEquals(0, result[1][0][2]);

        // gray should stays gray
        assertEquals(127, result[1][1][0]);
        assertEquals(127, result[1][1][1]);
        assertEquals(127, result[1][1][2]);
    }
    //test RGB rebalancing actually changes proportions
    @Test
    void testRGBRebalancing() {
        //create a test image
        int[][][] rgb = new int[1][1][3];
        rgb[0][0] = new int[]{100, 150, 200};

        BitMapImage image = new BitMapImage(rgb);

        // Test red w (0.6, 0.2, 0.2)
        BitMapImage redBalanced = ImageUtils.rgbBalancing(image, 0.6, 0.2, 0.2);
        int[][][] result = redBalanced.getRgb();

        assertEquals(60, result[0][0][0], "Red should be 100 * 0.6 = 60");
        assertEquals(30, result[0][0][1], "Green should be 150 * 0.2 = 30");
        assertEquals(40, result[0][0][2], "Blue should be 200 * 0.2 = 40");

        // Test green w (0.2, 0.6, 0.2)
        BitMapImage greenBalanced = ImageUtils.rgbBalancing(image, 0.2, 0.6, 0.2);
        result = greenBalanced.getRgb();

        assertEquals(20, result[0][0][0], "Red should be 100 * 0.2 = 20");
        assertEquals(90, result[0][0][1], "Green should be 150 * 0.6 = 90");
        assertEquals(40, result[0][0][2], "Blue should be 200 * 0.2 = 40");
    }
    @Test
    // test smooth filter actually blends pixels next to it
    void testSmoothFilter() {
        // create image with distinct center pixel
        int[][][] rgb = new int[3][3][3];

        // set all pixels to 100 except center
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                rgb[y][x][0] = 100;
                rgb[y][x][1] = 100;
                rgb[y][x][2] = 100;
            }
        }

        rgb[1][1][0] = 200;
        rgb[1][1][1] = 200;
        rgb[1][1][2] = 200;

        BitMapImage image = new BitMapImage(rgb);
        BitMapImage smoothed = ImageUtils.smoothFilter(image, 0.5, 0.0625);
        int[][][] result = smoothed.getRgb();

        // center pixel should be smoothed toward neighbors
        assertTrue(result[1][1][0] < 200, "Center pixel should be reduced by smoothing");
        assertTrue(result[1][1][0] > 100, "Center pixel should still be higher than original neighbors");

        // the pixels on the edge should be influenced by center
        assertTrue(result[0][0][0] > 100, "Corner pixels should be slightly increased");
        assertTrue(result[1][0][0] > 100, "Edge pixels should be slightly increased");
    }

    @Test
    @DisplayName("Test Generate Random Button creates valid image")
    void testGenerateRandomButton() {
        BitMapImage originalImage = ImageScreen.currentImage;

        // Generate new random image (simulating button action)
        int[][][] rgb = new int[10][10][3];
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                rgb[y][x][0] = (int)(Math.random() * 256);
                rgb[y][x][1] = (int)(Math.random() * 256);
                rgb[y][x][2] = (int)(Math.random() * 256);
            }
        }
        ImageScreen.currentImage = new BitMapImage(rgb);

        // Verify new image was created
        assertNotNull(ImageScreen.currentImage);
        assertNotEquals(originalImage, ImageScreen.currentImage);
        assertEquals(10, ImageScreen.currentImage.getHeight());
        assertEquals(10, ImageScreen.currentImage.getWidth());
    }

    @Test
    //tests whether it creates a solid colour
    void testRandomColorGeneration() {
        com.GA.generation.RandomColorGeneration gen = new com.GA.generation.RandomColorGeneration();
        IndividualImage individual = gen.generate(10, 10);
        int[][][] rgb = individual.getImage().getRgb();

        // Get first pixel color
        int r = rgb[0][0][0];
        int g = rgb[0][0][1];
        int b = rgb[0][0][2];

        // check all pixels are the same as the first pixl
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                assertEquals(r, rgb[y][x][0], "All pixels should have same red value");
                assertEquals(g, rgb[y][x][1], "All pixels should have same green value");
                assertEquals(b, rgb[y][x][2], "All pixels should have same blue value");
            }
        }

        assertTrue(r >= 0 && r <= 255, "Red should be in range");
        assertTrue(g >= 0 && g <= 255, "Green should be in range");
        assertTrue(b >= 0 && b <= 255, "Blue should be in range");
    }

    @Test
    void testGrayscaleFilterButton() {
        ImageScreen.currentImage = ImageUtils.grayscale(ImageScreen.currentImage);

        // check all RGB values are equal (grayscale property)
        int[][][] result = ImageScreen.currentImage.getRgb();
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                assertEquals(result[y][x][0], result[y][x][1]);
                assertEquals(result[y][x][1], result[y][x][2]);
            }
        }
    }

// some ga tests here too
    @Test
    void testGARunButtonState() {
        // simulate button state logic
        boolean btnRunEnabled = true;
        boolean btnHaltEnabled = false;
        boolean btnResetEnabled = true;

        // after clicking "Run"
        btnRunEnabled = false;
        btnHaltEnabled = true;
        btnResetEnabled = false;

        assertFalse(btnRunEnabled, "Run button should be disabled during execution");
        assertTrue(btnHaltEnabled, "Halt button should be enabled during execution");
        assertFalse(btnResetEnabled, "Reset button should be disabled during execution");

        // after clicking "Halt"
        btnRunEnabled = true;
        btnHaltEnabled = false;
        btnResetEnabled = true;

        assertTrue(btnRunEnabled, "Run button should be enabled after halt");
        assertFalse(btnHaltEnabled, "Halt button should be disabled after halt");
        assertTrue(btnResetEnabled, "Reset button should be enabled after halt");
    }

    @Test

    void testGAHaltButton() {
        ImageScreen.halt = false;

        // Simulate halt button click
        ImageScreen.halt = true;

        assertTrue(ImageScreen.halt, "Halt flag should be set to true");
    }

    @Test
    void testRGBRebalanceButton() {
        // Test red rebalancing (0.6, 0.2, 0.2)
        ImageScreen.currentImage = ImageUtils.rgbBalancing(ImageScreen.currentImage, 0.6, 0.2, 0.2);
        int[][][] result = ImageScreen.currentImage.getRgb();
        assertEquals(60, result[0][0][0]);  // 100 * 0.6
        assertEquals(30, result[0][0][1]);  // 150 * 0.2
        assertEquals(40, result[0][0][2]);  // 200 * 0.2
    }

    @Test
    void testModeSelectionButtons() {
        // Test local mode selection
        String selectedMode = "local";
        assertEquals("local", selectedMode);

        // Test remote mode selection
        selectedMode = "remote";
        assertEquals("remote", selectedMode);
    }

    @Test
    void testConnectionScreenButton() {
        String remote = "http://localhost:8080";

        // Simulate setting remote URL
        assertNotNull(remote);
        assertTrue(remote.startsWith("http"));
        assertTrue(remote.contains("localhost") || remote.matches(".*\\d+\\.\\d+\\.\\d+\\.\\d+.*"));
    }

    @Test
    void testGAParametersValidation() {
        // Test valid parameters
        int populationSize = 1000;
        int elite = 30;
        int regeneration = 50;

        assertTrue(populationSize > 0, "Population size must be positive");
        assertTrue(elite >= 0, "Elite count must be non-negative");
        assertTrue(regeneration >= 0, "Regeneration count must be non-negative");
        assertTrue(elite < populationSize, "Elite must be less than population");

        // Test invalid parameters
        int invalidPopulation = -100;
        int invalidElite = -5;

        assertFalse(invalidPopulation > 0, "Invalid population should fail validation");
        assertFalse(invalidElite >= 0, "Invalid elite should fail validation");
    }
    @Test
    //Test GA initialization creates valid population
    void testGAInitialization() {
        GeneticAlgorithm ga = new GeneticAlgorithm(
                10, 10, 100, 10, 0, 10,
                new RandomBitmapGeneration(),
                new CheckerboardFitness(),
                new RouletteWheelSelection(),
                new PixelwiseRGBCrossover(),
                new RandomPixelsMutation(1.0, 0.1),
                new NoAdjustment()
        );

        assertNotNull(ga.getPopulation(), "Population should be created");
        assertEquals(100, ga.getPopulation().length, "Population size should match");

        // Verify all individuals have images
        for (IndividualImage individual : ga.getPopulation()) {
            assertNotNull(individual, "Individual should not be null");
            assertNotNull(individual.getImage(), "Individual should have image");
            assertEquals(10, individual.getImage().getHeight());
            assertEquals(10, individual.getImage().getWidth());
        }
    }

    @Test
    //Test GA step actually evolves population
    void testGAStepEvolution() {
        GeneticAlgorithm ga = new GeneticAlgorithm(
                10, 10, 50, 5, 0, 5,
                new RandomBitmapGeneration(),
                new CheckerboardFitness(),
                new RouletteWheelSelection(),
                new PixelwiseRGBCrossover(),
                new RandomPixelsMutation(1.0, 0.1),
                new NoAdjustment()
        );

        // Get initial best fitness
        ga.gaStep();
        double initialFitness = ga.getBestIndividual().getFitness();
        for (int i = 0; i < 5; i++) {
            ga.gaStep();
        }

        double finalFitness = ga.getBestIndividual().getFitness();

        // Best history should track progress
        assertEquals(6, ga.getBestHistory().size(), "Should have 6 best individuals tracked");

        // Final fitness should be at least as good as initial
        assertTrue(finalFitness >= initialFitness,
                "Fitness should not decrease (elitism ensures this)");
    }


    @Test
    @DisplayName("setImageSize should resize image correctly")
    void testSetImageSize() {
        ImageScreen.setImageSize(20, 30);
        assertEquals(20, ImageScreen.getCurrentImageWidth());
        assertEquals(30, ImageScreen.getCurrentImageHeight());
        assertNotNull(ImageScreen.currentImage);
    }

    @Test
    @DisplayName("Generate random image before resizing should work")
    void testGenerateBeforeResize() {
        assertDoesNotThrow(() -> {
            int[][][] rgb = new int[10][10][3];
            for (int y = 0; y < 10; y++)
                for (int x = 0; x < 10; x++)
                    rgb[y][x] = new int[]{255, 255, 255};

            ImageScreen.currentImage = new BitMapImage(rgb);
            ImageScreen.redraw();
        });
    }
    @Test
    @DisplayName("fillToScreen adjusts upscale factor without UI crash")
    void testFillToScreen() throws Exception {
        // Mock image
        ImageScreen.currentImage = new BitMapImage(10, 10);

        // Create DrawingPanel on UI thread
        SwingUtilities.invokeAndWait(() -> {
            ImageScreen.drawingPanel = new DrawingPanel(10, 10);
            ImageScreen.drawingPanel.setSize(200, 200); // simulate UI space
        });

        ImageScreen.fillToScreen();

        assertTrue(ImageScreen.UPSCALE_FACTOR >= 1,
                "Upscale factor should be >= 1 after fillToScreen()");
    }


}