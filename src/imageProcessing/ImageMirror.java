package imageProcessing;

import java.awt.image.BufferedImage;

/**
 * 
 * @author Richard Jenkin
 * @version 1.0
 * 
 */
public class ImageMirror {

	public ImageMirror() {

	}

	public BufferedImage genFrame(BufferedImage inFrame) {
		int width = inFrame.getWidth();
		int height = inFrame.getHeight();
		BufferedImage outFrame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int col = 0; col < width - 1; col++) {
			outFrame.setRGB(width - col - 1, 0, 1, height, inFrame.getRGB(col, 0, 1, height, null, 0, 1), 0, 1);
		}

		return outFrame;
	}
}
