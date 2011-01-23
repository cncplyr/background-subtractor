package fileHandling;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * This class handles all file manipulation. Images are requested via
 * <code>String</code> filters, and images are returned as either a
 * <code>BufferedImage</code>, or a <code>List</code> of
 * <code>BufferedImage</code>s.
 * 
 * Images can be loaded in any format allowed by ImageIO, but all saved images
 * will be in the .png format.
 * 
 * It is possible to change the input and output folders using the
 * inputFolder/outputFolder getters and setters.
 * 
 * @author cncplyr
 * @version 0.2
 * 
 */
public class FileHandler {
	private String inputFolder = "input";
	private String outputFolder = "output";
	private String fileFormat = "png";
	private int currentImage = 0;
	private int totalImages = 0;

	public FileHandler() {
	}

	/**
	 * Returns the number of files matching the filter. E.g. nameFilter =
	 * "test", it will return how many file names match "test*".
	 * 
	 * @param nameFilter
	 *            The beginning of the name to find.
	 * @return The total number of files matching the input string.
	 */
	public int getTotalImagesMatching(final String nameFilter) {
		File inFolder = new File(inputFolder);
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

		totalImages = inFolder.list(filter).length;
		return totalImages;
	}

	/**
	 * HERESEY!!! HOW CAN I HAVE THIS METHOD HERE?!?! Because I know no better
	 * way of doing it...
	 * 
	 * @param nameFilter
	 * @return
	 */
	public List<String> getAllImageNamesMatching(final String nameFilter) {
		File inFolder = new File(inputFolder);
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

		return Arrays.asList(inFolder.list(filter));
	}

	/**
	 * Loads all images from the input folder that match the name filter,
	 * returned as a <code>List<code> of <code>BufferedImage</code>s. Allows
	 * skipping of images.
	 * 
	 * @param nameFilter
	 *            The beginning of the name to find.
	 * @param getEveryXthItem
	 *            e.g. 1 = load every image; 2 = load every second image; 10 =
	 *            load every 10th image.
	 * @return All the files that matched the name, not including files skipped.
	 */
	public List<BufferedImage> loadAllImagesMatching(final String nameFilter, int getEveryXthItem) {
		File inFolder = new File(inputFolder);
		int counter = 0;
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File folder, String name) {
				return name.startsWith(nameFilter);
			}
		};

		List<BufferedImage> images = new ArrayList<BufferedImage>();

		/* Solution 1 */
		for (String eachName : inFolder.list(filter)) {
			if (counter % getEveryXthItem == 0) {
				images.add(loadImage(eachName));
			}
			counter++;
		}

		/* Alternate Solution */
		// File[] files = folder.listFiles(filter);
		//
		// for (File eachFile : files) {
		// images.add(loadImageFromFile(eachFile));
		// }
		return images;
	}

	public BufferedImage getNextImage() {
		BufferedImage nextImage = loadImage("test");
		return nextImage;
	}


	public boolean isAnotherImage() {
		return (currentImage < totalImages);
	}


	/**
	 * Loads an image file from the input folder into a
	 * <code>BufferedImage</code>.
	 * 
	 * @param filename
	 *            The name of the image to load.
	 * @param fileFormat
	 *            The file format of the image to load.
	 * @return The image in bufferedImage form.
	 */
	public BufferedImage loadImage(String filename) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(inputFolder + File.separator + filename));
		} catch (IOException e) {
			System.out.println("Image not found: " + filename);
			e.printStackTrace();
		}
		return img;
	}

	/**
	 * Saves a Buffered Image as a .png with the given file name.
	 * 
	 * @param img
	 *            The BufferedImage to save.
	 * @param name
	 *            The file name to use.
	 */
	public void saveImage(BufferedImage img, String name) {
		new File(outputFolder).mkdir();

		File saveFile = new File(outputFolder + File.separator + name + "." + fileFormat);

		try {
			ImageIO.write(img, "png", saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getInputFolder() {
		return inputFolder;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
		System.out.println("Input folder changed to: " + inputFolder);
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}
}
