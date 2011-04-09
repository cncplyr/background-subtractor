package imageProcessing;

import java.awt.image.BufferedImage;

import metrics.Metrics;
import metrics.MetricsCentroid;

import fileHandling.CSVHandler;


/**
 * 
 * @author cncplyr
 * @version 0.3
 * 
 */
public class ImageSubtractor {
	private BufferedImage backgroundImage;
	private CSVHandler csvHandler;

	private BoundingBoxer boundingBoxer;
	private ImageBlurrer imageBlur;
	private ImageMasker imageMasker;
	private MetricsCentroid metricsCentroid;

	private int blurRadius;
	private int maskRadius;
	private int threshold;

	private Metrics prevMetrics;
	private int largestWidth;
	private int largestHeightStart, largestHeightEnd;

	/**
	 * Constructor. Initialises classes and integers needed.
	 * 
	 * @param width
	 * @param height
	 * @param threshold
	 */
	public ImageSubtractor(int width, int height, int threshold) {
		this.backgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		this.boundingBoxer = new BoundingBoxer();
		this.imageBlur = new ImageBlurrer();
		this.imageMasker = new ImageMasker();
		this.metricsCentroid = new MetricsCentroid();

		setBlurRadius(11);
		setMaskRadius(2);
		setThreshold(threshold);

		largestWidth = 0;
		largestHeightStart = 720;
		largestHeightEnd = 0;
	}

	/**
	 * Subtracts the current background image from the given input image, using
	 * a mask. Can also perform other operations on the mask, e.g.
	 * contractExpand().
	 * 
	 * @param inputImage
	 *            The image to subtract the background from.
	 * @return The subtracted image.
	 */
	public BufferedImage subtractBackground(BufferedImage inputImage) {
		// Get the mask from the blurred image
		BufferedImage mask = imageMasker.createMask(imageBlur.averageBlur(inputImage, blurRadius), backgroundImage, prevMetrics, threshold);
		// Get the bounding box from the mask
		Metrics imageMetrics = boundingBoxer.getBoundingBox(mask, prevMetrics);
		// Improve the image
		mask = imageMasker.contractExpand(mask, imageMetrics, maskRadius);
		// Mask the image
		BufferedImage maskedImage = imageMasker.applyMask(inputImage, mask);
		// Get metrics
		metricsCentroid.findCentroidMetrics(maskedImage, imageMetrics, prevMetrics);
		// Store bounding box to use in next iteration
		prevMetrics = imageMetrics;
		// Store it to the csv
		if (csvHandler != null) {
			csvHandler.writeCSVLine(imageMetrics.getMetrics());
		}
		// Check size
		checkLargestBBox(imageMetrics);
		// Return the image with debug information
		return metricsCentroid.drawMetrics(boundingBoxer.drawBoundingBox(maskedImage, imageMetrics), imageMetrics);
		// Return the image
		// return maskedImage;
	}

	public Metrics getLargestBoundingBox() {
		return new Metrics(0, largestHeightStart, largestWidth, largestHeightEnd);
	}

	public BufferedImage getBackgroundImage() {
		return backgroundImage;
	}

	public int getBlurRadius() {
		return blurRadius;
	}

	public int getMaskRadius() {
		return maskRadius;
	}

	public int getThreshold() {
		return threshold;
	}

	/**
	 * Set the background image to use. Background image is blurred upon calling
	 * this method automatically.
	 * 
	 * @param inputImage
	 */
	public void setBackgroundImage(BufferedImage inputImage) {
		if (inputImage == null) {
			throw new IllegalArgumentException("Background image is null!");
		} else {
			this.backgroundImage = imageBlur.averageBlur(inputImage, blurRadius);
		}
	}

	public void setBlurRadius(int radius) {
		blurRadius = radius;
	}

	public void setMaskRadius(int maskRadius) {
		this.maskRadius = maskRadius;
	}

	/**
	 * Set the threshold for background subtraction. This sets the limits of the
	 * colour comparison for judging whether a pixel in the input image matches
	 * the background image. Typical value is 20. This is the threshold both
	 * above and below, so threshold = 20 gives a variance of 40 in each of RGB
	 * values.
	 * 
	 * @param inputThreshold
	 *            The threshold to use.
	 */
	public void setThreshold(int inputThreshold) {
		this.threshold = inputThreshold;
	}

	public void setCSVHandler(CSVHandler csvHandler) {
		this.csvHandler = csvHandler;
	}

	private void checkLargestBBox(Metrics currentMetrics) {
		/* X-Dimensions */
		int tempX = 0;
		if (currentMetrics.getRelCentroidX() > ((currentMetrics.getAbsEndX() - currentMetrics.getAbsStartX()) / 2)) {
			// Case 1: Large +ve eccentricity#
			tempX = 2 * currentMetrics.getRelCentroidX();
		} else {
			// Case 2: Large -ve eccentricity
			tempX = currentMetrics.getAbsEndX() - currentMetrics.getAbsStartX() - currentMetrics.getRelCentroidX();
		}
		if (tempX > largestWidth / 2) {
			largestWidth = 2 * tempX;
		}

		/* Y-Dimensions */
		if (currentMetrics.getAbsStartY() < largestHeightStart) {
			largestHeightStart = currentMetrics.getAbsStartY();
		}
		if (currentMetrics.getAbsEndY() > largestHeightEnd) {
			largestHeightEnd = currentMetrics.getAbsEndY();
		}
	}
}
