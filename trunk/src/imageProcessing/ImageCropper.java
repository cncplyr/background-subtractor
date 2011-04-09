package imageProcessing;

import java.awt.image.BufferedImage;
import java.util.List;

import metrics.Metrics;

public class ImageCropper {

	public ImageCropper() {

	}

	public BufferedImage cropImage(BufferedImage inputImage, Metrics metrics) {
		int width = Math.abs(metrics.getAbsEndX() - metrics.getAbsStartX());
		int height = Math.abs(metrics.getAbsEndY() - metrics.getAbsStartY());
		BufferedImage croppedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		croppedImage.setRGB(0, 0, width, height, inputImage.getRGB(metrics.getAbsStartX(), metrics.getAbsStartY(), width, height, null, 0, width), 0, width);
		return croppedImage;
	}

}
