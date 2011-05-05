package backgroundSubtractor;

import imageProcessing.ImageCentraliser;
import imageProcessing.ImageSubtractor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import opencsv.CSVReader;

import metrics.Metrics;

import fileHandling.CSVHandler;
import fileHandling.FileHandler;
import fileHandling.FileName;

/**
 * 
 * @author Richard Jenkin
 * @version 1.0
 * 
 */
public class BackgroundSubtractor {
	private FileHandler fileHandler;
	private CSVHandler csvHandler;
	private ImageSubtractor imageSubtractor;
	private ImageCentraliser imageCentraliser;
	private int blurRadius;

	/**
	 * Constructor
	 */
	public BackgroundSubtractor(FileHandler fh, CSVHandler csvh) {
		this.fileHandler = fh;
		this.csvHandler = csvh;
		imageSubtractor = new ImageSubtractor(1280, 720, 25);
		imageSubtractor.setCSVHandler(csvHandler);
		imageCentraliser = new ImageCentraliser();
		blurRadius = 11;
	}

	/**
	 * Subtract the background from all images. Uses parameters already set.
	 */
	public void subtractAll() {
		/* Getting metrics */
		System.out.println("Subtracting background: ");
		// Set up csv writer
		csvHandler.openCSVStream();
		// Set up file handling and image properties
		imageSubtractor.setBlurRadius(blurRadius);
		fileHandler.setInputFolder("input");
		fileHandler.setOutputFolder("output" + File.separator + "p1");
		// Get the list of file names
		List<String> imageNames = fileHandler.getAllImageNamesMatching("image");
		int counter = 0;
		long iStart;
		// Subtract each one
		for (String name : imageNames) {
			iStart = System.currentTimeMillis();
			System.out.print(name);
			try {
				BufferedImage currentImage = fileHandler.loadImage(name);
				BufferedImage subtractedImage = imageSubtractor.subtractBackground(currentImage);
				fileHandler.saveImage(subtractedImage, FileName.formatFileName("frame", counter++, ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("\tDone! t=" + (System.currentTimeMillis() - iStart));
		}
		// Close csv writer
		csvHandler.closeCSVStream();

		/* Crop and centralise images */
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader("output" + File.separator + "metrics.csv"));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		String[] nextLine = null;


		imageCentraliser.setCropSize(imageSubtractor.getLargestBoundingBox());
		fileHandler.setInputFolder("output" + File.separator + "p1");
		// Get the list of file names
		List<String> imageNames2 = fileHandler.getAllImageNamesMatching("frame");
		counter = 0;
		for (String name : imageNames2) {
			iStart = System.currentTimeMillis();
			System.out.print(name);
			try {
				nextLine = reader.readNext();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (nextLine == null) {
				// TODO: Something broke, throw some exception?
			}

			try {
				BufferedImage currentImage = fileHandler.loadImage(name);
				BufferedImage centralisedImage = imageCentraliser.centralCrop(currentImage, new Metrics(nextLine));
				fileHandler.saveImage(centralisedImage, FileName.formatFileName("frame", counter++, ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("\tDone! t=" + (System.currentTimeMillis() - iStart));
		}

	}

	/**
	 * Get the current blur radius.
	 * 
	 * @return <code>int</code> blurRadius.
	 */
	public int getBlurRadius() {
		return blurRadius;
	}

	/**
	 * Sets the background image to use.
	 * 
	 * @param inputImage
	 *            The background image to subtract.
	 */
	public void setBackgroundImage(BufferedImage inputImage) {
		if (inputImage == null) {
			throw new IllegalArgumentException("Cannot set background image to null!");
		} else {
			imageSubtractor.setBackgroundImage(inputImage);
		}
	}

	/**
	 * Set the blur radius to use
	 * 
	 * @param radius
	 *            <code>int</code> Blur Radius.
	 */
	public void setBlurRadius(int radius) {
		blurRadius = radius;
	}
}
