package bgSubtract;

import imageProcessing.ImageSubtractor;

import java.awt.image.BufferedImage;

import fileHandling.FileHandler;

/**
 * 
 * @author cncplyr
 * @version 0.1
 * 
 */
public class BackgroundSubtractor {
	private FileHandler fileHandler;
	private ImageSubtractor imageSubtractor;

	public BackgroundSubtractor() {
		fileHandler = new FileHandler();
		imageSubtractor = new ImageSubtractor(1280, 720, 10);
	}

	public void subtractAll() {
		// TODO: Implement this
//		System.out.println("Subtracting background: ");
//		fileHandler.setInputFolder("input");
//		fileHandler.setOutputFolder("output");
//		String[] imageNames = fileHandler.loadAllFileNamesMatching(fileName);
//		for (String name : imageNames) {
//			System.out.print(name);
//			try {
//				BufferedImage currentImage = fileHandler.loadImage(name);
//				BufferedImage subtractedImage = imageSubtractor.subtractBackground(currentImage);
//				fileHandler.saveImage(subtractedImage, name);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			System.out.println("\tDone!");
//		}
	}

	public void setBackgroundImage(BufferedImage inputImage) {
		if (inputImage == null) {
			throw new IllegalArgumentException("Cannot set background image to null!");
		} else {
			imageSubtractor.setBackgroundImage(inputImage);
		}
	}
}
