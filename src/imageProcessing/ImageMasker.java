package imageProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the Circular _____ thing to remove odd outliers,
 * creating a smoother mask. Works only with masks.
 * 
 * @author cncplyr
 * @version 0.2
 * 
 */
public class ImageMasker {
	private Color alpha = new Color(0, 0, 0, 0);
	private Color black = new Color(255, 255, 255, 255);
	private BoundingBoxer boundingBoxer;

	public ImageMasker() {
		this.boundingBoxer = new BoundingBoxer();
	}

	/**
	 * Takes an image and a black/alpha mask, and applies the mask.
	 * 
	 * @param inputImage
	 * @param mask
	 * @return
	 */
	public BufferedImage applyMask(BufferedImage inputImage, BufferedImage mask) {
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		BufferedImage maskedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (mask.getRGB(x, y) == black.getRGB()) {
					maskedImage.setRGB(x, y, inputImage.getRGB(x, y));
				} else {
					maskedImage.setRGB(x, y, alpha.getRGB());
				}
			}
		}

		return maskedImage;
	}

	/**
	 * Takes an input image and background image, and creates a subtracted mask.
	 * 
	 * @param blurredInputImage
	 * @param blurredBackgroundImage
	 * @param threshold
	 * @return
	 */
	public BufferedImage createMask(BufferedImage blurredInputImage, BufferedImage blurredBackgroundImage, int threshold) {
		int width = blurredInputImage.getWidth();
		int height = blurredInputImage.getHeight();

		BufferedImage mask = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// Get the RGB values of the same pixel in each image.
				Color bgColour = new Color(blurredBackgroundImage.getRGB(x, y));
				Color imgColour = new Color(blurredInputImage.getRGB(x, y));

				// Compare them
				if ((imgColour.getRed() >= bgColour.getRed() - threshold && imgColour.getRed() <= bgColour.getRed() + threshold)
						&& (imgColour.getGreen() >= bgColour.getGreen() - threshold && imgColour.getGreen() <= bgColour.getGreen() + threshold)
						&& (imgColour.getBlue() >= bgColour.getBlue() - threshold && imgColour.getBlue() <= bgColour.getBlue() + threshold)) {
					// if colours match, remove it
					mask.setRGB(x, y, alpha.getRGB());
				} else {
					// else add it to the mask
					mask.setRGB(x, y, black.getRGB());
				}
			}
		}

		return mask;
	}

	/**
	 * Contracts and expands a mask, to remove erroneous strands of pixels.
	 * 
	 * @param inputMask
	 * @param times
	 *            The number of times to contract/expand.
	 * @return
	 */
	public BufferedImage contractExpand(BufferedImage inputMask, int times) {
		int[] maskBoundingBox = boundingBoxer.getBoundingBox(inputMask);
		// Contract the mask the required number of times
		for (int i = 0; i < times; i++) {
			inputMask = contractOne(inputMask, maskBoundingBox);
		}
		// Expand the mask the required number of times
		for (int i = 0; i < times; i++) {
			inputMask = expandOne(inputMask, maskBoundingBox);
		}
		return inputMask;
	}

	/**
	 * Takes a mask comprising of alpha and black levels, and contracts the mask
	 * by 1 pixel.
	 * 
	 * @param inputImage
	 * @return
	 */
	private BufferedImage contractOne(BufferedImage inputMask, int[] boundingBox) {
		int width = inputMask.getWidth();
		int height = inputMask.getHeight();
		BufferedImage finalMask = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		// Create local variables for the bounding box to keep the code neat
		// here, and in case the type of boundingBox gets changed later.
		int x1 = boundingBox[0];
		int x2 = boundingBox[2];
		int y1 = boundingBox[1];
		int y2 = boundingBox[3];

		List<Integer> colourGrid;

		/* Contract by one */
		// For each pixel
		for (int x = x1; x < x2; x++) {
			for (int y = y1; y < y2; y++) {
				if (inputMask.getRGB(x, y) == alpha.getRGB()) {
					// if already alpha, ignore it
					finalMask.setRGB(x, y, alpha.getRGB());
				} else {
					// else this pixel has a colour
					colourGrid = bruteForceColourGrid(inputMask, x, y);
					if (colourGrid.contains(alpha.getRGB())) {
						// if it is a border pixel, ignore it
						finalMask.setRGB(x, y, alpha.getRGB());
					} else {
						// else add it to the mask
						finalMask.setRGB(x, y, black.getRGB());
					}
				}
			}
		}

		return finalMask;
	}

	/**
	 * Takes a mask comprising of alpha and black levels, and expands the mask
	 * by 1 pixel.
	 * 
	 * @param inputMask
	 * @return
	 */
	private BufferedImage expandOne(BufferedImage inputMask, int[] boundingBox) {
		int width = inputMask.getWidth();
		int height = inputMask.getHeight();
		BufferedImage finalMask = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		// Create local variables for the bounding box to keep the code neat
		// here, and in case the type of boundingBox gets changed later.
		int x1 = boundingBox[0];
		int x2 = boundingBox[2];
		int y1 = boundingBox[1];
		int y2 = boundingBox[3];


		List<Integer> colourGrid;

		/* Expand by one */
		// For each pixel
		for (int x = x1; x < x2; x++) {
			for (int y = y1; y < y2; y++) {
				if (inputMask.getRGB(x, y) == black.getRGB()) {
					// if already in mask, copy it
					finalMask.setRGB(x, y, black.getRGB());
				} else {
					// else check pixel for bordering mask
					colourGrid = bruteForceColourGrid(inputMask, x, y);
					if (colourGrid.contains(black.getRGB())) {
						// if it bordering, add to mask
						finalMask.setRGB(x, y, black.getRGB());
					} else {
						// else ignore it
						finalMask.setRGB(x, y, alpha.getRGB());
					}
				}
			}
		}

		return finalMask;
	}

	/**
	 * A brute force method of getting a colour grid, until I can find out how
	 * getRGB() works properly.
	 * 
	 * TODO: Replace this method.
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @return
	 */
	private List<Integer> bruteForceColourGrid(BufferedImage image, int x, int y) {
		List<Integer> colourList = new ArrayList<Integer>();

		colourList.add(image.getRGB(x - 1, y - 1));
		colourList.add(image.getRGB(x, y - 1));
		colourList.add(image.getRGB(x + 1, y - 1));
		colourList.add(image.getRGB(x - 1, y));
		colourList.add(image.getRGB(x + 1, y));
		colourList.add(image.getRGB(x - 1, y + 1));
		colourList.add(image.getRGB(x, y + 1));
		colourList.add(image.getRGB(x + 1, y + 1));

		return colourList;
	}
}
