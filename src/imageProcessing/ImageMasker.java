package imageProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import metrics.Metrics;

/**
 * This class implements the Circular _____ thing to remove odd outliers,
 * creating a smoother mask. Works only with masks.
 * 
 * @author cncplyr
 * @version 0.3
 * 
 */
public class ImageMasker {
	private Color alpha = new Color(0, 0, 0, 0);
	private Color black = new Color(255, 255, 255, 255);

	public ImageMasker() {
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
	 * @param metrics
	 * @param threshold
	 * @return
	 */
	public BufferedImage createMask(BufferedImage blurredInputImage, BufferedImage blurredBackgroundImage, Metrics metrics, int threshold) {
		int imgWidth = blurredInputImage.getWidth();
		int imgHeight = blurredInputImage.getHeight();

		int startX, startY;
		int endX, endY;

		int bbHeight = 0, bbWidth = 0;

		boolean foundSomething = false;

		if (metrics == null) {
			// Just search the whole image
			startX = 0;
			startY = 0;
			endX = imgWidth;
			endY = imgHeight;
		} else {
			// Search a box twice as wide and twice as tall
			bbWidth = (Math.abs(metrics.getAbsEndX() - metrics.getAbsStartX())) / 2;
			bbHeight = (Math.abs(metrics.getAbsEndY() - metrics.getAbsStartY())) / 2;

			startX = metrics.getAbsStartX() - bbWidth;
			startY = metrics.getAbsStartY() - bbHeight;
			endX = metrics.getAbsEndX() + bbWidth;
			endY = metrics.getAbsEndY() + bbWidth;

			// Check search is in available range
			startX = (startX < 0) ? 0 : startX;
			startY = (startY < 0) ? 0 : startY;
			endX = (endX > imgWidth) ? imgWidth : endX;
			endY = (endY > imgHeight) ? imgHeight : endY;
		}

		BufferedImage mask = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);

		for (int x = startX; x < endX; x++) {
			for (int y = startY; y < endY; y++) {
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
					foundSomething = true;
				}
			}
		}

		if (!foundSomething) {
			// TODO: This doesn't work!
			// Tell metrics we are in a different place, so ignore velocity
			metrics.setRelVelocityX(-1);
			return createMask(blurredInputImage, blurredBackgroundImage, null, threshold);
		} else {
			return mask;
		}
	}

	/**
	 * Contracts and expands a mask, to remove erroneous strands of pixels.
	 * 
	 * @param inputMask
	 * @param metrics
	 * @param times
	 *            The number of times to contract/expand.
	 * @return
	 */
	public BufferedImage contractExpand(BufferedImage inputMask, Metrics metrics, int times) {
		// Contract the mask the required number of times
		for (int i = 0; i < times; i++) {
			inputMask = contractOne(inputMask, metrics);
		}
		// Expand the mask the required number of times
		for (int i = 0; i < times; i++) {
			inputMask = expandOne(inputMask, metrics);
		}
		return inputMask;
	}

	/**
	 * Takes a mask comprising of alpha and black levels, and contracts the mask
	 * by 1 pixel.
	 * 
	 * @param inputImage
	 * @param metrics
	 * @return
	 */
	private BufferedImage contractOne(BufferedImage inputMask, Metrics metrics) {
		int width = inputMask.getWidth();
		int height = inputMask.getHeight();
		BufferedImage finalMask = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		// Create local variables for the bounding box to keep the code neat
		// here, and in case the type of boundingBox gets changed later.
		int x1 = metrics.getAbsStartX();
		int x2 = metrics.getAbsEndX();
		int y1 = metrics.getAbsStartY();
		int y2 = metrics.getAbsEndY();

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
					colourGrid = getLocalColoursAsList(inputMask, x, y);
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
	 * @param metrics
	 * @return
	 */
	private BufferedImage expandOne(BufferedImage inputMask, Metrics metrics) {
		int width = inputMask.getWidth();
		int height = inputMask.getHeight();
		BufferedImage finalMask = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		// Create local variables for the bounding box to keep the code neat
		// here, and in case the type of boundingBox gets changed later.
		int x1 = metrics.getAbsStartX();
		int x2 = metrics.getAbsEndX();
		int y1 = metrics.getAbsStartY();
		int y2 = metrics.getAbsEndY();


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
					colourGrid = getLocalColoursAsList(inputMask, x, y);
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
	 * Returns the surrounding colours as a fixed-length list, enabling us to
	 * call colourGrid.contains on the list. NOTE: The size of this list cannot
	 * grow, nor should it in this class.
	 * 
	 * @param image
	 *            The image to get the pixels from.
	 * @param x
	 *            The x-coordinate of the current centre pixel.
	 * @param y
	 *            The y-coordinate of the current centre pixel.
	 * @return
	 */
	private List<Integer> getLocalColoursAsList(BufferedImage image, int x, int y) {
		List list = Arrays.asList(image.getRGB(x - 1, y - 1, 3, 3, null, 0, 3));
		return list;
	}
}
