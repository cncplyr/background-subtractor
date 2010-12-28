package bgSubtract;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class BgSubtract {
	// TODO: Change these numbers later after testing finished.
	private static int noOfBgImgs = 2;
	private static int noOfFgImgs = 1;
	private static BufferedImage bgImg = null;
	private static BufferedImage currentImg = null;
	private static int threshold = 10;
	private static int blurRadius = 5;

	public static void main(String[] args) throws Exception {
		System.out.println("=======================================");
		System.out.println("     Running Background Subtractor");
		System.out.println("=======================================");

		// Take arguments for number of background images
		// TODO: Replace with iterator? or foreach loop?
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-b")) {
					noOfBgImgs = Integer.parseInt(args[i + 1]);
				}
			}
		}
		System.out.println("Number of background images:\t" + noOfBgImgs);
		System.out.println("Number of foreground images:\t" + noOfFgImgs);
		System.out.println("Blur Radius:\t\t\t" + blurRadius);
		System.out.println("Colour Threshold:\t\t" + threshold);
		System.out.println("---------------------------------------");

		// Background Image Creation
		try {
			List<BufferedImage> bgImgs = new ArrayList<BufferedImage>();
			while (noOfBgImgs > 0) {
				bgImgs.add(averageBlur(ImageIO.read(new File("background"
						+ noOfBgImgs + ".jpg")), blurRadius));
				noOfBgImgs--;
			}
			bgImg = combineImages(bgImgs);
		} catch (IOException e) {
			System.out.println("Background image not found.");
		}

		// Subtract Background
		try {
			currentImg = averageBlur(ImageIO.read(new File("input1.jpg")),
					blurRadius);
		} catch (IOException e) {
			System.out.println("Input image not found!");
		}

		if (currentImg != null && bgImg != null) {
			if (currentImg.getWidth() != bgImg.getWidth()) {
				throw new Exception("Images must be of identical dimensions!");

			}
			System.out.println("Editing file...");
			saveImage(removeBackground(currentImg), "output1");
			System.out.println("File saved!");
			System.out.println("Exiting...");
			System.out.println("=======================================");
		}
	}

	/**
	 * Combines BufferedImages.
	 * 
	 * @param img
	 *            Array of images to combine.
	 * @return A single image of average pixel values of the inputs.
	 */
	public static BufferedImage combineImages(List<BufferedImage> img) {
		int height0 = img.get(0).getHeight();
		int width0 = img.get(0).getWidth();

		BufferedImage newImage = new BufferedImage(width0, height0,
				BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width0; x++) {
			for (int y = 0; y < height0; y++) {
				Color c0 = new Color(img.get(0).getRGB(x, y));
				Color c1 = new Color(img.get(1).getRGB(x, y));

				int red = (c0.getRed() + c1.getRed()) / 2;
				int green = (c0.getGreen() + c1.getGreen()) / 2;
				int blue = (c0.getBlue() + c1.getBlue()) / 2;

				Color blah = new Color(red, green, blue);

				// for crazy colours, jsut do newImage.setRGB(x, y,
				// img[0].getRGB - img[1].getRGB);
				newImage.setRGB(x, y, blah.getRGB());
			}
		}
		saveImage(newImage, "newbackground");
		return newImage;
	}

	/**
	 * Simple average blur
	 * 
	 * @param img
	 *            The image to blur.
	 * @param radius
	 *            The radius of the blur to apply.
	 * @return The Blurred Image.
	 */
	public static BufferedImage averageBlur(BufferedImage img, int radius) {
		BufferedImage newImg = null;

		float[] matrix = createAverageMatrix(radius);

		BufferedImageOp op = new ConvolveOp(new Kernel(radius, radius, matrix));
		return op.filter(img, newImg);
	}

	/**
	 * Makes a square matrix, where the sum of the contents adds up to 1.
	 * 
	 * @param size
	 *            The size of one side of the matrix.
	 * @return A matrix.
	 */
	public static float[] createAverageMatrix(int size) {
		int cells = size * size;
		float[] matrix = new float[cells];

		for (int i = 0; i < cells; i++) {
			matrix[i] = 1.0f / (float) cells;
		}

		return matrix;
	}

	public static BufferedImage removeBackground(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color bgColour = new Color(bgImg.getRGB(x, y));
				Color imgColour = new Color(img.getRGB(x, y));

				// newImage.setRGB(x, y, imgColour - bgColour);

				// int red = imgColour.getRed() - bgColour.getRed();
				// int green = imgColour.getGreen() - bgColour.getGreen();
				// int blue = imgColour.getBlue() - bgColour.getBlue();
				//
				// Color argh = new
				// Color(fixColoursInRange(red),fixColoursInRange(green),fixColoursInRange(blue));
				//
				// newImage.setRGB(x, y, argh.getRGB());

				if ((imgColour.getRed() >= bgColour.getRed() - threshold && imgColour
						.getRed() <= bgColour.getRed() + threshold)
						&& (imgColour.getGreen() >= bgColour.getGreen()
								- threshold && imgColour.getGreen() <= bgColour
								.getGreen()
								+ threshold)
						&& (imgColour.getBlue() >= bgColour.getBlue()
								- threshold && imgColour.getBlue() <= bgColour
								.getBlue()
								+ threshold)) {
					// if colours match, remove it
					int red = imgColour.getRed() - bgColour.getRed();
					int green = imgColour.getGreen() - bgColour.getGreen();
					int blue = imgColour.getBlue() - bgColour.getBlue();

					Color argh = new Color(fixColoursInRange(red),
							fixColoursInRange(green), fixColoursInRange(blue));

					newImage.setRGB(x, y, argh.getRGB());
					// newImage.setRGB(x, y, 0);
				} else {
					// else copy it over
					newImage.setRGB(x, y, imgColour.getRGB());
				}
			}
		}

		return newImage;
	}

	public static void saveImage(BufferedImage img, String name) {
		File saveFile = new File(name + ".jpg");
		try {
			ImageIO.write(img, "jpg", saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int fixColoursInRange(int colour) {
		if (colour < 0) {
			colour = 0;
		}
		if (colour > 255) {
			colour = 255;
		}
		return colour;
	}
}