package bgSubtract;

import imageProcessing.ImageBlurrer;
import imageProcessing.ImageCombiner;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import fileHandling.FileHandler;

/**
 * Creates a background image by taking averages of all images.
 * 
 * @author cncplyr
 * @version 0.003
 */
public class BackgroundCreator {
	private static FileHandler fileHandler;
	private static ImageBlurrer imageBlur;
	private static ImageCombiner imageCombiner;

	private static String fileName = "image";

	/**
	 * Constructor
	 * 
	 */
	public BackgroundCreator() {
		fileHandler = new FileHandler();
		imageBlur = new ImageBlurrer();
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

		fileHandler.setInputFolder("input");
		fileHandler.setOutputFolder("outputTEST");
		String[] imageNames = fileHandler.loadAllFileNamesMatching(fileName);
		int noOfImages = imageNames.length;
		List<BufferedImage> images = new ArrayList<BufferedImage>();

		if (noOfImages < 1) {
			throw new Exception("Could not find any images!");
		}

		/* Take 1/10, 1/20 or 1/30 of the images to save RAM */
		if (noOfImages < 200) {
			images = fileHandler.loadAllImagesMatching(fileName);
		} else if ((noOfImages >= 200) && (noOfImages < 1500)) {
			for (int i = 0; i < noOfImages; i = i + 10) {
				images.add(fileHandler.loadImage(imageNames[i]));
			}
		} else if ((noOfImages >= 1500) && (noOfImages < 3000)) {
			for (int i = 0; i < noOfImages; i = i + 20) {
				images.add(fileHandler.loadImage(imageNames[i]));
			}
		} else if (noOfImages >= 3000) {
			for (int i = 0; i < noOfImages; i = i + 30) {
				images.add(fileHandler.loadImage(imageNames[i]));
			}
		}

		BufferedImage returnImage = imageCombiner.averageMedianImages(images);
		/* End 10ths 20ths or 30ths */
	

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


		returnImage = imageBlur.averageBlur(returnImage, 11);
		System.out.println("Created! Time Taken: " + ((System.nanoTime() - startTime) / 1000000000) + " secs");
		fileHandler.saveImage(returnImage, "backgroundImage");
		return returnImage;
	}
}
