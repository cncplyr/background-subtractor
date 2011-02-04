package imageProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * 
 * @author cncplyr
 * @version 0.2
 *
 */
public class ImageSubtractor {
	private ImageBlurrer imageBlur;
	private BufferedImage backgroundImage;
	private int threshold;
	private int blurRadius;
	private Color alpha = new Color(0, 0, 0, 0);

	public ImageSubtractor(int width, int height, int threshold) {
		this.blurRadius = 11;
		this.threshold = threshold;

		this.imageBlur = new ImageBlurrer();
		this.backgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	public BufferedImage getBackgroundImage() {
		return backgroundImage;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setBlurRadius(int radius) {
		blurRadius = radius;
	}

	public int getBlurRadius() {
		return blurRadius;
	}

	public void setBackgroundImage(BufferedImage inputImage) {
		if (inputImage == null) {
			throw new IllegalArgumentException("Background image is null!");
		} else {
			this.backgroundImage = imageBlur.averageBlur(inputImage, blurRadius);
		}
	}

	public void setThreshold(int inputThreshold) {
		this.threshold = inputThreshold;
	}

	public BufferedImage subtractBackground(BufferedImage inputImage) {
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();

		BufferedImage blurredInputImage = imageBlur.averageBlur(inputImage, blurRadius);
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color bgColour = new Color(backgroundImage.getRGB(x, y));
				Color imgColour = new Color(blurredInputImage.getRGB(x, y));

				if ((imgColour.getRed() >= bgColour.getRed() - threshold && imgColour.getRed() <= bgColour.getRed() + threshold)
						&& (imgColour.getGreen() >= bgColour.getGreen() - threshold && imgColour.getGreen() <= bgColour.getGreen() + threshold)
						&& (imgColour.getBlue() >= bgColour.getBlue() - threshold && imgColour.getBlue() <= bgColour.getBlue() + threshold)) {
					// if colours match, remove it

					newImage.setRGB(x, y, alpha.getRGB());
				} else {
					// else copy over
					newImage.setRGB(x, y, inputImage.getRGB(x, y));
				}
			}
		}

		return newImage;
	}
}
