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
	 * Splits an image into horizontal slices. For 720p, this should be a
	 * multiple of 720, e.g. 2, 4, 6, 8, 10, 12, 16, 18, 20, etc.
	 * 
	 * @param inputImage
	 * @param splices
	 * @return
	 */
	public List<BufferedImage> splitImage(BufferedImage inputImage, int slices) {
		List<BufferedImage> returnList = new ArrayList<BufferedImage>();
		int width = inputImage.getWidth();
		int individualHeight = inputImage.getHeight() / slices;
		int currentStartHeight = 0;
		BufferedImage tmp = new BufferedImage(width, individualHeight, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < slices; i++) {
			currentStartHeight = individualHeight * i;
			// / TODO: This breaks when i = 5, for the 4th argument of getRGB
			// (end width). Apaprently coordinate is out of bounds.
			tmp.setRGB(0, 0, width, individualHeight, inputImage.getRGB(0, currentStartHeight, width, currentStartHeight + individualHeight, null, 0, width),
					0, width);
			//
			// switch (i) {
			// case 0:
			// tmp.setRGB(0, 0, width, individualHeight, inputImage.getRGB(0, 0
			// * individualHeight, width, 1 * individualHeight, null, 0, width),
			// 0, width);
			// break;
			// case 1:
			// tmp.setRGB(0, 0, width, individualHeight, inputImage.getRGB(0, 1
			// * individualHeight, width, 2 * individualHeight, null, 0, width),
			// 0, width);
			// break;
			// case 2:
			// tmp.setRGB(0, 0, width, individualHeight, inputImage.getRGB(0, 2
			// * individualHeight, width, 3 * individualHeight, null, 0, width),
			// 0, width);
			// break;
			// case 3:
			// tmp.setRGB(0, 0, width, individualHeight, inputImage.getRGB(0, 3
			// * individualHeight, width, 4 * individualHeight, null, 0, width),
			// 0, width);
			// break;
			// case 4:
			// tmp.setRGB(0, 0, width, individualHeight, inputImage.getRGB(0, 4
			// * individualHeight, width, 5 * individualHeight, null, 0, width),
			// 0, width);
			// break;
			// case 5:
			// tmp.setRGB(0, 0, width, individualHeight, inputImage.getRGB(0, 5
			// * individualHeight, width, 6 * individualHeight, null, 0, width),
			// 0, width);
			// break;
			// case 6:
			// tmp.setRGB(0, 0, width, individualHeight, inputImage.getRGB(0, 6
			// * individualHeight, width, 7 * individualHeight, null, 0, width),
			// 0, width);
			// break;
			// case 7:
			// tmp.setRGB(0, 0, width, individualHeight, inputImage.getRGB(0, 7
			// * individualHeight, width, 8 * individualHeight, null, 0, width),
			// 0, width);
			// break;
			// case 8:
			// tmp.setRGB(0, 0, width, individualHeight, inputImage.getRGB(0, 8
			// * individualHeight, width, 9 * individualHeight, null, 0, width),
			// 0, width);
			// break;
			// case 9:
			// tmp.setRGB(0, 0, width, individualHeight, inputImage.getRGB(0, 9
			// * individualHeight, width, 10 * individualHeight, null, 0,
			// width), 0, width);
			// break;
			// default:
			// break;
			// }
			returnList.add(tmp);
		}
		return returnList;
	}

	/**
	 * Combines horizontal slices of images together.
	 * 
	 * @param inputImages
	 * @return
	 * @throws Exception
	 */
	public BufferedImage combineImages(List<BufferedImage> inputImages) throws Exception {
		int images = inputImages.size();
		int width = inputImages.get(0).getWidth();
		int height = inputImages.get(0).getHeight();
		BufferedImage returnImage = new BufferedImage(width, height * images, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < images; i++) {
			returnImage.setRGB(0, i * height, width, height, inputImages.get(i).getRGB(0, 0, width, height, null, 0, width), 0, width);
		}
		return returnImage;
	}

	public BufferedImage combineImageQuarters(List<BufferedImage> inputImages) throws Exception {
		if (inputImages.size() != 4) {
			throw new Exception("Cannot combine 4 images if there are not 4 images");
		}
		int width = inputImages.get(0).getWidth();
		int height = inputImages.get(0).getHeight();
		BufferedImage returnImage = new BufferedImage(2 * width, 2 * height, BufferedImage.TYPE_INT_ARGB);
		returnImage.setRGB(0, 0, width, height, inputImages.get(0).getRGB(0, 0, width, height, null, 0, width), 0, width);
		returnImage.setRGB(width, 0, width, height, inputImages.get(1).getRGB(0, 0, width, height, null, 0, width), 0, width);
		returnImage.setRGB(0, height, width, height, inputImages.get(2).getRGB(0, 0, width, height, null, 0, width), 0, width);
		returnImage.setRGB(width, height, width, height, inputImages.get(3).getRGB(0, 0, width, height, null, 0, width), 0, width);
	
		return returnImage;
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

	//
	// /**
	// * THIS IS STUPIDLY SLOW!! DON'T USE!!
	// *
	// * @param imageNames
	// * @return
	// */
	// public BufferedImage averageMedianImagesByName(String[] imageNames) {
	// int noOfImages = imageNames.length;
	// BufferedImage firstImage;
	// int width = 0;
	// int height = 0;
	// try {
	// firstImage = fileHandler.loadImage(imageNames[0]);
	// width = fileHandler.loadImage(imageNames[0]).getWidth();
	// height = fileHandler.loadImage(imageNames[0]).getHeight();
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// }
	// BufferedImage averagedImage = new BufferedImage(width, height,
	// BufferedImage.TYPE_INT_ARGB);
	// List<Integer> currentPixel = new ArrayList<Integer>();
	//
	// if (noOfImages % 2 == 0) {
	// // If wrong number of images, remove the last one.
	// // There are plenty more fish in the sea!
	// noOfImages--;
	// }
	//
	// for (int x = 0; x < width; x++) {
	// for (int y = 0; y < height; y++) {
	// System.out.println(x + ", " + y);
	// for (String imageName : imageNames) {
	// try {
	// currentPixel.add(fileHandler.loadImage(imageName).getRGB(x, y));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// System.gc();
	// }
	// averagedImage.setRGB(x, y, averageFinder.findMedian(currentPixel));
	// currentPixel.clear();
	// }
	// }
	// return averagedImage;
	// }

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
