package bgSubtract;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 * 
 * This quick program will take a set of background images, and a set of
 * foreground images, and remove the background from the foreground images,
 * leaving transparent alpha behind.
 * 
 * Design Decision: The program actually gets a list of file names, then
 * recurses through the file names to get each file and process them in turn.
 * The reason for doing this rather than simply getting a List of Files or
 * BufferedImages was because it was taking up a stupidly large amount of RAM,
 * loading several hundred images at over 1MB each. Whilst I don't like this
 * implementation, it will have to do.
 * 
 * @author cncplyr
 * @version 1.0
 * 
 */
public class BackgroundSubtractor2 {
	private static int threshold = 20;
	private static int blurRadius = 11; // must be odd
	private static String fileFormat = "png";
	private static String inputFolder = "input";
	private static String outputFolder = "output";
	private static BufferedImage bgImg;
	private static long startTime;
	private static long endTime;
	private static Color alpha = new Color(0, 0, 0, 0);

	public static void main(String[] args) throws Exception {
		startTime = System.currentTimeMillis();

		System.out.println("=======================================");
		System.out.println("         Background Subtractor");
		System.out.println("=======================================");

		// Create output folder
		new File(outputFolder).mkdir();

		bgImg = averageBlur(loadImage("backgroundImage." + fileFormat), blurRadius);
		removeAllBackgrounds();

		System.out.println("\nComplete!");
		endTime = System.currentTimeMillis() - startTime;
		System.out.println("Took " + endTime + "ms to complete.");
		System.out.println("=======================================");

		exitProgram();
	}

	/**
	 * This method is here so we know where the exit is called from, rather than
	 * having System.exit() calls littered around the code.
	 * 
	 */
	private static void exitProgram() {
		System.exit(0);
	}

	private static void removeAllBackgrounds() throws Exception {
		System.out.println("Removing backgrounds...");
		int counter = 0;

		String[] fgImageNames = getAllFileNamesMatching("image");
		if (fgImageNames.length < 1) {
			exitProgram();
		} else {
			for (String currentName : fgImageNames) {
				System.out.print(currentName);
				saveImage(removeBackground(loadImage(currentName)), formatFileName("output", counter++));
				System.out.println("\tDone!");
				System.gc();
			}
		}
	}

	private static BufferedImage removeBackground(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();

		BufferedImage imgBlurred = averageBlur(img, blurRadius);
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color bgColour = new Color(bgImg.getRGB(x, y));
				Color imgColour = new Color(imgBlurred.getRGB(x, y));

				if ((imgColour.getRed() >= bgColour.getRed() - threshold && imgColour.getRed() <= bgColour.getRed() + threshold)
						&& (imgColour.getGreen() >= bgColour.getGreen() - threshold && imgColour.getGreen() <= bgColour.getGreen() + threshold)
						&& (imgColour.getBlue() >= bgColour.getBlue() - threshold && imgColour.getBlue() <= bgColour.getBlue() + threshold)) {
					// if colours match, remove it

					newImage.setRGB(x, y, alpha.getRGB());
				} else {
					// else copy (NON-BLURRED VERSION) over
					newImage.setRGB(x, y, img.getRGB(x, y));
				}
			}
		}

		return newImage;
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
	private static BufferedImage averageBlur(BufferedImage img, int radius) {
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
	private static float[] createAverageMatrix(int size) {
		int cells = size * size;
		float[] matrix = new float[cells];

		for (int i = 0; i < cells; i++) {
			matrix[i] = 1.0f / (float) cells;
		}

		return matrix;
	}



	/**
	 * Gets all the files in a the input folder, where the beginning of the
	 * files start with the name filter. E.g. nameFilter = "test", it will
	 * return all the file names matching test*.
	 * 
	 * @param nameFilter
	 *            The beginning of the name to find.
	 * @return A list of matching file names.
	 */
	private static String[] getAllFileNamesMatching(final String nameFilter) {
		File folder = new File(inputFolder);
		FilenameFilter filter = null;

		// Returns all files in the folder if (nameFilter == null)
		if (nameFilter != null) {
			filter = new FilenameFilter() {
				@Override
				public boolean accept(File folder, String name) {
					return name.startsWith(nameFilter);
				}
			};
		}

		return folder.list(filter);
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
	private static BufferedImage loadImage(String filename) throws Exception {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(inputFolder + File.separator + filename));
			img = stupidWorkAroundForJavaException(img);
		} catch (IOException e) {
			System.out.println("File not found! " + filename);
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
	private static void saveImage(BufferedImage img, String name) {
		File saveFile = new File(outputFolder + File.separator + name + "." + fileFormat);

		try {
			ImageIO.write(img, "png", saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Take a name and an Id number, and appends them. Adds leading zeros to the
	 * number to create a 5-digit number.
	 * 
	 * @param name
	 *            The filename to use.
	 * @param imageID
	 *            The file ID number to use.
	 * @return The complete name in the correct format.
	 */
	private static String formatFileName(String name, int imageID) {
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
	 * By copying it elem by elem to a new image, it suddenly works! Magic!!
	 * 
	 * Bug has been around for years:
	 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4957775
	 * 
	 * @param input
	 * @return
	 */
	private static BufferedImage stupidWorkAroundForJavaException(BufferedImage input) {
		BufferedImage tmp = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < input.getWidth(); x++) {
			for (int y = 0; y < input.getHeight(); y++) {
				tmp.setRGB(x, y, input.getRGB(x, y));
			}
		}
		return tmp;
	}
}