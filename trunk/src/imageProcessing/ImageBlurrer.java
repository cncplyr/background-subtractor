package imageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * 
 * @author cncplyr
 *
 */
public class ImageBlurrer {

	public ImageBlurrer() {

	}

	/**
	 * Simple average blur.
	 * 
	 * @param img
	 *            The image to blur.
	 * @param radius
	 *            The radius of the blur to apply.
	 * @return The Blurred Image.
	 */
	public BufferedImage averageBlur(BufferedImage img, int radius) {
		float[] matrix = createAverageMatrix(radius);

		BufferedImageOp averageBlurOp = new ConvolveOp(new Kernel(radius, radius, matrix));
		return averageBlurOp.filter(img, null);
	}

	/**
	 * Makes a square matrix, where the sum of the contents adds up to 1.
	 * 
	 * @param size
	 *            The size of one side of the matrix.
	 * @return A matrix.
	 */
	public float[] createAverageMatrix(int size) {
		int cells = size * size;
		float[] matrix = new float[cells];

		for (int i = 0; i < cells; i++) {
			matrix[i] = 1.0f / (float) cells;
		}

		return matrix;
	}
}
