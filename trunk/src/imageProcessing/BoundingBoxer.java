package imageProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * 
 * @author cncplyr
 * @version 0.2
 * 
 */
public class BoundingBoxer {

	public BoundingBoxer() {

	}

	/*
	 * Returns the (x,y) coordinates of the top left and bottom right of the
	 * bounding box.
	 */
	public Metrics getBoundingBox(BufferedImage inputImage, Metrics prevMetrics) {
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		Metrics metrics = new Metrics(width, height, 0, 0);
		// TODO: Implement some kind of search here
		int halfWidth, halfHeight;
		int startX = 0;
		int startY = 0;
		// TODO: endX doesn't work for some reason, when person moves
		// left->right
		int endX = width - 1;
		int endY = height - 1;
		if (prevMetrics != null) {
			halfWidth = Math.abs(prevMetrics.getAbsEndX() - prevMetrics.getAbsStartX()) / 2;
			startX = prevMetrics.getAbsStartX() - halfWidth;
			endX = prevMetrics.getAbsEndX() + halfWidth;
			startX = (startX < 0) ? 0 : startX;
			endX = (endY >= width) ? width - 1 : endX;

			halfHeight = Math.abs(prevMetrics.getAbsEndY() - prevMetrics.getAbsStartY()) / 2;
			startY = prevMetrics.getAbsStartY() - halfHeight;
			endY = prevMetrics.getAbsEndY() + halfHeight;
			startY = (startY < 0) ? 0 : startY;
			endY = (endY >= height) ? height - 1 : endY;
		}

		// START: Slightly Smarter Brute-Force Search (SSBFS)
		int[] scanline = new int[width];
		boolean currentScanlineFlag = false;

		// Find the first (x, y) pair
		for (int y = startY; y < height; y++) {
			scanline = inputImage.getRGB(0, y, width, 1, null, 0, width);
			for (int x = startX; x < width; x++) {
				if (!currentScanlineFlag) {
					// First non-alpha pixel in the line
					if ((scanline[x] & 0x0000FF) != 0) {
						if (x < metrics.getAbsStartX()) {
							// store the new start-x value
							metrics.setAbsStartX(x);
							currentScanlineFlag = true;
						}
					}
					if (currentScanlineFlag && y < metrics.getAbsStartY()) {
						// store the start-y value
						metrics.setAbsStartY(y);
					}
				}
			}
			currentScanlineFlag = false;
		}
		// Find teh last (x, y) pair
		for (int y = endY; y > metrics.getAbsStartY(); y--) {
			scanline = inputImage.getRGB(0, y, width, 1, null, 0, width);
			for (int x = width - 1; x > metrics.getAbsStartX(); x--) {
				if (!currentScanlineFlag) {
					// First non-alpha pixel in the line
					if ((scanline[x] & 0x0000FF) != 0) {
						if (x > metrics.getAbsEndX()) {
							// store the new start-x value
							metrics.setAbsEndX(x);
							currentScanlineFlag = true;
						}
					}
					if (currentScanlineFlag && y > metrics.getAbsEndY()) {
						// store the start-y value
						metrics.setAbsEndY(y);
					}
				}
			}
			currentScanlineFlag = false;
		}
		// END: Dumb brute-force search

		return metrics;
	}

	/**
	 * Creates a red border at the coordinates given.
	 * 
	 * N.B. I was going to program this using setRGB with an entire line, but
	 * this method proved to be just as fast, and less complicated so I stuck
	 * with this.
	 * 
	 * 
	 * @param image
	 * @param metrics
	 * @return The image with the border drawn.
	 */
	public BufferedImage drawBoundingBox(BufferedImage image, Metrics metrics) {
		// Move border out by 1 pixel so we don't lose information.
		int startX = metrics.getAbsStartX() - 1;
		int startY = metrics.getAbsStartY() - 1;
		int endX = metrics.getAbsEndX() + 1;
		int endY = metrics.getAbsEndY() + 1;

		for (int x = startX; x < endX; x++) {
			// Top line
			image.setRGB(x, startY, Color.red.getRGB());
			// Bottom line
			image.setRGB(x, endY, Color.red.getRGB());
		}
		for (int y = startY; y < endY; y++) {
			// Left line
			image.setRGB(startX, y, Color.red.getRGB());
			// Right line
			image.setRGB(endX, y, Color.red.getRGB());
		}

		return image;
	}
}
