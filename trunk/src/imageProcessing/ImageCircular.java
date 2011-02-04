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
 * @version 0.1
 * 
 */
public class ImageCircular {
	private Color alpha = new Color(0, 0, 0, 0);
	private Color black = new Color(255, 255, 255, 255);

	/**
	 * Does a 1-in, 1-out on the input mask, then returns the mask.
	 * 
	 * @param inputImage
	 * @return
	 */

	public BufferedImage circularIn(BufferedImage inputMask) {
		int width = inputMask.getWidth();
		int height = inputMask.getHeight();
		BufferedImage finalMask = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		List<Integer> colourGrid;

		/* Take it in by one */
		// For each pixel
		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
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

		/* Expand by one */
		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
				// if it is in the mask already, just fill in all the adjacent
				// pixels.
			}
		}

		return finalMask;
	}

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
