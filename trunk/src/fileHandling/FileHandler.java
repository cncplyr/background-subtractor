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
 * @version 0.3
 * 
 */
public class FileHandler {
	private String inputFolder = "input";
	private String outputFolder = "output";
	private String fileFormat = "png";
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
	 * Returns a <code>List</code> of all images in the current input folder
	 * that match the name filter. If loading from a different folder, must call
	 * setInputFolder first.
	 * 
	 * E.g. nameFilter = "test", it will return how many file names match
	 * "test*".
	 * 
	 * @param nameFilter
	 *            The name filter to use for searching for images.
	 * @return A <code>List</code> of file names as <code>String</code>s.
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

		/* Solution 2 - (Slightly slower?) */
		// File[] files = folder.listFiles(filter);
		//
		// for (File eachFile : files) {
		// images.add(loadImageFromFile(eachFile));
		// }
		return images;
	}

	/**
	 * Loads an image file from the input folder into a
	 * <code>BufferedImage</code>.
	 * 
	 * @param filename
	 *            The name of the image to load.
	 * @return The image in <code>BufferedImage</code> form.
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
	 * Saves a <code>BufferedImage</code> as a .png with the given file name.
	 * 
	 * @param img
	 *            The <code>BufferedImage</code> to save.
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

	/**
	 * Returns the currently selected input folder.
	 * 
	 * @return The current input folder as a <code>String</code>
	 */
	public String getInputFolder() {
		return inputFolder;
	}

	/**
	 * Returns the currently selected output folder.
	 * 
	 * @return The current output folder as a <code>String</code>
	 */
	public String getOutputFolder() {
		return outputFolder;
	}

	/**
	 * Sets the input folder to use.
	 * 
	 * @param inputFolder
	 *            The input folder to use.
	 */
	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
		System.out.println("Input folder changed to: " + inputFolder);
	}

	/**
	 * Sets the output folder to use.
	 * 
	 * @param outputFolder
	 *            The output folder to use.
	 */
	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}
}
