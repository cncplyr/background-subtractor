package bgSubtract;

import fileHandling.FileHandler;
import imageProcessing.BoundingBoxer;

import java.awt.image.BufferedImage;

/**
 * This is the main entry point to the program.
 * 
 * @author cncplyr
 * 
 */
public class Main {
	private static BackgroundCreator backgroundCreator;
	private static BackgroundSubtractor backgroundSubtractor;
	private static BoundingBoxer boundingBoxer;

	public static void main(String[] args) {
		backgroundCreator = new BackgroundCreator();
		backgroundSubtractor = new BackgroundSubtractor();
		boundingBoxer = new BoundingBoxer();

		/* Create Background */
		try {
			BufferedImage background = backgroundCreator.createBackground();
			backgroundSubtractor.setBackgroundImage(background);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Subtract background from each image */
		backgroundSubtractor.subtractAll();


		// /* Bounding Box Test */
		// long time = System.currentTimeMillis();
		// FileHandler fh = new FileHandler();
		// fh.setInputFolder("output");
		// fh.setOutputFolder("output");
		// BufferedImage someImage = fh.loadImage("output00000.png");
		// int[] testArray = boundingBoxer.getBoundingBox(someImage);
		// fh.saveImage(boundingBoxer.drawBoundingBox(someImage, testArray),
		// "boundingBoxText.png");
		//
		// System.out.println("Bounding Box: ");
		// System.out.println("Start: (" + testArray[0] + ", " + testArray[1] +
		// ")");
		// System.out.println("End:   (" + testArray[2] + ", " + testArray[3] +
		// ")");
		// System.out.println("Time Taken: " + (System.currentTimeMillis() -
		// time) + "ms");


		System.exit(0);
	}
}
