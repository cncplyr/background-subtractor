package bgSubtract;

import java.awt.image.BufferedImage;

/**
 * This is the main entry point to the program.
 * 
 * @author cncplyr
 * @version 0.3
 * 
 */
public class Main {
	private static BackgroundCreator backgroundCreator;
	private static BackgroundSubtractor backgroundSubtractor;

	public static void main(String[] args) {
		backgroundCreator = new BackgroundCreator();
		backgroundSubtractor = new BackgroundSubtractor();

		createBackground();
		subtractBackground();
		
		System.exit(0);
	}

	/**
	 * Creates the background.
	 */
	public static void createBackground() {
		BufferedImage background;
		try {
			background = backgroundCreator.createBackground();
			backgroundSubtractor.setBackgroundImage(background);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Subtracts background from each image.
	 */
	public static void subtractBackground() {
		backgroundSubtractor.subtractAll();
	}


}
