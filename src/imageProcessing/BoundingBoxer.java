package imageProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;

import metrics.Metrics;

/**
 * 
 * @author Richard Jenkin
 * @version 1.0
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

		// Prepare previous metrics for enhanced search
		int halfWidth, halfHeight;
		int startX = 0;
		int startY = 60;
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
			startY = (startY < 60) ? 60 : startY;
			endY = (endY >= height) ? height - 1 : endY;
		}

		// START: Single-Pass Pixel Search
		int[] scanline = new int[width];
		boolean currentScanlineFlag = false;

		// Find the first (x, y) pair
		for (int y = startY; y < height; y++) {
			scanline = inputImage.getRGB(0, y, width, 1, null, 0, width);
			for (int x = startX; x < width; x++) {
				// First non-alpha pixel in the line
				if ((scanline[x] & 0x0000FF) != 0) {
					currentScanlineFlag = true;
					if (x < metrics.getAbsStartX()) {
						// store the new start-x value
						metrics.setAbsStartX(x);
					} else if (x > metrics.getAbsEndX()) {
						// store the new end-x value
						metrics.setAbsEndX(x);
					}
				}
			}
			// Non-alpha pixels exist
			if (currentScanlineFlag) {
				if (y < metrics.getAbsStartY()) {
					// store the start-y value
					metrics.setAbsStartY(y);
				} else if (y > metrics.getAbsEndY()) {
					// store the end-y value
					metrics.setAbsEndY(y);
				}
			}
			currentScanlineFlag = false;
		}
		// END: Single-Pass Pixel Search

		return metrics;
	}

	/**
	 * Creates a red border at the coordinates given.
	 * 
	 * N.B. I was going to program this using setRGB with an entire line, but
	 * this method proved to be just as fast, if not faster.
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
