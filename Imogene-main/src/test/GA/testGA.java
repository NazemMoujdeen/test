package test.GA;

import com.GA.GeneticAlgorithm;
import com.GA.crossover.PixelwiseRGBCrossover;
import com.GA.fitness.CheckerboardFitness;
import com.GA.fitness.adjustment.NoAdjustment;
import com.GA.generation.RandomBitmapGeneration;
import com.GA.generation.RandomColorGeneration;
import com.GA.mutation.RandomPixelsMutation;
import com.GA.selection.RouletteWheelSelection;
import com.API.GAConnector;
import com.application.panels.ConnectionScreen;
import com.utils.BitMapImage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class testGA {
    
    @Test
    public void testLocalGA() {
        // this is for ga local
        int height = 10;
        int width = 10;
        int populationSize = 20;
        int elite = 5;
        int regeneration = 3;
        
        GeneticAlgorithm ga = new GeneticAlgorithm(
            height,
            width,
            populationSize,
            elite,
            0,
            regeneration,
            new RandomBitmapGeneration(),
            new CheckerboardFitness(),
            new RouletteWheelSelection(),
            new PixelwiseRGBCrossover(),
            new RandomPixelsMutation(1.0, 0.1),
            new NoAdjustment()
        );
        for (int i = 0; i < 5; i++) {
            ga.gaStep();
        }
        BitMapImage result = ga.getBestIndividual().getImage();
        assertNotNull(result);
        assertEquals(height, result.getRgb().length);
        assertEquals(width, result.getRgb()[0].length);
        assertEquals(3, result.getRgb()[0][0].length);
    }
    
    @Test
    public void testRemoteGA() {
        // this is for ga remote
        try {
            BitMapImage result = GAConnector.requestGA(
                10,
                10,
                20, 
                5, 
                3, 
                5,
                "Random Pixels",
                "Checkerboard",
                "Roulette Wheel",
                "Pixelwise RGB",
                "Random Pixels Randomization",
                ""
            );
            
            assertNotNull(result);
            assertEquals(10, result.getRgb().length);
            assertEquals(10, result.getRgb()[0].length);
            assertEquals(3, result.getRgb()[0][0].length);
            
        } catch (Exception e) {
            System.out.println("backend is not running");
        }
    }

        @Test
    public void testRandomColorGeneration() {
        int height = 10;
        int width = 10;
        int populationSize = 20;
        int elite = 5;
        int regeneration = 3;
        
        GeneticAlgorithm ga = new GeneticAlgorithm(
            height,
            width,
            populationSize,
            elite,
            0,
            regeneration,
            new RandomColorGeneration(),
            new CheckerboardFitness(),
            new RouletteWheelSelection(),
            new PixelwiseRGBCrossover(),
            new RandomPixelsMutation(1.0, 0.1),
            new NoAdjustment()
        );
        
        for (int i = 0; i < populationSize; i++) {
            BitMapImage image = ga.getPopulation()[i].getImage();
            assertNotNull(image);
            assertEquals(height, image.getHeight());
            assertEquals(width, image.getWidth()); 
            int[][][] rgb = image.getRgb();
            int r = rgb[0][0][0];
            int g = rgb[0][0][1];
            int b = rgb[0][0][2];
            
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    assertEquals(r, rgb[y][x][0], "All pixels should have same red value");
                    assertEquals(g, rgb[y][x][1], "All pixels should have same green value");
                    assertEquals(b, rgb[y][x][2], "All pixels should have same blue value");
                }
            }
        }
    }

}