package bgSubtract;

import imageProcessing.ImageBlurrer;
import imageProcessing.ImageSubtractor;

import java.awt.image.BufferedImage;

import fileHandling.FileHandler;

/**
 * This is the main entry point to the program.
 * 
 * @author cncplyr
 * 
 */
public class Main {
	private static BackgroundCreator backgroundCreator;
	private static BackgroundSubtractor backgroundSubtractor;

	public static void main(String[] args) {
		backgroundCreator = new BackgroundCreator();
		backgroundSubtractor = new BackgroundSubtractor();

		// /* Blur Test */
		// imageBlur = new ImageBlurrer();
		// fileHandler.setInputFolder("input");
		// fileHandler.setOutputFolder("output");
		// try {
		// long startTime = System.currentTimeMillis();
		// BufferedImage testImage = fileHandler.loadImage("image00038.png");
		// long midTime = System.currentTimeMillis();
		// System.out.println("Loaded image in " + (midTime - startTime) +
		// "ms");
		// fileHandler.saveImage(testImage, "blurOutput");
		// long endTime = System.currentTimeMillis();
		// System.out.println("Saved image in " + (endTime - midTime) + "ms");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }


		/* Create Background */
		try {
			BufferedImage background = backgroundCreator.createBackground();
			backgroundSubtractor.setBackgroundImage(background);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Subtract background from each image */
		backgroundSubtractor.subtractAll();

		System.exit(0);
	}
}
