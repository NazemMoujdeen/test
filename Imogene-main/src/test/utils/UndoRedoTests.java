package test.utils; // Or test.com.application.panels

import com.utils.BitMapImage;
import com.application.panels.ImageScreen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Stack;
import static org.junit.jupiter.api.Assertions.*;

public class UndoRedoTests {

    // stacks just like in the leftsidebar
    private Stack<BitMapImage> undoStack;
    private Stack<BitMapImage> redoStack;
    private BitMapImage currentImage;

    // test images
    private BitMapImage imageStateA;
    private BitMapImage imageStateB;
    private BitMapImage imageStateC;

    /**
     * same helper method from leftsidebar
     */
    private BitMapImage imageCopy(BitMapImage image) {
        if (image == null) return null;
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

    // helper to check if two images are the same
    private boolean areImagesEqual(BitMapImage img1, BitMapImage img2) {
        assertNotNull(img1, "Image 1 should not be null");
        assertNotNull(img2, "Image 2 should not be null");
        assertEquals(img1.getHeight(), img2.getHeight(), "Image heights must match");
        assertEquals(img1.getWidth(), img2.getWidth(), "Image widths must match");

        int[][][] rgb1 = img1.getRgb();
        int[][][] rgb2 = img2.getRgb();

        for(int y = 0; y < img1.getHeight(); y++) {
            for(int x = 0; x < img1.getWidth(); x++) {
                assertArrayEquals(rgb1[y][x], rgb2[y][x], "Pixel at [" + y + "][" + x + "] should match");
            }
        }
        return true;
    }


    @BeforeEach
    void setUp() {
        // initialize the stacks and images for each test
        undoStack = new Stack<>();
        redoStack = new Stack<>();

        imageStateA = new BitMapImage(2, 2);
        imageStateA.setPixel(0, 0, 255, 255, 255); // White

        imageStateB = new BitMapImage(2, 2);
        imageStateB.setPixel(0, 0, 100, 100, 100); // Gray

        imageStateC = new BitMapImage(2, 2);
        imageStateC.setPixel(0, 0, 0, 0, 0);       // Black

        // sets the initial screen image
        currentImage = imageStateA;
        ImageScreen.currentImage = currentImage;
    }

    //tests that the stacks are empty when they start
    @Test
    void testInitialState() {
        assertTrue(undoStack.isEmpty(), "Undo stack should be empty at start");
        assertTrue(redoStack.isEmpty(), "Redo stack should be empty at start");
    }

    @Test
    //tests the logic for applying one filter
    void testFilterActionLogic() {
        // pretends the filter button got pressed
        redoStack.clear();
        undoStack.push(imageCopy(currentImage));
        currentImage = imageStateB;

        assertEquals(1, undoStack.size(), "Undo stack should have 1 item");
        assertTrue(redoStack.isEmpty(), "Redo stack should be empty");

        // verify the correct image was pushed to the undo stack
        assertTrue(areImagesEqual(imageStateA, undoStack.peek()), "Undo stack should contain original image A");
    }

    @Test
    //Test logic for Undo button
    void testUndoActionLogic() {
        redoStack.clear();
        undoStack.push(imageCopy(currentImage));
        currentImage = imageStateB;

        BitMapImage redoImage = null;
        if (!undoStack.isEmpty()) {
            redoImage = imageCopy(currentImage);
            currentImage = undoStack.pop();
        }
        if (redoImage != null) {
            redoStack.push(redoImage);
        }

        assertTrue(undoStack.isEmpty(), "Undo stack should be empty");
        assertEquals(1, redoStack.size(), "Redo stack should have 1 item");
        assertTrue(areImagesEqual(imageStateA, currentImage), "Current image should be restored to State A");
        assertTrue(areImagesEqual(imageStateB, redoStack.peek()), "Redo stack should contain State B");
    }

    @Test
    //Test logic for Redo button
    void testRedoActionLogic() {

        redoStack.clear();
        undoStack.push(imageCopy(currentImage));
        currentImage = imageStateB;

        redoStack.push(imageCopy(currentImage));
        currentImage = undoStack.pop();

        BitMapImage undoImage = null;
        if (!redoStack.isEmpty()) {
            undoImage = imageCopy(currentImage);
            currentImage = redoStack.pop();
        }
        if (undoImage != null) {
            undoStack.push(undoImage);
        }

        assertEquals(1, undoStack.size(), "Undo stack should have 1 item");
        assertTrue(redoStack.isEmpty(), "Redo stack should be empty");
        assertTrue(areImagesEqual(imageStateB, currentImage), "Current image should be restored to State B");
        assertTrue(areImagesEqual(imageStateA, undoStack.peek()), "Undo stack should contain State A");
    }

    @Test
    //test if doing something after and undo should clear redo stack
    void testNewActionClearsRedo() {
        redoStack.clear();
        undoStack.push(imageCopy(currentImage));
        currentImage = imageStateB;

        redoStack.clear();
        undoStack.push(imageCopy(currentImage));
        currentImage = imageStateC;


        redoStack.push(imageCopy(currentImage));
        currentImage = undoStack.pop();

        redoStack.clear();
        undoStack.push(imageCopy(currentImage));
        currentImage = imageStateA;

        assertEquals(2, undoStack.size(), "Undo stack should have 2 items A, B");
        assertTrue(redoStack.isEmpty(), "Redo stack should be cleared by the new action");
        assertTrue(areImagesEqual(imageStateA, currentImage), "Current image should be the new state A");
        assertTrue(areImagesEqual(imageStateB, undoStack.peek()), "Top of undo stack should be State B");
    }
}