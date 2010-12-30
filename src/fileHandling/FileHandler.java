package fileHandling;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FileHandler {
	private String inputFolder = "input";
	private String outputFolder = "output";
	private String fileFormat = "png";

	public FileHandler() {
	}

	public FileHandler(String inputFolder, String outputFolder, String fileFormat) {
		this.setInputFolder(inputFolder);
		this.setOutputFolder(outputFolder);
		this.setFileFormat(fileFormat);
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
	public String[] getAllFileNamesMatching(final String nameFilter) {
		File folder = new File(getInputFolder());
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
			img = ImageIO.read(new File(getInputFolder() + File.separator + filename));
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
	public void saveImage(BufferedImage img, String name) {
		File saveFile = new File(getOutputFolder() + File.separator + name + "." + getFileFormat());

		try {
			if (getFileFormat().equals("jpg")) {
				ImageIO.write(img, "jpg", saveFile);
			} else if (getFileFormat().equals("png")) {
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