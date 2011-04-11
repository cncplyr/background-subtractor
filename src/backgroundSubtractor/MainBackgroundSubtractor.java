package backgroundSubtractor;

import java.awt.image.BufferedImage;

/**
 * This is the main entry point to the program.
 * 
 * @author cncplyr
 * @version 0.3
 * 
 */
public class MainBackgroundSubtractor {
	private static BackgroundCreator backgroundCreator;
	private static BackgroundSubtractor backgroundSubtractor;
	private static P2ImageCreator p2gen;

	public static void main(String[] args) {
		backgroundCreator = new BackgroundCreator();
		backgroundSubtractor = new BackgroundSubtractor();
		// p2gen = new P2ImageCreator();

		createBackground();
		subtractBackground();
		generateP2();

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

	public static void generateP2() {
		p2gen.generateImages();
	}

	/**
	 * Set up the GUI.
	 */
	public static void createGUI() {

	}
}
