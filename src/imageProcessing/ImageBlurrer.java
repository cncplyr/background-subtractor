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

		BufferedImage copiedImage = workAroundForJavaException(img);

		BufferedImageOp averageBlurOp = new ConvolveOp(new Kernel(size, size, matrix));
		// BufferedImageOp averageBlurOp = new ConvolveOp(new Kernel(size, size,
		// matrix), ConvolveOp.EDGE_NO_OP, null);
		return averageBlurOp.filter(copiedImage, null);
	}

	/**
	 * Simple average(median) blur. Uses a square matrix filter. Currently not
	 * used.
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
				int[] blur = img.getRGB(x - radius, y - radius, (2 * radius) + 1, (2 * radius) + 1, null, 0, (2 * radius) + 1);

				for (int neighbour : blur) {
					neighbours.add(neighbour);
				}

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

		for (int i = 0; i < cells; i++) {
			matrix[i] = 1.0f / (float) cells;
		}
		return matrix;
	}

	/**
	 * A work-around for a java exception. If you take a Buffered Image through
	 * ImageIO.read(new File("filename.jpg")), then pass that to ConvolveOp, it
	 * throws an error:
	 * 
	 * Exception in thread "main" java.awt.image.ImagingOpException: Unable to
	 * convolve src image at java.awt.image.ConvolveOp.filter(Unknown Source) at
	 * bgSubtract.BgSubtract.averageBlur(BgSubtract.java:159) at
	 * bgSubtract.BgSubtract.main(BgSubtract.java:74)
	 * 
	 * By copying it elem by elem to a new image, it suddenly works! Magic!!
	 * 
	 * Bug has been around for years:
	 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4957775
	 * 
	 * @param input
	 * @return
	 */
	public static BufferedImage workAroundForJavaException(BufferedImage input) {
		BufferedImage tmp = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < input.getWidth(); x++) {
			for (int y = 0; y < input.getHeight(); y++) {
				tmp.setRGB(x, y, input.getRGB(x, y));
			}
		}
		return tmp;
	}
}
