package bgSubtract;

import java.awt.image.BufferedImage;

import fileHandling.FileHandler;

public class Main {
	private static BackgroundCreator backgroundCreator;
	private static FileHandler fileHandler;

	public static void main(String[] args) {
		backgroundCreator = new BackgroundCreator();
		fileHandler = new FileHandler();

		try {
			BufferedImage test = backgroundCreator.createBackground();
			fileHandler.saveImage(test, "backgroundTest");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
