package fileHandling;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * 
 * @author cncplyr
 * @version 0.15
 * 
 */
public class FileHandler {
	private String inputFolder = "input";
	private String outputFolder = "output";
	private String fileFormat = "png";

	public FileHandler() {
	}

	/**
	 * Gets all the files in a the input folder, where the beginning of the
	 * files start with the name filter. E.g. nameFilter = "test", it will
	 * return all the file names matching test*.
	 * 
	 * If no name filter is used, returns all contents of the folder.
	 * 
	 * @param nameFilter
	 *            The beginning of the name to find.
	 * @return A list of matching file names.
	 */
	public String[] loadAllFileNamesMatching(final String nameFilter) {
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
	public BufferedImage loadImage(String filename) throws Exception {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(inputFolder + File.separator + filename));
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

	public BufferedImage loadImageFromFile(File file) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			System.out.println("File is not an image!");
			e.printStackTrace();
		}
		return img;
	}

	public List<BufferedImage> loadAllImagesMatching(final String nameFilter) {
		File folder = new File(inputFolder);
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File folder, String name) {
				return name.startsWith(nameFilter);
			}
		};

		List<BufferedImage> images = new ArrayList<BufferedImage>();

		File[] files = folder.listFiles(filter);

		for (File eachFile : files) {
			images.add(loadImageFromFile(eachFile));
		}

		return images;
	}

	/**
	 * Saves a Buffered Image with the given file name.
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
			if (fileFormat.equals("jpg")) {
				ImageIO.write(img, "jpg", saveFile);
			} else if (fileFormat.equals("png")) {
				ImageIO.write(img, "png", saveFile);
			}
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

	public String getFileFormat() {
		return fileFormat;
	}

	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
}
