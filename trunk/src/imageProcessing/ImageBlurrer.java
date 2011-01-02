package imageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maths.AverageFinder;

/**
 * TODO: This class doesn't work... null pointer exceptions...
 * 
 * @author cncplyr
 * 
 */
public class ImageBlurrer {
	private AverageFinder averageFinder;

	public ImageBlurrer() {
		averageFinder = new AverageFinder();
	}

	/**
	 * Simple average(mean) blur. Uses a square matrix filter.
	 * 
	 * @param img
	 *            The image to blur.
	 * @param size
	 *            The size of the blur to apply.
	 * @return The Blurred Image.
	 */
	public BufferedImage averageBlur(BufferedImage img, int size) {
		float[] matrix = createAverageMatrix(size);

		BufferedImageOp averageBlurOp = new ConvolveOp(new Kernel(size, size, matrix), ConvolveOp.EDGE_NO_OP, null);
		return averageBlurOp.filter(img, null);
	}

	/**
	 * Simple average(median) blur. Uses a square matrix filter.
	 * 
	 * @param img
	 * @param radius
	 * @return
	 */
	public BufferedImage medianBlur(BufferedImage img, int radius) {
		int width = img.getWidth();
		int height = img.getHeight();
		List<Integer> neighbours = new ArrayList<Integer>();
		BufferedImage returnImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int x = radius; x < width - radius; x++) {
			for (int y = radius; y < height - radius; y++) {
				// TODO: WHY DOESNT THIS WORK!! ARGH
				// int[] innerMatrix = img.getRGB(x - radius, y - radius, x +
				// radius, y + radius, null, 0, (2 * radius) + 1);

				neighbours.add(img.getRGB(x - 1, y - 1));
				neighbours.add(img.getRGB(x, y - 1));
				neighbours.add(img.getRGB(x + 1, y - 1));
				neighbours.add(img.getRGB(x - 1, y));
				neighbours.add(img.getRGB(x, y));
				neighbours.add(img.getRGB(x + 1, y));
				neighbours.add(img.getRGB(x - 1, y + 1));
				neighbours.add(img.getRGB(x, y + 1));
				neighbours.add(img.getRGB(x + 1, y + 1));

				// for (int neighbour : innerMatrix) {
				// neighbours.add(neighbour);
				// }
				returnImage.setRGB(x, y, averageFinder.findMedian(neighbours));

				neighbours.clear();
				System.out.println(x + ", " + y);
			}
		}

		return returnImage;
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

		for (float cell : matrix) {
			cell = 1.0f / (float) cells;
		}

		return matrix;
	}

	/**
	 * Makes a square matrix, where each cell is predefined as 0.0f.
	 * 
	 * @param size
	 *            The size of one side of the matrix.
	 * @return A matrix.
	 */
	public int[] createZeroedIntMatrix(int size) {
		int[] matrix = new int[size * size];

		for (int cell : matrix) {
			cell = 0;
		}
		return matrix;
	}
}
