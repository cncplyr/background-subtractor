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
	 * Combines two images into the first image (mean average).
	 * 
	 * @param firstImage
	 * @param additionalImage
	 */
	public BufferedImage averageTwoImages(BufferedImage firstImage, BufferedImage additionalImage) {
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

				red = (firstColour.getRed() + secondColour.getRed()) / 2;
				green = (firstColour.getGreen() + secondColour.getGreen()) / 2;
				blue = (firstColour.getBlue() + secondColour.getBlue()) / 2;

				Color temp = new Color(red, green, blue, 255);

				returnImage.setRGB(x, y, temp.getRGB());
			}
		}

		return returnImage;
	}

	/**
	 * Combines two images into the first image (mean average).
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

	/**
	 * THIS IS STUPIDLY SLOW!! DON'T USE!!
	 * 
	 * @param imageNames
	 * @return
	 */
	public BufferedImage averageMedianImagesByName(String[] imageNames) {
		int noOfImages = imageNames.length;
		BufferedImage firstImage;
		int width = 0;
		int height = 0;
		try {
			firstImage = fileHandler.loadImage(imageNames[0]);
			width = fileHandler.loadImage(imageNames[0]).getWidth();
			height = fileHandler.loadImage(imageNames[0]).getHeight();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		BufferedImage averagedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		List<Integer> currentPixel = new ArrayList<Integer>();

		if (noOfImages % 2 == 0) {
			// If wrong number of images, remove the last one.
			// There are plenty more fish in the sea!
			noOfImages--;
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				System.out.println(x + ", " + y);
				for (String imageName : imageNames) {
					try {
						currentPixel.add(fileHandler.loadImage(imageName).getRGB(x, y));
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.gc();
				}
				averagedImage.setRGB(x, y, averageFinder.findMedian(currentPixel));
				currentPixel.clear();
			}
		}
		return averagedImage;
	}

	public BufferedImage averageMedianImages(List<BufferedImage> images) {
		int noOfImages = images.size();
		int width = images.get(0).getWidth();
		int height = images.get(0).getHeight();
		BufferedImage averagedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		List<Integer> currentPixel = new ArrayList<Integer>();

		if (noOfImages % 2 == 0) {
			// If wrong number of images, remove the last one.
			// There are plenty more fish in the sea!
			images.remove(noOfImages - 1);
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				System.out.println(x + ", " + y);
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
