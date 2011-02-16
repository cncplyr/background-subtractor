package bgSubtract;

import imageProcessing.ImageSubtractor;

import java.awt.image.BufferedImage;
import java.util.List;

import fileHandling.FileHandler;

/**
 * 
 * @author cncplyr
 * @version 0.2
 * 
 */
public class BackgroundSubtractor {
	private FileHandler fileHandler;
	private ImageSubtractor imageSubtractor;
	private int blurRadius;

	public BackgroundSubtractor() {
		fileHandler = new FileHandler();
		imageSubtractor = new ImageSubtractor(1280, 720, 20);
		blurRadius = 11;
	}

	public void subtractAll() {
		System.out.println("Subtracting background: ");
		imageSubtractor.setBlurRadius(blurRadius);
		fileHandler.setInputFolder("input");
		fileHandler.setOutputFolder("output");
		List<String> imageNames = fileHandler.getAllImageNamesMatching("image");
		int counter = 0;
		long iStart;
		for (String name : imageNames) {
			iStart = System.currentTimeMillis();
			System.out.print(name);
			try {
				BufferedImage currentImage = fileHandler.loadImage(name);
				BufferedImage subtractedImage = imageSubtractor.subtractBackground(currentImage);
				fileHandler.saveImage(subtractedImage, formatFileName("outputImage", counter++));
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("\tDone! t=" + (System.currentTimeMillis() - iStart));
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

	/**
	 * Take a name and an Id number, and appends them. Adds leading zeros to the
	 * number to create a 5-digit number.
	 * 
	 * @param name
	 *            The filename to use.
	 * @param imageID
	 *            The file ID number to use.
	 * @return The complete name in the correct format.
	 */
	private static String formatFileName(String name, int imageID) {
		if (imageID > 9999) {
			name = name + Integer.toString(imageID);
		} else if (imageID > 999) {
			name = name + "0" + Integer.toString(imageID);
		} else if (imageID > 99) {
			name = name + "00" + Integer.toString(imageID);
		} else if (imageID > 9) {
			name = name + "000" + Integer.toString(imageID);
		} else {
			name = name + "0000" + Integer.toString(imageID);
		}
		return name;
	}
}
