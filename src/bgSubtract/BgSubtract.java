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
	private static int noOfBgImgs = 0; // # background images
	private static int noOfFgImgs = 0; // # foreground images
	private static int fgImgStartNumber = 0; // foreground start number
	private static int threshold = 10;
	private static int blurRadius = 10;
	private static String fileFormat = ".png";
	private static String inputFolder = "input";
	private static String outputFolder = "output";
	private static BufferedImage bgImg = null;
	private static BufferedImage currentImg = null;
	private static long startTime;
	private static long endTime;

	public static void main(String[] args) throws Exception {
		startTime = System.currentTimeMillis();
		System.out.println("=======================================");
		System.out.println("         Background Subtractor");
		System.out.println("=======================================");

		/* Take arguments for number of background images */
		for (int i = 0; i < args.length; i++) {
			if (i + 1 < args.length) {
				if (args[i].equals("-bg")) {
					// number of background images
					noOfBgImgs = Integer.parseInt(args[i + 1]);
				} else if (args[i].equals("-format")) {
					// image format
					if (args[i + 1].equals("png")) {
						fileFormat = ".png";
					} else {
						fileFormat = ".jpg";
					}
				} else if (args[i].equals("-inFolder")) {
					// input folder
					inputFolder = args[i + 1];
				} else if (args[i].equals("-outFolder")) {
					// output folder
					outputFolder = args[i + 1];
				} else if (args[i].equals("-startNo")) {
					fgImgStartNumber = Integer.parseInt(args[i + 1]);
				} else if (args[i].equals("-images")) {
					noOfFgImgs = Integer.parseInt(args[i + 1]);
				} else if (args[i].equals("-threshold")) {
					threshold = Integer.parseInt(args[i + 1]);
				} else if (args[i].equals("-blurRadius")) {
					blurRadius = Integer.parseInt(args[i + 1]);
				} else if (args[i].equals("--help")) {
					printHelpMessage();
				}
			}
		}

		if (noOfBgImgs == 0 || noOfFgImgs == 0) {
			printHelpMessage();
			System.exit(0);
		}

		// Create output folder
		new File(outputFolder).mkdir();

		System.out.println("Number of background images:\t" + noOfBgImgs);
		System.out.println("Number of foreground images:\t" + noOfFgImgs);
		System.out.println("Blur Radius:\t\t\t" + blurRadius);
		System.out.println("Colour Threshold:\t\t" + threshold);
		System.out.println("---------------------------------------");

		/* Background Image Creation */
		System.out.println("Creating background...");
		List<BufferedImage> bgImgs = new ArrayList<BufferedImage>();

		for (int bgID = 0; bgID < noOfBgImgs; bgID++) {
			// Get each image, blur it, add it to the list
			String currentBgFilename = formatFileName("background", bgID);
			BufferedImage currentImg = loadImage(currentBgFilename, fileFormat);
			System.out.print(currentBgFilename);
			bgImgs.add(averageBlur(currentImg, blurRadius));
			System.out.println("\tDone!");
		}
		// Combine the background images
		bgImg = combineImages(bgImgs);
		System.out.println("Background created!\n");

		/* Subtract Background */
		System.out.println("Subtracting background...");
		// Load foreground image
		for (int imageID = fgImgStartNumber; imageID < fgImgStartNumber
				+ noOfFgImgs; imageID++) {

			String currentFilename = formatFileName("image", imageID);
			currentImg = loadImage(currentFilename, fileFormat);
			System.out.print(currentFilename);

			// Subtract taht shizzle!!
			saveImage(removeBackground(currentImg),
					formatFileName("output", imageID - fgImgStartNumber));
			System.out.println("\tDone!");
		}
		System.out.println("\nComplete!");
		endTime = System.currentTimeMillis() - startTime;
		System.out.println("Took " + endTime + "ms to complete.");
		System.out.println("=======================================");
		System.exit(0);
	}

	public static BufferedImage removeBackground(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();

		BufferedImage imgBlurred = averageBlur(img, blurRadius);
		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color bgColour = new Color(bgImg.getRGB(x, y));
				Color imgColour = new Color(imgBlurred.getRGB(x, y));

				if ((imgColour.getRed() >= bgColour.getRed() - threshold && imgColour
						.getRed() <= bgColour.getRed() + threshold)
						&& (imgColour.getGreen() >= bgColour.getGreen()
								- threshold && imgColour.getGreen() <= bgColour
								.getGreen() + threshold)
						&& (imgColour.getBlue() >= bgColour.getBlue()
								- threshold && imgColour.getBlue() <= bgColour
								.getBlue() + threshold)) {
					// if colours match, remove it
					Color argh = new Color(0, 0, 0, 0);
					newImage.setRGB(x, y, argh.getRGB());
				} else {
					// else copy (NON-BLURRED VERSION) over
					newImage.setRGB(x, y, img.getRGB(x, y));
				}
			}
		}

		return newImage;
	}

	/**
	 * Combines BufferedImages.
	 * 
	 * @param img
	 *            Array of images to combine.
	 * @return A single image of average pixel values of the inputs.
	 * @throws Exception
	 */
	public static BufferedImage combineImages(List<BufferedImage> img)
			throws Exception {
		int height0 = img.get(0).getHeight();
		int width0 = img.get(0).getWidth();

		BufferedImage newImage = new BufferedImage(width0, height0,
				BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < width0; x++) {
			for (int y = 0; y < height0; y++) {
				Color c0 = new Color(img.get(0).getRGB(x, y));
				Color c1 = new Color(img.get(1).getRGB(x, y));

				int red = (c0.getRed() + c1.getRed()) / 2;
				int green = (c0.getGreen() + c1.getGreen()) / 2;
				int blue = (c0.getBlue() + c1.getBlue()) / 2;

				Color blah = new Color(red, green, blue, 255);

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
	 * @throws Exception
	 */
	public static BufferedImage loadImage(String filename, String fileFormat)
			throws Exception {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(inputFolder + File.separator + filename
					+ fileFormat));
			img = stupidWorkAroundForJavaException(img);
		} catch (IOException e) {
			System.out.println("File not found! " + filename + fileFormat);
			e.printStackTrace();
		}

		// Check if method worked before returning.
		if (img == null) {
			throw new Exception("File failed to load!");
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
		File saveFile = new File(outputFolder + File.separator + name
				+ fileFormat);

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

	public static String formatFileName(String name, int imageID) {
		if (imageID > 9999) {
			name = name + Integer.toString(imageID);
		} else if (imageID > 999) {
			name = name + "0" + Integer.toString(imageID);
		} else if (imageID > 99) {
			name = name + "00" + Integer.toString(imageID);
		} else if (imageID > 9) {
			name = name + "000" + Integer.toString(imageID);
		} else {
			name = name + "0000" + Integer.toString(imageID);
		}
		return name;
	}

	public static void printHelpMessage() {
		System.out.println("README");
		System.out.println("=============");
		System.out
				.println("This program will take a set of background images, and a set of foreground images, and remove the background from the foreground images, leaving transparent alpha behind.");
		System.out
				.println("If the input image files are not in the correct place, or referenced properly, the program will fail. You have been warned!");
		System.out.println("=============");
		System.out.println("USAGE:");
		System.out
				.println("\n\nYOU WILL ALWAYS WANT TO USE -bg, -startNo and -images\n\n");
		System.out
				.println("Background images must be called \"backgroundxxxxx.png\" where xxxxx is the id number, starting from 00000, with 5 digits total (leading zeros).");
		System.out
				.println("Foreground images must be called \"imagexxxxx.png\" where xxxxx is the id number, starting from 00000, with 5 digits total (leading zeros).");
		System.out
				.println("All input images must be in a subfolder called \"input\", unless modified.");
		System.out
				.println("All output images will be placed in a subfolder called \"output\", unless modified.");
		System.out.println("=============");
		System.out.println("Argument\tEffect");
		System.out.println("-inFormat\tImage Input format: png OR jpg");
		System.out.println("-outFormat\tImage Output format: png OR jpg");
		System.out.println("-inFolder\tImage Input folder override");
		System.out.println("-outFolder\tImage Output folder override");
		System.out.println("-bg\t\tNumber of background images");
		System.out.println("-startNo\tStart number of Foreground Images");
		System.out.println("-images\t\tNumber of Foreground Images");
		System.out.println("-threshold\tThreshold of colours for subtraction");
		System.out.println("-blurRadius\tRadius of Average Blur applied");
		System.out.println("--help\t\tDisplays this message");
		System.out.println("=============");
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
				input.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < input.getWidth(); x++) {
			for (int y = 0; y < input.getHeight(); y++) {
				tmp.setRGB(x, y, input.getRGB(x, y));
			}
		}
		return tmp;
	}
}