package imageProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import fileHandling.FileHandler;

import maths.AverageFinder;

/**
 * Functions for combining images.
 * 
 * @author cncplyr
 * 
 */
public class ImageCombiner {
	AverageFinder averageFinder;
	FileHandler fileHandler;

	public ImageCombiner() {
		averageFinder = new AverageFinder();
		fileHandler = new FileHandler();
	}

	/**
	 * Combines two images into the first image (mean average). When weight is
	 * 1, the two images are weighted equally.
	 * 
	 * @param firstImage
	 * @param additionalImage
	 */
	public BufferedImage averageTwoImagesIncremental(BufferedImage firstImage, BufferedImage additionalImage, int weight) {
		int height = firstImage.getHeight();
		int width = firstImage.getWidth();
		BufferedImage returnImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		int red = 0;
		int green = 0;
		int blue = 0;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color firstColour = new Color(firstImage.getRGB(x, y));
				Color secondColour = new Color(additionalImage.getRGB(x, y));

				red = ((weight * firstColour.getRed()) + secondColour.getRed()) / (weight + 1);
				green = ((weight * firstColour.getGreen()) + secondColour.getGreen()) / (weight + 1);
				blue = ((weight * firstColour.getBlue()) + secondColour.getBlue()) / (weight + 1);

				Color temp = new Color(red, green, blue, 255);

				returnImage.setRGB(x, y, temp.getRGB());
			}
		}

		return returnImage;
	}

	public BufferedImage averageMedianImages(List<BufferedImage> images) {
		int noOfImages = images.size();
		int width = images.get(0).getWidth();
		int height = images.get(0).getHeight();
		BufferedImage averagedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		List<Integer> currentPixel = new ArrayList<Integer>();

		if (noOfImages % 2 == 0) {
			// If wrong number of images (even), remove the last one.
			// There are plenty more fish in the sea!
			images.remove(noOfImages - 1);
		}

		// N.B. this is actually doing vertical lines, not horizontal lines.
		for (int x = 0; x < width; x++) {
			if ((x) % 128 == 0) {
				System.out.println((x / 128) + "0%");
			}
			for (int y = 0; y < height; y++) {
				for (BufferedImage image : images) {
					currentPixel.add(image.getRGB(x, y));
				}
				averagedImage.setRGB(x, y, averageFinder.findMedian(currentPixel));
				currentPixel.clear();
			}
		}

		return averagedImage;
	}
}
