package metrics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import maths.AverageFinder;

/**
 * This class calculates various useful metrics about an image.
 * 
 * @author Richard Jenkin
 * @version 1.0
 * 
 */
public class MetricsCentroid {
	AverageFinder avgFinder;

	/**
	 * Constructor
	 */
	public MetricsCentroid() {
		this.avgFinder = new AverageFinder();
	}

	/**
	 * Finds the x-centroid, x-eccentricity and x-velocity of an input image,
	 * using metrics from the previous image.
	 * 
	 * @param image
	 *            The image to find metrics for.
	 * @param imageMetrics
	 *            The given metrics. Should only contain the bounding-box
	 *            coordinates, and velocity flag. This is where the additional
	 *            metrics will be written to.
	 * @param prevMetrics
	 *            The metrics from the previous image. Should be complete. If
	 *            null, x-velocity will be 0.
	 */
	public Metrics findXMetrics(BufferedImage image, Metrics imageMetrics, Metrics prevMetrics) {
		/* X-Centroid */
		int boxWidth = imageMetrics.getAbsEndX() - imageMetrics.getAbsStartX();

		List<Integer> averages = new ArrayList<Integer>();

		for (int y = imageMetrics.getAbsStartY(); y < imageMetrics.getAbsEndY(); y++) {
			// for each scanline
			int[] row = image.getRGB(imageMetrics.getAbsStartX(), y, boxWidth, 1, null, 0, boxWidth);
			int start = boxWidth;
			int end = 0;

			// Find the first non-alpha
			for (int x = 0; x < boxWidth; x++) {
				if ((row[x] & 0x0000FF) != 0) {
					start = x;
					break;
				}
			}
			// Find the last non-alpha
			for (int x = boxWidth - 1; x > start; x--) {
				if ((row[x] & 0x0000FF) != 0) {
					end = x;
					break;
				}
			}
			// Add average to list of averages
			averages.add((start + end) / 2);
		}
		// Find the average
		int xAvgCentroid = avgFinder.findMedian(averages);
		// Store it
		imageMetrics.setRelCentroidX(xAvgCentroid);

		/* X-Eccentricity */
		int xRelCentre = (imageMetrics.getAbsEndX() - imageMetrics.getAbsStartX()) / 2;
		int xEccentricity = xAvgCentroid - xRelCentre;
		// Store it
		imageMetrics.setRelEccentricityX(xEccentricity);

		/* X-Velocity */
		int xVelocity = 0;
		if ((imageMetrics.getRelVelocityX() != -1) && (prevMetrics != null)) {
			xVelocity = (imageMetrics.getAbsStartX() + xAvgCentroid) - (prevMetrics.getAbsStartX() + prevMetrics.getRelCentroidX());
		}
		// Store it
		imageMetrics.setRelVelocityX(xVelocity);

		return imageMetrics;
	}

	/**
	 * Draws metrics to the image for testing and debugging purposes. Bounding
	 * Box: Red Box. Half-X Bounding Box: Red vertical line. X-Centroid: Green
	 * vertical line. X-Eccentricity: Distance between red and green vertical
	 * lines. X-Velocity: Green horizontal line connected to X-Centroid.
	 * 
	 * 
	 * @param image
	 *            The image to draw metrics on.
	 * @param imageMetrics
	 *            The metrics to draw.
	 * @return The image with the metrics drawn to it.
	 */
	public BufferedImage drawMetrics(BufferedImage image, Metrics imageMetrics) {
		/* X-Centroid */
		// Green line down image centre
		for (int y = imageMetrics.getAbsStartY(); y < imageMetrics.getAbsEndY(); y++) {
			image.setRGB(imageMetrics.getAbsStartX() + imageMetrics.getRelCentroidX(), y, Color.GREEN.getRGB());
		}
		/* X-Eccentricity */
		// Red line down bounding box centre
		for (int y = imageMetrics.getAbsStartY(); y < imageMetrics.getAbsEndY(); y++) {
			image.setRGB(imageMetrics.getAbsStartX() + imageMetrics.getRelCentroidX() - imageMetrics.getRelEccentricityX(), y, Color.RED.getRGB());
		}
		/* X-Velocity */
		int startX = imageMetrics.getAbsStartX() + imageMetrics.getRelCentroidX();
		int endX = imageMetrics.getAbsStartX() + imageMetrics.getRelCentroidX();
		int y = imageMetrics.getAbsStartY() + ((imageMetrics.getAbsEndY() - imageMetrics.getAbsStartY()) / 2);
		if (imageMetrics.getRelVelocityX() > 0) {
			// positive velocity, draw from velocity
			startX -= imageMetrics.getRelVelocityX();
		} else {
			// negative velocity, draw centroid first
			endX -= imageMetrics.getRelVelocityX();
		}
		for (int x = startX; x < endX; x++) {
			image.setRGB(x, y, Color.GREEN.getRGB());
		}

		return image;
	}
}
