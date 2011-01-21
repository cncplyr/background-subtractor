package imageProcessing;

import java.awt.image.BufferedImage;

/**
 * 
 * @author Ric
 * @version 0.1
 * 
 */
public class BoundingBoxer {

	public BoundingBoxer() {

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
		boundingBox[2] = width;
		boundingBox[3] = height;
		// Implement some kind of search here


		// START: Dumb brute-force search
		int[] scanline = new int[width];
		boolean currentScanlineFlag = false;

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
					// TODO: This if statement may be wrong (< might be a >)
					if (currentScanlineFlag && y < boundingBox[1]) {
						// store the start-y value
						boundingBox[1] = y;
					}
				} else {
					// Last non-alpha pixel in the line
					// TODO: WRITE THIS PART
				}
			}
			currentScanlineFlag = false;
		}
		// END: Dumb brute-force search

		return boundingBox;
	}

}
