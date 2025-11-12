//package FilesOperation;
//
//import javax.swing.*;
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class ImageFileManager {
//
//    // Save image to chosen location
//    public static void saveImage(BufferedImage image) {
//        if (image == null) {
//            System.out.println("No image to save!");
//            return;
//        }
//
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle("Save Image");
//
//        int result = fileChooser.showSaveDialog(null);
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser.getSelectedFile();
//            try {
//                ImageIO.write(image, "png", new File(file.getAbsolutePath() + ".png"));
//                System.out.println("Image saved successfully!");
//            } catch (IOException e) {
//                System.out.println("Error saving image: " + e.getMessage());
//            }
//        }
//    }
//
//    // Load image from chosen file
//    public static BufferedImage loadImage() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle("Open Image");
//
//        int result = fileChooser.showOpenDialog(null);
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser.getSelectedFile();
//            try {
//                return ImageIO.read(file);
//            } catch (IOException e) {
//                System.out.println("Error loading image: " + e.getMessage());
//            }
//        }
//        return null;
//    }
//}