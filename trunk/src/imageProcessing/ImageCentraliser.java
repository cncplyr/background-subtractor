package imageProcessing;

import java.awt.image.BufferedImage;

import metrics.Metrics;

public class ImageCentraliser {
	private Metrics cropSize;

	public ImageCentraliser() {
		cropSize = new Metrics(0, 0, 0, 0);
	}

	public BufferedImage centralCrop(BufferedImage image, Metrics imageMetrics) {
		BufferedImage returnImage = new BufferedImage(cropSize.getAbsEndX() - cropSize.getAbsStartX(), cropSize.getAbsEndY() - cropSize.getAbsStartY(),
				BufferedImage.TYPE_INT_ARGB);
		int offsetX = (cropSize.getAbsEndX() / 2) - imageMetrics.getRelCentroidX();
		int offsetY = imageMetrics.getAbsStartY() - cropSize.getAbsStartY();
		int imageWidth = imageMetrics.getAbsEndX() - imageMetrics.getAbsStartX();
		int imageHeight = imageMetrics.getAbsEndY() - imageMetrics.getAbsStartY();
		returnImage.setRGB(offsetX, offsetY, imageWidth, imageHeight,
				image.getRGB(imageMetrics.getAbsStartX(), imageMetrics.getAbsStartY(), imageWidth, imageHeight, null, 0, imageWidth), 0, imageWidth);

		return returnImage;
	}

	public void setCropSize(Metrics cropSize) {
		this.cropSize = cropSize;
		System.out.println("p1: (" + cropSize.getAbsStartX() + ", " + cropSize.getAbsStartY() + ")");
		System.out.println("p2: (" + cropSize.getAbsEndX() + ", " + cropSize.getAbsEndY() + ")");
	}

	public Metrics getCropSize() {
		return cropSize;
	}

}
