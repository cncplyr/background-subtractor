package backgroundSubtractor;

import imageProcessing.ImageCombiner;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import fileHandling.FileHandler;

/**
 * Creates a background image by taking averages of all images.
 * 
 * @author cncplyr
 * @version 0.3
 */
public class BackgroundCreator {
	private static FileHandler fileHandler;
	
	private static ImageCombiner imageCombiner;

	private static String fileName = "image";

	/**
	 * Constructor
	 */
	public BackgroundCreator(FileHandler fh) {
		this.fileHandler = fh;
		imageCombiner = new ImageCombiner();
	}

	/**
	 * Create a background image from all the images requested.
	 * 
	 * @return The created background image as a <code>BufferedImage</code>.
	 * @throws Exception
	 *             No input images found.
	 */
	public BufferedImage createBackground() throws Exception {
		System.out.println("----------------------");
		System.out.println("Creating Background...");
		System.out.println("----------------------");
		Long startTime = System.currentTimeMillis();

		/* Setup */
		fileHandler.setInputFolder("inputbg");
		fileHandler.setOutputFolder("input");
		List<BufferedImage> images = new ArrayList<BufferedImage>();

		/* Get Images */
		System.out.print("Loading Images...");
		int noOfImages = fileHandler.getTotalImagesMatching(fileName);
		if (noOfImages < 1) {
			/* No images found in folder, try other folder */
			fileHandler.setInputFolder("input");
			noOfImages = fileHandler.getTotalImagesMatching(fileName);
		}
		if (noOfImages < 1) {
			/* No images found */
			throw new Exception("Could not find any images!");
		} else if (noOfImages < 300) {
			/* Acceptable number of images for RAM */
			images = fileHandler.loadAllImagesMatching(fileName, 1);
		} else {
			/* Take 10% of the images to save RAM */
			int tenPercent = noOfImages / 10;
			images = fileHandler.loadAllImagesMatching(fileName, tenPercent);
		}
		long loadImagesTime = System.currentTimeMillis();
		System.out.println("\tDone!\nCompleted in " + (loadImagesTime - startTime) + "ms");

		/* Combine Images */
		System.out.println("Combining Images...");
		BufferedImage backgroundImage = imageCombiner.averageMedianImages(images);
		long combineImagesTime = System.currentTimeMillis();
		System.out.println("100%\tDone!\nCompleted in " + (combineImagesTime - loadImagesTime) + "ms");

		/* Save Image and return */
		System.out.println("----------------------");
		System.out.println("Background Created in " + (combineImagesTime - startTime) + "ms");
		System.out.println("----------------------");
		fileHandler.saveImage(backgroundImage, "backgroundImage");
		return backgroundImage;
	}
}
