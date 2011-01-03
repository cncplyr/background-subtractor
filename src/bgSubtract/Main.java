package bgSubtract;

import imageProcessing.ImageSubtractor;

import java.awt.image.BufferedImage;

import fileHandling.FileHandler;

public class Main {
	private static FileHandler fileHandler;
	private static BackgroundCreator backgroundCreator;
	private static ImageSubtractor imageSubtractor;
	private static String fileName = "image";

	public static void main(String[] args) {
		backgroundCreator = new BackgroundCreator();
		fileHandler = new FileHandler();
		imageSubtractor = new ImageSubtractor(1280, 720, 10);

		try {
			BufferedImage background = backgroundCreator.createBackground();
			imageSubtractor.setBackgroundImage(background);
		} catch (Exception e) {
			e.printStackTrace();
		}

		fileHandler.setInputFolder("input");
		fileHandler.setOutputFolder("outputTEST");
		String[] imageNames = fileHandler.loadAllFileNamesMatching(fileName);
		for (String name : imageNames) {
			try {
				BufferedImage currentImage = fileHandler.loadImage(name);
				
				
				
				BufferedImage subtractedImage = imageSubtractor.subtractBackground(currentImage);
				fileHandler.saveImage(subtractedImage, name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
