package imageProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * 
 * @author cncplyr
 * @version 0.1
 * 
 */
public class BoundingBoxer {

	public BoundingBoxer() {

	}

	public BufferedImage createBoundingBox(BufferedImage inputImage) {
		return drawBoundingBox(inputImage, getBoundingBox(inputImage));
	}

	/*
	 * Returns the (x,y) coordinates of the top left and bottom right of the
	 * bounding box.
	 */
	public int[] getBoundingBox(BufferedImage inputImage) {
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		int[] boundingBox = new int[4];
		boundingBox[0] = width;
		boundingBox[1] = height;
		boundingBox[2] = 0;
		boundingBox[3] = 0;
		// Implement some kind of search here


		// START: Dumb brute-force search
		int[] scanline = new int[width];
		boolean currentScanlineFlag = false;

		// Find the first (x, y) pair
		for (int y = 0; y < height; y++) {
			scanline = inputImage.getRGB(0, y, width, 1, null, 0, width);
			for (int x = 0; x < width; x++) {
				if (!currentScanlineFlag) {
					// First non-alpha pixel in the line
					if ((scanline[x] & 0x0000FF) != 0) {
						if (x < boundingBox[0]) {
							// store the new start-x value
							boundingBox[0] = x;
							currentScanlineFlag = true;
						}
					}
					if (currentScanlineFlag && y < boundingBox[1]) {
						// store the start-y value
						boundingBox[1] = y;
					}
				}
			}
			currentScanlineFlag = false;
		}
		// Find teh last (x, y) pair
		for (int y = height - 1; y > boundingBox[1]; y--) {
			scanline = inputImage.getRGB(0, y, width, 1, null, 0, width);
			for (int x = width - 1; x > boundingBox[0]; x--) {
				if (!currentScanlineFlag) {
					// First non-alpha pixel in the line
					if ((scanline[x] & 0x0000FF) != 0) {
						if (x > boundingBox[2]) {
							// store the new start-x value
							boundingBox[2] = x;
							currentScanlineFlag = true;
						}
					}
					if (currentScanlineFlag && y > boundingBox[3]) {
						// store the start-y value
						boundingBox[3] = y;
					}
				}
			}
			currentScanlineFlag = false;
		}
		// END: Dumb brute-force search

		return boundingBox;
	}

	/**
	 * Creates a red border at the coordinates given.
	 * 
	 * N.B. I was going to program this using setRGB with an entire line, but
	 * this method proved to be just as fast, and less complicated so I stuck
	 * with this.
	 * 
	 * @param image
	 * @param coords
	 *            The coordinates. The coordinates are in the form: coords[0] =
	 *            startX coords[1] = startY coords[2] = endX coords[3] = endY
	 * @return The image with the border drawn.
	 */
	public BufferedImage drawBoundingBox(BufferedImage image, int[] coords) {
		// Move border out by 1 pixel so we don't lose information.
		coords[0]--;
		coords[1]--;
		coords[2]++;
		coords[3]++;

		for (int x = coords[0]; x < coords[2]; x++) {
			// Top line
			image.setRGB(x, coords[1], Color.red.getRGB());
			// Bottom line
			image.setRGB(x, coords[3], Color.red.getRGB());
		}
		for (int y = coords[1]; y < coords[3]; y++) {
			// Left line
			image.setRGB(coords[0], y, Color.red.getRGB());
			// Right line
			image.setRGB(coords[2], y, Color.red.getRGB());
		}


		return image;
	}

}
