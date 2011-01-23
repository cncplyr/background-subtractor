package bgSubtract;

import imageProcessing.BoundingBoxer;
import imageProcessing.ImageSubtractor;

import java.awt.image.BufferedImage;
import java.util.List;

import fileHandling.FileHandler;

/**
 * 
 * @author cncplyr
 * @version 0.1
 * 
 */
public class BackgroundSubtractor {
	private FileHandler fileHandler;
	private BoundingBoxer boundingBoxer;
	private ImageSubtractor imageSubtractor;
	private int blurRadius;

	public BackgroundSubtractor() {
		fileHandler = new FileHandler();
		boundingBoxer = new BoundingBoxer();
		imageSubtractor = new ImageSubtractor(1280, 720, 20);
		blurRadius = 11;
	}

	public void subtractAll() {
		System.out.println("Subtracting background: ");
		imageSubtractor.setBlurRadius(blurRadius);
		fileHandler.setInputFolder("input");
		fileHandler.setOutputFolder("output");
		List<String> imageNames = fileHandler.getAllImageNamesMatching("image");
		for (String name : imageNames) {
			System.out.print(name);
			try {
				BufferedImage currentImage = fileHandler.loadImage(name);
				BufferedImage subtractedImage = imageSubtractor.subtractBackground(currentImage);
				fileHandler.saveImage(boundingBoxer.createBoundingBox(subtractedImage), name);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("\tDone!");
		}
	}

	public void setBackgroundImage(BufferedImage inputImage) {
		if (inputImage == null) {
			throw new IllegalArgumentException("Cannot set background image to null!");
		} else {
			imageSubtractor.setBackgroundImage(inputImage);
		}
	}

	public void setBlurRadius(int radius) {
		blurRadius = radius;
	}

	public int getBlurRadius() {
		return blurRadius;
	}
}
