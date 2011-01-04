package imageProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageSubtractor {
	private ImageBlurrer imageBlur;
	private BufferedImage backgroundImage;
	private int threshold;
	private Color alpha = new Color(0, 0, 0, 0);

	public ImageSubtractor(int width, int height, int threshold) {
		this.backgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		imageBlur = new ImageBlurrer();
	}

	public BufferedImage getBackgroundImage() {
		return backgroundImage;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setBackgroundImage(BufferedImage inputImage) {
		this.backgroundImage = inputImage;
		System.out.println("Background Image Updated!");
	}

	public void setThreshold(int inputThreshold) {
		this.threshold = inputThreshold;
	}

	public BufferedImage subtractBackground(BufferedImage inputImage) {
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();

		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color bgColour = new Color(backgroundImage.getRGB(x, y));
				Color imgColour = new Color(inputImage.getRGB(x, y));

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
