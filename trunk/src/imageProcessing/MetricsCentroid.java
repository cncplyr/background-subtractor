package imageProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import maths.AverageFinder;

public class MetricsCentroid {
	AverageFinder avgFinder;


	public MetricsCentroid() {
		this.avgFinder = new AverageFinder();
	}

	public void findCentroidAverage(BufferedImage image, Metrics imageMetrics) {
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
		int avgCentroid = avgFinder.findMedian(averages);
		// Store it
		imageMetrics.setRelCentroidX(avgCentroid);
	}

	public void findCentroidMetrics(BufferedImage image, Metrics imageMetrics, Metrics prevMetrics) {
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
		if (prevMetrics != null) {
			xVelocity = (imageMetrics.getAbsStartX() + xAvgCentroid) - (prevMetrics.getAbsStartX() + prevMetrics.getRelCentroidX());
			System.out.println("\nxVelocity: " + xVelocity);
		}
		// Store it
		imageMetrics.setRelVelocityX(xVelocity);
	}

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
