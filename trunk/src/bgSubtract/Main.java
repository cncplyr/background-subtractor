package bgSubtract;

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
	
	public static void main(String[] args) {
		backgroundCreator = new BackgroundCreator();
		backgroundSubtractor = new BackgroundSubtractor();
		new BoundingBoxer();

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
