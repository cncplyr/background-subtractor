package imageProcessing;

import java.awt.image.BufferedImage;

public class ImageCropper {

	public ImageCropper() {

	}

	public BufferedImage cropImage(BufferedImage inputImage, int[] boundingBox) {
		int width = boundingBox[2] - boundingBox[0] - 1;
		int height = boundingBox[3] - boundingBox[1] - 1;
		BufferedImage croppedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		croppedImage.setRGB(0, 0, width, height, inputImage.getRGB(boundingBox[0], boundingBox[1], width, height, null, 0, width), 0, width);
		return croppedImage;
	}

}
