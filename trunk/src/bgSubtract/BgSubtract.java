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

/**
 * 
 * @author cncplyr
 * 
 */
public class BgSubtract {
	// TODO: Change these numbers later after testing finished.
	private static int noOfBgImgs = 2;
	private static int noOfFgImgs = 1;
	private static BufferedImage bgImg = null;
	private static BufferedImage currentImg = null;
	private static int threshold = 10;
	private static int blurRadius = 10;
	private static String fileFormat = ".png";

	public static void main(String[] args) throws Exception {
		System.out.println("=======================================");
		System.out.println("     Running Background Subtractor");
		System.out.println("=======================================");

		// Take arguments for number of background images
		// TODO: Replace with iterator? or foreach loop?
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				// number of background images
				if (args[i].equals("-b")) {
					noOfBgImgs = Integer.parseInt(args[i + 1]);
					// format
				} else if (args[i].equals("-f")) {
					if (args[i + 1].equals("png")) {
						fileFormat = ".png";
					} else {
						fileFormat = ".jpg";
					}
				}
			}
		}
		System.out.println("Number of background images:\t" + noOfBgImgs);
		System.out.println("Number of foreground images:\t" + noOfFgImgs);
		System.out.println("Blur Radius:\t\t\t" + blurRadius);
		System.out.println("Colour Threshold:\t\t" + threshold);
		System.out.println("---------------------------------------");

		/* Background Image Creation */
		List<BufferedImage> bgImgs = new ArrayList<BufferedImage>();
		while (noOfBgImgs > 0) {
			// Get each image, blur it, add it to the list
			BufferedImage currentImg = loadImage("background" + noOfBgImgs,
					fileFormat);
			bgImgs.add(averageBlur(currentImg, blurRadius));
			noOfBgImgs--;
		}
		// Combine the background images
		bgImg = combineImages(bgImgs);

		/* Subtract Background */
		// Load and blur foreground image
		currentImg = loadImage("image00038", fileFormat);
		currentImg = averageBlur(currentImg, blurRadius);

		// Subtract that shizzle!!
		if (currentImg != null && bgImg != null) {
			if (currentImg.getWidth() != bgImg.getWidth()) {
				throw new Exception("Images must be of identical dimensions!");
			}
			System.out.println("Editing and saving file...");
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
		float[] matrix = createAverageMatrix(radius);

		BufferedImageOp averageBlurOp = new ConvolveOp(new Kernel(radius,
				radius, matrix));
		return averageBlurOp.filter(img, null);
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
								.getGreen() + threshold)
						&& (imgColour.getBlue() >= bgColour.getBlue()
								- threshold && imgColour.getBlue() <= bgColour
								.getBlue() + threshold)) {
					// if colours match, remove it
					int red = imgColour.getRed() - bgColour.getRed();
					int green = imgColour.getGreen() - bgColour.getGreen();
					int blue = imgColour.getBlue() - bgColour.getBlue();

					Color argh = new Color(clipColoursInRange(red),
							clipColoursInRange(green), clipColoursInRange(blue));

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

	/**
	 * Clips colours within 255, incase they went out.
	 * 
	 * @param colour
	 * @return
	 */
	public static int clipColoursInRange(int colour) {
		if (colour < 0) {
			colour = 0;
		}
		if (colour > 255) {
			colour = 255;
		}
		return colour;
	}

	/**
	 * Loads an image file into a BufferedImage. Includes
	 * stupidWorkAroundForJavaException().
	 * 
	 * @param filename
	 *            The name of the image to load.
	 * @param fileFormat
	 *            The file format of the image to load.
	 * @return The image in bufferedImage form.
	 */
	public static BufferedImage loadImage(String filename, String fileFormat) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("png\\" + filename + fileFormat));
			img = stupidWorkAroundForJavaException(img);
		} catch (IOException e) {
			System.out.println("File not found! " + filename + fileFormat);
			e.printStackTrace();
		}
		return img;
	}

	/**
	 * Saves a Buffered Image with the given file name.
	 * 
	 * @param img
	 *            The BufferedImage to save.
	 * @param name
	 *            The file name to use.
	 */
	public static void saveImage(BufferedImage img, String name) {
		File saveFile = new File(name + fileFormat);
		try {
			if (fileFormat.equals(".jpg")) {
				ImageIO.write(img, "jpg", saveFile);
			} else if (fileFormat.equals(".png")) {
				ImageIO.write(img, "png", saveFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A completely stupid retarded work-around for a completely stupid retarded
	 * java exception. If you take a Buffered Image through ImageIO.read(new
	 * File("filename.jpg")), then pass that to ConvolveOp, it throws an error:
	 * 
	 * Exception in thread "main" java.awt.image.ImagingOpException: Unable to
	 * convolve src image at java.awt.image.ConvolveOp.filter(Unknown Source) at
	 * bgSubtract.BgSubtract.averageBlur(BgSubtract.java:159) at
	 * bgSubtract.BgSubtract.main(BgSubtract.java:74)
	 * 
	 * By copying it elem by elem to a new image, and then putting that back, it
	 * suddenly works! Magic!!
	 * 
	 * Bug has been around for years:
	 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4957775
	 * 
	 * @param input
	 * @return
	 */
	public static BufferedImage stupidWorkAroundForJavaException(
			BufferedImage input) {
		BufferedImage tmp = new BufferedImage(input.getWidth(),
				input.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < input.getWidth(); x++) {
			for (int y = 0; y < input.getHeight(); y++) {
				tmp.setRGB(x, y, input.getRGB(x, y));
			}
		}
		return tmp;
	}
}