package bgSubtract;

import imageProcessing.ImageBlurrer;
import imageProcessing.ImageSubtractor;

import java.awt.image.BufferedImage;

import fileHandling.FileHandler;

/**
 * This is the main entry point to the program.
 * 
 * @author Ric
 * 
 */
public class Main {
	private static FileHandler fileHandler;
	private static BackgroundCreator backgroundCreator;
	private static ImageBlurrer imageBlur;
	private static ImageSubtractor imageSubtractor;
	private static String fileName = "image";

	public static void main(String[] args) {
		backgroundCreator = new BackgroundCreator();
		fileHandler = new FileHandler();
		imageBlur = new ImageBlurrer();
		imageSubtractor = new ImageSubtractor(1280, 720, 10);

		// /* Blur Test */
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
			imageSubtractor.setBackgroundImage(background);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Subtract background from each image */
		// System.out.println("Subtracting background: ");
		// fileHandler.setInputFolder("input");
		// fileHandler.setOutputFolder("outputTEST");
		// String[] imageNames = fileHandler.loadAllFileNamesMatching(fileName);
		// for (String name : imageNames) {
		// System.out.print(name);
		// try {
		// BufferedImage currentImage = fileHandler.loadImage(name);
		// BufferedImage subtractedImage =
		// imageSubtractor.subtractBackground(currentImage);
		// fileHandler.saveImage(subtractedImage, name);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// System.out.println("\tDone!");
		// }
		System.exit(0);
	}
}
