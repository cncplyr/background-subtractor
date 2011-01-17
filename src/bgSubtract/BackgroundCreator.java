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
 * @version 0.004
 */
public class BackgroundCreator {
	private static FileHandler fileHandler;
	private static ImageCombiner imageCombiner;

	private static String fileName = "image";

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

		// /* Divide into any number of subdivisions */
		// BufferedImage tmp = fileHandler.loadImage(imageNames[0]);
		// List<BufferedImage> tmpImageParts = new ArrayList<BufferedImage>();
		// int parts = 10;
		// int counter = 0;
		//
		// System.out.println("Splitting Images:");
		// fileHandler.setOutputFolder("BgImageSplits" + parts);
		// for (String imageName : imageNames) {
		// System.out.print(imageName);
		// counter = 0;
		// tmp = fileHandler.loadImage(imageName);
		// tmpImageParts = imageCombiner.splitImage(tmp, parts);
		// for (BufferedImage eachPart : tmpImageParts) {
		// fileHandler.saveImage(eachPart, counter + imageName);
		// counter++;
		// }
		// System.out.println("\tDone!");
		// }
		// fileHandler.setInputFolder("BgImageSplits" + parts);
		// System.out.println("Completed!");
		//
		// System.out.println("Averaging Images");
		// for (int i = 0; i < parts; i++) {
		// System.out.print("Part " + i);
		// fileHandler.saveImage(imageCombiner.averageMedianImages(fileHandler.getAllImagesMatching(i
		// + fileName)), "bgImage" + i);
		// System.out.println("\tDone!");
		// }
		// fileHandler.setOutputFolder("outputv1");
		//
		// System.out.println("Recombining Images:");
		// BufferedImage returnImage =
		// imageCombiner.combineImages(fileHandler.getAllImagesMatching("bgImage"));
		// /* END Divide into any number of subdivisions */




		// /* Divide into quarters */
		// BufferedImage tmp = fileHandler.loadImage(imageNames[0]);
		// int width = tmp.getWidth() / 2;
		// int height = tmp.getHeight() / 2;
		// BufferedImage tmpQuarter = new BufferedImage(width, height,
		// BufferedImage.TYPE_INT_ARGB);
		//
		// fileHandler.setOutputFolder("inputBg");
		//
		// System.out.println("Splitting Images:");
		// for (String imageName : imageNames) {
		// System.out.print(imageName);
		// tmp = fileHandler.loadImage(imageName);
		// /* Top Left */
		// System.out.print("\tq0");
		// tmpQuarter.setRGB(0, 0, width, height, tmp.getRGB(0, 0, width,
		// height, null, 0, width), 0, width);
		// fileHandler.saveImage(tmpQuarter, "tl" + imageName);
		// /* Top Right */
		// System.out.print("\tq1");
		// tmpQuarter.setRGB(0, 0, width, height, tmp.getRGB(width, 0, width,
		// height, null, 0, width), 0, width);
		// fileHandler.saveImage(tmpQuarter, "tr" + imageName);
		// /* Bottom Left */
		// System.out.print("\tq2");
		// tmpQuarter.setRGB(0, 0, width, height, tmp.getRGB(0, height, width,
		// height, null, 0, width), 0, width);
		// fileHandler.saveImage(tmpQuarter, "bl" + imageName);
		// /* Bottom Right */
		// System.out.print("\tq3");
		// tmpQuarter.setRGB(0, 0, width, height, tmp.getRGB(width, height,
		// width, height, null, 0, width), 0, width);
		// fileHandler.saveImage(tmpQuarter, "br" + imageName);
		// System.out.println("\tDone!");
		// }
		//
		// fileHandler.setInputFolder("inputBg");
		//
		// System.out.println("Combining Images: ");
		// fileHandler.saveImage(imageCombiner.averageMedianImages(fileHandler.getAllImagesMatching("tl"
		// + fileName)), "bgImage0");
		// fileHandler.saveImage(imageCombiner.averageMedianImages(fileHandler.getAllImagesMatching("tr"
		// + fileName)), "bgImage1");
		// fileHandler.saveImage(imageCombiner.averageMedianImages(fileHandler.getAllImagesMatching("bl"
		// + fileName)), "bgImage2");
		// fileHandler.saveImage(imageCombiner.averageMedianImages(fileHandler.getAllImagesMatching("br"
		// + fileName)), "bgImage3");
		//
		// fileHandler.setOutputFolder("output");
		//
		// BufferedImage returnImage =
		// imageCombiner.combineImageQuarters(fileHandler.getAllImagesMatching("bgImage"));
		// /* End Divide into quarters */

		/* Save Image and return */
		System.out.println("----------------------");
		System.out.println("Background Created in " + (combineImagesTime - startTime) + "ms");
		System.out.println("----------------------");
		fileHandler.saveImage(backgroundImage, "backgroundImage");
		return backgroundImage;
	}
}
