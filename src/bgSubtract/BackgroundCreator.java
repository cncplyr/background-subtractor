package bgSubtract;

import imageProcessing.ImageCombiner;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import fileHandling.FileHandler;

/**
 * Creates a background image by taking averages of all images.
 * 
 * @author cncplyr
 * @version 0.002
 */
public class BackgroundCreator {
	private static FileHandler fileHandler;
	private static ImageCombiner imageCombiner;

	private static String fileName = "image";
	private static int blurRadius = 0;
	private static BufferedImage bgImage;

	/**
	 * Constructor
	 * 
	 */
	public BackgroundCreator() {
		fileHandler = new FileHandler();
		imageCombiner = new ImageCombiner();
	}

	/**
	 * Create a background image from all the images requested.
	 * 
	 * @return
	 * @throws Exception
	 */
	public BufferedImage createBackground() throws Exception {
		System.out.println("Creating Background...");
		Long startTime = System.nanoTime();

		BufferedImage returnImage = imageCombiner.averageMedianImages(fileHandler.getAllImagesMatching(fileName));

		// String[] imageNames = fileHandler.getAllFileNamesMatching(fileName);
		//
		// if (imageNames.length < 1) {
		// throw new Exception("Could not find any images!");
		// }
		//
		// BufferedImage returnImage =
		// imageCombiner.averageMedianImagesByName(imageNames);

		System.out.println("Created! Time Taken: " + (System.nanoTime() - startTime));
		return returnImage;
	}
}
