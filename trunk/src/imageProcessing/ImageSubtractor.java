package imageProcessing;

import java.awt.image.BufferedImage;

/**
 * 
 * @author cncplyr
 * @version 0.3
 * 
 */
public class ImageSubtractor {
	private BoundingBoxer boundingBoxer;
	private ImageBlurrer imageBlur;
	private ImageCropper imageCrop;
	private ImageMasker imageMasker;
	private BufferedImage backgroundImage;

	private int blurRadius;
	private int maskRadius;
	private int threshold;

	/**
	 * Constructor. Initialises classes and integers needed.
	 * 
	 * @param width
	 * @param height
	 * @param threshold
	 */
	public ImageSubtractor(int width, int height, int threshold) {
		this.boundingBoxer = new BoundingBoxer();
		this.imageBlur = new ImageBlurrer();
		this.imageCrop = new ImageCropper();
		this.imageMasker = new ImageMasker();
		this.backgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		setBlurRadius(11);
		setMaskRadius(2);
		setThreshold(threshold);
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
		BufferedImage mask = imageMasker.createMask(imageBlur.averageBlur(inputImage, blurRadius), backgroundImage, threshold);
		int[] imageBBox = boundingBoxer.getBoundingBox(mask);
		mask = imageMasker.contractExpand(mask, imageBBox, maskRadius);
		return imageCrop.cropImage(imageMasker.applyMask(inputImage, mask), imageBBox);
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
}
