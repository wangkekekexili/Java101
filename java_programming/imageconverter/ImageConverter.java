
import java.io.File;

import edu.duke.*;

/**
 * Write a description of Grayscale here.
 * 
 * @author kewang
 */
public class ImageConverter {
    
    public static ImageResource gray(ImageResource image) {
        int height = image.getHeight();
        int width = image.getWidth();
        ImageResource newImage = new ImageResource(width, height);
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                Pixel oldPixel = image.getPixel(col, row);
                int average = (oldPixel.getRed() + oldPixel.getGreen() + oldPixel.getBlue()) / 3;
                Pixel newPixel = newImage.getPixel(col, row);
                newPixel.setRed(average);
                newPixel.setGreen(average);
                newPixel.setBlue(average);
            }
        }
        return newImage;
    }

    public static void multipleGray() {
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            ImageResource result = gray(new ImageResource(f));
            File resultFile = new File(f.getParentFile(), "gray_" + f.getName());
            result.setFileName(resultFile.toString());
            result.save();
        }
    }
    
    public static ImageResource invert(ImageResource image) {
        int height = image.getHeight();
        int width = image.getWidth();
        ImageResource newImage = new ImageResource(width, height);
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                Pixel oldPixel = image.getPixel(col, row);
                Pixel newPixel = newImage.getPixel(col, row);
                newPixel.setRed(255 - oldPixel.getRed());
                newPixel.setGreen(255 - oldPixel.getGreen());
                newPixel.setBlue(255 - oldPixel.getBlue());
            }
        }
        return newImage;
    }
    
    public static void multipleInvert() {
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            ImageResource ir = invert(new ImageResource(f));
            File resultFile = new File(f.getParentFile(), "inverted_" + f.getName());
            ir.setFileName(resultFile.toString());
            ir.save();
        }
    }
    
    public static void testGray() {
        ImageResource ir = new ImageResource();
        ImageResource gray = gray(ir);
        gray.draw();
    }
}
